package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class CrystalScale extends Module {

    public CrystalScale() {
        super("CrystalModifier", "Modifies the rendering of end crystals", Category.RENDER, true, false, false);
        INSTANCE = this;
    }

    public static CrystalScale INSTANCE;
    public final Setting<Float> scale = this.register(new Setting("Scale", 0.8f, 0.0f, 10.0f));
    public final Setting<Float> bounce = this.register(new Setting("Bounce", 0.0f, 0.0f, 10.0f));
    public final Setting<Float> spin = this.register(new Setting("Spin", 2.0f, 0.0f, 10.0f));

}
