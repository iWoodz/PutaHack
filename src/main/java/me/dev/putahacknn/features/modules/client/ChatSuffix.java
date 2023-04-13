package me.dev.putahacknn.features.modules.client;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix extends Module {

    public ChatSuffix() {
        super("ChatSuffix", "modify chat", Category.CLIENT, true, false, false);
    }

    public enum SuffixType {
        Cancer,
        Normal
    }

    public final Setting<SuffixType> suffixType = this.register(new Setting("Suffix Type", SuffixType.Normal));

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            try {
                String message = ((CPacketChatMessage) event.getPacket()).getMessage();
                if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/")) {
                    return;
                }
                message = message + (suffixType.getValue() == SuffixType.Cancer ? " \u23D0\u23D0 \u264B\u2654\u23D0\uFF30\u03C5\u019A\uD835\uDE22\uD835\uDE8A\u039B\uFF28\u039B\u0188\u0199.\u0273\u0273\u23D0\u2654\u264B" : " \u23D0 \u2654\uFF30\u03C5\u019A\u039B\uFF28\u039B\u0188\u0199.\u0273\u0273\u2654");
                if (message.length() >= 256) {
                    message = message.substring(0, 256);
                }
                ((CPacketChatMessage) event.getPacket()).message = message;
            } catch (Exception ignored) {

            }
        }
    }

}
