package me.dev.putahacknn.features.modules.misc;

import me.dev.putahacknn.features.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class  ChatColors
            extends Module {
    public ChatColors() {
        super("GreenText", "makes ur text green on 2bpvp", Module.Category.MISC, true, false, false);
    }
    @SubscribeEvent
    public void onChatMessage(ClientChatEvent event) {
        String message = event.getMessage();
        char firstChar = message.charAt(0);

        if (Character.isLetter(firstChar)) {
            String prefixedMessage = "&2" + message;
            event.setMessage(prefixedMessage);
        }
    }
}