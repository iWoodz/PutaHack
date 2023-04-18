package me.dev.putahacknn.features.modules.misc;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoReply extends Module {


    public AutoReply() {
        super("AutoReply", "informs ppl that ur afk", Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Receive event){
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = (SPacketChat) event.getPacket();
            if (packet.getChatComponent() instanceof TextComponentString) {
                final String component =  packet.getChatComponent().getFormattedText();
                if (component.toLowerCase().contains("whispers: ")){
                    mc.player.sendChatMessage("/r stay out of my dms nn dog, im afk with putahack.nn https://discord.gg/7B2q8bZgT3 ");
                }
            }


        }


    }

}
