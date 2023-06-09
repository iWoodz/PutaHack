package dev.putahack.listener.event.render;

import dev.putahack.listener.bus.CancelableEvent;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public class EventLimitFramerate extends CancelableEvent {
    private int fps;

    public EventLimitFramerate(int fps) {
        this.fps = fps;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }
}
