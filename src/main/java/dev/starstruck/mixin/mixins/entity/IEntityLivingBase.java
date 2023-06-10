package dev.starstruck.mixin.mixins.entity;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author aesthetical
 * @since 06/07/23
 */
@Mixin(EntityLivingBase.class)
public interface IEntityLivingBase {

    @Accessor("ticksSinceLastSwing")
    int getTicksSinceLastSwing();
}
