package dev.starstruck.listener.event.render;

import dev.starstruck.listener.bus.CancelableEvent;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public class EventFOV extends CancelableEvent {
    private float value;
    private final boolean useFoVSetting;

    public EventFOV(float value, boolean useFoVSetting) {
        this.value = value;
        this.useFoVSetting = useFoVSetting;
    }

    public boolean getUseFoVSetting() {
        return useFoVSetting;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
