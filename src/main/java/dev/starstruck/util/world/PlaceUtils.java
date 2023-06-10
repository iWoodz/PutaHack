package dev.starstruck.util.world;

import dev.starstruck.util.world.holder.Placement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class PlaceUtils {

    /**
     * The minecraft game instance
     */
    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Gets a {@link Placement} for a position
     * @param pos the position
     * @return a {@link Placement} object for this placement or null
     */
    public static Placement getPlacementAt(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos n = pos.offset(facing);
            if (!WorldUtils.isReplaceable(n)) {
                return new Placement(n, facing.getOpposite());
            }
        }

        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos n = pos.offset(facing);
            if (WorldUtils.isReplaceable(n)) {
                for (EnumFacing f : EnumFacing.values()) {
                    BlockPos nn = n.offset(f);
                    if (!WorldUtils.isReplaceable(nn)) {
                        return new Placement(nn, f.getOpposite());
                    }
                }
            }
        }

        return null;
    }

    /**
     * Calculates a non-strict, basic hit vector for this {@link Placement}
     * @param placement the {@link Placement} object
     * @return a hit vector
     */
    public static Vec3d getBasicHitVec(Placement placement) {
        return new Vec3d(placement.getPos())
                .add(new Vec3d(placement.getFacing().getDirectionVec())
                        .scale(0.5));
    }
}
