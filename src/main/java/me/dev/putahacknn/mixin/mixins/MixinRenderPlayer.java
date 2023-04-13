

package me.dev.putahacknn.mixin.mixins;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.modules.render.TexturedChams;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.*;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer
{
    @Inject(method = { "renderEntityName" }, at = { @At("HEAD") }, cancellable = true)
    public void renderEntityNameHook(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (PutaHacknn.moduleManager.isModuleEnabled("NameTags")) {
            info.cancel();
        }
    }
    
    @Overwrite
    public ResourceLocation getEntityTexture(final AbstractClientPlayer entity) {
        if (PutaHacknn.moduleManager.isModuleEnabled("TexturedChams")) {
            GL11.glColor4f((int) TexturedChams.red.getValue() / 255.0f, (int)TexturedChams.green.getValue() / 255.0f, (int)TexturedChams.blue.getValue() / 255.0f, (int)TexturedChams.alpha.getValue() / 255.0f);
            return new ResourceLocation("minecraft:steve_skin1.png");
        }
        return entity.getLocationSkin();
    }
}
