package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class Colors extends Module {

    public Colors() {
        super("Colors", "c", Category.WOODZ, true, false, false);
        INSTANCE = this;
    }

    public static Colors INSTANCE;
    public final Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));

}
