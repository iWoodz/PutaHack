package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class TexturedChams extends Module
{
    public static Setting<Integer> red;
    public static Setting<Integer> green;
    public static Setting<Integer> blue;
    public static Setting<Integer> alpha;
    public static Setting<Boolean> rainbow;
    public static Setting<Integer> rainbowhue;
    
    public TexturedChams() {
        super("TexturedChams", "hi yes", Module.Category.RENDER, true, false, true);
        TexturedChams.red = (Setting<Integer>)this.register(new Setting("Red", 168, 0, 255));
        TexturedChams.green = (Setting<Integer>)this.register(new Setting("Green", 0, 0, 255));
        TexturedChams.blue = (Setting<Integer>)this.register(new Setting("Blue", 232, 0, 255));
        TexturedChams.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 150, 0, 255));
        TexturedChams.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", false));
        TexturedChams.rainbowhue = (Setting<Integer>)this.register(new Setting("Brightness", 150, 0, 255));
    }
}
