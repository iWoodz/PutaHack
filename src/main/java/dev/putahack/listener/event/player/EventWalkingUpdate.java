package dev.putahack.listener.event.player;

import dev.putahack.listener.bus.CancelableEvent;
import dev.putahack.listener.event.EventStage;

/**
 * @author aesthetical
 * @since 06/05/23
 */
public class EventWalkingUpdate extends CancelableEvent {
    private final EventStage stage;

    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;

    public EventWalkingUpdate(EventStage stage, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.stage = stage;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public EventStage getStage() {
        return stage;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
