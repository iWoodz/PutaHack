package me.dev.putahacknn.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.modules.combat.AutoTrap;
import me.dev.putahacknn.features.modules.combat.Killaura;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Hud extends Module {
    public static Hud INSTANCE = new Hud();

    private final Setting<Page> page = this.register(new Setting<>("Page", Page.GLOBAL));
    private final Setting<Boolean> grayColors = this.register(new Setting<>("Gray", true, v -> page.getValue() == Page.GLOBAL));
    public Setting<Boolean> lowerCase = this.register(new Setting<>("LowerCase", false, v -> page.getValue() == Page.GLOBAL));
    private final Setting<Boolean> renderingUp = this.register(new Setting<>("RenderingUp", true, v -> page.getValue() == Page.GLOBAL));
    private final Setting<Boolean> skeetBar = this.register(new Setting<>("SkeetMode", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> puta = this.register(new Setting<>("Puta Color", false, v -> page.getValue() == Page.ELEMENTS && skeetBar.getValue()));
    private final Setting<Boolean> watermark = this.register(new Setting<>("Watermark", true, v -> page.getValue() == Page.ELEMENTS));
    public Setting<String> watermarkString = this.register(new Setting<>("Text", "Putahacknn"));
    private final Setting<Boolean> watermarkShort = this.register(new Setting<>("Shorten", false, v -> watermark.getValue() && page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> watermarkVerColor = this.register(new Setting<>("VerColor", true, v -> watermark.getValue() && page.getValue() == Page.ELEMENTS));
    private final Setting<Integer> waterMarkY = this.register(new Setting<>("Height", 2, 2, 12, v -> page.getValue() == Page.ELEMENTS && watermark.getValue()));
    private final Setting<Boolean> idWatermark = this.register(new Setting<>("IdWatermark", true, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> pvp = this.register(new Setting<>("PvpInfo", true, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> textRadar = this.register(new Setting<>("TextRadar", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> coords = this.register(new Setting<>("Position(XYZ)", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> direction = this.register(new Setting<>("Direction", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> armor = this.register(new Setting<>("Armor", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> lag = this.register(new Setting<>("LagNotifier", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> greeter = this.register(new Setting<>("Welcomer", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<GreeterMode> greeterMode = this.register(new Setting<>("Mode", GreeterMode.PLAYER, v -> page.getValue() == Page.ELEMENTS && greeter.getValue()));
    private final Setting<Boolean> greeterNameColor = this.register(new Setting<>("NameColor", true, v -> greeter.getValue() && greeterMode.getValue() == GreeterMode.PLAYER && page.getValue() == Page.ELEMENTS));
    private final Setting<String> greeterText = this.register(new Setting<>("WelcomerText", "i gain funds from thin air, my wealth is unreachable", v -> greeter.getValue() && greeterMode.getValue() == GreeterMode.CUSTOM && page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> arrayList = this.register(new Setting<>("ArrayList", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> black = this.register(new Setting<>("black", false, v -> page.getValue() == Page.ELEMENTS && arrayList.getValue()));
    private final Setting<Boolean> forgeHax = this.register(new Setting<>("ForgeHax", false, v -> page.getValue() == Page.ELEMENTS && arrayList.getValue()));
    private final Setting<Boolean> arrayListLine = this.register(new Setting<>("Outline", false, v -> page.getValue() == Page.ELEMENTS && arrayList.getValue()));
    private final Setting<Boolean> arrayListRect = this.register(new Setting<>("Rect", false, v -> page.getValue() == Page.ELEMENTS && arrayList.getValue()));
    private final Setting<Boolean> arrayListRectColor = this.register(new Setting<>("ColorRect", false, v -> page.getValue() == Page.ELEMENTS && arrayList.getValue() && arrayListRect.getValue()));
    private final Setting<Boolean> arrayListGlow = this.register(new Setting<>("Glow", true, v -> page.getValue() == Page.ELEMENTS && arrayList.getValue()));
    private final Setting<Boolean> hideInChat = this.register(new Setting<>("HideInChat", true, v -> page.getValue() == Page.ELEMENTS && arrayList.getValue()));
    private final Setting<Boolean> potions = this.register(new Setting<>("Potions", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> potionColor = this.register(new Setting<>("PotionColor", false, v -> page.getValue() == Page.ELEMENTS && potions.getValue()));
    private final Setting<Boolean> ping = this.register(new Setting<>("Ping", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> speed = this.register(new Setting<>("Speed", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> tps = this.register(new Setting<>("TPS", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> fps = this.register(new Setting<>("FPS", false, v -> page.getValue() == Page.ELEMENTS));
    private final Setting<Boolean> time = this.register(new Setting<>("Time", false, v -> page.getValue() == Page.ELEMENTS));
    private final Timer timer = new Timer();
    private Map<String, Integer> players = new HashMap<>();

    private int color;

    public Hud() {
        super("Hud", "i hope whoever made this kills themsel", Category.CLIENT, true, false, false);
        setInstance();
    }

    public static Hud getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Hud();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private enum GreeterMode {
        PLAYER,
        CUSTOM
    }

    private enum Page {
        ELEMENTS,
        GLOBAL
    }

    @Override
    public void onUpdate() {
        if (timer.passedMs(500)) {
            players = getTextRadarMap();
            timer.reset();
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) return;

        int width = PutaHacknn.textManager.scaledWidth;
        int height = PutaHacknn.textManager.scaledHeight;

        color = ColorUtil.toRGBA(ClickGui.getInstance().getColor2().getRed(), ClickGui.getInstance().getColor2().getGreen(), ClickGui.getInstance().getColor2().getBlue());

        if (watermark.getValue()) {
            String putaString = watermarkString.getValue() + " ";
            String verColor = watermarkVerColor.getValue() ? "" + ChatFormatting.WHITE : "";
            String verString = verColor + (watermarkShort.getValue() ? PutaHacknn.MODVER.substring(0, 4) : PutaHacknn.MODVER);

            if ((ClickGui.getInstance()).rainbow.getValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    PutaHacknn.textManager.drawString((lowerCase.getValue() ? putaString.toLowerCase() : putaString) + verString, 2.0F, waterMarkY.getValue(), PutaHacknn.colorManager.getRainbow().getRGB(), true);
                } else {
                    if (watermarkVerColor.getValue()) {
                        drawDoubleRainbowRollingString((lowerCase.getValue() ? putaString.toLowerCase() : putaString), verString, 2.0f, waterMarkY.getValue(), true);
                    } else {
                        PutaHacknn.textManager.drawRollingRainbowString((lowerCase.getValue() ? putaString.toLowerCase() : putaString) + verString, 2.0f, waterMarkY.getValue(), true);
                    }
                }
            } else {
                PutaHacknn.textManager.drawString((lowerCase.getValue() ? putaString.toLowerCase() : putaString) + verString, 2.0F, waterMarkY.getValue(), color, true);
            }
        }

        Color color = new Color(ClickGui.getInstance().getColor2().getRed(), ClickGui.getInstance().getColor2().getGreen(), ClickGui.getInstance().getColor2().getBlue());

        if (skeetBar.getValue()) {

            if (puta.getValue()) {
                RenderUtil.drawHGradientRect(0, 0, width / 5.0f, 1, ColorUtil.toRGBA(0, 180, 255), ColorUtil.toRGBA(255, 180, 255));
                RenderUtil.drawHGradientRect(width / 5.0f, 0, (width / 5.0f) * 2.0f, 1, ColorUtil.toRGBA(255, 180, 255), ColorUtil.toRGBA(255, 255, 255));
                RenderUtil.drawHGradientRect((width / 5.0f) * 2.0f, 0, (width / 5.0f) * 3.0f, 1, ColorUtil.toRGBA(255, 255, 255), ColorUtil.toRGBA(255, 255, 255));
                RenderUtil.drawHGradientRect((width / 5.0f) * 3.0f, 0, (width / 5.0f) * 4.0f, 1, ColorUtil.toRGBA(255, 255, 255), ColorUtil.toRGBA(255, 180, 255));
                RenderUtil.drawHGradientRect((width / 5.0f) * 4.0f, 0, width, 1, ColorUtil.toRGBA(255, 180, 255), ColorUtil.toRGBA(0, 180, 255));
            }
            if (ClickGui.getInstance().rainbow.getValue() && ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway && !puta.getValue()) {
                int[] arrayOfInt = {1};
                RenderUtil.drawHGradientRect(0, 0, width / 2.0f, 1, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB(), ColorUtil.rainbow(20 * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB());
                RenderUtil.drawHGradientRect(width / 2.0f, 0, width, 1, ColorUtil.rainbow(20 * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB(), ColorUtil.rainbow(40 * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB());
                arrayOfInt[0] = arrayOfInt[0] + 1;
            }
            if (!ClickGui.getInstance().rainbow.getValue() && !puta.getValue()) {
                RenderUtil.drawHGradientRect(0, 0, width / 2.0f, 1, ColorUtil.thehahafunny(color, 50, 1000).getRGB(), ColorUtil.thehahafunny(color, 200, 1).getRGB());
                RenderUtil.drawHGradientRect(width / 2.0f, 0, width, 1, ColorUtil.thehahafunny(color, 200, 1).getRGB(), ColorUtil.thehahafunny(color, 50, 1000).getRGB());
            }
        }

        if (textRadar.getValue()) drawTextRadar(watermark.getValue() ? waterMarkY.getValue() + 2 : 2);

        if (pvp.getValue()) drawPvPInfo();

        this.color = ColorUtil.toRGBA(ClickGui.getInstance().getColor2().getRed(), ClickGui.getInstance().getColor2().getGreen(), ClickGui.getInstance().getColor2().getBlue());
        if (idWatermark.getValue()) {
            String putaString = "putahack";
            String domainString = ChatFormatting.GRAY + ".nn";
            float offset = PutaHacknn.textManager.scaledHeight / 2.0f - 30.0f;
            if ((ClickGui.getInstance()).rainbow.getValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    PutaHacknn.textManager.drawString(putaString + domainString, 2.0f, offset, PutaHacknn.colorManager.getRainbow().getRGB(), true);
                } else {
                    PutaHacknn.textManager.drawRollingRainbowString(putaString, 2.0f, offset, true);
                    PutaHacknn.textManager.drawString(domainString, PutaHacknn.textManager.getStringWidth(putaString) + (FontMod.getInstance().isOn() ? -1.0f : 1.4f), offset, -1, true);
                }
            } else {
                PutaHacknn.textManager.drawString(putaString + domainString, 2.0f, offset, this.color, true);
            }
        }
        int[] counter1 = {1};
        boolean inChat = mc.currentScreen instanceof GuiChat;
        long enabledMods = PutaHacknn.moduleManager.modules.stream().filter(module -> module.isOn() && module.isDrawn()).count();
        int j = (inChat && !renderingUp.getValue()) ? 14 : 0;
        int rectColor = black.getValue() ? ColorUtil.newAlpha(getPutaColor(counter1[0] + 1), 60) : arrayListRectColor.getValue() ? (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.toRGBA(ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRed(), ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getGreen(), ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getBlue(), 60) : ColorUtil.toRGBA(PutaHacknn.colorManager.getRainbow().getRed(), PutaHacknn.colorManager.getRainbow().getGreen(), PutaHacknn.colorManager.getRainbow().getBlue(), 60)) : ColorUtil.toRGBA((ColorUtil.thehahafunny(color, 50, counter1[0]).getRed()), (ColorUtil.thehahafunny(color, 50, counter1[0]).getGreen()), (ColorUtil.thehahafunny(color, 50, counter1[0]).getBlue()), 60) : ColorUtil.toRGBA(10, 10, 10, 60);
        int glowColor = black.getValue() ? ColorUtil.newAlpha(getPutaColor(counter1[0] + 1), 60) : (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.toRGBA(ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRed(), ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getGreen(), ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getBlue(), 60) : ColorUtil.toRGBA(PutaHacknn.colorManager.getRainbow().getRed(), PutaHacknn.colorManager.getRainbow().getGreen(), PutaHacknn.colorManager.getRainbow().getBlue(), 60)) : ColorUtil.toRGBA((ColorUtil.thehahafunny(color, 50, counter1[0]).getRed()), (ColorUtil.thehahafunny(color, 50, counter1[0]).getGreen()), (ColorUtil.thehahafunny(color, 50, counter1[0]).getBlue()), 60);
        if (arrayList.getValue()) {
            if (renderingUp.getValue()) {
                if (inChat && hideInChat.getValue()) {
                    PutaHacknn.textManager.drawString(enabledMods + " mods enabled", (width - 2 - PutaHacknn.textManager.getStringWidth(enabledMods + " mods enabled")), 2 + j, (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : this.color, true);
                } else {
                    for (int k = 0; k < PutaHacknn.moduleManager.sortedModules.size(); k++) {
                        Module module = PutaHacknn.moduleManager.sortedModules.get(k);
                        String str = module.getName() + ChatFormatting.GRAY + ((module.getInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getInfo() + ChatFormatting.GRAY + "]") : "");
                        if (forgeHax.getValue()) {
                            str = module.getName() + ChatFormatting.GRAY + ((module.getInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getInfo() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + "<") : ChatFormatting.RESET + "<");
                        }

                        if (arrayListRect.getValue()) {
                            Gui.drawRect((width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 1,
                                    j == 0 ? 0 : (2 + j * 10),
                                    width,
                                    (2 + j * 10) + 10,
                                    rectColor);
                        }

                        if (arrayListGlow.getValue()) {
                            RenderUtil.drawGlow((width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 1,
                                    j == 0 ? 0 : (2 + j * 10) - 4,
                                    width,
                                    (2 + j * 10) + 15,
                                    glowColor);
                        }

                        if (arrayListLine.getValue()) {
                            Gui.drawRect((width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 2, j == 0 ? 0 : (2 + j * 10),
                                    (width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 1, (2 + j * 10) + 10,
                                    black.getValue() ? getPutaColor(counter1[0] - 2) :
                                            (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB());

                            int a = k + 1;
                            if (a >= PutaHacknn.moduleManager.sortedModules.size()) a = k;
                            Module nextModule = PutaHacknn.moduleManager.sortedModules.get(a);
                            String nextStr = nextModule.getName() + ChatFormatting.GRAY + ((nextModule.getInfo() != null) ? (" [" + ChatFormatting.WHITE + nextModule.getInfo() + ChatFormatting.GRAY + "]") : "");
                            if (forgeHax.getValue()) {
                                nextStr = nextModule.getName() + ChatFormatting.GRAY + ((nextModule.getInfo() != null) ? (" [" + ChatFormatting.WHITE + nextModule.getInfo() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + "<") : ChatFormatting.RESET + "<");
                            }

                            Gui.drawRect((width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 2, (2 + (j + 1) * 10),
                                    a == k ? width : (width - 2 - ((lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) +
                                            (((lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) -
                                                    (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(nextStr.toLowerCase()) : PutaHacknn.textManager.getStringWidth(nextStr))))) - 1,
                                    (2 + (j + 1) * 10) + 1,
                                    black.getValue() ? getPutaColor(counter1[0] - 2) :
                                            (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB());
                        }

                        PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                                (width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))), (2 + j * 10),
                                black.getValue() ? getPutaColor(counter1[0] - 2) :
                                        (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                        j++;
                        counter1[0] = counter1[0] + 1;
                    }
                }
            } else {
                if (inChat && hideInChat.getValue()) {
                    PutaHacknn.textManager.drawString(enabledMods + " mods enabled", (width - 2 - PutaHacknn.textManager.getStringWidth(enabledMods + " mods enabled")), height - j - 11, (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : this.color, true);
                } else {
                    for (int k = 0; k < PutaHacknn.moduleManager.sortedModules.size(); k++) {
                        Module module = PutaHacknn.moduleManager.sortedModules.get(k);
                        String str = module.getName() + ChatFormatting.GRAY + ((module.getInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getInfo() + ChatFormatting.GRAY + "]") : "");
                        if (forgeHax.getValue()) {
                            str = module.getName() + ChatFormatting.GRAY + ((module.getInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getInfo() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + "<") : ChatFormatting.RESET + "<");
                        }
                        j += 10;

                        if (arrayListRect.getValue()) {
                            Gui.drawRect((width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 1,
                                    (height - j),
                                    width,
                                    j == 1 ? height : (height - j) + 10,
                                    rectColor);
                        }

                        if (arrayListGlow.getValue()) {
                            RenderUtil.drawGlow((width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 1,
                                    (height - j) - 4,
                                    width,
                                    j == 1 ? height : (height - j) + 15,
                                    glowColor);
                        }

                        if (arrayListLine.getValue()) {
                            Gui.drawRect((width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 2, (height - j),
                                    (width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 1, j == 1 ? height : (height - j) + 10,
                                    black.getValue() ? getPutaColor(counter1[0] - 2) :
                                            (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB());

                            int a = k + 1;
                            if (a >= PutaHacknn.moduleManager.sortedModules.size()) a = k;
                            Module nextModule = PutaHacknn.moduleManager.sortedModules.get(a);
                            String nextStr = nextModule.getName() + ChatFormatting.GRAY + ((nextModule.getInfo() != null) ? (" [" + ChatFormatting.WHITE + nextModule.getInfo() + ChatFormatting.GRAY + "]") : "");
                            if (forgeHax.getValue()) {
                                nextStr = nextModule.getName() + ChatFormatting.GRAY + ((nextModule.getInfo() != null) ? (" [" + ChatFormatting.WHITE + nextModule.getInfo() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + "<") : ChatFormatting.RESET + "<");
                            }

                            Gui.drawRect((width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) - 2, (height - j) - 1,
                                    a == k ? width : (width - 2 - ((lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))) +
                                            (((lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) -
                                                    (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(nextStr.toLowerCase()) : PutaHacknn.textManager.getStringWidth(nextStr))))) - 1,
                                    (height - j),
                                    black.getValue() ? getPutaColor(counter1[0] - 2) :
                                            (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB());
                        }

                        PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                                (width - 2 - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str))), (height - j),
                                black.getValue() ? getPutaColor(counter1[0] - 2) :
                                        (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                        counter1[0] = counter1[0] + 1;

                    }
                }
            }
        }
        String grayString = grayColors.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && renderingUp.getValue()) ? 13 : (renderingUp.getValue() ? -2 : 0);

        if (renderingUp.getValue()) {
            if (potions.getValue()) {
                java.util.List<PotionEffect> effects = new ArrayList<>((Minecraft.getMinecraft()).player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    String str = getColoredPotionString(potionEffect);
                    i += 10;

                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                            (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) - 2),
                            (height - 2 - i),
                            potionColor.getValue() ? potionEffect.getPotion().getLiquidColor() :
                                    ((ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB()), true);
                    counter1[0] = counter1[0] + 1;
                }
            }
            if (speed.getValue()) {
                String str = grayString + "Speed " + ChatFormatting.WHITE + PutaHacknn.speedManager.getSpeedKpH() + " km/h";
                i += 10;

                PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                        (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) - 2),
                        (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                counter1[0] = counter1[0] + 1;
            }
            if (time.getValue()) {
                String str = grayString + "Time " + ChatFormatting.WHITE + (new SimpleDateFormat("h:mm a")).format(new Date());
                i += 10;

                PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                        (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) - 2),
                        (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                counter1[0] = counter1[0] + 1;
            }
            if (tps.getValue()) {
                String str = grayString + "TPS " + ChatFormatting.WHITE + PutaHacknn.serverManager.getTPS();
                i += 10;

                PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                        (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) - 2),
                        (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                counter1[0] = counter1[0] + 1;
            }

            String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.getDebugFPS();
            String str1 = grayString + "Ping " + ChatFormatting.WHITE + PutaHacknn.serverManager.getPing();

            if (PutaHacknn.textManager.getStringWidth(str1) > PutaHacknn.textManager.getStringWidth(fpsText)) {
                if (ping.getValue()) {
                    i += 10;
                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? str1.toLowerCase() : str1, (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str1.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str1)) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);counter1[0] = counter1[0] + 1;
                }
                if (fps.getValue()) {
                    i += 10;
                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? fpsText.toLowerCase() : fpsText, (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(fpsText.toLowerCase()) : PutaHacknn.textManager.getStringWidth(fpsText)) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);counter1[0] = counter1[0] + 1;
                }
            } else {
                if (fps.getValue()) {
                    i += 10;

                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? fpsText.toLowerCase() : fpsText,
                            (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(fpsText.toLowerCase()) : PutaHacknn.textManager.getStringWidth(fpsText)) - 2),
                            (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                    counter1[0] = counter1[0] + 1;
                }
                if (ping.getValue()) {
                    i += 10;

                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? str1.toLowerCase() : str1,
                            (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str1.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str1)) - 2),
                            (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            if (potions.getValue()) {
                java.util.List<PotionEffect> effects = new ArrayList<>((Minecraft.getMinecraft()).player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    String str = getColoredPotionString(potionEffect);

                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                            (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) - 2),
                            (2 + i++ * 10),
                            potionColor.getValue() ? potionEffect.getPotion().getLiquidColor() :
                                    ((ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB()), true);
                    counter1[0] = counter1[0] + 1;
                }
            }
            if (speed.getValue()) {
                String str = grayString + "Speed " + ChatFormatting.WHITE + PutaHacknn.speedManager.getSpeedKpH() + " km/h";

                PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                        (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) - 2),
                        (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                counter1[0] = counter1[0] + 1;
            }
            if (time.getValue()) {
                String str = grayString + "Time " + ChatFormatting.WHITE + (new SimpleDateFormat("h:mm a")).format(new Date());

                PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                        (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) - 2),
                        (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                counter1[0] = counter1[0] + 1;
            }
            if (tps.getValue()) {
                String str = grayString + "TPS " + ChatFormatting.WHITE + PutaHacknn.serverManager.getTPS();

                PutaHacknn.textManager.drawString(lowerCase.getValue() ? str.toLowerCase() : str,
                        (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str)) - 2),
                        (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                counter1[0] = counter1[0] + 1;
            }

            String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.getDebugFPS();
            String str1 = grayString + "Ping " + ChatFormatting.WHITE + PutaHacknn.serverManager.getPing();

            if (PutaHacknn.textManager.getStringWidth(str1) > PutaHacknn.textManager.getStringWidth(fpsText)) {
                if (ping.getValue()) {
                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? str1.toLowerCase() : str1,
                            (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str1.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str1)) - 2),
                            (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                    counter1[0] = counter1[0] + 1;
                }
                if (fps.getValue()) {
                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? fpsText.toLowerCase() : fpsText,
                            (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(fpsText.toLowerCase()) : PutaHacknn.textManager.getStringWidth(fpsText)) - 2),
                            (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (fps.getValue()) {
                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? fpsText.toLowerCase() : fpsText,
                            (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(fpsText.toLowerCase()) : PutaHacknn.textManager.getStringWidth(fpsText)) - 2),
                            (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                    counter1[0] = counter1[0] + 1;
                }
                if (ping.getValue()) {
                    PutaHacknn.textManager.drawString(lowerCase.getValue() ? str1.toLowerCase() : str1,
                            (width - (lowerCase.getValue() ? PutaHacknn.textManager.getStringWidth(str1.toLowerCase()) : PutaHacknn.textManager.getStringWidth(str1)) - 2),
                            (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Sideway) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : PutaHacknn.colorManager.getRainbow().getRGB()) : ColorUtil.thehahafunny(color, 50, counter1[0]).getRGB(), true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }

        boolean inHell = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell");

        int posX = (int) mc.player.posX;
        int posY = (int) mc.player.posY;
        int posZ = (int) mc.player.posZ;

        float nether = !inHell ? 0.125F : 8.0F;

        int hposX = (int) (mc.player.posX * nether);
        int hposZ = (int) (mc.player.posZ * nether);
        int yawPitch = (int) MathHelper.wrapDegrees(mc.player.rotationYaw);
        int p = coords.getValue() ? 0 : 11;

        i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0;

        String coordinates = (lowerCase.getValue() ? "XYZ: ".toLowerCase() : "XYZ: ") + ChatFormatting.WHITE + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + hposX + ", " + hposZ + ChatFormatting.GRAY + "]" + ChatFormatting.WHITE) : (posX + ", " + posY + ", " + posZ + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + hposX + ", " + hposZ + ChatFormatting.GRAY + "]"));
        String direction = this.direction.getValue() ? RotationUtil.getDirection4D(false) : "";
        String yaw = this.direction.getValue() ? (lowerCase.getValue() ? "Yaw: ".toLowerCase() : "Yaw: ") + ChatFormatting.WHITE + yawPitch : "";
        String coords = this.coords.getValue() ? coordinates : "";

        i += 10;

        if (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && this.direction.getValue()) {
            yaw = "";
            direction = (lowerCase.getValue() ? "Yaw: ".toLowerCase() : "Yaw: ") + ChatFormatting.WHITE + yawPitch + ChatFormatting.RESET + " " + getFacingDirectionShort();
        }
        if ((ClickGui.getInstance()).rainbow.getValue()) {
            String rainbowCoords = this.coords.getValue() ? ((lowerCase.getValue() ? "XYZ: ".toLowerCase() : "XYZ: ") + ChatFormatting.WHITE + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + hposX + ", " + hposZ + ChatFormatting.GRAY + "]" + ChatFormatting.WHITE) : (posX + ", " + posY + ", " + posZ + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + hposX + ", " + hposZ + ChatFormatting.GRAY + "]"))) : "";
            if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                PutaHacknn.textManager.drawString(direction, 2.0F, (height - i - 11 + p), PutaHacknn.colorManager.getRainbow().getRGB(), true);
                PutaHacknn.textManager.drawString(yaw, 2.0F, (height - i - 22 + p), PutaHacknn.colorManager.getRainbow().getRGB(), true);
                PutaHacknn.textManager.drawString(rainbowCoords, 2.0F, (height - i), PutaHacknn.colorManager.getRainbow().getRGB(), true);
            } else {
                if (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && this.direction.getValue()) {
                    drawDoubleRainbowRollingString((lowerCase.getValue() ? "Yaw: ".toLowerCase() : "Yaw: "), "" + ChatFormatting.WHITE + yawPitch, 2.0f, (height - i - 11 + p), true);
                    String uh = "Yaw: " + ChatFormatting.WHITE + yawPitch;
                    PutaHacknn.textManager.drawRollingRainbowString(" " + getFacingDirectionShort(), 2.0f + PutaHacknn.textManager.getStringWidth(uh), (height - i - 11 + p), true);
                } else {
                    PutaHacknn.textManager.drawRollingRainbowString(this.direction.getValue() ? direction : "", 2.0f, (height - i - 11 + p), true);
                    drawDoubleRainbowRollingString(this.direction.getValue() ? (lowerCase.getValue() ? "Yaw: ".toLowerCase() : "Yaw: ") : "", this.direction.getValue() ? ("" + ChatFormatting.WHITE + yawPitch) : "", 2.0f, (height - i - 22 + p), true);
                }
                drawDoubleRainbowRollingString(this.coords.getValue() ? (lowerCase.getValue() ? "XYZ: ".toLowerCase() : "XYZ: ") : "", this.coords.getValue() ? ("" + ChatFormatting.WHITE + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + hposX + ", " + hposZ + ChatFormatting.GRAY + "]" + ChatFormatting.WHITE) : (posX + ", " + posY + ", " + posZ + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + hposX + ", " + hposZ + ChatFormatting.GRAY + "]"))) : "", 2.0F, (height - i), true);
            }
        } else {
            PutaHacknn.textManager.drawString(direction, 2.0F, (height - i - 11 + p), this.color, true);
            PutaHacknn.textManager.drawString(yaw, 2.0F, (height - i - 22 + p), this.color, true);
            PutaHacknn.textManager.drawString(coords, 2.0F, (height - i), this.color, true);
        }

        if (armor.getValue()) drawArmorHUD();

        if (greeter.getValue()) drawWelcomer();

        if (lag.getValue()) drawLagOMeter();
    }

    private void drawWelcomer() {
        int width = PutaHacknn.textManager.scaledWidth;
        String nameColor = greeterNameColor.getValue() ? "" + ChatFormatting.WHITE : "";
        String text = (lowerCase.getValue() ? "Welcome, ".toLowerCase() : "Welcome, ");

        if (greeterMode.getValue() == GreeterMode.PLAYER) {
            if (greeter.getValue())
                text = text + nameColor + mc.player.getDisplayNameString();

            if ((ClickGui.getInstance()).rainbow.getValue()) {

                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    PutaHacknn.textManager.drawString(text + ChatFormatting.RESET + " :')", width / 2.0F - PutaHacknn.textManager.getStringWidth(text) / 2.0F + 2.0F, 2.0F, PutaHacknn.colorManager.getRainbow().getRGB(), true);
                } else {

                    if (greeterNameColor.getValue()) {
                        drawDoubleRainbowRollingString((lowerCase.getValue() ? "Welcome,".toLowerCase() : "Welcome,"), ((FontMod.getInstance().isOn() ? "" : " ")) + ChatFormatting.WHITE + mc.player.getDisplayNameString(), width / 2.0F - PutaHacknn.textManager.getStringWidth(text) / 2.0F + 2.0F, 2.0F, true);
                        PutaHacknn.textManager.drawRollingRainbowString(" :')", width / 2.0F - PutaHacknn.textManager.getStringWidth(text) / 2.0F + 1.5f + PutaHacknn.textManager.getStringWidth(text) - (FontMod.getInstance().isOn() ? 1.5f : 0.0f), 2.0f, true);
                    } else {
                        PutaHacknn.textManager.drawRollingRainbowString((lowerCase.getValue() ? "Welcome,".toLowerCase() : "Welcome, ") + mc.player.getDisplayNameString() + " :^)", width / 2.0F - PutaHacknn.textManager.getStringWidth(text) / 2.0F + 2.0F, 2.0F, true);
                    }
                }
            } else {
                PutaHacknn.textManager.drawString(text + ChatFormatting.RESET + " :')", width / 2.0F - PutaHacknn.textManager.getStringWidth(text) / 2.0F + 2.0F, 2.0F, color, true);
            }
        } else {
            String lel = greeterText.getValue();
            if (greeter.getValue())
                lel = greeterText.getValue();
            if ((ClickGui.getInstance()).rainbow.getValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    PutaHacknn.textManager.drawString(lel, width / 2.0F - PutaHacknn.textManager.getStringWidth(lel) / 2.0F + 2.0F, 2.0F, PutaHacknn.colorManager.getRainbow().getRGB(), true);
                } else {
                    PutaHacknn.textManager.drawRollingRainbowString(lel, width / 2.0F - PutaHacknn.textManager.getStringWidth(lel) / 2.0F + 2.0F, 2.0F, true);
                }
            } else {
                PutaHacknn.textManager.drawString(lel, width / 2.0F - PutaHacknn.textManager.getStringWidth(lel) / 2.0F + 2.0F, 2.0F, color, true);
            }
        }
    }

    private void drawLagOMeter() {
        int width = PutaHacknn.textManager.scaledWidth;
        if (PutaHacknn.serverManager.isServerNotResponding()) {
            String text = ChatFormatting.RED + (lowerCase.getValue() ? "Server is lagging for ".toLowerCase() : "Server is lagging for ") + MathUtil.round((float) PutaHacknn.serverManager.serverRespondingTime() / 1000.0F, 1) + "s.";
            PutaHacknn.textManager.drawString(text, width / 2.0F - PutaHacknn.textManager.getStringWidth(text) / 2.0F + 2.0F, 20.0F, color, true);
        }
    }

    private void drawArmorHUD() {
        int width = PutaHacknn.textManager.scaledWidth;
        int height = PutaHacknn.textManager.scaledHeight;
        GlStateManager.enableTexture2D();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
        for (ItemStack is : mc.player.inventory.armorInventory) {
            iteration++;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            PutaHacknn.textManager.drawStringWithShadow(s, x + 19 - 2 - PutaHacknn.textManager.getStringWidth(s), y + 9, 0xffffff);

            if (true) {
                int dmg = 0;
                int itemDurability = is.getMaxDamage() - is.getItemDamage();
                float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
                float red = 1 - green;
                if (true) {
                    dmg = 100 - (int) (red * 100);
                } else {
                    dmg = itemDurability;
                }
                PutaHacknn.textManager.drawStringWithShadow(dmg + "", x + 8 - PutaHacknn.textManager.getStringWidth(dmg + "") / 2, y - 11, ColorUtil.toRGBA((int) (red * 255), (int) (green * 255), 0));
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    private void drawPvPInfo() {
        float yOffset = PutaHacknn.textManager.scaledHeight / 2.0f;

        int totemCount = mc.player.inventory.mainInventory.stream().filter((itemStack) -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();

        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totemCount += mc.player.getHeldItemOffhand().getCount();
        }

        int pingCount = PutaHacknn.serverManager.getPing();

        EntityPlayer target = EntityUtil.getClosestEnemy(7);

        //Strings

        String totemString = "" + (totemCount != 0 ? ChatFormatting.GREEN : ChatFormatting.RED) + totemCount;
        String pingColor;
        String safetyColor;

        String htrColor = String.valueOf(
                (target != null && mc.player.getDistance(target) <= PutaHacknn.moduleManager.getModuleByClass(Killaura.class).range.getValue())
                        ? ChatFormatting.GREEN
                        : ChatFormatting.DARK_RED);

        String plrColor = String.valueOf(
                (target != null && mc.player.getDistance(target) <= 5 && PutaHacknn.moduleManager.getModuleByClass(AutoTrap.class).isEnabled())
                        ? ChatFormatting.GREEN
                        : ChatFormatting.DARK_RED);

        String htr = "HTR";
        String plr = "PLR";

        //Stuff

        if (pingCount < 40) {
            pingColor = String.valueOf(ChatFormatting.GREEN);

        } else if (pingCount < 65) {
            pingColor = String.valueOf(ChatFormatting.DARK_GREEN);

        } else if (pingCount < 80) {
            pingColor = String.valueOf(ChatFormatting.YELLOW);

        } else if (pingCount < 110) {
            pingColor = String.valueOf(ChatFormatting.RED);

        } else if (pingCount < 160) {
            pingColor = String.valueOf(ChatFormatting.DARK_RED);

        } else {
            pingColor = String.valueOf(ChatFormatting.DARK_RED);
        }

        if (!EntityUtil.isSafe(mc.player, 0, true)) {
            safetyColor = String.valueOf(ChatFormatting.DARK_RED);

        } else {
            safetyColor = String.valueOf(ChatFormatting.GREEN);
        }

        //HTR

        PutaHacknn.textManager.drawString(
                htrColor + htr,
                2.0f,
                yOffset - 20.0f,
                color,
                true);

        //PLR

        PutaHacknn.textManager.drawString(
                plrColor + plr,
                2.0f,
                yOffset - 10.0f,
                color,
                true);

        //Ping

        PutaHacknn.textManager.drawString(
                pingColor + pingCount + " MS",
                2.0F,
                yOffset,
                color,
                true);

        //Totems

        PutaHacknn.textManager.drawString(
                totemString,
                2.0F,
                yOffset + 10.0f,
                color,
                true);

        //Safety

        PutaHacknn.textManager.drawString(
                safetyColor + "LBY",
                2.0F,
                yOffset + 20.0f,
                color,
                true);
    }

    private void drawDoubleRainbowRollingString(String first, String second, float x, float y, boolean shadow) {
        PutaHacknn.textManager.drawRollingRainbowString(first, x, y, shadow);
        PutaHacknn.textManager.drawString(second, x + PutaHacknn.textManager.getStringWidth(first), y, -1, shadow);
    }

    private void drawTextRadar(int yOffset) {

        if (!players.isEmpty()) {

            int y = PutaHacknn.textManager.getFontHeight() + 7 + yOffset;

            for (Map.Entry<String, Integer> player : players.entrySet()) {

                String text = player.getKey() + " ";

                int textHeight = PutaHacknn.textManager.getFontHeight() + 1;

                if (ClickGui.getInstance().rainbow.getValue()) {

                    if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                        PutaHacknn.textManager.drawString(text, 2.0F, (float) y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        y += textHeight;

                    } else {
                        PutaHacknn.textManager.drawString(text, 2.0F, (float) y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        y += textHeight;
                    }

                } else {
                    PutaHacknn.textManager.drawString(text, 2.0F, (float) y, color, true);
                    y += textHeight;
                }
            }
        }
    }

    private Map<String, Integer> getTextRadarMap() {
        Map<String, Integer> retval = new HashMap<>();

        DecimalFormat dfDistance = new DecimalFormat("#.#");
        dfDistance.setRoundingMode(RoundingMode.CEILING);
        StringBuilder distanceSB = new StringBuilder();

        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.isInvisible() || player.getName().equals(mc.player.getName())) continue;

            int distanceInt = (int) mc.player.getDistance(player);
            String distance = dfDistance.format(distanceInt);

            if (distanceInt >= 25) {
                distanceSB.append(ChatFormatting.GREEN);

            } else if (distanceInt > 10) {
                distanceSB.append(ChatFormatting.YELLOW);

            } else {
                distanceSB.append(ChatFormatting.RED);
            }
            distanceSB.append(distance);

            retval.put(
                    (PutaHacknn.friendManager.isFriend(player.getName()) ? ChatFormatting.GOLD + "< > " + ChatFormatting.RESET : "")
                            + (PutaHacknn.friendManager.isFriend(player) ? ChatFormatting.AQUA : ChatFormatting.RESET)
                            + player.getName()
                            + " "
                            + ChatFormatting.WHITE
                            + "["
                            + ChatFormatting.RESET
                            + distanceSB
                            + "m"
                            + ChatFormatting.WHITE
                            + "] "
                            + ChatFormatting.GREEN,
                    (int) mc.player.getDistance(player));

            distanceSB.setLength(0);
        }

        if (!retval.isEmpty()) {
            retval = MathUtil.sortByValue(retval, false);
        }

        return retval;
    }

    private int getPutaColor(int n) {
        int n2 = PutaHacknn.moduleManager.modules.size();
        int n3 = new Color(255, 255, 255).getRGB();
        int n4 = Color.WHITE.getRGB();
        int n5 = new Color(110, 0, 255).getRGB();
        int n6 = n2 / 5;
        if (n < n6) {
            return n3;
        }
        if (n < n6 * 2) {
            return n5;
        }
        if (n < n6 * 3) {
            return n4;
        }
        if (n < n6 * 4) {
            return n5;
        }
        if (n < n6 * 5) {
            return n3;
        }
        return n3;
    }

    private String getFacingDirectionShort() {
        int dirnumber = RotationUtil.getDirection4D();

        if (dirnumber == 0) {
            return "(+Z)";
        }
        if (dirnumber == 1) {
            return "(-X)";
        }
        if (dirnumber == 2) {
            return "(-Z)";
        }
        if (dirnumber == 3) {
            return "(+X)";
        }
        return "Loading...";
    }

    private String getColoredPotionString(PotionEffect effect) {
        Potion potion = effect.getPotion();
        return I18n.format(potion.getName()) + " " + (effect.getAmplifier() + 1) + " " + ChatFormatting.WHITE + Potion.getPotionDurationString(effect, 1.0f);
    }
}
