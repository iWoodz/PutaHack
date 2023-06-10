package dev.starstruck.util.render;

import java.awt.*;

public class ColorUtils {
    /**
     * black people
     * @param firstColor
     * @param secondColor
     * @param time
     * @param index
     * @param timePerIndex
     * @param speed
     * @return
     */
    public static int getColorSwitch(Color firstColor, Color secondColor, float time, int index, long timePerIndex, double speed) {
        long now = (long) ((speed / 10.0) * System.currentTimeMillis() + -index * timePerIndex);

        float rd = (firstColor.getRed() - secondColor.getRed()) / time;
        float gd = (firstColor.getGreen() - secondColor.getGreen()) / time;
        float bd = (firstColor.getBlue() - secondColor.getBlue()) / time;

        float rd2 = (secondColor.getRed() - firstColor.getRed()) / time;
        float gd2 = (secondColor.getGreen() - firstColor.getGreen()) / time;
        float bd2 = (secondColor.getBlue() - firstColor.getBlue()) / time;

        int re1 = Math.round(secondColor.getRed() + rd * (now % (long) time));
        int ge1 = Math.round(secondColor.getGreen() + gd * (now % (long) time));
        int be1 = Math.round(secondColor.getBlue() + bd * (now % (long) time));
        int re2 = Math.round(firstColor.getRed() + rd2 * (now % (long) time));
        int ge2 = Math.round(firstColor.getGreen() + gd2 * (now % (long) time));
        int be2 = Math.round(firstColor.getBlue() + bd2 * (now % (long) time));

        if (now % ((long) time * 2L) < (long) time) {
            return new Color(re2, ge2, be2).getRGB();
        } else {
            return new Color(re1, ge1, be1).getRGB();
        }
    }

    public static int gradientRainbow(Color c, float min, int delay) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0f + 50.0F / (float) (delay + 100.0) * 2.0f) % 2.0f - 1.0f);
        brightness = (min + (1.0f - min) * brightness) % 2.0f;
        return Color.getHSBColor(hsb[0], hsb[1], brightness).getRGB();
    }

    public static int rainbow(int delay, float speed, float saturation, float brightness) {
        // mega homosexual
        double hue = (Math.abs((System.currentTimeMillis() + 10) + delay) * (double) (speed / 100.0f)) % 360.0;
        return Color.getHSBColor((float) (hue / 360.0f), saturation, brightness).getRGB();
    }
}
