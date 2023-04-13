package me.dev.putahacknn.features.modules.movement;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class AutoCenter extends Module {

    public AutoCenter() {
        super("AutoCenter", "Centers your di@$ in her hol- wait what??!", Category.MOVEMENT, true, false, false);
    }

    public final Setting<Boolean> alwaysCenter = this.register(new Setting("Always Center", false));

    @Override
    public void onEnable() {
        if (mc.player != null && mc.world != null) {
            if (!alwaysCenter.getValue()) {
                doCenter();
            }
        }
    }

    @Override
    public void onUpdate() {
        if (mc.player != null && mc.world != null) {
            if (alwaysCenter.getValue()) {
                doCenter();
            }
        }
    }

    public void doCenter() {
        double x = (Math.floor(mc.player.posX) + 0.5f) - mc.player.posX;
        double z = (Math.floor(mc.player.posZ) + 0.5f) - mc.player.posZ;
        double speed = Math.hypot(x, z);
        double base = mc.player.isSneaking() ? 0.057f : 0.287f;
        if (speed > base) {
            x *= (base / speed);
            z *= (base / speed);
        }
        mc.player.motionX = x;
        mc.player.motionZ = z;
    }

}
