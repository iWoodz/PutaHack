package me.dev.putahacknn.features.modules.misc;

import me.dev.putahacknn.features.DiscordPresence;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class RPC
        extends Module {
    public static RPC INSTANCE;
    public Setting<Boolean> showIP = this.register(new Setting<Boolean>("ShowIP", Boolean.valueOf(false)));
    public Setting<String> state = this.register(new Setting<String>("State", "BitchWare.fatcunt"));

    public RPC() {
        super("RPC", "Discord rich presence", Module.Category.MISC, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}