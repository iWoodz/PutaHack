package dev.putahack.util.math.rotate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.function.Function;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public enum BodyPart {
    HEAD(Entity::getEyeHeight),
    TORSO((e) -> e.getEyeHeight() - 0.2f),
    LEGS((e) -> e.getEyeHeight() / 2.0f),
    FEET((e) -> 0.0f);

    private final Function<EntityLivingBase, Float> function;

    BodyPart(Function<EntityLivingBase, Float> function) {
        this.function = function;
    }

    public float apply(EntityLivingBase t) {
        return function.apply(t);
    }
}
