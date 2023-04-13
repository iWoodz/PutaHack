package me.dev.putahacknn.event.events;

import me.dev.putahacknn.event.EventStage;

public class PerspectiveEvent extends EventStage {

    float aspect;

    public PerspectiveEvent(float aspect) {
        this.aspect = aspect;
    }

    public void setAspectRatio(float aspect) {
        this.aspect = aspect;
    }

    public float getAspect() {
        return aspect;
    }

}
