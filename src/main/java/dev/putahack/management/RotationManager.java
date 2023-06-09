package dev.putahack.management;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.EventStage;
import dev.putahack.listener.event.network.EventPacket;
import dev.putahack.listener.event.player.EventWalkingUpdate;
import dev.putahack.mixin.duck.IEntityLivingBase;
import dev.putahack.mixin.mixins.network.packet.client.ICPacketPlayer;
import dev.putahack.util.timing.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;

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

        ((IEntityLivingBase) Minecraft.getMinecraft().player)
                .setRenderYaw(serverRotations[0]);
        ((IEntityLivingBase) Minecraft.getMinecraft().player)
                .setRenderPitch(serverRotations[1]);
        updateDistance();

        event.setYaw(clientRotations[0]);
        event.setPitch(clientRotations[1]);
        event.cancel();
    }

    /**
     * @see net.minecraft.entity.EntityLivingBase#updateDistance(float, float)
     */
    private void updateDistance() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        double deltaX = player.posX - player.prevPosX;
        double deltaZ = player.posZ - player.prevPosZ;

        float distanceSq = (float) (deltaX * deltaX + deltaZ * deltaZ);
        float offset = player.renderYawOffset;

        if (distanceSq > 0.0025000002f) {
            float yaw = (float)MathHelper.atan2(deltaZ, deltaX) * (180.0f / (float) Math.PI) - 90.0f;
            float diff = MathHelper.abs(MathHelper.wrapDegrees(serverRotations[0]) - yaw);

            if (95.0f < diff && diff < 265.0f) {
                offset = yaw - 180.0f;
            } else {
                offset = yaw;
            }
        }

        if (player.swingProgress > 0.0f) {
            offset = serverRotations[0];
        }

        float offsetDiff = MathHelper.wrapDegrees(offset - player.renderYawOffset);
        player.renderYawOffset += offsetDiff * 0.3f;
        offsetDiff = MathHelper.wrapDegrees(serverRotations[0] - player.renderYawOffset);

        if (offsetDiff < -75.0f) {
            offsetDiff = -75.0f;
        }

        if (offsetDiff >= 75.0f) {
            offsetDiff = 75.0f;
        }

        player.renderYawOffset = serverRotations[0] - offsetDiff;

        if (offsetDiff * offsetDiff > 2500.0f) {
            player.renderYawOffset += offsetDiff * 0.2f;
        }
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
