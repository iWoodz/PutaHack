package dev.putahack.listener.event.render;

import net.minecraft.client.gui.ScaledResolution;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class EventRender2D {
    private final ScaledResolution resolution;
    private final float partialTicks;

    public EventRender2D(ScaledResolution resolution, float partialTicks) {
        this.resolution = resolution;
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
