package dev.starstruck.mixin.mixins.render.gui;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author aesthetical
 * @since 06/09/23
 */
@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    private static final String COLOR_CODE_DICTIONARY = "0123456789abcdefklmnor";


}
