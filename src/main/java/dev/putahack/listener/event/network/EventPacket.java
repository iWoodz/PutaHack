package dev.putahack.listener.event.network;

import dev.putahack.listener.bus.CancelableEvent;
import net.minecraft.network.Packet;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class EventPacket extends CancelableEvent {
    private final Packet<?> packet;

    public EventPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) packet;
    }

    public static class In extends EventPacket {

        public In(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Out extends EventPacket {

        public Out(Packet<?> packet) {
            super(packet);
        }
    }
}
