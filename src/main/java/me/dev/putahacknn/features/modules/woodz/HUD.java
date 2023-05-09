package me.dev.putahacknn.features.modules.woodz;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.EntityUtil;
import me.dev.putahacknn.util.PlayerUtil;
import me.dev.putahacknn.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HUD extends Module {

    public static HUD INSTANCE = new HUD();
    public final Setting<Integer> alpha = this.register(new Setting<>("Alpha", 255, 0, 255));
    public final Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
    public final Setting<Integer> ySpace = this.register(new Setting<>("YSpace", 10, 0, 20));
    public final Setting<Integer> sortDelay = this.register(new Setting<>("SortDelay", 1000, 0, 10000));
    public final Setting<Boolean> watermark = this.register(new Setting<>("Watermark", true));
    public final Setting<String> watermarkText = this.register(new Setting<>("WatermarkText", PutaHacknn.MODNAME + " " + PutaHacknn.MODVER));
    public final Setting<Integer> watermarkX = this.register(new Setting<>("WatermarkX", 2, 0, 100, v -> watermark.getValue()));
    public final Setting<Boolean> watermarkOffset = this.register(new Setting<>("WatermarkOffset", false, v -> watermark.getValue()));
    public final Setting<Boolean> watermark2 = this.register(new Setting<>("Watermark2", true));
    public final Setting<Integer> watermark2y = this.register(new Setting<>("Watermark2Y", 100, 0, 600, v -> watermark2.getValue()));
    public final Setting<Boolean> arraylist = this.register(new Setting<>("Arraylist", true));
    public final Setting<Boolean> retardMode = this.register(new Setting<>("RetardMode", true, v -> arraylist.getValue()));
    public final Setting<Boolean> ping = this.register(new Setting<>("Ping", true));
    public final Setting<Boolean> coords = this.register(new Setting<>("Coords", true));
    public final Setting<Boolean> direction = this.register(new Setting<>("Direction", true));
    public final Setting<Boolean> netherCoords = this.register(new Setting<>("NetherCoords", true));
    public final Setting<Boolean> speed = this.register(new Setting<>("Speed", true));
    public final Setting<Boolean> armor = this.register(new Setting<>("Armor", true));
    public final Setting<Boolean> potionEffects = this.register(new Setting<>("PotionEffects", true));
    public final Setting<Boolean> potionEffectsFag = this.register(new Setting<>("PotionEffectsWave", true));
    public final Setting<Boolean> lag = this.register(new Setting<>("Lag", true));
    public final Setting<Boolean> tps = this.register(new Setting<>("TPS", true));
    public final Setting<Boolean> tpsAvg = this.register(new Setting<>("TPS-Average", true));
    public final Setting<Boolean> fps = this.register(new Setting<>("FPS", true));
    public final Setting<Rendering> rendering = this.register(new Setting<>("Rendering", Rendering.UP));
    public final Setting<Boolean> welcomer = this.register(new Setting<>("Welcomer", true));
    public final Setting<String> welcomerText = this.register(new Setting<>("WelcomerText", "Hello %s :^)"));
    public final Setting<Boolean> totems = this.register(new Setting<>("Totems", true));
    public final Setting<Integer> totemX = this.register(new Setting<>("TotemX", 2, -5, 5));
    public Timer sortTimer = new Timer();
    public static final ItemStack TOTEM = new ItemStack(Items.TOTEM_OF_UNDYING);
    public boolean needsSort;
    public List<Module> modules = new ArrayList<>();

    public HUD() {
        super("HUD", "as9d0fka309ethasdhgiao3rilkwtndoigroafoi3wriopuaiow84uj", Category.WOODZ, true, false, false);
    }

    public void sortModules() {
        modules.sort(Comparator.comparing(mod -> -PutaHacknn.textManager.getStringWidth(mod.getDisplayName() + (mod.getDisplayName().length() == 0 ? "" : (" \u00a77[\u00a7f" + mod.getDisplayName() + "\u00a77]")))));
    }

    public static int changeAlpha(int origColor, final int userInputedAlpha) {
        origColor = origColor & 0x00FFFFFF;
        return (userInputedAlpha << 24) | origColor;
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        final ScaledResolution resolution = new ScaledResolution(mc);
        final int color = changeAlpha(getColor().getRGB(), alpha.getValue());
        if (needsSort && sortTimer.passedMs(sortDelay.getValue())) {
            sortModules();
            sortTimer.reset();
        }

        int count = 10;
        int index = 0;

        if (watermark.getValue()) {
            index++;
            PutaHacknn.textManager.drawStringWithShadow(watermarkText.getValue(), watermarkX.getValue(), 2 + (watermarkOffset.getValue() ? 10 : 0), color(index, count));
        }

        if (watermark2.getValue()) {
            PutaHacknn.textManager.drawStringWithShadow("PutaHack.nn", 2, watermark2y.getValue(), color);
        }

        if (welcomer.getValue()) {
            index++;
            String welcomerString = String.format(welcomerText.getValue(), mc.player.getName());
            PutaHacknn.textManager.drawStringWithShadow(welcomerString, (resolution.getScaledWidth() / 2F) - (PutaHacknn.textManager.getStringWidth(welcomerString) / 2F) + 2, 2, color(index, count));
        }

        if (lag.getValue()) {
            if (PutaHacknn.serverManager.isServerNotResponding()) {
                String lagString = "Server hasn't responded in " + String.format("%.2f", (PutaHacknn.serverManager.serverRespondingTime() / 1000f)) + "s";
                PutaHacknn.textManager.drawStringWithShadow(lagString, (resolution.getScaledWidth() / 2F) - (PutaHacknn.textManager.getStringWidth(lagString) / 2F) + 2, welcomer.getValue() ? 12 : 2, color(index, count));
            }
        }

        boolean renderingUp = rendering.getValue() == Rendering.UP;
        boolean chatOpened = mc.ingameGUI.getChatGUI().getChatOpen();
        if (arraylist.getValue()) {
            int offset = renderingUp ? 2 : resolution.getScaledHeight() - (chatOpened ? 24 : 10);
            for (int i = 0, modulesSize = modules.size(); i < modulesSize; i++) {
                final Module module = modules.get(i);
                if ((module.isEnabled()) && module.isDrawn()) {
                    String suffix = (module.getDisplayName().length() == 0 ? "" : (" \u00a77[\u00a7f" + module.getDisplayName() + "\u00a77]"));
                    String nameAndLabel = module.getDisplayName() + suffix;
                    PutaHacknn.textManager.drawStringWithShadow(nameAndLabel, resolution.getScaledWidth() - PutaHacknn.textManager.getStringWidth(nameAndLabel) - 2, offset, color(i, modulesSize));
                    offset += (renderingUp ? 10 : -10);
                }
            }
        }

        if (armor.getValue()) {
            final int width = resolution.getScaledWidth() >> 1;
            final int height = resolution.getScaledHeight() - 55 - (mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
            GlStateManager.enableTexture2D();
            for (int i = 0; i < 4; ++i) {
                final ItemStack is = mc.player.inventory.armorInventory.get(i);
                if (is.isEmpty()) continue;
                final int x = width - 90 + (9 - i - 1) * 20 + 2;
                if (armor.getValue()) {
                    GlStateManager.enableDepth();
                    mc.getRenderItem().zLevel = 200.0f;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, height);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, is, x, height, "");
                    mc.getRenderItem().zLevel = 0.0f;
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                }
                final int dmg = (int) PlayerUtil.getDamageInPercent(is);
                final String dmgString = String.valueOf(dmg);
                PutaHacknn.textManager.drawStringWithShadow(dmgString, x + 8 - (PutaHacknn.textManager.getStringWidth(dmgString) >> 1), height - 8, changeAlpha(ColorUtil.getDurabilityColor(is), alpha.getValue()));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }

        int offset = renderingUp ? (chatOpened ? 24 : 10) : 2;

        if (potionEffects.getValue()) {
            int size = mc.player.getActivePotionEffects().size();
            int i = 0;
            for (PotionEffect effect : mc.player.getActivePotionEffects()) {
                i++;
                int amplifier = effect.getAmplifier();
                String potionString = I18n.format(effect.getEffectName()) + (amplifier > 0 ? (" " + (amplifier + 1) + "") : "") + ": " + ChatFormatting.WHITE + Potion.getPotionDurationString(effect, 1);
                boolean fag = false;
                if (potionEffectsFag.getValue()) {
                    index++;
                    fag = true;
                }
                int potionColor = fag ? color(i, size) : effect.getPotion().getLiquidColor();
                PutaHacknn.textManager.drawStringWithShadow(potionString, resolution.getScaledWidth() - PutaHacknn.textManager.getStringWidth(potionString) - 2, renderingUp ? resolution.getScaledHeight() - offset : offset, changeAlpha(potionColor, alpha.getValue()));
                offset += ySpace.getValue();
            }
        }

        if (speed.getValue()) {
            index++;
            String speedString = "Speed: \u00a7f" + String.format("%.2f", PutaHacknn.speedManager.getSpeedKpH()) + "km/h";
            PutaHacknn.textManager.drawStringWithShadow(speedString, resolution.getScaledWidth() - PutaHacknn.textManager.getStringWidth(speedString) - 2, renderingUp ? resolution.getScaledHeight() - offset : offset, color(index, count));
            offset += ySpace.getValue();
        }

        if (tps.getValue()) {
            index++;
            String tpsString = "TPS: \u00a7f" + String.format("%.2f", PutaHacknn.serverManager.getTPS()) + (tpsAvg.getValue() ? (" \u00a77(\u00a7f" + String.format("%.2f", PutaHacknn.serverManager.getTPS()) + "\u00a77)") : "");
            PutaHacknn.textManager.drawStringWithShadow(tpsString, resolution.getScaledWidth() - PutaHacknn.textManager.getStringWidth(tpsString) - 2, renderingUp ? resolution.getScaledHeight() - offset : offset, color(index, count));
            offset += ySpace.getValue();
        }
        try {
            if (ping.getValue()) {
                index++;
                String pingString = "Ping: \u00a7f" + EntityUtil.getPing(mc.player) + "ms";
                PutaHacknn.textManager.drawStringWithShadow(pingString, resolution.getScaledWidth() - PutaHacknn.textManager.getStringWidth(pingString) - 2, renderingUp ? resolution.getScaledHeight() - offset : offset, color(index, count));
                offset += ySpace.getValue();
            }
        } catch (Exception ignored) {

        }
        if (fps.getValue()) {
            index++;
            String pingString = "FPS: \u00a7f" + Minecraft.getDebugFPS();
            PutaHacknn.textManager.drawStringWithShadow(pingString, resolution.getScaledWidth() - PutaHacknn.textManager.getStringWidth(pingString) - 2, renderingUp ? resolution.getScaledHeight() - offset : offset, color(index, count));
        }

        if (totems.getValue()) {
            renderTotemHUD();
        }

        if (coords.getValue()) {
            index++;
            String coordsString = "XYZ: \u00a7f" + getRoundedDouble(mc.player.posX) + "\u00a77,\u00a7f " + getRoundedDouble(mc.player.posY) + "\u00a77,\u00a7f " + getRoundedDouble(mc.player.posZ);
            if (netherCoords.getValue() && mc.player.dimension != 1) {
                coordsString += " \u00a77(\u00a7f" + getRoundedDouble(getDimensionCoord(mc.player.posX)) + "\u00a77,\u00a7f " + getRoundedDouble(getDimensionCoord(mc.player.posZ)) + "\u00a77)";
            }
            if (direction.getValue()) {
                coordsString += " \u00a77(\u00a7f" + PlayerUtil.convertToCoords(mc.player.getHorizontalFacing()) + "\u00a77)";
            }
            PutaHacknn.textManager.drawStringWithShadow(coordsString, 2, resolution.getScaledHeight() - (chatOpened ? 24 : 10), color(index, count));
        }
    }

    public int flowingColor(int index, int count) {
        float[] hsb = new float[3];
        int color = getColor().getRGB();
        Color.RGBtoHSB(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, hsb);
        float brightness = Math.abs(((getOffset() + (index / (float) count) * 2) % 2) - 1);
        brightness = 0.5f + (0.4f * brightness);
        hsb[2] = brightness % 1f;
        return changeAlpha(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]), alpha.getValue());
    }

    public int color(int index, int count) {
        int color = getColor().getRGB();
        if (retardMode.getValue()) {// why?
            return flowingColor(index, count);
        }
        return changeAlpha(color, alpha.getValue());
    }

    public Color getColor() {
        return new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());
    }

    public float getOffset() {
        return (System.currentTimeMillis() % 2000) / 1000f;
    }

    public void renderTotemHUD() {
        ScaledResolution resolution = new ScaledResolution(mc);
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        int totems = PlayerUtil.getItemCount(Items.TOTEM_OF_UNDYING);
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int y = height - 55 - (mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
            int x = i - 189 + 9 * 20 + (totemX.getValue());
            GlStateManager.enableDepth();
            mc.getRenderItem().zLevel = 200F;
            mc.getRenderItem().renderItemAndEffectIntoGUI(TOTEM, x, y);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, TOTEM, x, y, "");
            mc.getRenderItem().zLevel = 0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            PutaHacknn.textManager.drawStringWithShadow(totems + "", x + 19 - (totemX.getValue()) - PutaHacknn.textManager.getStringWidth(totems + ""), y + 9, changeAlpha(0xffffff, alpha.getValue()));
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }

    public int getColorByDistance(double dist) {
        return (Color.HSBtoRGB((float)(Math.max(0.0, Math.min(dist * dist, 2500.0) / (double)(2500.0f)) / 3.0), 1.0f, 0.8f) | 0xFF000000);
    }

    public int getColorByDistance(Entity entity) {
        return (Color.HSBtoRGB((float)(Math.max(0.0, Math.min(mc.player.getDistanceSq(entity), 2500.0) / (double)(2500.0f)) / 3.0), 1.0f, 0.8f) | 0xFF000000);
    }

    public int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0F, Math.min(player.getHealth(), 36) / 36) / 3.0F, 1.0F, 0.8f) | 0xFF000000;
    }

    public static double getDimensionCoord(double coord) {
        if (mc.player.dimension == -1) {
            return coord * 8;
        } else if (mc.player.dimension == 0) {
            return coord / 8;
        }
        return coord;
    }

    public String getRoundedDouble(double pos) {
        return String.format("%.2f", pos);
    }

    public enum Rendering {
        UP,
        DOWN
    }

}
