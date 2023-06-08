package dev.putahack.util.math.rotate;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class RotationUtils {

    /**
     * The minecraft game instance
     */
    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Calculates rotations towards an entity
     * @param target the target entity
     * @param bodyPart the body part to face towards
     * @return the calculated rotations
     */
    public static float[] entity(EntityLivingBase target, BodyPart bodyPart) {
        Vec3d eyeVec = mc.player.getPositionEyes(1.0f);
        Vec3d targetEyeVec = target.getPositionVector().add(
                0.0, bodyPart.apply(target), 0.0);

        double diffX = targetEyeVec.x - eyeVec.x;
        double diffZ = targetEyeVec.z - eyeVec.z;

        float yaw = (float) -(Math.toDegrees(Math.atan2(diffX, diffZ)));
        float pitch = (float) (-Math.toDegrees(Math.atan2(
                targetEyeVec.y - eyeVec.y, Math.hypot(diffX, diffZ))));

        return new float[] {
                yaw, MathHelper.clamp(pitch, -90.0f, 90.0f) };
    }
}
