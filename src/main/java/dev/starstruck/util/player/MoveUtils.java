package dev.starstruck.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;

/**
 * @author aesthetical
 * @since 06/11/23
 */
public class MoveUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isMoving() {
        return mc.player.movementInput.moveForward != 0.0f
                || mc.player.movementInput.moveStrafe != 0.0f;
    }

    public static void strafe(double speed) {
        if (speed <= 0.0) return;

        double rad = getDirection();
        mc.player.motionX = -Math.sin(rad) * speed;
        mc.player.motionZ = Math.cos(rad) * speed;
    }

    public static double getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }

        float forward = 1.0f;
        if (mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }

        if (mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }

        if (mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }

        return Math.toRadians(rotationYaw);
    }

    public static double getBaseNCPSpeed() {
        double base = 0.2783;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            base *= 1.0 + 0.2 * (mc.player
                    .getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1);
        }
        return base;
    }

    public static double getSpeed() {
        return Math.sqrt(mc.player.motionX + mc.player.motionX
                * mc.player.motionZ + mc.player.motionZ);
    }
}
