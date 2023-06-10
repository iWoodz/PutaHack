package dev.starstruck.module.render;

import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.network.EventPacket;
import dev.starstruck.mixin.mixins.network.packet.server.ISPacketTimeUpdate;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import net.minecraft.network.play.server.SPacketTimeUpdate;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class TimeChanger extends Module {
    private final Setting<Double> time = new Setting<>(
            22.0, 0.01, 0.0, 24.0, "Time");

    public TimeChanger() {
        super("TimeChanger",
                "Changes the client-sided time",
                ModuleCategory.RENDER);
    }

    @Listener
    public void onPacketIn(EventPacket.In event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            SPacketTimeUpdate packet = event.getPacket();
            ((ISPacketTimeUpdate) packet)
                    .setWorldTime((long) (time.getValue() * 1000.0));
        }
    }
}
