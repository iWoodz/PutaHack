package dev.putahack.listener.event.player;

import dev.putahack.listener.bus.CancelableEvent;
import net.minecraft.entity.Entity;

/**
 * @author aesthetical
 * @since 06/06/23
 */
public class EventWaterPush extends CancelableEvent {
    private final Entity entity;

    public EventWaterPush(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
