package dev.starstruck.listener.event.input;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class EventKeyInput {
    private final int keyCode;

    public EventKeyInput(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
