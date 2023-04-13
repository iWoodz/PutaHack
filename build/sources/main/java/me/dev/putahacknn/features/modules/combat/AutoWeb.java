package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.event.events.Render3DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.*;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class AutoWeb extends Module {

    public AutoWeb() {
        super("AutoWeb", "web but auto", Category.COMBAT, true, false, false);
    }

    public enum Placement {
        Feet,
        Face,
        Both
    }

    public final Setting<Placement> placement = this.register(new Setting("Placement", Placement.Feet));
    public final Setting<Integer> blocksPerTick = this.register(new Setting("Blocks Per Tick", 10, 0, 20));
    public final Setting<Float> targetRange = this.register(new Setting("Target Range", 12.0f, 0.0f, 15.0f));
    public final Setting<Boolean> extrapolation = this.register(new Setting("Extrapolation", true));
    public final Setting<Integer> extrapolationTicks = this.register(new Setting("Extrapolation Ticks", 3));
    public final Setting<Boolean> placeSwing = this.register(new Setting("Place Swing", true));
    public final Setting<Boolean> packet = this.register(new Setting("Packet", true));
    public final Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
    public final Setting<Boolean> render = this.register(new Setting("Render", true));
    public final Setting<Integer> red = this.register(new Setting("Red", 30, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 167, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting("Alpha", 70, 0, 255));
    public final Setting<Boolean> renderExtrapolation = this.register(new Setting("Render Extrapolation", true));
    public final Setting<Integer> extrapolationRed = this.register(new Setting("Extrapolation Red", 255, 0, 255));
    public final Setting<Integer> extrapolationGreen = this.register(new Setting("Extrapolation Green", 10, 0, 255));
    public final Setting<Integer> extrapolationBlue = this.register(new Setting("Extrapolation Blue", 10, 0, 255));
    public final Setting<Integer> extrapolationAlpha = this.register(new Setting("Extrapolation Alpha", 70, 0, 255));
    public final Setting<Float> lineWidth = this.register(new Setting("Line Width", 1.5f, 0.0f, 10.0f));
    public final Setting<Boolean> fill = this.register(new Setting("Fill", true));
    public final Setting<Boolean> outline = this.register(new Setting("Outline", true));
    public BlockPos renderPos;
    public BlockPos renderExtrapolationPos;
    public int placed = 0;

    @Override
    public void onUpdate() {
        placed = 0;
        EntityLivingBase target = EntityUtil.getTarget(targetRange.getValue());
        if (target != null) {
            BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
            if (placement.getValue() == Placement.Face) {
                if (BlockUtil.hasNeighbour(targetPos.up())) {
                    placeBlock(targetPos.up());
                    renderPos = targetPos.up();
                }
            } else if (placement.getValue() == Placement.Feet) {
                if (BlockUtil.hasNeighbour(targetPos)) {
                    placeBlock(targetPos);
                    renderPos = targetPos;
                }
            } else if (placement.getValue() == Placement.Both) {
                if (BlockUtil.hasNeighbour(targetPos)) {
                    placeBlock(targetPos);
                    renderPos = targetPos;
                }
                if (BlockUtil.hasNeighbour(targetPos.up())) {
                    placeBlock(targetPos.up());
                    renderPos = targetPos.up();
                }
            }
            if (extrapolation.getValue()) {
                Vec3d targetVec = MathUtil.extrapolatePlayerPosition((EntityPlayer) target, extrapolationTicks.getValue());
                BlockPos extrapolatedTargetPos = new BlockPos(targetVec.x, targetVec.y, targetVec.z);
                if (BlockUtil.hasNeighbour(extrapolatedTargetPos)) {
                    placeBlock(extrapolatedTargetPos);
                    renderExtrapolationPos = extrapolatedTargetPos;
                }
            }
        } else {
            renderExtrapolationPos = null;
            renderPos = null;
        }
    }

    public void placeBlock(BlockPos pos) {
        int webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        int oldSlot = mc.player.inventory.currentItem;
        if (placed < blocksPerTick.getValue() && webSlot != -1 && canPlaceBlock(pos)) {
            if (placeSwing.getValue()) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            mc.player.connection.sendPacket(new CPacketHeldItemChange(webSlot));
            BlockUtil.placeBlock(pos, true, packet.getValue(), rotate.getValue());
            mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
            placed++;
        }
    }

    public boolean canPlaceBlock(BlockPos pos) {
        if (mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        if (render.getValue() && renderPos != null) {
            RenderUtil.drawBoxESP(renderPos, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), true, new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), lineWidth.getValue(), outline.getValue(), fill.getValue(), alpha.getValue(), true);
        }
        if (renderExtrapolation.getValue() && renderExtrapolationPos != null) {
            RenderUtil.drawBoxESP(renderExtrapolationPos, new Color(extrapolationRed.getValue(), extrapolationGreen.getValue(), extrapolationBlue.getValue(), extrapolationAlpha.getValue()), true, new Color(extrapolationRed.getValue(), extrapolationGreen.getValue(), extrapolationBlue.getValue(), extrapolationAlpha.getValue()), lineWidth.getValue(), outline.getValue(), fill.getValue(), alpha.getValue(), true);
        }
    }

}

