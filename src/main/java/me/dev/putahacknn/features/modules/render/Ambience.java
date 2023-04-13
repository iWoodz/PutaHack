package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class Ambience extends Module {

    public Ambience() {
        super("Ambience", "no", Category.RENDER, true, false, false);
        this.register(red);
        this.register(green);
        this.register(blue);
        this.register(alpha);
    }

    public static Setting<Integer> red = new Setting("Red", 255, 0, 255);
    public static Setting<Integer> green = new Setting("Green", 255, 0, 255);
    public static Setting<Integer> blue = new Setting("Blue", 255, 0, 255);
    public static Setting<Integer> alpha = new Setting("Alpha", 100, 0, 255);
    
}
