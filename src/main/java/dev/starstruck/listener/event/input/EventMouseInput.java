package dev.starstruck.listener.event.input;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class EventMouseInput {
    private final int button;

    public EventMouseInput(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }
}
