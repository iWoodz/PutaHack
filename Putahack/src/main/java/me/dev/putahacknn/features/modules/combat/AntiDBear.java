package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.*;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import scala.tools.reflect.quasiquotes.Holes;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AntiDBear extends Module {

    public AntiDBear() {
        super("AntiDBear", "makes dbear mad (fills holes around him specifically)", Category.COMBAT, true, false, false);
    }

    public final Setting<Float> targetRange = this.register(new Setting("Target Range", 10.0f, 0.0f, 20.0f));
    public final Setting<Float> range = this.register(new Setting("Range", 5.0f, 0.0f, 10.0f));
    public final Setting<Float> rangeY = this.register(new Setting("Y Range", 3.5f, 0.0f, 10.0f));
    public final Setting<Float> wallsRange = this.register(new Setting("Walls Range", 3.0f, 0.0f, 10.0f));
    public final Setting<Float> smartRange = this.register(new Setting("Smart Range", 2.5f, 0.0f, 10.0f));
    public final Setting<Boolean> doubles = this.register(new Setting("Doubles", true));
    public final Setting<Boolean> enemyHolePause = this.register(new Setting("Enemy Hole Pause", true));
    public final Setting<Boolean> extrapolation = this.register(new Setting("Extrapolation", true));
    public final Setting<Integer> extrapolationTicks = this.register(new Setting("Extrapolation Ticks", 2, 0, 10));
    public final Setting<Boolean> crystalBreaker = this.register(new Setting("Crystal Breaker", true));
    public final Setting<Boolean> packetBreak = this.register(new Setting("Packet Break", true));
    public final Setting<Boolean> crystalSwing = this.register(new Setting("Crystal Swing", true));
    public final Setting<Boolean> prioritizeWebs = this.register( new Setting("Prioritize Webs", false));
    public final Setting<Boolean> swing = this.register(new Setting("Swing", true));
    public final Setting<Boolean> smart = this.register(new Setting("Smart", true));
    public final Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
    public final Setting<Boolean> packet = this.register(new Setting("Packet", true));
    public final Setting<Boolean> render = this.register(new Setting("Render", true));
    public final Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting("Alpha", 55, 0, 255));
    public final Setting<Boolean> outline = this.register(new Setting("Outline", true));
    public final Setting<Boolean> fill = this.register(new Setting("Fill", true));
    public final Setting<Float> lineWidth = this.register(new Setting("Line Width", 2.5f, 0.0f, 3.0f));
    public final Setting<Integer> shrinkDelay = this.register(new Setting("Shrink Delay", 0, 0, 150));
    public final Setting<Float> shrinkFactor = this.register(new Setting("Shrink Factor", 0.6f, 0.0f, 1.0f));
    public final Setting<Integer> removeTime = this.register(new Setting("Remove Time", 300, 0, 1000));
    public float shrink;
    public BlockPos renderPos = null;
    public Timer timer = new Timer();
    public Timer shrinkTimer = new Timer();
    public Entity target;

    @Override
    public void onUpdate() {
        int blockSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1 ? InventoryUtil.findHotbarBlock(BlockObsidian.class) : InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        target = EntityUtil.getTarget(targetRange.getValue());
        if (fullNullCheck() || blockSlot == -1 || smart.getValue() && target == null) {
            renderPos = null;
            return;
        }
        if (enemyHolePause.getValue()) {
            if (HoleUtil.isInHole(target)) {
                renderPos = null;
                return;
            }
        }
        if (timer.passedMs(removeTime.getValue())) {
            renderPos = null;
        }
        if (shrink < 0.5f) {
            if (shrinkTimer.passedMs(shrinkDelay.getValue())) {
                shrink += shrinkFactor.getValue() / 10;
                shrinkTimer.reset();
            }
        }
        for (HoleUtil.Hole hole : HoleUtil.getHoles(range.getValue(), rangeY.getValue().intValue(), new BlockPos(mc.player.getPositionVector()), doubles.getValue())) {
            if (!smart.getValue()) {
                if (hole.doubleHole) {
                    if (!intersectsWithEntity(hole.pos1)) {
                        placeBlock(hole.pos1);
                        renderPos = hole.pos1;
                    }
                    if (!intersectsWithEntity(hole.pos2)) {
                        placeBlock(hole.pos2);
                        renderPos = hole.pos2;
                    }
                } else {
                    if (!intersectsWithEntity(hole.pos1)) {
                        placeBlock(hole.pos1);
                        renderPos = hole.pos1;
                    }
                }
            } else {
                Vec3d targetVec = MathUtil.extrapolatePlayerPosition((EntityPlayer) target, extrapolationTicks.getValue());
                if (hole.doubleHole) {
                    if (isPlayerCloseToHole(hole.pos1, target) || isPlayerCloseToHole(hole.pos1, targetVec) && extrapolation.getValue()) {
                        if (!intersectsWithEntity(hole.pos1)) {
                            placeBlock(hole.pos1);
                            renderPos = hole.pos1;
                        }
                    }
                    if (isPlayerCloseToHole(hole.pos2, target) || isPlayerCloseToHole(hole.pos2, targetVec) && extrapolation.getValue()) {
                        if (!intersectsWithEntity(hole.pos2)) {
                            placeBlock(hole.pos2);
                            renderPos = hole.pos2;
                        }
                    }
                } else {
                    if (isPlayerCloseToHole(hole.pos1, target) || isPlayerCloseToHole(hole.pos1, targetVec) && extrapolation.getValue()) {
                        if (!intersectsWithEntity(hole.pos1)) {
                            placeBlock(hole.pos1);
                            renderPos = hole.pos1;
                        }
                    }
                }
            }
        }
    }

    public boolean isPlayerCloseToHole(BlockPos pos, Entity target) {
        if (mc.player.getPositionVector().distanceTo(new Vec3d(pos)) > range.getValue()) {
            return false;
        }
        if (target.getPositionVector().distanceTo(new Vec3d(pos)) < smartRange.getValue() && BlockUtil.canSeePos(pos)) {
            return true;
        } else if (target.getPositionVector().distanceTo(new Vec3d(pos)) < wallsRange.getValue() && !BlockUtil.canSeePos(pos)) {
            return true;
        }
        return false;
    }

    public boolean isPlayerCloseToHole(BlockPos pos, Vec3d target) {
        if (mc.player.getPositionVector().distanceTo(new Vec3d(pos)) > range.getValue()) {
            return false;
        }
        if (target.distanceTo(new Vec3d(pos)) < smartRange.getValue() && BlockUtil.canSeePos(pos)) {
            return true;
        } else if (target.distanceTo(new Vec3d(pos)) < wallsRange.getValue() && !BlockUtil.canSeePos(pos)) {
            return true;
        }
        return false;
    }

    double normalize(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }

    public boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityExpBottle || entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox().shrink(0.25f))) return true;
        }
        return false;
    }

    public boolean intersectsWithSelf(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityPlayer)) continue;
            if (entity == mc.player && new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox().shrink(0.005f))) return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (render.getValue() && renderPos != null) {
            Color color = new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());
            AxisAlignedBB bb = new AxisAlignedBB(renderPos);
            if (outline.getValue()) {
                GL11.glLineWidth(lineWidth.getValue());
                RenderUtil.renderBB(3, bb.shrink(shrink), color, color);
            }
            if (fill.getValue()) {
                RenderUtil.renderBB(7, bb.shrink(shrink), color, color);
            }
        }
    }

    public void placeBlock(BlockPos pos) {
        if (fullNullCheck() || pos == null) {
            return;
        }
        int obsidianSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        int webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        int blocksSlot = prioritizeWebs.getValue() ? webSlot != -1 ? webSlot : obsidianSlot != -1 ? obsidianSlot : eChestSlot : obsidianSlot != -1 ? obsidianSlot : eChestSlot;
        int oldSlot = mc.player.inventory.currentItem;
        if (crystalBreaker.getValue()) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderCrystal && new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                    EntityUtil.attackEntity(entity, packetBreak.getValue(), crystalSwing.getValue());
                }
            }
        }
        if (!intersectsWithEntity(pos) && BlockUtil.canPlaceBlock(pos) && !intersectsWithSelf(pos)) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(blocksSlot));
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), false, null, swing.getValue());
            mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        }
        shrink = 0.1f;
        timer.reset();
    }

}
