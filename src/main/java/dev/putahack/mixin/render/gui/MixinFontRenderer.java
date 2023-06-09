package dev.putahack.mixin.render.gui;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    private static final String COLOR_CODE_DICTIONARY = "0123456789abcdefklmnor";


}
