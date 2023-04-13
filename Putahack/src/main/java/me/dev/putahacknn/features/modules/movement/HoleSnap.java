package me.dev.putahacknn.features.modules.movement;

import me.dev.putahacknn.event.events.MoveEvent;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HoleSnap extends Module {

    public HoleSnap() {
        super("HoleSnap", "snap hole", Category.MOVEMENT, true, false, false);
    }

    public final Setting<Float> holeHRange = this.register(new Setting("Hole H Range", 3.5f, 0.0f, 10.0f));
    public final Setting<Float> holeVRange = this.register(new Setting("Hole V Range", 2.0f, 0.0f, 10.0f));
    public final Setting<Boolean> step = this.register(new Setting("Step", true));
    public final Setting<Float> stepHeight = this.register(new Setting("Step Height", 2.0f, 0.0f, 4.0f));
    public final Setting<StepMode> stepMode = this.register(new Setting("Step Mode", StepMode.Vanilla));
    public final Setting<Boolean> timer = this.register(new Setting("Timer", true));
    public final Setting<Float> timerMultiplier = this.register(new Setting("Timer Multiplier", 4.5f, 0.0f, 10.0f));
    public final Setting<Boolean> disableNoHoles = this.register(new Setting("Disable No Holes", false));
    public final Setting<Boolean> fake = this.register(new Setting("Fake", false));
    /**
    Credits: By Primooctopus33
     */

    public enum StepMode {
        NCP,
        Vanilla
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
        mc.timer.tickLength = 50.0f;
        if (step.getValue() && stepMode.getValue() == StepMode.NCP && Step.getInstance().isEnabled()) {
            Step.getInstance().disable(false);
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (findHolePosition() == null || fullNullCheck()) {
            Command.sendMessage("No holes found, Disabling!");
            mc.player.stepHeight = 0.5f;
            mc.timer.tickLength = 50.0f;
            this.disable();
        }
        if (step.getValue() && stepMode.getValue() == StepMode.Vanilla) {
            mc.player.stepHeight = stepHeight.getValue();
        } else if (step.getValue() && stepMode.getValue() == StepMode.NCP) {
            Step.getInstance().vanilla.setValue(false);
            Step.getInstance().enable(false);
        } else {
            mc.player.stepHeight = 0.5f;
        }
        if (timer.getValue()) {
            mc.timer.tickLength = 50.0f / timerMultiplier.getValue();
        } else {
            mc.timer.tickLength = 50.0f;
        }
        if (findHolePosition() != null) {
            Vec3d holePosition = new Vec3d(findHolePosition().getX() + 0.5f, mc.player.posY, findHolePosition().getZ() + 0.5f);
            double yawRad = Math.toRadians(getRotationTo(mc.player.getPositionVector(), holePosition).x);
            double distance = mc.player.getPositionVector().distanceTo(holePosition);
            double speed = mc.player.onGround ? -Math.min(0.2805, distance / 2.0) : (-getDefaultMoveSpeed() + 0.02);
            mc.player.motionX = -Math.sin(yawRad) * speed;
            mc.player.motionZ = Math.cos(yawRad) * speed;
            if (new AxisAlignedBB(findHolePosition()).intersects(mc.player.getEntityBoundingBox())) {
                Command.sendMessage("Snapped into a hole, Disabling!");
                mc.player.stepHeight = 0.5f;
                mc.timer.tickLength = 50.0f;
                this.disable();
            }
        } else {
            Command.sendMessage("No holes found, Disabling!");
            mc.player.stepHeight = 0.5f;
            mc.timer.tickLength = 50.0f;
            if (disableNoHoles.getValue()) {
                this.disable();
            }
        }
    }

    public static double getDefaultMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player != null && mc.player.isPotionActive(Potion.getPotionById(1))) {
            int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public BlockPos findHolePosition() {
        return getPossibleHoles().stream().filter(pos -> mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()) < holeHRange.getValue()).min(Comparator.comparingDouble(pos -> mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()))).orElse(null);
    }

    public List<BlockPos> getPossibleHoles() {
        List<BlockPos> holeList = new ArrayList<>();
        BlockUtil.getSphere(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), holeHRange.getValue(), holeVRange.getValue().intValue(), false, true, 0).stream().forEach(pos -> {
            if (!isHole(pos)) return;
            if (isHole(pos)) {
                holeList.add(pos);
            }
        });
        return holeList;
    }

    public boolean isHole(BlockPos pos) {
        if (getBlock(pos) == Blocks.AIR && getBlock(pos.up()) == Blocks.AIR && getBlock(pos.up().up()) == Blocks.AIR && isValidBlock(pos.north()) && isValidBlock(pos.east()) && isValidBlock(pos.south()) && isValidBlock(pos.west()) && isValidBlock(pos.down())) {
            return true;
        }
        return false;
    }

    public boolean isValidBlock(BlockPos pos) {
        if (getBlock(pos) == Blocks.OBSIDIAN || getBlock(pos) == Blocks.BEDROCK) {
            return true;
        }
        return false;
    }

    public Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    public Vec2f getRotationTo(Vec3d posTo, Vec3d posFrom) {
        return this.getRotationFromVec(posTo.subtract(posFrom));
    }

    public Vec2f getRotationFromVec(Vec3d vec) {
        double xz = Math.hypot(vec.x, vec.z);
        float yaw = (float) this.normalizeAngle(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
        float pitch = (float) this.normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f(yaw, pitch);
    }

    public double normalizeAngle(Double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

}
