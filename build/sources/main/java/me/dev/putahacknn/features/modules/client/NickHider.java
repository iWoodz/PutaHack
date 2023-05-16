package me.dev.putahacknn.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class NickHider
        extends Module {
    public final Setting<String> NameString = this.register(new Setting<String>("Name", "New Name Here..."));
    private static NickHider instance;

    public NickHider() {
        super("NameSpoofer", "hide ya shi", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    @Override
    public void onEnable() {
        Command.sendMessage(ChatFormatting.GRAY + "yah name was changed to " + ChatFormatting.GREEN + this.NameString.getValue());
    }

    public static NickHider getInstance() {
        if (instance == null) {
            instance = new NickHider();
        }
        return instance;
    }
}
