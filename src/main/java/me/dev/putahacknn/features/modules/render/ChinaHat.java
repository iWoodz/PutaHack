package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.Render3DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.ColorUtill;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.awt.*;
/**
 * Credits: By Woodz
 */
public class ChinaHat
        extends Module {
    public final Setting<Integer> red = this.register(new Setting<>("Red", 255, 1, 255));
    public final Setting<Integer> green = this.register(new Setting<>("Green", 255, 1, 255));
    public final Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 1, 255));
    public final Setting<Integer> red2 = this.register(new Setting<>("Red2", 255, 1, 255));
    public final Setting<Integer> green2 = this.register(new Setting<>("Green2", 255, 1, 255));
    public final Setting<Integer> blue2 = this.register(new Setting<>("Blue2", 255, 1, 255));
    public final Setting<Integer> points = this.register(new Setting<>("Points", 12, 4, 64));
    public final Setting<Boolean> firstPerson = this.register(new Setting<>("FirstPerson", false));

    public ChinaHat() {
        super("ChinaHat", "ching chong", Category.RENDER, true, false, false);
    }

    public static double interpolate(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    public static void drawHat(Entity entity, double d, float f, int n, float f2, float f3, int n2) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDepthMask(false);
        GL11.glLineWidth(f2);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2929);
        GL11.glBegin(3);
        double d2 = ChinaHat.interpolate(entity.prevPosX, entity.posX, f) - ChinaHat.mc.getRenderManager().viewerPosX;
        double d3 = ChinaHat.interpolate(entity.prevPosY + (double)f3, entity.posY + (double)f3, f) - ChinaHat.mc.getRenderManager().viewerPosY;
        double d4 = ChinaHat.interpolate(entity.prevPosZ, entity.posZ, f) - ChinaHat.mc.getRenderManager().viewerPosZ;
        GL11.glColor4f((float)new Color(n2).getRed() / 255.0f, (float)new Color(n2).getGreen() / 255.0f, (float)new Color(n2).getBlue() / 255.0f, 0.15f);
        for (int i = 0; i <= n; ++i) {
            GL11.glVertex3d(d2 + d * Math.cos((double)i * Math.PI * 2.0 / (double)n), d3, d4 + d * Math.sin((double)i * Math.PI * 2.0 / (double)n));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (ChinaHat.mc.gameSettings.thirdPersonView != 0 || this.firstPerson.getValue()) {
            for (int i = 0; i < 400; ++i) {
                float f = ColorUtill.getGradientOffset(new Color(this.red2.getValue(), this.green2.getValue(), this.blue2.getValue(), 255), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255), (double)Math.abs(System.currentTimeMillis() / 7L - (long)(i / 2)) / 120.0).getRGB();
                if (ChinaHat.mc.player.isElytraFlying()) {
                    ChinaHat.drawHat(ChinaHat.mc.player, 0.009 + (double)i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 1.1f - (float)i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
                    continue;
                }
                if (ChinaHat.mc.player.isSneaking()) {
                    ChinaHat.drawHat(ChinaHat.mc.player, 0.009 + (double)i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 1.1f - (float)i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
                    continue;
                }
                ChinaHat.drawHat(ChinaHat.mc.player, 0.009 + (double)i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 2.2f - (float)i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
            }
        }
    }
}
