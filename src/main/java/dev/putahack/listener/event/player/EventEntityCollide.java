package dev.putahack.listener.event.player;

import dev.putahack.listener.bus.CancelableEvent;
import net.minecraft.entity.Entity;

/**
 * @author aesthetical
 * @since 06/06/23
 */
public class EventEntityCollide extends CancelableEvent {
    private final Entity entity, collidingWith;

    public EventEntityCollide(Entity entity, Entity collidingWith) {
        this.entity = entity;
        this.collidingWith = collidingWith;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity getCollidingWith() {
        return collidingWith;
    }
}
