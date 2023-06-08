package dev.putahack.module.render;

import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;

import java.awt.*;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class Ambience extends Module {
    private final Setting<Color> color = new Setting<>(HUD.primaryColor.getValue(), "Color");
    private final Setting<Float> intensity = new Setting<>(
            0.95f, 0.01f, 0.1f, 1.0f, "Intensity");

    public Ambience() {
        super("Ambience", "Changes how the world looks", ModuleCategory.RENDER);
    }
}
