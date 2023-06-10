package dev.starstruck.listener.event.render;

import dev.starstruck.listener.bus.CancelableEvent;

/**
 * @author aesthetical
 * @since 06/05/23
 */
public class EventArmAnimationSpeed extends CancelableEvent {
    private int speed;

    public EventArmAnimationSpeed(int speed) {
        this.speed = speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
