package dev.starstruck.listener.event.player;

import dev.starstruck.listener.bus.CancelableEvent;
import net.minecraft.entity.Entity;

/**
 * @author aesthetical
 * @since 06/06/23
 */
public class EventPushOutOfBlocks extends CancelableEvent {
    private final Entity entity;

    public EventPushOutOfBlocks(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
