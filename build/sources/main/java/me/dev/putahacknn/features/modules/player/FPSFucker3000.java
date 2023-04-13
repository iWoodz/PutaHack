package me.dev.putahacknn.features.modules.player;

import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.util.BlockUtil;
import me.dev.putahacknn.util.DamageUtil;
import me.dev.putahacknn.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL20;

import java.awt.*;

public class FPSFucker3000 extends Module {
    public FPSFucker3000() {
        super("FPSFucker3000", "fucks multiple things, mainly your fps (not to mention your mother)", Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        for (int i = 0; i < 2147483647; i++) {
            BlockUtil.getSphere(new BlockPos(mc.player.getPositionVector()), 200.0f, 200, false, true, 190).forEach(pos -> {
                RenderUtil.drawRoundedRectangle(100, 100, 200, 200, 4999);
            });
        }
        BlockUtil.getSphere(new BlockPos(mc.player.getPositionVector()), 200.0f, 200, false, true, 190).forEach(pos -> {
            RenderUtil.drawRoundedRectangle(100, 100, 200, 200, 4999);
            if (DamageUtil.calculateDamage(pos, mc.player) < 10) {
                RenderUtil.drawRoundedRectangle(100, 100, 100, 100, 100);
                RenderUtil.renderBB(8, new AxisAlignedBB(pos).expand(69, 69, 69), Color.BLACK, Color.WHITE);
            }
            GlStateManager.pushMatrix();
            GlStateManager.glBegin(1);
            GlStateManager.pushMatrix();
            GlStateManager.glBegin(2);
            GlStateManager.pushMatrix();
            GL20.glUseProgram(0);
        });
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(BlockPos.ORIGIN, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
    }

    @Override
    public void onLogout() {
        this.disable();
    }
}


