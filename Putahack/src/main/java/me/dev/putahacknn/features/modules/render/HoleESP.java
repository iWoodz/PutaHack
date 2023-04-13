package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.util.BlockUtil;
import me.dev.putahacknn.util.HoleUtil;
import me.dev.putahacknn.util.RenderUtil;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class HoleESP extends Module {

    public HoleESP() {
        super("HoleESP", "Makes finding dbear a lot easier", Module.Category.RENDER, true, false, false);
    }

    public enum ColorType {
        Static,
        Dynamic
    }

    public enum HoleMode {
        Full,
        Gradient
    }

    public final Setting<Float> range = this.register(new Setting("Range", 12.0f, 0.0f, 20.0f));
    public final Setting<Float> lineWidth = this.register(new Setting("Line Width", 1.5f, 0.0f, 10.0f));
    public final Setting<Boolean> doubles = this.register(new Setting("Doubles", true));
    public final Setting<Boolean> onlyOpenDoubles = this.register(new Setting("Only Open Doubles", false));
    public final Setting<Boolean> noSideOutline = this.register(new Setting("No Side Outline", false));
    public final Setting<HoleMode> holeMode = this.register(new Setting("Hole Mode", HoleMode.Gradient));
    public final Setting<Float> height = this.register(new Setting("Height", 1.0f, 0.0f, 10.0f));
    public final Setting<Boolean> outline = this.register(new Setting("Outline", true));
    public final Setting<Boolean> fill = this.register(new Setting("Fill", true));
    public final Setting<ColorType> colorType = this.register(new Setting("Color Type", ColorType.Dynamic));
    public final Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting("Alpha", 110, 0, 255));
    public final Setting<Integer> unSafeRed = this.register(new Setting("Unsafe Red", 255, 0, 255));
    public final Setting<Integer> unSafeGreen = this.register(new Setting("Unsafe Green", 255, 0, 255));
    public final Setting<Integer> unSafeBlue = this.register(new Setting("Unsafe Blue", 255, 0, 255));
    public final Setting<Integer> unSafeAlpha = this.register(new Setting("Unsafe Alpha", 110, 0, 255));
    public final Setting<Integer> safeRed = this.register(new Setting("Safe Red", 255, 0, 255));
    public final Setting<Integer> safeGreen = this.register(new Setting("Safe Green", 255, 0, 255));
    public final Setting<Integer> safeBlue = this.register(new Setting("Safe Blue", 255, 0, 255));
    public final Setting<Integer> safeAlpha = this.register(new Setting("Safe Alpha", 110, 0, 255));

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        NonNullList<HoleUtil.Hole> holeList = NonNullList.create();
        BlockUtil.getSphere(new BlockPos(mc.player.getPositionVector()), range.getValue(), range.getValue().intValue(), false, true, 0).forEach(pos -> {
            if (!isHole(pos) || mc.world.getBlockState(pos).getBlock() != Blocks.AIR) return;
            if (HoleUtil.isObbyHole(pos)) {
                holeList.add(new HoleUtil.Hole(false, false, pos));
            } else if (HoleUtil.isBedrockHoles(pos)) {
                holeList.add(new HoleUtil.Hole(true, false, pos));
            } else if (HoleUtil.isDoubleHole(pos) != null) {
                holeList.add(HoleUtil.isDoubleHole(pos));
            }
        });
        holeList.forEach(hole -> {
            if (onlyOpenDoubles.getValue() && hole.doubleHole) {
                if (BlockUtil.getBlock(hole.pos1.up()) != Blocks.AIR || BlockUtil.getBlock(hole.pos2.up()) != Blocks.AIR) {
                    return;
                }
            }
            Color colorNoAlpha = colorType.getValue() == ColorType.Static ? new Color(red.getValue(), green.getValue(), blue.getValue(), 0) : hole.bedrock ? new Color(safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue(), 0) : new Color(unSafeRed.getValue(), unSafeGreen.getValue(), unSafeBlue.getValue(), 0);
            Color color = colorType.getValue() == ColorType.Static ? new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()) : hole.bedrock ? new Color(safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue(), safeAlpha.getValue()) : new Color(unSafeRed.getValue(), unSafeGreen.getValue(), unSafeBlue.getValue(), unSafeAlpha.getValue());
            GL11.glLineWidth(lineWidth.getValue());
            AxisAlignedBB holeBB = hole.doubleHole ? new AxisAlignedBB((double)hole.pos1.getX(), (double)hole.pos1.getY(), (double)hole.pos1.getZ(), (double)(hole.pos2.getX() + 1), (double)(hole.pos2.getY() + 1), (double)(hole.pos2.getZ() + 1)) : new AxisAlignedBB(hole.pos1);
            holeBB = new AxisAlignedBB(holeBB.minX, holeBB.minY, holeBB.minZ, holeBB.maxX, holeBB.minY + this.height.getValue().doubleValue(), holeBB.maxZ);
            if (fill.getValue()) {
                RenderUtil.renderBB(7, holeBB, color, holeMode.getValue() == HoleMode.Gradient ? colorNoAlpha : color);
            }
            if (outline.getValue()) {
                AxisAlignedBB noSideHoleBB = hole.doubleHole ? new AxisAlignedBB((double)hole.pos1.getX(), (double)hole.pos1.getY(), (double)hole.pos1.getZ(), (double)(hole.pos2.getX() + 1), (double)(hole.pos2.getY() + 1), (double)(hole.pos2.getZ() + 1)) : new AxisAlignedBB(hole.pos1);
                noSideHoleBB = new AxisAlignedBB(noSideHoleBB.minX, noSideHoleBB.minY, noSideHoleBB.minZ, noSideHoleBB.maxX, noSideHoleBB.minY + 0.009f, noSideHoleBB.maxZ);
                RenderUtil.renderBB(3, noSideOutline.getValue() ? noSideHoleBB : holeBB, color, holeMode.getValue() == HoleMode.Gradient && !noSideOutline.getValue() ? colorNoAlpha : color);
            }
        });
    }

    public boolean isHole(BlockPos pos) {
        if (HoleUtil.isObbyHole(pos) || HoleUtil.isBedrockHoles(pos) || HoleUtil.isDoubleHole(pos) != null) {
            return true;
        }
        return false;
    }
}

