package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class Animations extends Module {

    public Animations() {
        super("AntiSwing", "you used confusion! it was very effective! detective_bear1 - 130hp, you - 1hp", Category.WOODZ, true, false, false);
        INSTANCE = this;
    }

    public static Animations INSTANCE;
    public final Setting<Boolean> noDip = this.register(new Setting("No Dip", true));
    public final Setting<Boolean> swing = this.register(new Setting("Swing", true));

    @Override
    public void onUpdate() {
        if (noDip.getValue()) {
            mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
            mc.entityRenderer.itemRenderer.equippedProgressOffHand = 1.0f;
            mc.entityRenderer.itemRenderer.itemStackOffHand = mc.player.getHeldItemOffhand();
        }
    }

}
