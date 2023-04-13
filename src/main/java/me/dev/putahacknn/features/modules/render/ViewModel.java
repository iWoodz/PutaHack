package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class ViewModel extends Module {
    public Setting<Float> sizeX = this.register(new Setting("SizeX", 1.0F, 0.0F, 2.0F));
    public Setting<Float> sizeY = this.register(new Setting("SizeY", 1.0F, 0.0F, 2.0F));
    public Setting<Float> sizeZ = this.register(new Setting("SizeZ", 1.0F, 0.0F, 2.0F));
    public Setting<Float> rotationX = this.register(new Setting("rotationX", 0.0F, 0.0F, 1.0F));
    public Setting<Float> rotationY = this.register(new Setting("rotationY", 0.0F, 0.0F, 1.0F));
    public Setting<Float> rotationZ = this.register(new Setting("rotationZ", 0.0F, 0.0F, 1.0F));
    public Setting<Float> positionX = this.register(new Setting("positionX", 0.0F, -2.0F, 2.0F));
    public Setting<Float> positionY = this.register(new Setting("positionY", 0.0F, -2.0F, 2.0F));
    public Setting<Float> positionZ = this.register(new Setting("positionZ", 0.0F, -2.0F, 2.0F));
    public Setting<Float> itemFOV = this.register(new Setting("ItemFOV", 1.0F, 0.0F, 2.0F));
    private static ViewModel INSTANCE = new ViewModel();

    public ViewModel() {
        super("Viewmodel", "makes ur hands deformed", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ViewModel getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ViewModel();
        }

        return INSTANCE;
    }
}


