package me.dev.putahacknn.features.modules.misc;

import me.dev.putahacknn.event.events.DeathEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.Timer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoKit extends Module {

    public AutoKit() {
        super("AutoKit", "automatically gives you a kit", Category.MISC, true, false, false);
    }

    public final Setting<String> kitName = this.register(new Setting("Kit Name", "Kit Name"));
    public final Setting<Integer> ping = this.register(new Setting("Ping", 60, 0, 1000));
    public Timer timer = new Timer();

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.player.equals(mc.player)) {
            mc.player.respawnPlayer();
            if (timer.passedMs(ping.getValue() * 10)) {
                mc.player.connection.sendPacket(new CPacketChatMessage("/kit " + kitName.getValue()));
                timer.reset();
                return;
            }
        }
    }

}
