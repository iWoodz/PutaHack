package dev.putahack.util.io;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class AudioUtils {

    /**
     * The minecraft game instance
     */
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void playClick() {
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
