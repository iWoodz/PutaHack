package me.dev.putahacknn.manager;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.Feature;
import me.dev.putahacknn.features.gui.font.CustomFont;
import me.dev.putahacknn.features.modules.client.ClickGui;
import me.dev.putahacknn.features.modules.client.FontMod;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.Timer;
import me.dev.putahacknn.util.Util;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.io.InputStream;
import java.util.regex.Pattern;

public class TextManager
        extends Feature {
    private final Timer idleTimer = new Timer();
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    public CustomFont customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
    public CustomFont customFont2 = new CustomFont(getFont("Lato-Regular.ttf", 18), true, false);
    private boolean idling;

    public TextManager() {
        this.updateResolution();
    }

    public void init(boolean startup) {
        FontMod cFont = PutaHacknn.moduleManager.getModuleByClass(FontMod.class);
        try {
            this.setFontRenderer(new Font(cFont.fontName.getValue(), cFont.fontStyle.getValue(), cFont.fontSize.getValue()), cFont.antiAlias.getValue(), cFont.fractionalMetrics.getValue());
        } catch (Exception exception) {
            // empty catch block
        }
    }

    public void drawStringCustomFont(String text, float x, float y, int color, boolean shadow) {
        if (shadow) {
            customFont2.drawStringWithShadow(text, x, y, color);
        } else {
            customFont2.drawString(text, x, y, color);
        }
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public void drawString(String text, float x, float y, int color, boolean shadow, boolean customFont) {
        if (customFont) {
            this.customFont2.drawString(text, x, y, color, shadow);
        } else {
            mc.fontRenderer.drawString(text, (int) x, (int) y, color);
        }
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (PutaHacknn.moduleManager.isModuleEnabled(FontMod.getInstance().getName())) {
            if (shadow) {
                this.customFont.drawStringWithShadow(text, x, y, color);
            } else {
                this.customFont.drawString(text, x, y, color);
            }
            return;
        }
        TextManager.mc.fontRenderer.drawString(text, x, y, color, shadow);
    }

    public void drawRollingRainbowString(String text, float x, float y, boolean shadow) {
        Pattern.compile("(?i)\u00a7[0-9A-FK-OR]").matcher(text).replaceAll("");
        int[] arrayOfInt = {1};
        char[] stringToCharArray = (text).toCharArray();
        float f = 0.0f + x;
        for (char c : stringToCharArray) {
            drawString(String.valueOf(c), f,
                    y, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB(), shadow);
            f += getStringWidth(String.valueOf(c));
            arrayOfInt[0] = arrayOfInt[0] + 1;
        }
    }

    public int getStringWidth(String text) {
        if (PutaHacknn.moduleManager.isModuleEnabled(FontMod.getInstance().getName())) {
            return this.customFont.getStringWidth(text);
        }
        return TextManager.mc.fontRenderer.getStringWidth(text);
    }

    public int getFontHeight() {
        if (PutaHacknn.moduleManager.isModuleEnabled(FontMod.getInstance().getName())) {
            String text = "A";
            return this.customFont.getStringHeight(text);
        }
        return TextManager.mc.fontRenderer.FONT_HEIGHT;
    }

    public void setFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }

    public Font getCurrentFont() {
        return this.customFont.getFont();
    }

    public void updateResolution() {
        this.scaledWidth = TextManager.mc.displayWidth;
        this.scaledHeight = TextManager.mc.displayHeight;
        this.scaleFactor = 1;
        boolean flag = Util.mc.isUnicode();
        int i = TextManager.mc.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        double scaledWidthD = this.scaledWidth / this.scaleFactor;
        double scaledHeightD = this.scaledHeight / this.scaleFactor;
        this.scaledWidth = MathHelper.ceil(scaledWidthD);
        this.scaledHeight = MathHelper.ceil(scaledHeightD);
    }

    public Font getFont(String fontFileName, float size) {
        try {
            InputStream fontFromFile = TextManager.class.getResourceAsStream("/assets/minecraft/fonts/" + fontFileName);
            Font font = Font.createFont(0, fontFromFile);
            font = font.deriveFont(0, size);
            fontFromFile.close();
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("default", 0, (int) size);
        }
    }

    public String getIdleSign() {
        if (this.idleTimer.passedMs(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }
}

