package dev.putahack.mixin.duck;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public interface IEntityLivingBase {

    float getRenderYaw();
    float getRenderPitch();

    void setRenderYaw(float yaw);
    void setRenderPitch(float pitch);
}
