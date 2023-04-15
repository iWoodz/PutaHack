package me.dev.putahacknn.features.modules.client;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

import java.awt.*;


public class Colors
        extends Module {
    public static Colors INSTANCE;
    public final Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting("Alpha", 255, 0, 255));

    public Colors() {
        super("Colors", "color.", Category.CLIENT, true, false, false);
        INSTANCE = this;
    }
    public Color getColor() {
        return new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());
    }
}
