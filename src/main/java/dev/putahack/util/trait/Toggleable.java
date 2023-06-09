package dev.putahack.util.trait;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public interface Toggleable {
    void onEnable();
    void onDisable();

    boolean isToggled();
    void setState(boolean state);
}
