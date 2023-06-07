package dev.putahack.util.io;

import net.minecraft.client.Minecraft;

import java.io.File;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class FileUtils {

    /**
     * The root directory for putahack.nn
     */
    public static final File root = new File(
            Minecraft.getMinecraft().gameDir, "putahack.nn");
}
