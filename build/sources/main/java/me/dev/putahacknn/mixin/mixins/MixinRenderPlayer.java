

package me.dev.putahacknn.mixin.mixins;

import me.dev.putahacknn.PutaHacknn;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.*;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer {
    @Inject(method = {"renderEntityName"}, at = {@At("HEAD")}, cancellable = true)
    public void renderEntityNameHook(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (PutaHacknn.moduleManager.isModuleEnabled("NameTags")) {
            info.cancel();
        }
    }
}
