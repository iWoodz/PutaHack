package me.dev.putahacknn.features.modules.client;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.ClientEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.TextUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Management extends Module {

    public Management() {
        super("Management", "cope.", Category.CLIENT, true, false, false);
        INSTANCE = this;
    }

    public static Management INSTANCE;
    public Setting<Boolean> notifyToggles = register(new Setting("ChatNotify", false, "hahahahahahaahahahahahahahahahahahahahahahaahaha"));
    public Setting<TextUtil.Color> bracketColor = register(new Setting("BracketColor", TextUtil.Color.GRAY));
    public Setting<TextUtil.Color> commandColor = register(new Setting("NameColor", TextUtil.Color.LIGHT_PURPLE));
    public Setting<String> commandBracket = register(new Setting("Bracket", "<"));
    public Setting<String> commandBracket2 = register(new Setting("Bracket2", ">"));

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 &&
                equals(event.getSetting().getFeature()))
            PutaHacknn.commandManager.setClientMessage(getCommandMessage());
    }

    @Override
    public void onLogin() {
        PutaHacknn.commandManager.setClientMessage(getCommandMessage());
    }

    public String getCommandMessage() {
        return TextUtil.coloredString(this.commandBracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString("PutaHack.nn", this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2.getPlannedValue(), this.bracketColor.getPlannedValue());
    }
}