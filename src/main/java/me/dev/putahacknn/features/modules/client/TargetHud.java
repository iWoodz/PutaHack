package me.dev.putahacknn.features.modules.client;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.features.gui.font.CustomFont;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.modules.misc.PopCounter;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.*;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TargetHud extends Module {

    public TargetHud() {
        super("TargetHud", "does your mother", Category.CLIENT, true, false, false);
    }

    public final Setting<Integer> x = this.register(new Setting("X", 406, 0, 1000));
    public final Setting<Integer> y = this.register(new Setting("Y", 410, 0, 600));
    public final Setting<Boolean> defaults = this.register(new Setting("Default", false));
    public final Setting<Integer> red = this.register(new Setting("Red", 145, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 145, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 235, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting("Alpha", 40, 0, 255));
    public final Setting<Integer> rainbowSpeed = this.register(new Setting("Rainbow Speed", 15, 1, 15));
    public final Setting<Float> enemyRange = this.register(new Setting("Enemy Range", 24.0f, 0.0f, 50.0f));
    public Timer timer = new Timer();
    public float maxHealth = 36.0f;
    private float animTime = 0.5f;
    private float animProgress = 0.0f;
    private int initialX = 406;
    private int initialY = 0;
    private boolean animate = false;
    private long animationStartTime = 0;

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }

        EntityPlayer target = EntityUtil.getClosestEnemy(enemyRange.getValue());
        if (target == null) {
            return;
        }

        int j = 0;
        j++;
        float health = target.getHealth() + target.getAbsorptionAmount();
        maxHealth += (health - maxHealth) * 0.1f;

        if (target.getDistance(mc.player) <= enemyRange.getValue()) {

            if (!animate) {
                animate = true;
                animationStartTime = System.currentTimeMillis();
                initialX = 406;
                initialY = -54;
            }
        } else {

            animate = false;
            animProgress = 0.0f;
        }


        if (animate) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - animationStartTime;
            animProgress = Math.min(1.0f, (float) elapsedTime / (animTime * 1000));

            int easedX = (int) easeOutBack(animProgress, initialX, x.getValue() - initialX, 1.0f);
            int easedY = (int) easeOutBack(animProgress, initialY, y.getValue() - initialY, 1.0f);


            x.setValue(easedX);
            y.setValue(easedY);
        }

        RenderUtil.drawRect(x.getValue(), y.getValue(), x.getValue() + 150, y.getValue() + 54, ColorUtil.toARGB(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()));
        RenderUtil.drawRect(x.getValue() - 0.5f + 0.2f, y.getValue() - 0.5f, x.getValue() + 150.5f, y.getValue() + 54.5f - 0.2f, ColorUtil.toARGB(red.getValue(), green.getValue(), blue.getValue(), 70));
        RenderUtil.drawRect(x.getValue() - 1.0f + 0.2f, y.getValue() - 1.0f, x.getValue() + 151, y.getValue() + 55 - 0.2f, ColorUtil.toARGB(red.getValue(), green.getValue(), blue.getValue(), 60));
        RenderUtil.drawRect(x.getValue() - 1.5f + 0.2f, y.getValue() - 1.5f, x.getValue() + 151.5f, y.getValue() + 55.5f - 0.2f, ColorUtil.toARGB(red.getValue(), green.getValue(), blue.getValue(), 50));
        RenderUtil.drawRect(x.getValue() - 2.0f + 0.2f, y.getValue() - 2.0f, x.getValue() + 152, y.getValue() + 56 - 0.2f, ColorUtil.toARGB(red.getValue(), green.getValue(), blue.getValue(), 40));
        drawPlayerHeadOnScreen(x.getValue() + 5, y.getValue() + 5, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f, (AbstractClientPlayer) target);
        PutaHacknn.textManager.customFont2.drawString("Name " + target.getName(), x.getValue() + 40, y.getValue() + 5.5f, -1, false);
        PutaHacknn.textManager.customFont2.drawString("Pops " + PopCounter.getPopCount(target), x.getValue() + 40, y.getValue() + 14.0f, -1, false);
        PutaHacknn.textManager.customFont2.drawString("Distance " + MathUtil.round(target.getDistance(mc.player), 1), x.getValue() + 80, y.getValue() + 14.0f, -1, false);
        RenderUtil.drawGradientSideways(x.getValue() + 5, y.getValue() + 43, x.getValue() + maxHealth * 3.3f, y.getValue() + 48, ColorUtil.rainbow(rainbowSpeed.getValue(), 0, 1, 1, 1).hashCode(), ColorUtil.getColor(1 * (x.getValue() + 5) * 1.4f + j / 6f, 0.5f, 1));
        PutaHacknn.textManager.customFont2.drawString(MathUtil.round(health, 1) + "", x.getValue() + maxHealth * 3.5f, y.getValue() + 42, -1);

        int iteration = 0;
        int i = this.x.getValue() + 28;
        int y = this.y.getValue() + (PutaHacknn.moduleManager.getModuleByName("CustomFont").isEnabled() ? PutaHacknn.textManager.customFont2.getHeight() + 2 : renderer.getFontHeight()) * 4 - 14;
        for (ItemStack is : target.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
        }
    }

    private float easeOutBack(float t, float b, float c, float d) {
        float s = 1.70158f;
        return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
    }

    public void drawPlayerHeadOnScreen(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        ResourceLocation skinLocation = target.getLocationSkin();
        mc.getTextureManager().bindTexture(skinLocation);
        GL11.glEnable(3042);
        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
        GL11.glDisable(3042);
    }

    @Override
    public void onUpdate() {
        if (defaults.getValue()) {
            x.setValue(573);
            y.setValue(506);
            defaults.setValue(false);
        }
    }
}