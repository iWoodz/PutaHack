package dev.putahack.util.trait;

public interface IToggleable {
    void onEnable();
    void onDisable();

    boolean isToggled();
    void setState(boolean state);
}
