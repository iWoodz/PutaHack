package dev.putahack.module.combat;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.network.EventPacket;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;
import dev.putahack.util.timing.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class Criticals extends Module {
    private final Setting<Mode> mode = new Setting<>(Mode.PACKET, "Mode");
    private final Setting<Double> delay = new Setting<>(0.5, 0.01, 0.0, 5.0, "Delay");

    private final Timer timer = new Timer();

    public Criticals() {
        super("Criticals", "Makes your attacks critical hits", ModuleCategory.COMBAT);
    }

    @Listener
    public void onPacketOut(EventPacket.Out event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = event.getPacket();
            if (packet.getAction() != Action.ATTACK
                    || !(packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase)) return;

            // TODO: vanilla checks here
            if (!mc.player.onGround || mc.player.isInWater()) return;

            switch (mode.getValue()) {

            }
        }
    }

    public enum Mode {
        PACKET, NEW_NCP
    }
}
