package dev.putahack.mixin.render.item;

import dev.putahack.PutaHack;
import dev.putahack.module.render.ViewModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author aesthetical
 * @since 06/05/23
 *
 * @see dev.putahack.module.render.ViewModel
 */
@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    private static ViewModel VIEW_MODEL;

    @Inject(method = "renderItemInFirstPerson(F)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemRenderer;rotateArroundXAndY(FF)V",
            shift = Shift.BEFORE))
    public void hookRenderItemInFirstPerson$rotateArroundXAndY(float partialTicks, CallbackInfo info) {
        if (VIEW_MODEL == null) {
            ViewModel viewModel = PutaHack.get().getModules().get(ViewModel.class);
            if (viewModel == null) return;
            VIEW_MODEL = viewModel;
        }

        if (!VIEW_MODEL.isToggled()) return;

        GlStateManager.translate(VIEW_MODEL.translateX.getValue(),
                VIEW_MODEL.translateY.getValue(),
                VIEW_MODEL.translateZ.getValue());
        GlStateManager.scale(VIEW_MODEL.scaleX.getValue(),
                VIEW_MODEL.scaleY.getValue(),
                VIEW_MODEL.scaleZ.getValue());
    }
}
