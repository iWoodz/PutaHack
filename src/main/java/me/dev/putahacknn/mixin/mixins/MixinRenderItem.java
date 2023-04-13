package me.dev.putahacknn.mixin.mixins;

import me.dev.putahacknn.features.Feature;
import me.dev.putahacknn.features.modules.render.ViewModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class MixinRenderItem {

    @Inject(method={"renderItemModel"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift=At.Shift.BEFORE)})
    private void mortIsNotObese(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if ((ViewModel.getINSTANCE().enabled.getValue()) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && !Feature.fullNullCheck()) {
            GlStateManager.scale(ViewModel.getINSTANCE().sizeX.getValue(), ViewModel.getINSTANCE().sizeY.getValue(), ViewModel.getINSTANCE().sizeZ.getValue());
            GlStateManager.rotate((ViewModel.getINSTANCE().rotationX.getValue() * 360.0f), 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate((ViewModel.getINSTANCE().rotationY.getValue() * 360.0f), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((ViewModel.getINSTANCE().rotationZ.getValue() * 360.0f), 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(ViewModel.getINSTANCE().positionX.getValue(), ViewModel.getINSTANCE().positionY.getValue(), ViewModel.getINSTANCE().positionZ.getValue());
        }
    }

}