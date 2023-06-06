package dev.putahack.module.combat;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.network.EventPacket;
import dev.putahack.mixin.network.packet.server.ISPacketEntityVelocity;
import dev.putahack.mixin.network.packet.server.ISPacketExplosion;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

/**
 * @author aesthetical
 * @since 06/05/23
 */
public class Velocity extends Module {
    private final Setting<Float> horizontal = new Setting<>(
            0.0f, 0.01f, 0.0f, 200.0f, "Horizontal");
    private final Setting<Float> vertical = new Setting<>(
            0.0f, 0.01f, 0.0f, 200.0f, "Vertical");

    public Velocity() {
        super("Velocity", "Reduces knock-back and shit", ModuleCategory.COMBAT);
    }

    @Listener
    public void onPacketIn(EventPacket.In event) {
        // null-check, just dont modify anything until player and world is not null
        if (mc.player == null || mc.world == null) return;

        if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity packet = event.getPacket();
            if (packet.getEntityID() != mc.player.getEntityId()) return;

            // we can just cancel if we're at 0%
            if (horizontal.getValue() == 0.0f && vertical.getValue() == 0.0f) {
                event.cancel();
                return;
            }

            ((ISPacketEntityVelocity) packet).setMotionX((int) (packet.getMotionX() * (horizontal.getValue() / 100.0f)));
            ((ISPacketEntityVelocity) packet).setMotionY((int) (packet.getMotionY() * (vertical.getValue() / 100.0f)));
            ((ISPacketEntityVelocity) packet).setMotionZ((int) (packet.getMotionZ() * (horizontal.getValue() / 100.0f)));

        } else if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion packet = event.getPacket();

            // we can just cancel if we're at 0% again for explosions
            if (horizontal.getValue() == 0.0f && vertical.getValue() == 0.0f) {
                event.cancel();
                return;
            }

            ((ISPacketExplosion) packet).setMotionX(packet.getMotionX() * (horizontal.getValue() / 100.0f));
            ((ISPacketExplosion) packet).setMotionY(packet.getMotionY() * (vertical.getValue() / 100.0f));
            ((ISPacketExplosion) packet).setMotionZ(packet.getMotionZ() * (horizontal.getValue() / 100.0f));
        }
    }
}
