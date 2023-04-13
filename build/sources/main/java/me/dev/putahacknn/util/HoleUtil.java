package me.dev.putahacknn.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class HoleUtil implements Util {
    public static BlockPos[] holeOffsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0)};

    public static boolean isHole(BlockPos pos) {
        boolean isHole = false;
        int amount = 0;
        for (BlockPos p : holeOffsets) {
            if (HoleUtil.mc.world.getBlockState(pos.add((Vec3i)p)).getMaterial().isReplaceable()) continue;
            ++amount;
        }
        if (amount == 5) {
            isHole = true;
        }
        return isHole;
    }

    public static boolean isInHole(Entity entity) {
        for (Hole hole : getHoles(3, new BlockPos(entity.posX, entity.posY, entity.posZ), true)) {
            if (hole.doubleHole) {
                if (new AxisAlignedBB(hole.pos1).intersects(entity.getEntityBoundingBox().shrink(0.3f)) || new AxisAlignedBB(hole.pos2).intersects(entity.getEntityBoundingBox().shrink(0.3f))) {
                    return true;
                }
            } else if (new AxisAlignedBB(hole.pos1).intersects(entity.getEntityBoundingBox().shrink(0.3f))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isObbyHole(BlockPos pos) {
        boolean isHole = true;
        int bedrock = 0;
        for (BlockPos off : holeOffsets) {
            Block b = HoleUtil.mc.world.getBlockState(pos.add((Vec3i)off)).getBlock();
            if (!HoleUtil.isSafeBlock(pos.add((Vec3i)off))) {
                isHole = false;
                continue;
            }
            if (b != Blocks.OBSIDIAN && b != Blocks.ENDER_CHEST && b != Blocks.ANVIL) continue;
            ++bedrock;
        }
        if (HoleUtil.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR || HoleUtil.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.AIR) {
            isHole = false;
        }
        if (bedrock < 1) {
            isHole = false;
        }
        return isHole;
    }

    public static boolean isBedrockHoles(BlockPos pos) {
        boolean isHole = true;
        for (BlockPos off : holeOffsets) {
            Block b = HoleUtil.mc.world.getBlockState(pos.add((Vec3i)off)).getBlock();
            if (b == Blocks.BEDROCK) continue;
            isHole = false;
        }
        if (HoleUtil.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR || HoleUtil.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.AIR) {
            isHole = false;
        }
        return isHole;
    }

    public static Hole isDoubleHole(BlockPos pos) {
        if (HoleUtil.checkOffset(pos, 1, 0)) {
            return new Hole(false, true, pos, pos.add(1, 0, 0));
        }
        if (HoleUtil.checkOffset(pos, 0, 1)) {
            return new Hole(false, true, pos, pos.add(0, 0, 1));
        }
        return null;
    }

    public static boolean checkOffset(BlockPos pos, int offX, int offZ) {
        return HoleUtil.mc.world.getBlockState(pos).getBlock() == Blocks.AIR && HoleUtil.mc.world.getBlockState(pos.add(offX, 0, offZ)).getBlock() == Blocks.AIR && HoleUtil.isSafeBlock(pos.add(0, -1, 0)) && HoleUtil.isSafeBlock(pos.add(offX, -1, offZ)) && HoleUtil.isSafeBlock(pos.add(offX * 2, 0, offZ * 2)) && HoleUtil.isSafeBlock(pos.add(-offX, 0, -offZ)) && HoleUtil.isSafeBlock(pos.add(offZ, 0, offX)) && HoleUtil.isSafeBlock(pos.add(-offZ, 0, -offX)) && HoleUtil.isSafeBlock(pos.add(offX, 0, offZ).add(offZ, 0, offX)) && HoleUtil.isSafeBlock(pos.add(offX, 0, offZ).add(-offZ, 0, -offX));
    }

    static boolean isSafeBlock(BlockPos pos) {
        return HoleUtil.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || HoleUtil.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK || HoleUtil.mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST;
    }

    static boolean isBedrock(BlockPos pos) {
        return HoleUtil.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    public static boolean isBedrockDoubleHole(final BlockPos pos) {
        for (final EnumFacing f : EnumFacing.HORIZONTALS) {
            final int offX = f.getXOffset();
            final int offZ = f.getZOffset();
            if (mc.world.getBlockState(pos.add(offX, 0, offZ)).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.add(offX * -2, 0, offZ * -2)).getBlock() == Blocks.BEDROCK && mc.world.getBlockState(pos.add(offX * -1, 0, offZ * -1)).getBlock() == Blocks.AIR && isBedrock(pos.add(0, -1, 0)) && isBedrock(pos.add(offX * -1, -1, offZ * -1))) {
                if (offZ == 0 && isBedrock(pos.add(0, 0, 1)) && isBedrock(pos.add(0, 0, -1)) && isBedrock(pos.add(offX * -1, 0, 1)) && isBedrock(pos.add(offX * -1, 0, -1))) {
                    return true;
                }
                if (offX == 0 && isBedrock(pos.add(1, 0, 0)) && isBedrock(pos.add(-1, 0, 0)) && isBedrock(pos.add(1, 0, offZ * -1)) && isBedrock(pos.add(-1, 0, offZ * -1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Hole> getHoles(double range, BlockPos playerPos, boolean doubles) {
        ArrayList<Hole> holes = new ArrayList<Hole>();
        List<BlockPos> circle = getSphere(range, playerPos, true, false);
        for (BlockPos pos : circle) {
            Hole dh;
            if (HoleUtil.mc.world.getBlockState(pos).getBlock() != Blocks.AIR) continue;
            if (HoleUtil.isObbyHole(pos)) {
                holes.add(new Hole(false, false, pos));
                continue;
            }
            if (HoleUtil.isBedrockHoles(pos)) {
                holes.add(new Hole(true, false, pos));
                continue;
            }
            if (!doubles || (dh = HoleUtil.isDoubleHole(pos)) == null || HoleUtil.mc.world.getBlockState(dh.pos1.add(0, 1, 0)).getBlock() != Blocks.AIR && HoleUtil.mc.world.getBlockState(dh.pos2.add(0, 1, 0)).getBlock() != Blocks.AIR) continue;
            holes.add(dh);
        }
        return holes;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        circleblocks.add(new BlockPos(x, y + plus_y, z));
                    }
                }
            }
        }
        return circleblocks;
    }

    public static List<Hole> getHoles(float range, int rangeY, BlockPos playerPos, boolean doubles) {
        ArrayList<Hole> holes = new ArrayList<Hole>();
        for (BlockPos pos : getSphere(playerPos, range, rangeY, false, true, 0)) {
            Hole dh;
            if (HoleUtil.mc.world.getBlockState(pos).getBlock() != Blocks.AIR) continue;
            if (HoleUtil.isObbyHole(pos)) {
                holes.add(new Hole(false, false, pos));
                continue;
            }
            if (HoleUtil.isBedrockHoles(pos)) {
                holes.add(new Hole(true, false, pos));
                continue;
            }
            if (!doubles || (dh = HoleUtil.isDoubleHole(pos)) == null || HoleUtil.mc.world.getBlockState(dh.pos1.add(0, 1, 0)).getBlock() != Blocks.AIR && HoleUtil.mc.world.getBlockState(dh.pos2.add(0, 1, 0)).getBlock() != Blocks.AIR) continue;
            holes.add(dh);
        }
        return holes;
    }

    public static List<BlockPos> getSphere(double range, BlockPos pos, boolean sphere, boolean hollow) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        int x = cx - (int)range;
        while ((double)x <= (double)cx + range) {
            int z = cz - (int)range;
            while ((double)z <= (double)cz + range) {
                int y = sphere ? cy - (int)range : cy;
                while (true) {
                    double d = y;
                    double d2 = sphere ? (double)cy + range : (double)cy + range;
                    if (!(d < d2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < range * range) || hollow && dist < (range - 1.0) * (range - 1.0))) {
                        BlockPos l = new BlockPos(x, y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static class Hole {
        public boolean bedrock;
        public boolean doubleHole;
        public BlockPos pos1;
        public BlockPos pos2;

        public Hole(boolean bedrock, boolean doubleHole, BlockPos pos1, BlockPos pos2) {
            this.bedrock = bedrock;
            this.doubleHole = doubleHole;
            this.pos1 = pos1;
            this.pos2 = pos2;
        }

        public Hole(boolean bedrock, boolean doubleHole, BlockPos pos1) {
            this.bedrock = bedrock;
            this.doubleHole = doubleHole;
            this.pos1 = pos1;
        }
    }
}
