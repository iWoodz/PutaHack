package dev.starstruck.util.world;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class WorldUtils {

    /**
     * The minecraft game instance
     */
    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Checks if a block is replaceable
     * @param pos the position the block is at
     * @return if the block at this pos is replaceable
     */
    public static boolean isReplaceable(BlockPos pos) {
        return mc.world.getBlockState(pos).getMaterial().isReplaceable();
    }
}
