package me.dev.putahacknn.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;

public class NameChanger
        extends Module {
    public final Setting<String> NameString = this.register(new Setting<String>("Name", "New Name Here"));
    private static NameChanger instance;

    public NameChanger() {
        super("NameChanger", "Changes name", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    @Override
    public void onEnable() {
        Command.sendMessage((Object) ChatFormatting.DARK_PURPLE + "Success! Name succesfully changed to " + (Object)ChatFormatting.LIGHT_PURPLE + this.NameString.getValue());
    }

    public static NameChanger getInstance() {
        if (instance == null) {
            instance = new NameChanger();
        }
        return instance;
    }
}

