package dev.starstruck.util.world.holder;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class Placement {

    private final BlockPos pos;
    private final EnumFacing facing;

    public Placement(BlockPos pos, EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }

    public BlockPos getPos() {
        return pos;
    }

    public EnumFacing getFacing() {
        return facing;
    }
}
