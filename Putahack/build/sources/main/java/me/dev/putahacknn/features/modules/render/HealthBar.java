package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.event.events.Render3DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class HealthBar extends Module {

    public HealthBar() {
        super("HealthBar", "woodz is fat af part 2", Category.RENDER, true, false, false);
    }

    public final Setting<Boolean> renderOwn = this.register(new Setting("Render Own", false));
    public final Setting<Float> scale = this.register(new Setting("Scale", 7.5f, 3.5f, 10.0f));

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                if (entity == mc.player && !renderOwn.getValue()) {
                    continue;
                }
                Vec3d interpolatedRenderPos = new Vec3d(interpolatePos(entity.posX, entity.lastTickPosX), interpolatePos(entity.posY, entity.lastTickPosY), interpolatePos(entity.posZ, entity.lastTickPosZ));
                renderHealthBar(interpolatedRenderPos, (EntityLivingBase) entity);
            }
        }
    }

    public double interpolatePos(double newPosition, double oldPosition) {
        return oldPosition + (newPosition - oldPosition) * mc.getRenderPartialTicks();
    }

    public void renderHealthBar(Vec3d vec3d, EntityLivingBase entity) {
        double scale = 0.0018 + (this.scale.getValue() / 1000);
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(vec3d.x - mc.renderManager.viewerPosX, vec3d.y - mc.renderManager.viewerPosY, vec3d.z - mc.renderManager.viewerPosZ);
        GlStateManager.rotate(-mc.renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(mc.renderManager.playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
        GlStateManager.scale(-scale, -scale, scale);
        RenderUtil.drawRect(-55, -((entity.getHealth() + entity.getAbsorptionAmount()) * 8.6f), -45, 0, ColorUtil.toARGB(255, 65, 68, 210));
        RenderUtil.drawOutlineRect(-55,  -((entity.getHealth() + entity.getAbsorptionAmount()) * 8.6f), -45, 0, ColorUtil.toARGB(20, 20, 20, 245));
        GlStateManager.enableDepth();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.disableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.popMatrix();
    }

}
