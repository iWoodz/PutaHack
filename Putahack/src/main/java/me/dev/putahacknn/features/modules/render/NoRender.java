package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class NoRender extends Module {

    public NoRender() {
        super("NoRender", "Stops specific things from being rendered", Category.RENDER, true, false, false);
        INSTANCE = this;
    }

    public static NoRender INSTANCE;
    public final Setting<Boolean> armor = this.register(new Setting("Armor", true));
    public final Setting<Boolean> advancements = this.register(new Setting("Advancements", true));

}
