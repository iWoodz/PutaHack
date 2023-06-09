package dev.putahack.management;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.EventStage;
import dev.putahack.listener.event.network.EventPacket;
import dev.putahack.listener.event.player.EventWalkingUpdate;
import dev.putahack.mixin.mixins.network.packet.client.ICPacketPlayer;
import dev.putahack.util.timing.Timer;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author aesthetical
 * @since 06/05/23
 */
public class RotationManager {

    /**
     * A timer to keep track with how long we should spoof rotations to the server for
     */
    private final Timer spoofTimer = new Timer();

    /**
     * The rotations that are in sync with the server
     */
    private final float[] serverRotations = new float[2];

    /**
     * The rotations we are spoofing with
     */
    private float[] clientRotations;

    @Listener
    public void onPacketOut(EventPacket.Out event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = event.getPacket();
            if (!((ICPacketPlayer) packet).isRotating()) return;

            serverRotations[0] = packet.getYaw(0.0f);
            serverRotations[1] = packet.getPitch(0.0f);
        }
    }

    @Listener(receiveCanceled = true)
    public void onWalkingUpdate(EventWalkingUpdate event) {
        if (event.getStage() != EventStage.PRE
                || clientRotations == null) return;

        if (spoofTimer.hasPassed(6, false)) {
            clientRotations = null;
            return;
        }

        event.setYaw(clientRotations[0]);
        event.setPitch(clientRotations[1]);
        event.cancel();
    }

    /**
     * Spoofs rotations to the server
     * @param rotations the server rotations
     */
    public void spoof(float[] rotations) {
        spoofTimer.resetTime();
        clientRotations = rotations;
    }

    /**
     * Gets the server rotations
     * @return the server rotation array
     */
    public float[] getServerRotations() {
        return serverRotations;
    }

    /**
     * If we are spoofing rotations
     * @return if the clientRotations are not null
     */
    public boolean isSpoofingRotations() {
        return clientRotations != null;
    }
}
