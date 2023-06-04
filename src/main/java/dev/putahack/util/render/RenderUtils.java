package dev.putahack.util.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import static baritone.api.utils.Helper.mc;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class RenderUtils {

    public static void scissor(double x, double y, double width, double height) {
        final ScaledResolution sr = new ScaledResolution(mc);
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        glEnable(GL_SCISSOR_TEST);
        glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

    public static void endScissor() {
        glDisable(GL_SCISSOR_TEST);
    }

    public static void rect(double x, double y, double width, double height, int color) {
        glPushMatrix();

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 0, 1);

        color(color);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION);
        buffer.pos(x, y, 0.0).endVertex();
        buffer.pos(x, y + height, 0.0).endVertex();
        buffer.pos(x + width, y + height, 0.0).endVertex();
        buffer.pos(x + width, y, 0.0).endVertex();
        tessellator.draw();

        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static void gradientRect(double x, double y, double width, double height, int tl, int bl, int tr, int br) {
        glEnable(GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glDisable(GL_TEXTURE_2D);

        glShadeModel(GL_SMOOTH);

        glBegin(GL_QUADS);
        {
            color(tr);
            glVertex2d(x + width, y);
            color(tl);
            glVertex2d(x, y);
            color(bl);
            glVertex2d(x, y + height);
            color(br);
            glVertex2d(x + width, y + height);
        }
        glEnd();

        glShadeModel(GL_FLAT);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void triangle(double x, double y, double width, double height, int color) {
        glEnable(GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glDisable(GL_TEXTURE_2D);

        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

        width *= 0.5;

        color(color);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL_POLYGON, DefaultVertexFormats.POSITION);
        buffer.pos(x, y, 0.0).endVertex();
        buffer.pos(x - width, y + height, 0.0).endVertex();
        buffer.pos(x, y + height, 0.0).endVertex();
        buffer.pos(x + width, y + height, 0.0).endVertex();
        buffer.pos(x, y, 0.0).endVertex();
        tessellator.draw();

        glDisable(GL_POLYGON_SMOOTH);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void renderTexture(ResourceLocation loc, double x, double y, int w, int h) {
        glPushMatrix();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        mc.getTextureManager().bindTexture(loc);

        color(0xFFFFFFFF);

        // this is from mc 1.8.9 code cause 1.7.2 fucking sucks

        float u = 0.0f;
        float v = 0.0f;

        float f = 1.0F / (float) w;
        float f1 = 1.0F / (float) h;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + h, 0.0D).tex(u * f, (v + (float) h) * f1).endVertex();
        buffer.pos(x + w, y + h, 0.0D).tex((u + (float) w) * f, (v + (float) h) * f1).endVertex();
        buffer.pos(x + w, y, 0.0D).tex((u + (float) w) * f, v * f1).endVertex();
        buffer.pos(x, y, 0.0).tex(u * f, v * f1).endVertex();
        tessellator.draw();

        glDisable(GL_BLEND);
        glPopMatrix();
    }

    public static void color(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;

        glColor4f(red, green, blue, alpha);
    }
}
