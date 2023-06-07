package dev.putahack.util.math.rotate;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class RotationUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Calculates rotations towards an entity
     * @param target the target entity
     * @param bodyPart the body part to face towards
     * @return the calculated rotations
     */
    public static float[] entity(EntityLivingBase target, BodyPart bodyPart) {
        return new float[2];
    }
}
