package dev.putahack.module.render;

import dev.putahack.PutaHack;
import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.render.EventRender2D;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;
import dev.putahack.util.render.ColorUtils;

import java.awt.*;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class HUD extends Module {
    public static final Setting<Color> primaryColor = new Setting<>(new Color(120, 105, 225), "Primary Color");
    public static final Setting<Color> secondaryColor = new Setting<>(new Color(95, 145, 255), "Secondary Color");

    public HUD() {
        super("HUD", "what do u think this is retard", ModuleCategory.RENDER);

        // set to on by default
        setState(true);
    }

    @Listener
    public void onRender2D(EventRender2D event) {
        mc.fontRenderer.drawStringWithShadow(
                PutaHack.getName() + " " + PutaHack.getVersion(),
                3.0f, 3.0f, ColorUtils.rainbow(5000, 5.0f, 1.0f, 1.0f));

    }

    public enum Colors {
        STATIC, RAINBOW, GRADIENT, SWITCH
    }
}
