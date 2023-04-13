package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.event.events.BlockEvent;
import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.modules.combat.HoleCampFix;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.BlockUtil;
import me.dev.putahacknn.util.RenderUtil;
import me.dev.putahacknn.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class BeepBoop extends Module {

    public BeepBoop() {
        super("BeepBoop", "One of the only robot modules dbear has ever used", Category.WOODZ, true, false, false);
        INSTANCE = this;
    }

    public static BeepBoop INSTANCE;
    public final Setting<Boolean> instantMine = this.register(new Setting("Instant Mine", true));
    public final Setting<Integer> instantDelay = this.register(new Setting("Instant Delay", 10, 0, 500));
    public final Setting<Boolean> silentInstaMine = this.register(new Setting("Silent InstaMine", false));
    public final Setting<Boolean> render = this.register(new Setting("Render", true));
    public final Setting<Integer> outlineR = this.register(new Setting("Outline Red", 255, 0, 255));
    public final Setting<Integer> outlineG = this.register(new Setting("Outline Green", 255, 0, 255));
    public final Setting<Integer> outlineB = this.register(new Setting("Outline Blue", 255, 0, 255));
    public final Setting<Integer> outlineA = this.register(new Setting("Outline Alpha", 90, 0, 255));
    public final Setting<Integer> fillR = this.register(new Setting("Fill Red", 255, 0, 255));
    public final Setting<Integer> fillG = this.register(new Setting("Fill Green", 255, 0, 255));
    public final Setting<Integer> fillB = this.register(new Setting("Fill Blue", 255, 0, 255));
    public final Setting<Integer> fillA = this.register(new Setting("Fill Alpha", 90, 0, 255));
    public Color outline;
    public Color fill;
    public Timer instantTimer = new Timer();
    public Timer timer = new Timer();
    public BlockPos minedPos;
    public EnumFacing mineFacing;
    public long startTime;

    @SubscribeEvent
    public void onBlock(BlockEvent event) {
        if (event.pos != null && canBreakBlock(event.pos)) {
            hitPos(event.pos, event.facing);
            startTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (minedPos != null && render.getValue()) {
            outline = new Color(outlineR.getValue() / 255.0f, outlineG.getValue() / 255.0f, outlineB.getValue() / 255.0f, outlineA.getValue() / 255.0f);
            fill = new Color(fillR.getValue() / 255.0f, fillG.getValue() / 255.0f, fillB.getValue() / 255.0f, fillA.getValue() / 255.0f);
            AxisAlignedBB fillBB = new AxisAlignedBB(minedPos);
            AxisAlignedBB lineBB = new AxisAlignedBB(minedPos);
            final float breakTime = mc.world.getBlockState(minedPos).getBlockHardness((World) mc.world, minedPos) * 20.0f * 2.0f;
            double shrinkFactor = normalize((double) (System.currentTimeMillis() - startTime), 0.0, breakTime);
            shrinkFactor = MathHelper.clamp(shrinkFactor, 0.0, 1.0);
            fillBB = fillBB.shrink(shrinkFactor);
            lineBB = lineBB.shrink(shrinkFactor);
            RenderUtil.renderBB(7, fillBB, (Color) fill, (Color) fill);
            RenderUtil.renderBB(3, lineBB, (Color) outline, (Color) outline);
        }
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (minedPos != null && mineFacing != null && canBreakBlock(minedPos)) {
            if (instantMine.getValue()) {
                int oldSlot = mc.player.inventory.currentItem;
                if (instantTimer.passedMs(instantDelay.getValue()) && minedPos != null && mineFacing != null && canBreakBlock(minedPos) && crystalCheck(minedPos)) {
                    if (silentInstaMine.getValue() && getBestAvailableToolSlot(mc.world.getBlockState(minedPos).getBlock().getBlockState().getBaseState()) != -1) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(getBestAvailableToolSlot(mc.world.getBlockState(minedPos).getBlock().getBlockState().getBaseState())));
                    }
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, minedPos, mineFacing));
                    if (silentInstaMine.getValue() && oldSlot != -1) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                    }
                    instantTimer.reset();
                }
            } else {
                int oldSlot = mc.player.inventory.currentItem;
                if (timer.passedMs(200) && BlockUtil.getBlock(minedPos) != Blocks.AIR) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(getBestAvailableToolSlot(mc.world.getBlockState(minedPos).getBlock().getBlockState().getBaseState())));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, minedPos, mineFacing));
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                    timer.reset();
                } else if (BlockUtil.getBlock(minedPos) == Blocks.AIR) {
                    minedPos = null;
                    mineFacing = null;
                }
            }
        }
    }

    public void hitPos(BlockPos pos, EnumFacing facing) {
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
        minedPos = pos;
        mineFacing = facing;
    }

    public static int getBestAvailableToolSlot(IBlockState blockState) {
        int toolSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            int eff;
            float speed;
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty || !((speed = stack.getDestroySpeed(blockState)) > 1.0f) || !((double) (speed = (float) ((double) speed + ((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0 ? Math.pow(eff, 2.0) + 1.0 : 0.0))) > max))
                continue;
            max = speed;
            toolSlot = i;
        }
        return toolSlot;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketBlockChange && !instantMine.getValue()) {
            if (((SPacketBlockChange) event.getPacket()).getBlockPosition() == minedPos && ((SPacketBlockChange) event.getPacket()).getBlockState().getMaterial().isReplaceable() || ((SPacketBlockChange) event.getPacket()).getBlockPosition() == minedPos && BlockUtil.getBlock(minedPos) == Blocks.AIR) {
                minedPos = null;
                mineFacing = null;
            }
        }
    }

    public double normalize(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }

    public boolean canBreakBlock(final BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1.0f;
    }

    public boolean crystalCheck(BlockPos pos) {
        if (HoleCampFix.INSTANCE.isEnabled() && HoleCampFix.INSTANCE.lastHitPos != null && HoleCampFix.INSTANCE.lastHitPos.equals(pos)) {
            return HoleCampFix.INSTANCE.intersectsWithCrystal(pos.up());
        } else {
            return true;
        }
    }

}
