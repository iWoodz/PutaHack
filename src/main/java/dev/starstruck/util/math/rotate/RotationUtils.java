package dev.starstruck.util.math.rotate;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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

    /**
     * Calculates rotations towards a block face
     * @param pos the block position
     * @param facing the face of this block position
     * @return the calculated rotations
     */
    public static float[] blocK(BlockPos pos, EnumFacing facing) {
        Vec3d eyeVec = mc.player.getPositionEyes(1.0f);
        Vec3d vec = new Vec3d(
                pos.getX() + 0.5 + (facing.getXOffset() / 2.0),
                pos.getY() - 0.5 + (facing.getYOffset() / 2.0),
                pos.getZ() + 0.5 + (facing.getZOffset() / 2.0));

        double diffX = vec.x - eyeVec.x;
        double diffZ = vec.z - eyeVec.z;

        float yaw = (float) -(Math.toDegrees(Math.atan2(diffX, diffZ)));
        float pitch = (float) (-Math.toDegrees(Math.atan2(
                vec.y - eyeVec.y, Math.hypot(diffX, diffZ))));

        return new float[] {
                yaw, MathHelper.clamp(pitch, -90.0f, 90.0f) };
    }
}
