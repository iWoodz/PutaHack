package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.Render3DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.ColorUtill;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Credits: By Woodz
 */
public class EntityCircle
        extends Module {
    public final Setting<Boolean> player;
    public final Setting<Double> width;
    public final Setting<Boolean> crystal = this.register(new Setting<>("Crystal", true));
    public final Setting<Double> height;
    public final Setting<Boolean> onlyOthers;
    public final Setting<Double> range;

    private boolean new1(Double d) {
        return this.crystal.getValue();
    }

    private boolean new0(Double d) {
        return this.crystal.getValue();
    }

    public EntityCircle() {
        super("EntityCircle", "jello", Category.RENDER, true, false, false);
        this.width = this.register(new Setting<>("Width", 1.0, 0.1, 7.0, this::new0));
        this.height = this.register(new Setting<>("Height", 2.0, 0.1, 7.0, this::new1));
        this.player = this.register(new Setting<>("Player", true));
        this.onlyOthers = this.register(new Setting<>("OnlyOthers", true));
        this.range = this.register(new Setting<>("Range", 6.0, 0.0, 12.0));
    }

    private double easeInOutQuad(double d) {
        return d < 0.5 ? 2.0 * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 2.0) / 2.0;
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        double renderRange = this.range.getValue();
        for (Entity entity : EntityCircle.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal && this.crystal.getValue() ||
                    entity instanceof EntityPlayer &&
                            ((this.player.getValue() && !this.onlyOthers.getValue()) ||
                                    (this.onlyOthers.getValue() && entity != EntityCircle.mc.player)))) {
                continue;
            }

            double distanceSq = EntityCircle.mc.player.getDistanceSq(entity);
            if (distanceSq > renderRange * renderRange) {
                continue;
            }
            double d = 1500.0;
            double d2 = (double)System.currentTimeMillis() % d;
            boolean bl = d2 > d / 2.0;
            double d3 = d2 / (d / 2.0);
            d3 = !bl ? 1.0 - d3 : d3 - 1.0;
            d3 = this.easeInOutQuad(d3);
            EntityCircle.mc.entityRenderer.disableLightmap();
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glDisable(2884);
            GL11.glShadeModel(7425);
            EntityCircle.mc.entityRenderer.disableLightmap();
            double d4 = entity.width;
            double d5 = (double)entity.height + 0.1;
            if (entity instanceof EntityEnderCrystal) {
                d4 = this.width.getValue();
                d5 = this.height.getValue();
            }
            double d6 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.getRenderPartialTicks() - EntityCircle.mc.getRenderManager().viewerPosX;
            double d7 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.getRenderPartialTicks() - EntityCircle.mc.getRenderManager().viewerPosY + d5 * d3;
            double d8 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.getRenderPartialTicks() - EntityCircle.mc.getRenderManager().viewerPosZ;
            double d9 = d5 / 3.0 * (d3 > 0.5 ? 1.0 - d3 : d3) * (double)(bl ? -1 : 1);
            for (int i = 0; i < 360; i += 5) {
                Color color = PutaHacknn.colorManager.getColor();
                double d10 = d6 - Math.sin((double)i * Math.PI / 180.0) * d4;
                double d11 = d8 + Math.cos((double)i * Math.PI / 180.0) * d4;
                double d12 = d6 - Math.sin((double)(i - 5) * Math.PI / 180.0) * d4;
                double d13 = d8 + Math.cos((double)(i - 5) * Math.PI / 180.0) * d4;
                GL11.glBegin(7);
                GL11.glColor4f((float) ColorUtill.pulseColor(color, 200, 1).getRed() / 255.0f, (float)ColorUtill.pulseColor(color, 200, 1).getGreen() / 255.0f, (float)ColorUtill.pulseColor(color, 200, 1).getBlue() / 255.0f, 0.0f);
                GL11.glVertex3d(d10, d7 + d9, d11);
                GL11.glVertex3d(d12, d7 + d9, d13);
                GL11.glColor4f((float)ColorUtill.pulseColor(color, 200, 1).getRed() / 255.0f, (float)ColorUtill.pulseColor(color, 200, 1).getGreen() / 255.0f, (float)ColorUtill.pulseColor(color, 200, 1).getBlue() / 255.0f, 200.0f);
                GL11.glVertex3d(d12, d7, d13);
                GL11.glVertex3d(d10, d7, d11);
                GL11.glEnd();
                GL11.glBegin(2);
                GL11.glVertex3d(d12, d7, d13);
                GL11.glVertex3d(d10, d7, d11);
                GL11.glEnd();
            }
            GL11.glEnable(2884);
            GL11.glShadeModel(7424);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
    }
}
