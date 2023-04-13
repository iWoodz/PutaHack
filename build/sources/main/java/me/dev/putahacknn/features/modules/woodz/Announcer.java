package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.Timer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Announcer extends Module {

    public Announcer() {
        super("Announcer", "no", Category.WOODZ, true, false, false);
    }

    public final Setting<Integer> delay = this.register(new Setting("Delay", 15, 0, 30));
    public Timer timer = new Timer();

    @Override
    public void onUpdate() {
        if (timer.passedS(delay.getValue())) {
            if (ThreadLocalRandom.current().nextInt(1, 3) == 1) {
                mc.player.connection.sendPacket(new CPacketChatMessage("I just got some bitches (unlike you) thanks to PutaHack.nn!"));
            } else if (ThreadLocalRandom.current().nextInt(1, 3) == 2) {
                mc.player.connection.sendPacket(new CPacketChatMessage("PutaHack.nn\u306E\u304A\u304B\u3052\u3067\u3001\u79C1\u306F\uFF08\u3042\u306A\u305F\u3068\u306F\u7570\u306A\u308A\uFF09\u3044\u304F\u3064\u304B\u306E\u611A\u75F4\u3092\u624B\u306B\u5165\u308C\u307E\u3057\u305F\uFF01"));
            }
            timer.reset();
        }
    }

}
