package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class AutoCrystalRewrite extends Module {

    /**
     * Credits: By Primooctopus33
     */

    public enum PlaceLogic {
        BreakPlace,
        PlaceBreak
    }

    public enum AutoSwitch {
        Silent,
        Normal,
        None
    }

    public enum AttackMode {
        Id,
        Normal
    }

    public enum Swing {
        Offhand,
        Mainhand
    }

    public AutoCrystalRewrite() {
        super("AutoCrystalRewrite", "auto but crystal", Category.COMBAT, true, false, false);
        INSTANCE = this;
    }

    public static AutoCrystalRewrite INSTANCE;
    public final Setting<PlaceLogic> placeLogic = this.register(new Setting("Place Logic", PlaceLogic.BreakPlace));
    public final Setting<Float> placeRange = this.register(new Setting("Place Range", 5.0f, 0.0f, 10.0f));
    public final Setting<Float> placeWallRange = this.register(new Setting("Place Wall Range", 3.5f, 0.0f, 10.0f));
    public final Setting<Integer> placeDelay = this.register(new Setting("Place Delay", 6, 0, 400));
    public final Setting<Boolean> oneDotThirteen = this.register(new Setting("1.13+", false));
    public final Setting<Float> maxSelfDamage = this.register(new Setting("Max Self Damage", 10.0f, 0.0f, 36.0f));
    public final Setting<Float> minPlaceDamage = this.register(new Setting("Min Place Damage", 6.0f, 0.0f, 36.0f));
    public final Setting<Float> facePlaceHealth = this.register(new Setting("Face Place Health", 14.0f, 0.0f, 36.0f));
    public final Setting<Integer> facePlaceArmor = this.register(new Setting("Face Place Armor", 15, 0, 100));
    public final Setting<AutoSwitch> autoSwitch = this.register(new Setting("Auto Switch", AutoSwitch.Silent));
    public final Setting<Boolean> fake = this.register(new Setting("Fake", false));
    public final Setting<Boolean> swing = this.register(new Setting("Swing", true));
    public final Setting<Swing> swingMode = this.register(new Setting("Swing Mode", Swing.Mainhand));
    public final Setting<Float> breakRange = this.register(new Setting("Break Range", 5.0f, 0.0f, 10.0f));
    public final Setting<Float> breakWallsRange = this.register(new Setting("Break Walls Range", 5.0f, 0.0f, 10.0f));
    public final Setting<Integer> breakDelay = this.register(new Setting("Break Delay", 20, 0, 400));
    public final Setting<Float> minBreakDamage = this.register(new Setting("Min Break Damage", 3.0f, 0.0f, 36.0f));
    public final Setting<AttackMode> attackMode = this.register(new Setting("Attack Mode", AttackMode.Normal));
    public final Setting<Boolean> packetBreak = this.register(new Setting("Packet Break", true));
    public final Setting<Boolean> instantPlaceAfter = this.register(new Setting("Instant Place After", false));
    public final Setting<Boolean> attackById = this.register(new Setting("Attack By Id", true));
    public final Setting<Integer> idBypassCooldown = this.register(new Setting("Id Bypass Cooldown", 15, 0, 100));
    public final Setting<Boolean> setDead = this.register(new Setting("Set Dead", true));
    public final Setting<Boolean> antiSuicide = this.register(new Setting("Anti Suicide", true));
    public final Setting<Float> targetRange = this.register(new Setting("Target Range", 10.0f, 0.0f, 15.0f));
    public final Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
    public final Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting("Alpha", 40, 0, 255));
    public boolean shouldIdBypass = false;
    public Timer placeTimer = new Timer();
    public Timer breakTimer = new Timer();
    public EntityLivingBase currentTarget;
    public BlockPos renderPos;
    public BlockPos lastPos;
    public AxisAlignedBB renderBB;
    public long startTime = 0;
    public float time;
    public int idBypassTicks;
    public float yaw;
    public float pitch;
    public boolean placing;

    @Override
    public void onUpdate() {
        if (fullNullCheck() || fake.getValue()) {
            return;
        }
        idBypassTicks++;
        if (idBypassTicks > ThreadLocalRandom.current().nextInt(idBypassCooldown.getValue() - 2, idBypassCooldown.getValue())) {
            shouldIdBypass = true;
            idBypassTicks = 0;
        } else {
            shouldIdBypass = false;
        }
        EntityLivingBase target = EntityUtil.getTarget(targetRange.getValue());
        if (target == null) {
            placing = false;
            currentTarget = null;
        } else {
            currentTarget = target;
            if (placeLogic.getValue() == PlaceLogic.BreakPlace) {
                breakCrystal(target);
                placeCrystal(target);
            } else {
                placeCrystal(target);
                breakCrystal(target);
            }
        }
    }

    public void placeCrystal(EntityLivingBase target) {
        NonNullList<BlockPos> placeList = NonNullList.create();
        BlockUtil.getSphere(new BlockPos(mc.player.getPositionVector()), placeRange.getValue(), placeRange.getValue().intValue(), false, true, 0).stream().forEach(pos -> {
            if (!BlockUtil.canPlaceCrystal(pos, true, oneDotThirteen.getValue())) {
                return;
            }
            placeList.add(pos);
        });
        float minimumDamage = getMinDamage(target, true);
        BlockPos finalPos = null;
        NonNullList<BlockPos> finalPlaceList = NonNullList.create();
        for (BlockPos pos : placeList) {
            if (DamageUtil.calculateDamage(pos, mc.player) > maxSelfDamage.getValue() || DamageUtil.calculateDamage(pos, target) < minimumDamage || DamageUtil.calculateDamage(pos, mc.player) > (mc.player.getHealth() + mc.player.getAbsorptionAmount()) && antiSuicide.getValue() || !CrystalUtil.canSeePos(pos) && new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f).distanceTo(mc.player.getPositionVector()) > placeWallRange.getValue()) continue;
            if (DamageUtil.calculateDamage(pos, mc.player) < maxSelfDamage.getValue() && new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f).distanceTo(mc.player.getPositionVector()) < placeRange.getValue()) {
                finalPlaceList.add(pos);
            }
        }
        finalPos = finalPlaceList.stream().filter(pos -> DamageUtil.calculateDamage(pos, mc.player) < maxSelfDamage.getValue()).filter(pos -> DamageUtil.calculateDamage(pos, target) > minimumDamage).max(Comparator.comparingDouble(pos -> DamageUtil.calculateDamage(pos, target))).orElse(null);
        if (finalPos != null) {
            if (rotate.getValue()) {
                rotateToPos(finalPos);
            }
            placeCrystalOnBlock(finalPos);
        }
    }

    public void breakCrystal(EntityLivingBase target) {
        EntityEnderCrystal crystal;
        float minimumDamage = getMinDamage(target, false);
        NonNullList<EntityEnderCrystal> crystalList = NonNullList.create();
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                if (entity.getPositionVector().distanceTo(mc.player.getPositionVector()) > breakRange.getValue()) continue;
                if (DamageUtil.calculateDamage(entity, mc.player) > maxSelfDamage.getValue() || !mc.player.canEntityBeSeen(entity) && entity.getPositionVector().distanceTo(mc.player.getPositionVector()) > breakWallsRange.getValue() || DamageUtil.calculateDamage(entity, mc.player) > (mc.player.getAbsorptionAmount() + mc.player.getHealth())) continue;
                if (DamageUtil.calculateDamage(entity, target) > minimumDamage) {
                    crystalList.add((EntityEnderCrystal) entity);
                }
            }
        }
        crystal = crystalList.stream().filter(entity -> DamageUtil.calculateDamage(entity, mc.player) < maxSelfDamage.getValue() && DamageUtil.calculateDamage(entity, target) > minimumDamage).max(Comparator.comparingDouble(entity -> DamageUtil.calculateDamage(entity, target))).orElse(null);
        if (crystal != null) {
            if (breakTimer.passedMs(breakDelay.getValue())) {
                if (rotate.getValue()) {
                    rotateToEntity(crystal);
                }
                if (attackMode.getValue() == AttackMode.Normal) {
                    if (swing.getValue()) {
                        mc.player.swingArm(swingMode.getValue() == Swing.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
                    }
                    EntityUtil.attackEntity(crystal, packetBreak.getValue(), false);
                } else {
                    attackById(crystal.entityId);
                }
                breakTimer.reset();
            }
        }
    }

    public void attackById(int id) {
        CPacketUseEntity attackPacket = new CPacketUseEntity();
        attackPacket.entityId = id;
        attackPacket.action = CPacketUseEntity.Action.ATTACK;
        mc.player.connection.sendPacket(attackPacket);
        if (swing.getValue()) {
            mc.player.swingArm(swingMode.getValue() == Swing.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
        }
    }

    public float getMinDamage(EntityLivingBase target, boolean place) {
        float targetHealth = target.getHealth() + target.getAbsorptionAmount();
        return (targetHealth < facePlaceHealth.getValue() || DamageUtil.isArmorLow((EntityPlayer) target, facePlaceArmor.getValue())) ? 2.0f : place ? minPlaceDamage.getValue() : minBreakDamage.getValue();
    }

    public void placeCrystalOnBlock(BlockPos pos) {
        EnumHand crystalHand = mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        int crystalSlot = InventoryUtil.findHotbarBlock(ItemEndCrystal.class);
        int oldSlot = mc.player.inventory.currentItem;
        if (crystalSlot != -1 && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (autoSwitch.getValue() == AutoSwitch.Normal) {
                mc.player.inventory.currentItem = crystalSlot;
            } else if (autoSwitch.getValue() == AutoSwitch.Silent) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(crystalSlot));
            }
        }
        if (placeTimer.passedMs(placeDelay.getValue())) {
            placing = true;
            if (swing.getValue()) {
                mc.player.swingArm(swingMode.getValue() == Swing.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
            }
            BlockUtil.placeCrystalOnBlock(pos, crystalHand, false, false);
            renderPos = pos;
            placing = false;
            placeTimer.reset();
        }
        if (oldSlot != -1 && autoSwitch.getValue() == AutoSwitch.Silent) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (fake.getValue()) {
            return;
        }
        if (event.getPacket() instanceof SPacketSpawnObject && ((SPacketSpawnObject) event.getPacket()).getType() == 51 && shouldIdBypass && attackById.getValue() && currentTarget != null) {
            CPacketUseEntity attackPacket = new CPacketUseEntity();
            attackPacket.entityId = ((SPacketSpawnObject) event.getPacket()).getEntityID();
            attackPacket.action = CPacketUseEntity.Action.ATTACK;
            mc.player.connection.sendPacket(attackPacket);
        }
        try {
            if (event.getPacket() instanceof SPacketSoundEffect && ((SPacketSoundEffect) event.getPacket()).getCategory() == SoundCategory.BLOCKS && ((SPacketSoundEffect) event.getPacket()).getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE && setDead.getValue()) {
                mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal && entity.getPositionVector().distanceTo(new Vec3d(((SPacketSoundEffect) event.getPacket()).getX(), ((SPacketSoundEffect) event.getPacket()).getY(), ((SPacketSoundEffect) event.getPacket()).getZ())) < 7.0f).forEach(entity -> {
                    entity.setDead();
                    mc.world.removeEntityFromWorld(entity.getEntityId());
                });
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                if (setDead.getValue()) {
                    mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal && entity.getDistance(((SPacketExplosion) event.getPacket()).getX(), ((SPacketExplosion) event.getPacket()).getY(), ((SPacketExplosion) event.getPacket()).getZ()) < 6).forEach(entity -> {
                        Objects.requireNonNull(mc.world.getEntityByID(entity.getEntityId())).setDead();
                        mc.world.removeEntityFromWorld(entity.getEntityId());
                    });
                }
                if (instantPlaceAfter.getValue()) {
                    EntityLivingBase target = EntityUtil.getTarget(targetRange.getValue());
                    if (target != null && target.getPositionVector().distanceTo(mc.player.getPositionVector()) < targetRange.getValue()) {
                        BlockPos placePos = new BlockPos(Math.floor(((SPacketExplosion) event.getPacket()).getX()), Math.floor(((SPacketExplosion) event.getPacket()).getY()), Math.floor(((SPacketExplosion) event.getPacket()).getZ())).down();
                        if (DamageUtil.calculateDamage(placePos, target) > minPlaceDamage.getValue() && DamageUtil.calculateDamage(placePos, mc.player) < maxSelfDamage.getValue() && DamageUtil.calculateDamage(placePos, mc.player) < (mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
                            if (mc.player.getPositionVector().distanceTo(new Vec3d(placePos.getX() + 0.5f, placePos.getY(), placePos.getZ() + 0.5f)) < placeRange.getValue() || mc.player.getPositionVector().distanceTo(new Vec3d(placePos.getX() + 0.5f, placePos.getY(), placePos.getZ() + 0.5f)) < placeWallRange.getValue() && !CrystalUtil.canSeePos(placePos)) {
                                placeCrystalOnBlock(placePos);
                            }
                        }
                    }
                }
            }
            if (event.getPacket() instanceof SPacketDestroyEntities && setDead.getValue()) {
                for (int ids : ((SPacketDestroyEntities) event.getPacket()).getEntityIDs()) {
                    if (mc.world.getEntityByID(ids) instanceof EntityEnderCrystal) {
                        Objects.requireNonNull(mc.world.getEntityByID(ids)).setDead();
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (fake.getValue()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer && currentTarget != null) {
            ((CPacketPlayer) event.getPacket()).yaw = yaw;
            ((CPacketPlayer) event.getPacket()).pitch = pitch;
            ((CPacketPlayer) event.getPacket()).rotating = true;
        }
    }

    public void rotateToPos(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f));
        yaw = angle[0];
        pitch = angle[1];
    }

    public void rotateToEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
        yaw = angle[0];
        pitch = angle[1];
    }

    @Override
    public void onDisable() {
        renderBB = null;
        renderPos = null;
        lastPos = null;
        startTime = 0L;
        time = 0.0f;
        placeTimer.reset();
        breakTimer.reset();
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (renderPos != null && !fake.getValue()) {
            EntityLivingBase target = EntityUtil.getTarget(targetRange.getValue());
            if (lastPos == null) {
                lastPos = renderPos;
                renderBB = new AxisAlignedBB(renderPos);
                time = 0.0f;
                startTime = System.currentTimeMillis();
            }
            if (!lastPos.equals(renderPos)) {
                lastPos = renderPos;
                time = 0.0f;
            }
            double x = renderPos.getX() - renderBB.minX;
            double y = renderPos.getY() - renderBB.minY;
            double z = renderPos.getZ() - renderBB.minZ;
            float moveSpeed = time / 1150.0f * 0.8f;
            if (moveSpeed > 1.0f) {
                moveSpeed = 1.0f;
            }
            if (target != null) {
                drawText(renderBB, MathUtil.round(DamageUtil.calculateDamage(renderPos, mc.player), 1) + " / " + MathUtil.round(DamageUtil.calculateDamage(renderPos, target), 1));
            }
            Color renderColor = new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());
            Color renderOutlineColor = new Color(red.getValue(), green.getValue(), blue.getValue(), 160);
            renderBB = renderBB.offset(x * moveSpeed, y * moveSpeed, z * moveSpeed);
            double amount = MathUtil.normalize((System.currentTimeMillis() - startTime), 0.0, 80.0D);
            amount = MathHelper.clamp(amount / 2.0, 0.0, 0.5);
            if (!placing && target == null) {
                RenderUtil.renderBB(7, renderBB.shrink(amount), renderColor, renderColor);
                RenderUtil.renderBB(3, renderBB.shrink(amount), renderOutlineColor, renderOutlineColor);
            } else {
                RenderUtil.renderBB(7, target == null && renderPos != null ? renderBB.shrink(0.5f).grow(amount) : renderBB.shrink(0.5f).shrink(amount), renderColor, renderColor);
                RenderUtil.renderBB(3, target == null && renderPos != null ? renderBB.shrink(0.5f).grow(amount) : renderBB.shrink(0.5f).shrink(amount), renderOutlineColor, renderOutlineColor);
            }
            GlStateManager.pushMatrix();
            GlStateManager.resetColor();
            GlStateManager.popMatrix();
            time += renderBB.equals(new AxisAlignedBB(renderPos)) ? 0.0f : 50.0f;
        }
    }

    public static void drawText(AxisAlignedBB pos, String text) {
        if (pos == null) {
            return;
        }
        GlStateManager.pushMatrix();
        RenderUtil.glBillboardDistanceScaled((float) pos.minX + 0.5f, (float) pos.minY + 0.5f, (float) pos.minZ + 0.5f, RenderUtil.mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-((double) PutaHacknn.textManager.getStringWidth(text) / 2.0), 0.0, 0.0);
        PutaHacknn.textManager.drawStringCustomFont(text, 0.0f, 0.0f, ColorUtil.toARGB(255, 255, 255, 255), true);
        GlStateManager.popMatrix();
    }

}
