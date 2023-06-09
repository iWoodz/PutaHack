package dev.putahack.mixin.mixins.item;

import dev.putahack.PutaHack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(ItemBlock.class)
public class MixinItemBlock {

    @Redirect(method = "onItemUse", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;getHeldItem(Lnet/minecraft/util/EnumHand;)Lnet/minecraft/item/ItemStack;"))
    public ItemStack hookOnItemUse$getHeldItem(EntityPlayer player, EnumHand hand) {
        if (!PutaHack.get().getInventory().isDesynced()
                || hand == EnumHand.OFF_HAND) return player.getHeldItem(hand);
        return PutaHack.get().getInventory().getStack(player);
    }
}
