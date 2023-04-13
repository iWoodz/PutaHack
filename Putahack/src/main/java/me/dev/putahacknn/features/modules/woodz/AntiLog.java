package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.event.events.DeathEvent;
import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;

public class AntiLog extends Module {

    public AntiLog() {
        super("AntiLog", "who knew woodz was a birch log all along", Category.WOODZ, true, false, false);
    }

    public enum Logic {
        PlaceBreak,
        BreakPlace
    }

    public final Setting<Logic> logic = this.register(new Setting("Logic", Logic.BreakPlace));
    public final Setting<Float> minDamage = this.register(new Setting("Min Damage", 7.0f, 0.0f, 36.0f));
    public final Setting<Boolean> deathDisable = this.register(new Setting("Death Disable", true));
    public final Setting<Boolean> oneDotThirteen = this.register(new Setting("One Dot Thirteen", false));
    public final Setting<Float> placeRange = this.register(new Setting("Place Range", 5.0f, 0.0f, 10.0f));
    public final Setting<Integer> placeDelay = this.register(new Setting("Place Delay", 10, 0, 100));
    public final Setting<Float> breakRange = this.register(new Setting("Break Range", 5.0f, 0.0f, 10.0f));
    public final Setting<Integer> breakDelay = this.register(new Setting("Break Delay", 10, 0, 100));
    public final Setting<Boolean> extrapolation = this.register(new Setting("Extrapolation", true));
    public final Setting<Integer> extrapolationTicks = this.register(new Setting("Extrapolation Ticks", 3, 0, 10));
    public final Setting<Boolean> idPredict = this.register(new Setting("Id Predict", true));
    public final Setting<Boolean> setDead = this.register(new Setting("Set Dead", true));
    public final Setting<Boolean> silent = this.register(new Setting("Silent", true));
    public final Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
    public final Setting<Boolean> swing = this.register(new Setting("Swing", false));
    public final Setting<Boolean> packet = this.register(new Setting("Packet", true));
    public Timer placeTimer = new Timer();
    public Timer timer = new Timer();

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        NonNullList<BlockPos> placePositions = NonNullList.create();
        BlockUtil.getSphere(new BlockPos(mc.player.getPositionVector()), placeRange.getValue(), placeRange.getValue().intValue(), false, true, 0).stream().filter(pos -> BlockUtil.canPlaceCrystal(pos, true, oneDotThirteen.getValue())).filter(pos -> DamageUtil.calculateDamage(pos, mc.player) > minDamage.getValue()).forEach(placePositions::add);
        BlockPos finalPlacePos = placePositions.stream().filter(pos -> mc.player.getPositionVector().distanceTo(new Vec3d(pos)) < placeRange.getValue()).max(Comparator.comparingDouble(pos -> DamageUtil.calculateDamage(pos, mc.player))).orElse(null);
        if (logic.getValue() == Logic.PlaceBreak) {
            if (extrapolation.getValue()) {
                Vec3d playerPos = MathUtil.extrapolatePlayerPosition(mc.player, extrapolationTicks.getValue());
                if (findFirstUnsafePos(playerPos) != null) {
                    BlockUtil.placeCrystalOnBlock(findFirstUnsafePos(playerPos), EnumHand.MAIN_HAND, swing.getValue(), false, silent.getValue());
                }
            }
            if (finalPlacePos != null) {
                if (placeTimer.passedMs(placeDelay.getValue())) {
                    BlockUtil.placeCrystalOnBlock(finalPlacePos, EnumHand.MAIN_HAND, swing.getValue(), false, silent.getValue());
                    placeTimer.reset();
                }
            }
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderCrystal && DamageUtil.calculateDamage(entity, mc.player) > minDamage.getValue() && entity.getPositionVector().distanceTo(mc.player.getPositionVector()) < breakRange.getValue()) {
                    if (timer.passedMs(breakDelay.getValue())) {
                        EntityUtil.attackEntity(entity, packet.getValue(), swing.getValue());
                        timer.reset();
                    }
                }
            }
        } else if (logic.getValue() == Logic.BreakPlace) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderCrystal && DamageUtil.calculateDamage(entity, mc.player) > minDamage.getValue() && entity.getPositionVector().distanceTo(mc.player.getPositionVector()) < breakRange.getValue()) {
                    if (timer.passedMs(breakDelay.getValue())) {
                        EntityUtil.attackEntity(entity, packet.getValue(), swing.getValue());
                        timer.reset();
                    }
                }
            }
            if (extrapolation.getValue()) {
                Vec3d playerPos = MathUtil.extrapolatePlayerPosition(mc.player, extrapolationTicks.getValue());
                if (findFirstUnsafePos(playerPos) != null) {
                    BlockUtil.placeCrystalOnBlock(findFirstUnsafePos(playerPos), EnumHand.MAIN_HAND, swing.getValue(), false, silent.getValue());
                }
            }
            if (finalPlacePos != null) {
                if (placeTimer.passedMs(placeDelay.getValue())) {
                    BlockUtil.placeCrystalOnBlock(finalPlacePos, EnumHand.MAIN_HAND, swing.getValue(), false, silent.getValue());
                    placeTimer.reset();
                }
            }
        }
    }

    public BlockPos findFirstUnsafePos(Vec3d vec) {
        BlockPos finalPos = null;
        BlockPos targetPos = new BlockPos(vec).down();
        if (BlockUtil.canPlaceCrystal(targetPos, true, oneDotThirteen.getValue())) {
            finalPos = targetPos;
        } else if (BlockUtil.canPlaceCrystal(targetPos.north(), true, oneDotThirteen.getValue())) {
            finalPos = targetPos.north();
        } else if (BlockUtil.canPlaceCrystal(targetPos.east(), true, oneDotThirteen.getValue())) {
            finalPos = targetPos.east();
        } else if (BlockUtil.canPlaceCrystal(targetPos.south(), true, oneDotThirteen.getValue())) {
            finalPos = targetPos.south();
        } else if (BlockUtil.canPlaceCrystal(targetPos.west(), true, oneDotThirteen.getValue())) {
            finalPos = targetPos.west();
        }
        return finalPos;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (setDead.getValue()) {
            if (event.getPacket() instanceof SPacketSoundEffect && ((SPacketSoundEffect) event.getPacket()).getCategory() == SoundCategory.BLOCKS && ((SPacketSoundEffect) event.getPacket()).getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(((SPacketSoundEffect) event.getPacket()).getX(), ((SPacketSoundEffect) event.getPacket()).getY(), ((SPacketSoundEffect) event.getPacket()).getZ()) < breakRange.getValue()) {
                        entity.setDead();
                    }
                }
            }
        }
        if (idPredict.getValue()) {
            if (event.getPacket() instanceof SPacketSpawnObject) {
                if (((SPacketSpawnObject) event.getPacket()).getType() == 51 && new Vec3d(((SPacketSpawnObject) event.getPacket()).getX(), ((SPacketSpawnObject) event.getPacket()).getY(), ((SPacketSpawnObject) event.getPacket()).getZ()).distanceTo(mc.player.getPositionVector()) < breakRange.getValue()) {
                    CPacketUseEntity attackPacket = new CPacketUseEntity();
                    attackPacket.entityId = ((SPacketSpawnObject) event.getPacket()).getEntityID();
                    attackPacket.action = CPacketUseEntity.Action.ATTACK;
                    mc.player.connection.sendPacket(attackPacket);
                    if (swing.getValue()) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.player == mc.player && deathDisable.getValue()) {
            this.disable();
        }
    }

}
