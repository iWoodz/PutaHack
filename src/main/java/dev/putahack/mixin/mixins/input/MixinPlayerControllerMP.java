package dev.putahack.mixin.mixins.input;

import dev.putahack.PutaHack;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Redirect(method = "processRightClickBlock", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;getHeldItem(Lnet/minecraft/util/EnumHand;)Lnet/minecraft/item/ItemStack;"))
    public ItemStack hookProcessRightClickBlock$getHeldItem(EntityPlayerSP player, EnumHand hand) {
        if (!PutaHack.get().getInventory().isDesynced()
                || hand == EnumHand.OFF_HAND) return player.getHeldItem(hand);
        return PutaHack.get().getInventory().getStack(player);
    }
}
