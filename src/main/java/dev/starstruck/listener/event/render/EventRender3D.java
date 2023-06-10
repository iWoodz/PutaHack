package dev.starstruck.listener.event.render;

import dev.starstruck.listener.bus.CancelableEvent;
import dev.starstruck.listener.event.EventStage;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public class EventRender3D extends CancelableEvent {
    private final EventStage stage;
    private final float partialTicks;

    public EventRender3D(EventStage stage, float partialTicks) {
        this.stage = stage;
        this.partialTicks = partialTicks;
    }

    public EventStage getStage() {
        return stage;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
