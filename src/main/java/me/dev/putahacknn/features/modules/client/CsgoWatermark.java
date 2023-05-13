package me.dev.putahacknn.features.modules.client;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.RenderUtil;
import me.dev.putahacknn.util.Timer;

public class CsgoWatermark
        extends Module {
    Timer delayTimer = new Timer();
    public Setting<Integer> X = this.register(new Setting<Integer>("watermarkx", 0, 0, 300));
    public Setting<Integer> Y = this.register(new Setting<Integer>("watermarky", 0, 0, 300));
    public Setting<Integer> delay = this.register(new Setting<Integer>("delay", 240, 0, 600));
    public Setting<Integer> saturation = this.register(new Setting<Integer>("saturation", 127, 1, 255));
    public Setting<Integer> brightness = this.register(new Setting<Integer>("brightness", 100, 0, 255));
    public float hue;
    public int red = 1;
    public int green = 1;
    public int blue = 1;
    private String message = "";

    public CsgoWatermark() {
        super("Watermark", "extra", Module.Category.CLIENT, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.drawCsgoWatermark();
    }

    public void drawCsgoWatermark() {
        int padding = 5;
        this.message = "PutaHack 0.1.4 | " + CsgoWatermark.mc.player.getName() + " | " + PutaHacknn.serverManager.getPing() + "Ms";
        Integer textWidth = CsgoWatermark.mc.fontRenderer.getStringWidth(this.message);
        Integer textHeight = CsgoWatermark.mc.fontRenderer.FONT_HEIGHT;
        RenderUtil.drawRectangleCorrectly(this.X.getValue(), this.Y.getValue(), textWidth + 8, textHeight + 4, ColorUtil.toRGBA(0, 0, 0, 150));
        RenderUtil.drawRectangleCorrectly(this.X.getValue(), this.Y.getValue(), textWidth + 8, 2, ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue()));
        CsgoWatermark.mc.fontRenderer.drawString(this.message, (float)(this.X.getValue() + 3), (float)(this.Y.getValue() + 3), ColorUtil.toRGBA(255, 255, 255, 255), false);
    }
}
