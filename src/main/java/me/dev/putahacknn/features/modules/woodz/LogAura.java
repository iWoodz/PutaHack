package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.event.events.Render3DEvent;
import me.dev.putahacknn.event.events.UpdateWalkingPlayerEvent;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.gui.font.CustomFont;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Bind;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.*;
import me.dev.putahacknn.util.Timer;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static me.dev.putahacknn.util.RenderUtil.camera;

public class LogAura extends Module {

    public LogAura() {
        super("LogAura", "Honestly, I kinda hate British people. I know thats kinda racist but British people piss me off. - iWoodz", Category.WOODZ, true, false, false);
        INSTANCE = this;
    }

    public enum RotateMode {
        Both,
        Break,
        Place,
        Off
    }

    public enum FastMode {
        Ignore,
        Ghost,
        Sound,
        Off
    }

    public enum AutoSwitch {
        Silent,
        None,
        Always,
        NoGap
    }

    public enum Placements {
        Damage,
        Nearby,
        Safe
    }

    public enum ArrayListMode {
        Latency,
        Player,
        CPS
    }

    public enum PTeleportMode {
        Sound,
        Packet,
        None
    }

    public enum SwingMode {
        Mainhand,
        Offhand,
        None
    }

    public enum When {
        Never,
        Place,
        Break,
        Both
    }

    public enum RenderMode {
        Pretty,
        Solid,
        Outline
    }

    public static LogAura INSTANCE;
    private final Setting<Double> breakRange = this.register(new Setting<Double>("Break Range", 5.0, 0.0, 6.0));
    private final Setting<Double> placeRange = this.register(new Setting<Double>("Place Range", 5.0, 0.0, 6.0));
    private final Setting<Double> breakRangeWall = this.register(new Setting<Double>("Break Range Wall", 3.0, 0.0, 6.0));
    private final Setting<Double> placeRangeWall = this.register(new Setting<Double>("Place Range Wall", 3.0, 0.0, 6.0));
    private final Setting<Double> targetRange = this.register(new Setting<Double>("Target Range", 15.0, 0.0, 20.0));
    private final Setting<Integer> placeDelay = this.register(new Setting<Integer>("Place Delay", 0, 0, 10));
    private final Setting<Integer> breakDelay = this.register(new Setting<Integer>("Break Delay", 0, 0, 10));
    private final Setting<Boolean> sortBlocks = this.register(new Setting("Sort Blocks", true));
    private final Setting<Boolean> ignoreSelfDamage = this.register(new Setting("Ignore Self", false));
    private final Setting<Integer> minPlace = this.register(new Setting<Integer>("MinPlace", 9, 0, 36));
    private final Setting<Integer> maxSelfPlace = this.register(new Setting<Integer>("MaxSelfPlace", 5, 0, 36));
    private final Setting<Integer> minBreak = this.register(new Setting<Integer>("MinBreak", 9, 0, 36));
    private final Setting<Integer> maxSelfBreak = this.register(new Setting<Integer>("MaxSelfBreak", 5, 0, 36));
    private final Setting<Boolean> antiSuicide = this.register(new Setting("Anti Suicide", true));
    public final Setting<RotateMode> rotateMode = this.register(new Setting("Rotate", RotateMode.Off));
    public final Setting<Integer> maxYaw = this.register(new Setting<Integer>("MaxYaw", 180, 0, 180));
    private final Setting<Boolean> raytrace = this.register(new Setting("Raytrace", false));
    private final Setting<FastMode> fastMode = this.register(new Setting("Fast", FastMode.Sound));
    public final Setting<AutoSwitch> autoSwitch = this.register(new Setting("Switch", AutoSwitch.NoGap));
    private final Setting<Boolean> silentSwitchHand = this.register(new Setting("Hand Activation", true));
    private final Setting<Boolean> antiWeakness = this.register(new Setting("Anti Weakness", true));
    private final Setting<Integer> maxCrystals = this.register(new Setting<Integer>("MaxCrystal", 1, 1, 4));
    private final Setting<Boolean> ignoreTerrain = this.register(new Setting("Terrain Trace", true));
    private final Setting<Placements> crystalLogic = this.register(new Setting("Placements", Placements.Damage));
    private final Setting<Boolean> thirteen = this.register(new Setting("1.13", false));
    private final Setting<Boolean> attackPacket = this.register(new Setting("AttackPacket", true));
    private final Setting<Boolean> packetSafe = this.register(new Setting("Packet Safe", true));
    private final Setting<Boolean> noBreakCalcs = this.register(new Setting("No Break Calcs", false));
    private final Setting<ArrayListMode> arrayListMode = this.register(new Setting("Array List Mode", ArrayListMode.Player));
    private final Setting<Boolean> debug = this.register(new Setting("Debug", false));
    private final Setting<Boolean> threaded = this.register(new Setting("Threaded", false));
    private final Setting<Boolean> antiStuck = this.register(new Setting("Anti Stuck", false));
    private final Setting<Integer> maxAntiStuckDamage = this.register(new Setting<Integer>("Stuck Self Damage", 8, 0, 36));
    private final Setting<Boolean> predictCrystal = this.register(new Setting("Predict Crystal", true));
    private final Setting<Boolean> predictBlock = this.register(new Setting("Predict Block", true));
    private final Setting<PTeleportMode> predictTeleport = this.register(new Setting("P Teleport", PTeleportMode.Sound));
    private final Setting<Boolean> entityPredict = this.register(new Setting("Entity Predict", true));
    private final Setting<Integer> predictedTicks = this.register(new Setting<Integer>("Predict Ticks", 2, 0, 5));
    private final Setting<Boolean> palceObiFeet = this.register(new Setting("Enabled", false));
    private final Setting<Boolean> ObiYCheck = this.register(new Setting("YCheck", false));
    private final Setting<Boolean> rotateObiFeet = this.register(new Setting("Rotate", false));
    private final Setting<Integer> timeoutTicksObiFeet = this.register(new Setting<Integer>("Timeout", 3, 0, 5));
    private final Setting<Boolean> noMP = this.register(new Setting("NoMultiPlace", false));
    private final Setting<Integer> facePlaceHP = this.register(new Setting<Integer>("TabbottHP", 0, 0, 36));
    private final Setting<Integer> facePlaceDelay = this.register(new Setting<Integer>("TabbottDelay", 0, 0, 10));
    private final Setting<Bind> fpbind = this.register(new Setting<Bind>("TabbottBind", new Bind(-1)));
    private final Setting<Integer> fuckArmourHP = this.register(new Setting<Integer>("Armour%", 0, 0, 100));
    private final Setting<When> when = this.register(new Setting("When", When.Place));
    private final Setting<RenderMode> mode = this.register(new Setting("Mode", RenderMode.Pretty));
    private final Setting<Boolean> fade = this.register(new Setting("Fade", false));
    private final Setting<Integer> fadeTime = this.register(new Setting<Integer>("FadeTime", 200, 0, 1000));
    private final Setting<Boolean> flat = this.register(new Setting("Flat", false));
    private final Setting<Double> height = this.register(new Setting<Double>("FlatHeight", 0.2, -2.0, 2.0));
    private final Setting<Integer> width = this.register(new Setting<Integer>("Width", 1, 1, 10));
    public final Setting<Boolean> circleRender = this.register(new Setting("Circle Render", true));
    public final Setting<Float> circleRadius = this.register(new Setting("Circle Radius", 0.5f, 0.0f, 10.0f));
    public final Setting<Integer> fillR = this.register(new Setting("Fill R", 255, 0, 255));
    public final Setting<Integer> fillG = this.register(new Setting("Fill G", 255, 0, 255));
    public final Setting<Integer> fillB = this.register(new Setting("Fill B", 255, 0, 255));
    public final Setting<Integer> fillA = this.register(new Setting("Fill A", 70, 0, 255));
    public final Setting<Integer> lineR = this.register(new Setting("Line R", 255, 0, 255));
    public final Setting<Integer> lineG = this.register(new Setting("Line G", 255, 0, 255));
    public final Setting<Integer> lineB = this.register(new Setting("Line B", 255, 0, 255));
    public final Setting<Integer> lineA = this.register(new Setting("Line A", 200, 0, 255));
    private final Setting<Boolean> renderDamage = this.register(new Setting("RenderDamage", true));
    private final Setting<SwingMode> swing = this.register(new Setting("Swing", SwingMode.Offhand));
    private final Setting<Boolean> placeSwing = this.register(new Setting("Place Swing", true));
    public Color renderFillColour = new Color(fillR.getValue(), fillG.getValue(), fillB.getValue(), fillA.getValue());
    public Color renderBoxColour = new Color(lineR.getValue(), lineG.getValue(), lineB.getValue(), lineA.getValue());
    private final List<EntityEnderCrystal> attemptedCrystals = new ArrayList<>();
    private final ArrayList<RenderPos> renderMap = new ArrayList<>();
    private final ArrayList<BlockPos> currentTargets = new ArrayList<>();
    private final Timer crystalsPlacedTimer = new Timer();
    private EntityEnderCrystal stuckCrystal;
    private boolean alreadyAttacking;
    private boolean placeTimeoutFlag;
    private boolean hasPacketBroke;
    private boolean didAnything;
    private boolean facePlacing;
    private long start;
    private long crystalLatency;
    private int placeTimeout;
    private int breakTimeout;
    private int breakDelayCounter;
    private int placeDelayCounter;
    private int facePlaceDelayCounter;
    private int obiFeetCounter;
    private int crystalsPlaced;
    public EntityPlayer ezTarget;
    public ArrayList<BlockPos> staticPos;
    public EntityEnderCrystal staticEnderCrystal;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public final void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotateMode.getValue() != RotateMode.Off) {
            this.doCrystalAura();
        }
    }

    @Override
    public void onUpdate() {
        if (this.rotateMode.getValue() == RotateMode.Off) {
            try {
                this.doCrystalAura();
            } catch (Exception ignored) {

            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public final void onPacketSend(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getStage() == 0
                && event.getPacket() instanceof CPacketUseEntity
                && (packet = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK
                && packet.getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
            if (this.fastMode.getValue() == FastMode.Ghost) {
                Objects.requireNonNull(packet.getEntityFromWorld(mc.world)).setDead();
                mc.world.removeEntityFromWorld(packet.entityId);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public final void onPacketReceive(PacketEvent.Receive event) {

        // crystal predict check
        SPacketSpawnObject spawnObjectPacket;
        if (event.getPacket() instanceof SPacketSpawnObject
                && (spawnObjectPacket = event.getPacket()).getType() == 51
                && this.predictCrystal.getValue()) {
            // for each player on the server
            for (EntityPlayer target : new ArrayList<>(mc.world.playerEntities)) {
                // if the crystal is valid for the given player
                if (this.isCrystalGood(new EntityEnderCrystal(mc.world, spawnObjectPacket.getX(), spawnObjectPacket.getY(), spawnObjectPacket.getZ()), target) != 0) {
                    if (debug.getValue()) {
                        Command.sendMessage("predict break");
                    }
                    // set up the break packet
                    CPacketUseEntity predict = new CPacketUseEntity();
                    predict.entityId = spawnObjectPacket.getEntityID();
                    predict.action = CPacketUseEntity.Action.ATTACK;
                    mc.player.connection.sendPacket(predict);
                    // swing arm
                    if (swing.getValue() != SwingMode.None) {
                        mc.player.swingArm(swing.getValue() == SwingMode.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
                    }
                    // sets up the packet safe
                    if (packetSafe.getValue()) {
                        hasPacketBroke = true;
                        didAnything = true;
                    }
                    // only do it once
                    break;
                }
            }
        }

        // sets the 'player pos' of a teleporting player to where they're going to tp to
        if (event.getPacket() instanceof SPacketEntityTeleport) {
            SPacketEntityTeleport tpPacket = event.getPacket();
            Entity e = mc.world.getEntityByID(tpPacket.getEntityId());
            if (e == mc.player) return;
            if (e instanceof EntityPlayer && predictTeleport.getValue() == PTeleportMode.Packet) {
                e.setEntityBoundingBox(e.getEntityBoundingBox().offset(tpPacket.getX(), tpPacket.getY(), tpPacket.getZ()));
            }
        }

        // same as above but works on the sound effect rather than the tp packet
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect soundPacket = event.getPacket();
            if (soundPacket.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT && predictTeleport.getValue() == PTeleportMode.Sound) {
                mc.world.loadedEntityList.spliterator().forEachRemaining(player -> {
                    if (player instanceof EntityPlayer && player != mc.player) {
                        if (player.getDistance(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()) <= targetRange.getValue()) {
                            player.setEntityBoundingBox(player.getEntityBoundingBox().offset(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()));
                        }
                    }
                });
            }
            // unsure how this would ever lead to a crash but i dont wanna touch it atm
            try {
                if (soundPacket.getCategory() == SoundCategory.BLOCKS
                        && soundPacket.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (Entity crystal : new ArrayList<>(mc.world.loadedEntityList)) {
                        if (crystal instanceof EntityEnderCrystal)
                            if (crystal.getDistance(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()) <= breakRange.getValue()) {
                                crystalLatency = System.currentTimeMillis() - start;
                                if (fastMode.getValue() == FastMode.Sound) {
                                    crystal.setDead();
                                }
                            }
                    }
                }
            } catch (NullPointerException ignored) {
            }
        }

        // attempt at a place predict, currently doesn't place
        if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion explosionPacket = event.getPacket();
            BlockPos pos = new BlockPos(Math.floor(explosionPacket.getX()), Math.floor(explosionPacket.getY()), Math.floor(explosionPacket.getZ())).down();
            if (this.predictBlock.getValue()) {
                for (EntityPlayer player : new ArrayList<>(mc.world.playerEntities)) {
                    if (this.isBlockGood(pos, player) > 0) {
                        BlockUtil.placeCrystalOnBlock(pos, EnumHand.MAIN_HAND, autoSwitch.getValue() == AutoSwitch.Silent);
                    }
                }
            }
        }
    }

    public void doCrystalAura() {
        if (nullCheck()) {
            this.disable();
            return;
        }
        didAnything = false;

        if (placeDelayCounter > placeTimeout && (facePlaceDelayCounter >= facePlaceDelay.getValue() || !facePlacing)) {
            start = System.currentTimeMillis();
            this.placeCrystal();
        }
        if (breakDelayCounter > breakTimeout && (!hasPacketBroke || !packetSafe.getValue())) {
            if (debug.getValue()) {
                Command.sendMessage("Attempting break");
            }
            if (noBreakCalcs.getValue()) {
                breakCrystalNoCalcs();
            } else {
                if (antiStuck.getValue() && stuckCrystal != null) {
                    this.breakCrystal(stuckCrystal);
                    stuckCrystal = null;
                } else {
                    this.breakCrystal(null);
                }
            }
        }

        if (!didAnything) {
            hasPacketBroke = false;
        }

        breakDelayCounter++;
        placeDelayCounter++;
        facePlaceDelayCounter++;
        obiFeetCounter++;
    }

    private void clearMap(BlockPos checkBlock) {
        List<RenderPos> toRemove = new ArrayList<>();
        if (checkBlock == null || renderMap.isEmpty()) return;
        for (RenderPos pos : renderMap) {
            if (pos.pos.getX() == checkBlock.getX() && pos.pos.getY() == checkBlock.getY() && pos.pos.getZ() == checkBlock.getZ())
                toRemove.add(pos);
        }
        renderMap.removeAll(toRemove);
    }

    private void placeCrystal() {
        ArrayList<BlockPos> placePositions;
        placePositions = this.getBestBlocks();
        currentTargets.clear();
        currentTargets.addAll(placePositions);
        if (placePositions == null) {
            return;
        }
        if (placePositions.size() > 0) {
            boolean offhandCheck = false;
            int slot = InventoryUtil.findHotbarBlock(ItemEndCrystal.class);
            int old = mc.player.inventory.currentItem;
            EnumHand hand = null;
            int stackSize = getCrystalCount(false);
            alreadyAttacking = false;
            if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && (autoSwitch.getValue() == AutoSwitch.Always|| autoSwitch.getValue() == AutoSwitch.NoGap)) {
                    if (autoSwitch.getValue() == AutoSwitch.NoGap) {
                        if (mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE) {
                            return;
                        }
                    }
                    if (this.findCrystalsHotbar() == -1) return;
                    mc.player.inventory.currentItem = this.findCrystalsHotbar();
                    mc.playerController.updateController();
                }
            } else {
                offhandCheck = true;
            }
            if (autoSwitch.getValue() == AutoSwitch.Silent) {
                if (slot != -1) {
                    if (mc.player.isHandActive() && silentSwitchHand.getValue()) {
                        hand = mc.player.getActiveHand();
                    }
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                }
            }
            placeDelayCounter = 0;
            facePlaceDelayCounter = 0;
            didAnything = true;
            for (BlockPos targetBlock : placePositions) {
                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal || mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal || autoSwitch.getValue() == AutoSwitch.Silent) {
                    if (setYawPitch(targetBlock)) {
                        EntityEnderCrystal cCheck = CrystalUtil.isCrystalStuck(targetBlock.up());
                        if (cCheck != null && antiStuck.getValue()) {
                            stuckCrystal = cCheck;
                            if (debug.getValue()) {
                                Command.sendMessage("SHITS STUCK");
                            }
                        }
                        BlockUtil.placeCrystalOnBlock(targetBlock, offhandCheck ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, placeSwing.getValue());
                        if (debug.getValue()) {
                            Command.sendMessage("placing");
                        }
                        crystalsPlaced++;
                    }
                } else if (debug.getValue()) {
                    Command.sendMessage("doing yawstep on place");
                }
            }
            int newSize = getCrystalCount(offhandCheck);
            if (autoSwitch.getValue() == AutoSwitch.Silent) {
                if (slot != -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(old));
                    if (silentSwitchHand.getValue() && hand != null) {
                        mc.player.setActiveHand(hand);
                    }
                }
            }

            if (newSize == stackSize) {
                didAnything = false;
            }
        }
    }

    private int getCrystalCount(boolean offhand) {
        if (offhand) {
            return mc.player.getHeldItemOffhand().stackSize;
        } else {
            return mc.player.getHeldItemMainhand().stackSize;
        }
    }


    private void breakCrystalNoCalcs() {
        for (final Entity e : mc.world.loadedEntityList) {
            if (!(e instanceof EntityEnderCrystal)) continue;
            if (e.isDead) continue;
            if (mc.player.getDistance(e) > breakRange.getValue()) continue;
            if (!mc.player.canEntityBeSeen(e)) {
                if (raytrace.getValue()) continue;
                if (mc.player.getDistance(e) > breakRangeWall.getValue()) continue;
            }

            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                boolean shouldWeakness = true;
                if (mc.player.isPotionActive(MobEffects.STRENGTH)) {
                    if (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                        shouldWeakness = false;
                    }
                }
                if (shouldWeakness) {
                    if (!alreadyAttacking) {
                        this.alreadyAttacking = true;
                    }
                    int newSlot = -1;
                    for (int i = 0; i < 9; i++) {
                        final ItemStack stack = mc.player.inventory.getStackInSlot(i);
                        if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
                            newSlot = i;
                            mc.playerController.updateController();
                            break;
                        }
                    }
                    if (newSlot != -1) {
                        mc.player.inventory.currentItem = newSlot;
                    }
                }
            }
            EntityEnderCrystal crystal = (EntityEnderCrystal) e;
            if (setYawPitch(crystal)) {
                EntityUtil.attackEntity(crystal, this.attackPacket.getValue());

                if (debug.getValue()) {
                    Command.sendMessage("breaking");
                }
                breakDelayCounter = 0;
            } else if (debug.getValue()) {
                Command.sendMessage("doing yawstep on break");
            }
        }
    }

    private void breakCrystal(EntityEnderCrystal overwriteCrystal) {
        EntityEnderCrystal crystal;
        if (threaded.getValue()) {
            Threads threads = new Threads();
            threads.start();
            crystal = staticEnderCrystal;
        } else {
            crystal = this.getBestCrystal();
        }
        if (overwriteCrystal != null) {
            if (debug.getValue()) {
                Command.sendMessage("Overwriting Crystal");
            }
            if (CrystalUtil.calculateDamage(overwriteCrystal, mc.player, ignoreTerrain.getValue()) < maxAntiStuckDamage.getValue()) {
                crystal = overwriteCrystal;
            }
        }
        if (crystal == null) return;
        if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            boolean shouldWeakness = true;
            if (mc.player.isPotionActive(MobEffects.STRENGTH)) {
                if (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                    shouldWeakness = false;
                }
            }
            if (shouldWeakness) {
                if (!alreadyAttacking) {
                    this.alreadyAttacking = true;
                }
                int newSlot = -1;
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.player.inventory.getStackInSlot(i);
                    if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
                        newSlot = i;
                        mc.playerController.updateController();
                        break;
                    }
                }
                if (newSlot != -1) {
                    mc.player.inventory.currentItem = newSlot;
                }
            }
        }
        didAnything = true;
        if (setYawPitch(crystal)) {
            EntityUtil.attackEntity(crystal, this.attackPacket.getValue());
            if (swing.getValue() != SwingMode.None) {
                mc.player.swingArm(swing.getValue() == SwingMode.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
            }
            if (debug.getValue()) {
                Command.sendMessage("breaking");
            }
            breakDelayCounter = 0;
        } else if (debug.getValue()) {
            Command.sendMessage("doing yawstep on break");
        }
    }

    public EntityEnderCrystal getBestCrystal() {
        double bestDamage = 0;
        EntityEnderCrystal bestCrystal = null;
        for (Entity e : mc.world.loadedEntityList) {
            if (!(e instanceof EntityEnderCrystal)) continue;
            final EntityEnderCrystal crystal = (EntityEnderCrystal) e;
            for (EntityPlayer target : new ArrayList<>(mc.world.playerEntities)) {
                if (mc.player.getPositionVector().distanceTo(target.getPositionVector()) > targetRange.getValue()) continue;
                if (entityPredict.getValue() && rotateMode.getValue() == RotateMode.Off) {
                    float f = target.width / 2.0F, f1 = target.height;
                    target.setEntityBoundingBox(new AxisAlignedBB(target.posX - (double) f, target.posY, target.posZ - (double) f, target.posX + (double) f, target.posY + (double) f1, target.posZ + (double) f));
                    Entity y = CrystalUtil.getPredictedPosition(target, predictedTicks.getValue());
                    target.setEntityBoundingBox(y.getEntityBoundingBox());
                }
                double targetDamage = this.isCrystalGood(crystal, target);
                if (targetDamage <= 0) continue;
                if (targetDamage > bestDamage) {
                    bestDamage = targetDamage;
                    this.ezTarget = target;
                    bestCrystal = crystal;
                }
            }
        }
        if (bestCrystal != null && (when.getValue() == When.Both || when.getValue() == When.Break)) {
            BlockPos renderPos = bestCrystal.getPosition().down();
            clearMap(renderPos);
            renderMap.add(new RenderPos(renderPos, bestDamage));
        }
        return bestCrystal;
    }


    private ArrayList<BlockPos> getBestBlocks() {
        ArrayList<RenderPos> posArrayList = new ArrayList<>();
        if (getBestCrystal() != null && fastMode.getValue() == FastMode.Off) {
            placeTimeoutFlag = true;
            return null;
        }

        if (placeTimeoutFlag) {
            placeTimeoutFlag = false;
            return null;
        }

        for (EntityPlayer target : new ArrayList<>(mc.world.playerEntities)) {
            if (mc.player.getDistanceSq(target) > MathUtil.square(targetRange.getValue().floatValue())) continue;
            if (entityPredict.getValue()) {
                float f = target.width / 2.0F, f1 = target.height;
                target.setEntityBoundingBox(new AxisAlignedBB(target.posX - (double) f, target.posY, target.posZ - (double) f, target.posX + (double) f, target.posY + (double) f1, target.posZ + (double) f));
                Entity y = CrystalUtil.getPredictedPosition(target, predictedTicks.getValue());
                target.setEntityBoundingBox(y.getEntityBoundingBox());
            }
            for (BlockPos blockPos : CrystalUtil.possiblePlacePositions(this.placeRange.getValue().floatValue(), true, this.thirteen.getValue())) {
                double targetDamage = isBlockGood(blockPos, target);
                if (targetDamage <= 0) continue;
                posArrayList.add(new RenderPos(blockPos, targetDamage));
            }
        }

        //sorting all place positions
        if (sortBlocks.getValue()) posArrayList.sort(new DamageComparator());

        //making sure all positions are placeble and wont block each other
        if (maxCrystals.getValue() > 1) {
            final List<BlockPos> blockedPosList = new ArrayList<>();
            final List<RenderPos> toRemove = new ArrayList<>();
            for (RenderPos test : posArrayList) {
                boolean blocked = false;
                for (final BlockPos blockPos : blockedPosList) {
                    if (blockPos.getX() == test.pos.getX() && blockPos.getY() == test.pos.getY() && blockPos.getZ() == test.pos.getZ()) {
                        blocked = true;
                    }
                }
                if (!blocked) {
                    blockedPosList.addAll(getBlockedPositions(test.pos));
                } else toRemove.add(test);
            }
            posArrayList.removeAll(toRemove);
        }
        //taking the best out of the list
        int maxCrystals = this.maxCrystals.getValue();
        if (facePlacing && noMP.getValue()) {
            maxCrystals = 1;
        }
        final ArrayList<BlockPos> finalArrayList = new ArrayList<>();
        IntStream.range(0, Math.min(maxCrystals, posArrayList.size())).forEachOrdered(n -> {
            RenderPos pos = posArrayList.get(n);
            if (when.getValue() == When.Both || when.getValue() == When.Place) {
                clearMap(pos.pos);
                if (pos.pos != null) renderMap.add(pos);
            }
            finalArrayList.add(pos.pos);
        });
        return finalArrayList;
    }

    private ArrayList<BlockPos> getBlockedPositions(final BlockPos pos) {
        ArrayList<BlockPos> list = new ArrayList<>();
        list.add(pos.add(1, -1, 1));
        list.add(pos.add(1, -1, -1));
        list.add(pos.add(-1, -1, 1));
        list.add(pos.add(-1, -1, -1));
        list.add(pos.add(-1, -1, 0));
        list.add(pos.add(1, -1, 0));
        list.add(pos.add(0, -1, -1));
        list.add(pos.add(0, -1, 1));
        list.add(pos.add(1, 0, 1));
        list.add(pos.add(1, 0, -1));
        list.add(pos.add(-1, 0, 1));
        list.add(pos.add(-1, 0, -1));
        list.add(pos.add(-1, 0, 0));
        list.add(pos.add(1, 0, 0));
        list.add(pos.add(0, 0, -1));
        list.add(pos.add(0, 0, 1));
        list.add(pos.add(1, 1, 1));
        list.add(pos.add(1, 1, -1));
        list.add(pos.add(-1, 1, 1));
        list.add(pos.add(-1, 1, -1));
        list.add(pos.add(-1, 1, 0));
        list.add(pos.add(1, 1, 0));
        list.add(pos.add(0, 1, -1));
        list.add(pos.add(0, 1, 1));
        return list;
    }

    private double isCrystalGood(final EntityEnderCrystal crystal, final EntityPlayer target) {
        if (this.isPlayerValid(target)) {
            if (mc.player.canEntityBeSeen(crystal)) {
                if (mc.player.getDistanceSq(crystal) > MathUtil.square(this.breakRange.getValue().floatValue())) {
                    return 0;
                }
            } else {
                if (mc.player.getDistanceSq(crystal) > MathUtil.square(this.breakRangeWall.getValue().floatValue())) {
                    return 0;
                }
            }
            if (crystal.isDead) return 0;
            if (attemptedCrystals.contains(crystal)) return 0;
            double targetDamage = CrystalUtil.calculateDamage(crystal, target, ignoreTerrain.getValue());
            // set min damage to 2 if we want to kill the dude fast
            facePlacing = false;
            double miniumDamage = this.minBreak.getValue();
            if (((EntityUtil.getHealth(target) <= facePlaceHP.getValue()) || CrystalUtil.getArmourFucker(target, fuckArmourHP.getValue()) || fpbind.getValue().isDown()) && targetDamage < minBreak.getValue()) {
                miniumDamage = EntityUtil.isInHole(target) ? 1 : 2;
                facePlacing = true;
            }


            if (targetDamage < miniumDamage && EntityUtil.getHealth(target) - targetDamage > 0) return 0;
            double selfDamage = 0;
            if (!ignoreSelfDamage.getValue()) {
                selfDamage = CrystalUtil.calculateDamage(crystal, mc.player, ignoreTerrain.getValue());
            }
            if (selfDamage > maxSelfBreak.getValue()) return 0;
            if (EntityUtil.getHealth(mc.player) - selfDamage <= 0 && this.antiSuicide.getValue()) return 0;
            switch (crystalLogic.getValue()) {
                case Safe:
                    return targetDamage - selfDamage;
                case Damage:
                    return targetDamage;
                case Nearby:
                    double distance = mc.player.getDistanceSq(crystal);
                    return targetDamage - distance;
            }
        }
        return 0;
    }

    private double isBlockGood(BlockPos blockPos, EntityPlayer target) {
        if (this.isPlayerValid(target)) {
            // if raytracing and cannot see block
            if (!CrystalUtil.canSeePos(blockPos) && raytrace.getValue()) return 0;
            // if cannot see pos use wall range, else use normal
            if (!CrystalUtil.canSeePos(blockPos)) {
                if (mc.player.getDistanceSq(blockPos) > MathUtil.square(this.placeRangeWall.getValue().floatValue())) {
                    return 0;
                }
            } else {
                if (mc.player.getDistanceSq(blockPos) > MathUtil.square(this.placeRange.getValue().floatValue())) {
                    return 0;
                }
            }
            double targetDamage = CrystalUtil.calculateDamage(blockPos, target, ignoreTerrain.getValue());

            facePlacing = false;
            double miniumDamage = this.minPlace.getValue();
            if (((EntityUtil.getHealth(target) <= facePlaceHP.getValue()) || CrystalUtil.getArmourFucker(target, fuckArmourHP.getValue()) || fpbind.getValue().isDown()) && targetDamage < minPlace.getValue()) {
                miniumDamage = EntityUtil.isInHole(target) ? 1 : 2;
                facePlacing = true;
            }

            if (targetDamage < miniumDamage && EntityUtil.getHealth(target) - targetDamage > 0) return 0;
            double selfDamage = 0;
            if (!ignoreSelfDamage.getValue()) {
                selfDamage = CrystalUtil.calculateDamage(blockPos, mc.player, ignoreTerrain.getValue());
            }
            if (selfDamage > maxSelfPlace.getValue()) return 0;
            if (EntityUtil.getHealth(mc.player) - selfDamage <= 0 && this.antiSuicide.getValue()) return 0;
            switch (crystalLogic.getValue()) {
                case Safe:
                    return targetDamage - selfDamage;
                case Damage:
                    return targetDamage;
                case Nearby:
                    double distance = mc.player.getDistanceSq(blockPos);
                    return targetDamage - distance;
            }
        }
        return 0;
    }

    private boolean isPlayerValid(EntityPlayer player) {
        if (player.getHealth() + player.getAbsorptionAmount() <= 0 || player == mc.player) return false;
        if (PutaHacknn.friendManager.isFriend(player.getName())) return false;
        if (player.getName().equals(mc.player.getName())) return false;
        if (player.getDistanceSq(mc.player) > 13 * 13) return false;
        if (this.palceObiFeet.getValue() && obiFeetCounter >= timeoutTicksObiFeet.getValue() && mc.player.getDistance(player) < 5) {
            try {
                this.blockObiNextToPlayer(player);
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void blockObiNextToPlayer(EntityPlayer player) {
        if (ObiYCheck.getValue() && Math.floor(player.posY) == Math.floor(mc.player.posY)) return;
        obiFeetCounter = 0;
        BlockPos pos = EntityUtil.getFlooredPos(player).down();
        if (EntityUtil.isInHole(player) || mc.world.getBlockState(pos).getBlock() == Blocks.AIR) return;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                BlockPos checkPos = pos.add(i, 0, j);
                if (mc.world.getBlockState(checkPos).getMaterial().isReplaceable()) {
                    if (PlayerUtil.findObiInHotbar() != -1) {
                        int oldSlot = mc.player.inventory.currentItem;
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtil.findHotbarBlock(BlockObsidian.class)));
                        BlockUtil.placeBlock(checkPos, EnumHand.MAIN_HAND, rotateObiFeet.getValue(), packetSafe.getValue(), true);
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                    }
                }
            }
        }
    }

    public static void drawCircle(float x, float y, float z, float radius, Color color, float lineWidth) {
        BlockPos pos = new BlockPos(x, y, z);
        AxisAlignedBB bb = new AxisAlignedBB((double) pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double) pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY,
                (double) pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ,
                (double) (pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX,
                (double) (pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double) (pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            drawCircleVertices(bb, radius, color, lineWidth);
        }
    }

    public static void drawCircleVertices(AxisAlignedBB bb, float radius, Color color, float lineWidth) {
        float r = (float) color.getRed() / 255.0f;
        float g = (float) color.getGreen() / 255.0f;
        float b = (float) color.getBlue() / 255.0f;
        float a = (float) color.getAlpha() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(lineWidth);
        for (int i = 0; i < 360; i++) {
            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.getCenter().x + (Math.sin((i * 3.1415926D / 180)) * radius), bb.minY, bb.getCenter().z + (Math.cos((i * 3.1415926D / 180)) * radius)).color(r, g, b, a).endVertex();
            buffer.pos(bb.getCenter().x + (Math.sin(((i + 1) * 3.1415926D / 180)) * radius), bb.minY, bb.getCenter().z + (Math.cos(((i + 1) * 3.1415926D / 180)) * radius)).color(r, g, b, a).endVertex();
            tessellator.draw();
        }
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private int findCrystalsHotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }

    private boolean setYawPitch(EntityEnderCrystal crystal) {
        if (rotateMode.getValue() == RotateMode.Off || rotateMode.getValue() == RotateMode.Place) return true;
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), crystal.getPositionEyes(mc.getRenderPartialTicks()));
        float yaw = angle[0];
        float pitch = angle[1];
        float spoofedYaw = PutaHacknn.rotationManager.getSpoofedYaw();
        if (Math.abs(spoofedYaw - yaw) > maxYaw.getValue() && Math.abs(spoofedYaw - 360 - yaw) > maxYaw.getValue() && Math.abs(spoofedYaw + 360 - yaw) > maxYaw.getValue()) {
            PutaHacknn.rotationManager.setPlayerRotations(Math.abs(spoofedYaw - yaw) < 180 ? spoofedYaw > yaw ? (spoofedYaw - maxYaw.getValue()) : (spoofedYaw + maxYaw.getValue()) : spoofedYaw > yaw ? (spoofedYaw + maxYaw.getValue()) : (spoofedYaw - maxYaw.getValue()), pitch);
            return false;
        } else {
            PutaHacknn.rotationManager.setPlayerRotations(yaw, pitch);
        }
        return true;
    }

    public boolean setYawPitch(BlockPos pos) {
        if (rotateMode.getValue() == RotateMode.Off || rotateMode.getValue() == RotateMode.Break) return true;
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
        float yaw = angle[0];
        float pitch = angle[1];
        float spoofedYaw = PutaHacknn.rotationManager.getSpoofedYaw();
        if (Math.abs(spoofedYaw - yaw) > maxYaw.getValue() && Math.abs(spoofedYaw - 360 - yaw) > maxYaw.getValue() && Math.abs(spoofedYaw + 360 - yaw) > maxYaw.getValue()) {
            PutaHacknn.rotationManager.setPlayerRotations(Math.abs(spoofedYaw - yaw) < 180 ? spoofedYaw > yaw ? (spoofedYaw - maxYaw.getValue()) : (spoofedYaw + maxYaw.getValue()) : spoofedYaw > yaw ? (spoofedYaw + maxYaw.getValue()) : (spoofedYaw - maxYaw.getValue()), pitch);
            return false;
        } else {
            PutaHacknn.rotationManager.setPlayerRotations(yaw, pitch);
        }
        return true;
    }

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        if (renderMap.isEmpty()) return;
        boolean outline = false;
        boolean solid = false;
        switch (mode.getValue()) {
            case Pretty:
                outline = true;
                solid = true;
                break;
            case Solid:
                outline = false;
                solid = true;
                break;
            case Outline:
                outline = true;
                solid = false;
                break;
        }
        List<RenderPos> toRemove = new ArrayList<>();
        for (RenderPos renderPos : renderMap) {
            int fillAlpha = renderFillColour.getAlpha();
            int boxAlpha = renderBoxColour.getAlpha();
            if (currentTargets.contains(renderPos.pos)) {
                renderPos.fadeTimer = 0;
            } else if (!fade.getValue()) {
                toRemove.add(renderPos);
            } else {
                renderPos.fadeTimer++;
                fillAlpha = (int) (fillAlpha - (fillAlpha * (renderPos.fadeTimer / fadeTime.getValue())));
                boxAlpha = (int) (boxAlpha - (boxAlpha * (renderPos.fadeTimer / fadeTime.getValue())));
            }
            if (renderPos.fadeTimer > fadeTime.getValue())
                toRemove.add(renderPos);
            if (toRemove.contains(renderPos)) continue;
            if (circleRender.getValue()) {
                drawCircle(renderPos.pos.getX(), renderPos.pos.getY() + 1, renderPos.pos.getZ(), circleRadius.getValue(), new Color(renderFillColour.getRed(), renderFillColour.getGreen(), renderFillColour.getBlue(), boxAlpha), (float) width.getValue());
            } else {
                RenderUtil.drawBoxESP((flat.getValue()) ? new BlockPos(renderPos.pos.getX(), renderPos.pos.getY() + 1, renderPos.pos.getZ()) : renderPos.pos, new Color(renderFillColour.getRed(), renderFillColour.getGreen(), renderFillColour.getBlue(), Math.max(fillAlpha, 0)), new Color(renderBoxColour.getRed(), renderBoxColour.getGreen(), renderBoxColour.getBlue(), Math.max(boxAlpha, 0)), width.getValue(), outline, solid, true, (flat.getValue()) ? height.getValue() : 0f, false, false, false, false, 0);
            }
            if (renderDamage.getValue())
                RenderUtil.drawText(renderPos.pos, String.valueOf(MathUtil.round(renderPos.damage, 1)));
        }
        renderMap.removeAll(toRemove);
    }

    @Override
    public void onEnable() {
        placeTimeout = this.placeDelay.getValue();
        breakTimeout = this.breakDelay.getValue();
        placeTimeoutFlag = false;
        ezTarget = null;
        facePlacing = false;
        attemptedCrystals.clear();
        hasPacketBroke = false;
        alreadyAttacking = false;
        obiFeetCounter = 0;
        crystalLatency = 0;
        start = 0;
        staticEnderCrystal = null;
        staticPos = null;
        crystalsPlaced = 0;
        crystalsPlacedTimer.reset();
    }

    public float getCPS() {
        return crystalsPlaced / (crystalsPlacedTimer.getPassedTimeMs() / 1000f);
    }

    @Override
    public String getDisplayInfo() {
        switch (arrayListMode.getValue()) {
            case Latency:
                return crystalLatency + "ms";
            case CPS:
                return "" + MathUtil.round(getCPS(), 2);
            case Player:
                return this.ezTarget != null ? this.ezTarget.getName() : null;
            default:
                return "";
        }
    }

    static class RenderPos {
        public RenderPos(BlockPos pos, Double damage) {
            this.pos = pos;
            this.damage = damage;
        }

        Double damage;
        double fadeTimer;
        BlockPos pos;
    }

    static class DamageComparator implements Comparator<RenderPos> {
        @Override
        public int compare(RenderPos a, RenderPos b) {
            return b.damage.compareTo(a.damage);
        }
    }

    final class Threads extends Thread {
        EntityEnderCrystal bestCrystal;

        public Threads() {
        }

        @Override
        public void run() {
            bestCrystal = LogAura.INSTANCE.getBestCrystal();
            LogAura.INSTANCE.staticEnderCrystal = bestCrystal;
        }
    }

}
