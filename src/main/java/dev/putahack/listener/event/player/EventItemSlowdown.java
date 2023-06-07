package dev.putahack.listener.event.player;

import net.minecraft.util.MovementInput;

/**
 * @author aesthetical
 * @since 06/06/23
 */
public class EventItemSlowdown {
    private final MovementInput input;

    public EventItemSlowdown(MovementInput input) {
        this.input = input;
    }

    public MovementInput getInput() {
        return input;
    }
}
