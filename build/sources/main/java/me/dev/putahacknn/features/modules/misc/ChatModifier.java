package me.dev.putahacknn.features.modules.misc;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatModifier
        extends Module {
    private static ChatModifier INSTANCE = new ChatModifier();
    public Setting<Boolean> clean = this.register(new Setting<Boolean>("NoChatBackground", Boolean.valueOf(false), "Cleans your chat"));
    public Setting<Boolean> infinite = this.register(new Setting<Boolean>("InfiniteChat", Boolean.valueOf(false), "Makes your chat infinite."));
    public boolean check;

    public ChatModifier() {
        super("BetterChat", "Modifies your chat", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static ChatModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatModifier();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (fullNullCheck()) {
            return;
        }
        try {
            if (event.getPacket() instanceof CPacketChatMessage) {
                String s = ((CPacketChatMessage) event.getPacket()).getMessage();
                this.check = !s.startsWith(PutaHacknn.commandManager.getPrefix());
            }
        } catch (Exception ignored) {

        }
    }
}

