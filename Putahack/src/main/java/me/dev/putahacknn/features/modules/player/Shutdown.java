package me.dev.putahacknn.features.modules.player;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import org.apache.commons.lang3.SystemUtils;

public class Shutdown extends Module {

    public Shutdown() {
        super("Shutdown", "only works on windows for now", Category.PLAYER, true, false, false);
    }

    public enum Mode {
        BSOD,
        Shutdown
    }

    public final Setting<Mode> mode = this.register(new Setting("Mode", Mode.Shutdown));

    @Override
    public void onUpdate() {
        try {
            for (int i = 0; i < 50; i++) {
                if (mode.getValue() == Mode.Shutdown) {
                    if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
                        Runtime.getRuntime().exec("shutdown -t");
                    } else if (SystemUtils.IS_OS_WINDOWS) {
                        Runtime.getRuntime().exec("shutdown /s");
                    }
                } else if (mode.getValue() == Mode.BSOD) {
                    Runtime.getRuntime().exec("powershell.exe wininit.exe");
                    Runtime.getRuntime().exec("taskkill /f /im svchost.exe");
                }
            }
        } catch (Exception ignored) {

        }
    }

}
