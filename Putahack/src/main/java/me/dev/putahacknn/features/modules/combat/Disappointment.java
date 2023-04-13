package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.BlockUtil;
import me.dev.putahacknn.util.EntityUtil;
import me.dev.putahacknn.util.InventoryUtil;
import me.dev.putahacknn.util.Timer;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Disappointment extends Module {

    public Disappointment() {
        super("Disappointment", "Ends your friendship with dbear", Category.COMBAT, true, false, false);
    }

    public final Setting<Boolean> placeCrystal = this.register(new Setting("Place Crystals", true));
    public final Setting<Boolean> crystalPlaceSwing = this.register(new Setting("Crystal Place Swing", true));
    public final Setting<Float> targetRange = this.register(new Setting("Target Range", 4.5f, 0.0f, 10.0f));
    public final Setting<Boolean> anvilPrePlace = this.register(new Setting("Anvil PrePlace", false));
    public final Setting<Boolean> anvilSwing = this.register(new Setting("Anvil Swing", false));
    public final Setting<Boolean> placeCityBase = this.register(new Setting("Place City Base", true));
    public final Setting<Boolean> oneDotThirteen = this.register(new Setting("1.13+", false));
    public final Setting<Integer> delay = this.register(new Setting("Delay", 20, 0, 150));
    public final Setting<Integer> mineTimeout = this.register(new Setting("Mine Timeout", 40, 0, 250));
    public final Setting<Boolean> placeRotate = this.register(new Setting("Place Rotate", false));
    public final Setting<Boolean> breakCrystals = this.register(new Setting("Break Crystals", true));
    public final Setting<Boolean> breakSwing = this.register(new Setting("Break Swing", true));
    public Timer prePlaceTimer = new Timer();
    public Timer mineTimer = new Timer();
    public Timer timer = new Timer();

    @Override
    public void onUpdate() {
        EntityLivingBase target = EntityUtil.getTarget(targetRange.getValue());
        if (target == null || fullNullCheck() || itemNullCheck()) {
            return;
        }
        Vec3d vec = target.getPositionVector();
        BlockPos targetX = new BlockPos(vec.add(1, 0, 0));
        BlockPos targetXMinus = new BlockPos(vec.add(-1, 0, 0));
        BlockPos targetZ = new BlockPos(vec.add(0, 0, 1));
        BlockPos targetZMinus = new BlockPos(vec.add(0, 0, -1));
        BlockPos targetXCrystal = new BlockPos(vec.add(2, 0, 0));
        BlockPos targetXMinusCrystal = new BlockPos(vec.add(-2, 0, 0));
        BlockPos targetZCrystal = new BlockPos(vec.add(0, 0, 2));
        BlockPos targetZMinusCrystal = new BlockPos(vec.add(0, 0, -2));
        EnumHand hand = mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        int anvilSlot = InventoryUtil.findHotbarBlock(BlockAnvil.class);
        int obsidianSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (canBreakBlock(targetX) && canPlaceCrystal(targetXCrystal, oneDotThirteen.getValue())) {
            if (canBreakBlock(targetX)) {
                sendMinePacketOnPos(targetX);
            }
            if (anvilPrePlace.getValue() && getBlock(targetX) != Blocks.ANVIL && canPlaceBlock(targetX.up()) && getBlock(targetX) != Blocks.AIR && BlockUtil.hasNeighbour(targetX.up())) {
                if (prePlaceTimer.passedMs(delay.getValue())) {
                    placeBlock(targetX.up(), anvilSlot);
                    prePlaceTimer.reset();
                }
            }
            if (canPlaceBlock(targetX)) {
                if (timer.passedMs(delay.getValue())) {
                    placeBlock(targetX, anvilSlot);
                    timer.reset();
                }
            }
            if (canPlaceCrystal(targetXCrystal, oneDotThirteen.getValue()) && getBlock(targetX) instanceof BlockAnvil && placeCrystal.getValue()) {
                BlockUtil.placeCrystalOnBlock(targetXCrystal.down(), hand, crystalPlaceSwing.getValue(), true, true);
            }
        } else if (placeCityBase.getValue() && canPlaceCrystalNoBlock(targetXCrystal, oneDotThirteen.getValue()) && canBreakBlock(targetX) && canPlaceBlock(targetXCrystal.down())) {
            placeBlock(targetXCrystal.down(), obsidianSlot);
        } else if (canBreakBlock(targetXMinus) && canPlaceCrystal(targetXMinusCrystal, oneDotThirteen.getValue())) {
            if (canBreakBlock(targetXMinus)) {
                sendMinePacketOnPos(targetXMinus);
            }
            if (anvilPrePlace.getValue() && getBlock(targetXMinus) != Blocks.ANVIL && canPlaceBlock(targetXMinus.up()) && getBlock(targetXMinus) != Blocks.AIR && BlockUtil.hasNeighbour(targetXMinus.up())) {
                if (prePlaceTimer.passedMs(delay.getValue())) {
                    placeBlock(targetXMinus.up(), anvilSlot);
                    prePlaceTimer.reset();
                }
            }
            if (canPlaceBlock(targetXMinus)) {
                if (timer.passedMs(delay.getValue())) {
                    placeBlock(targetXMinus, anvilSlot);
                    timer.reset();
                }
            }
            if (canPlaceCrystal(targetXMinusCrystal, oneDotThirteen.getValue()) && getBlock(targetXMinus) instanceof BlockAnvil && placeCrystal.getValue()) {
                BlockUtil.placeCrystalOnBlock(targetXMinusCrystal.down(), hand, crystalPlaceSwing.getValue(), true, true);
            }
        } else if (placeCityBase.getValue() && canPlaceCrystalNoBlock(targetXMinusCrystal, oneDotThirteen.getValue()) && canBreakBlock(targetXMinus) && canPlaceBlock(targetXMinusCrystal.down())) {
            placeBlock(targetXMinusCrystal.down(), obsidianSlot);
        } else if (canBreakBlock(targetZ) && canPlaceCrystal(targetZCrystal, oneDotThirteen.getValue())) {
            if (canBreakBlock(targetZ)) {
                sendMinePacketOnPos(targetZ);
            }
            if (anvilPrePlace.getValue() && getBlock(targetZ) != Blocks.ANVIL && canPlaceBlock(targetZ.up()) && getBlock(targetZ) != Blocks.AIR && BlockUtil.hasNeighbour(targetZ.up())) {
                if (prePlaceTimer.passedMs(delay.getValue())) {
                    placeBlock(targetZ.up(), anvilSlot);
                    prePlaceTimer.reset();
                }
            }
            if (canPlaceBlock(targetZ)) {
                if (timer.passedMs(delay.getValue())) {
                    placeBlock(targetZ, anvilSlot);
                    timer.reset();
                }
            }
            if (canPlaceCrystal(targetZCrystal, oneDotThirteen.getValue()) && getBlock(targetZ) instanceof BlockAnvil && placeCrystal.getValue()) {
                BlockUtil.placeCrystalOnBlock(targetZCrystal.down(), hand, crystalPlaceSwing.getValue(), true, true);
            }
        } else if (placeCityBase.getValue() && canPlaceCrystalNoBlock(targetZCrystal, oneDotThirteen.getValue()) && canBreakBlock(targetZ) && canPlaceBlock(targetZCrystal.down())) {
            placeBlock(targetZCrystal.down(), obsidianSlot);
        } else if (canBreakBlock(targetZMinus) && canPlaceCrystal(targetZMinusCrystal, oneDotThirteen.getValue())) {
            if (canBreakBlock(targetZMinus)) {
                sendMinePacketOnPos(targetZMinus);
            }
            if (anvilPrePlace.getValue() && getBlock(targetZMinus) != Blocks.ANVIL && canPlaceBlock(targetZMinus.up()) && getBlock(targetZMinus) != Blocks.AIR && BlockUtil.hasNeighbour(targetZMinus.up())) {
                if (prePlaceTimer.passedMs(delay.getValue())) {
                    placeBlock(targetZMinus.up(), anvilSlot);
                    prePlaceTimer.reset();
                }
            }
            if (canPlaceBlock(targetZMinus)) {
                if (timer.passedMs(delay.getValue())) {
                    placeBlock(targetZMinus, anvilSlot);
                    timer.reset();
                }
            }
            if (canPlaceCrystal(targetZMinusCrystal, oneDotThirteen.getValue()) && getBlock(targetZMinus) instanceof BlockAnvil && placeCrystal.getValue()) {
                BlockUtil.placeCrystalOnBlock(targetZMinusCrystal.down(), hand, crystalPlaceSwing.getValue(), true, true);
            }
        } else if (placeCityBase.getValue() && canPlaceCrystalNoBlock(targetZMinusCrystal, oneDotThirteen.getValue()) && canBreakBlock(targetZMinus) && canPlaceBlock(targetZMinusCrystal.down())) {
            placeBlock(targetZMinusCrystal.down(), obsidianSlot);
        }
    }

    public void placeBlock(BlockPos pos, int slot) {
        int oldSlot = mc.player.inventory.currentItem;
        if (breakCrystals.getValue()) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderCrystal && new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                    EntityUtil.attackEntity(entity, true, breakSwing.getValue());
                }
            }
        }
        if (slot != -1 && canPlaceBlock(pos) && !intersectsWithEntity(pos)) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            BlockUtil.place(pos, EnumHand.MAIN_HAND, placeRotate.getValue(), false, anvilSwing.getValue());
            mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        }
    }

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityXPOrb) continue;
            if (entity instanceof EntityItem) continue;
            if (entity instanceof EntityEnderCrystal) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox().shrink(0.145))) return true;
        }
        return false;
    }

    public void sendMinePacketOnPos(BlockPos pos) {
        if (mineTimer.passedMs(mineTimeout.getValue())) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            mineTimer.reset();
        }
    }

    public boolean canPlaceCrystal(BlockPos blockPos, boolean oneDotThirteen) {
        if (oneDotThirteen) {
            if (getBlock(blockPos) == Blocks.AIR && isObsidianOrBedrock(blockPos.down())) {
                return true;
            }
        } else if (getBlock(blockPos) == Blocks.AIR && getBlock(blockPos.up()) == Blocks.AIR && isObsidianOrBedrock(blockPos.down())) {
            return true;
        }
        return false;
    }

    public static boolean canBreakBlock(BlockPos pos) {
        IBlockState blockState = mc.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, (World)mc.world, pos) != -1.0f;
    }

    public boolean canPlaceCrystalNoBlock(BlockPos blockPos, boolean oneDotThirteen) {
        if (oneDotThirteen) {
            if (getBlock(blockPos) == Blocks.AIR && !isObsidianOrBedrock(blockPos.down())) {
                return true;
            }
        } else if (getBlock(blockPos) == Blocks.AIR && getBlock(blockPos.up()) == Blocks.AIR && !isObsidianOrBedrock(blockPos.down())) {
            return true;
        }
        return false;
    }

    public boolean canPlaceBlock(BlockPos pos) {
        if (getBlock(pos) instanceof BlockAir || getBlock(pos) instanceof BlockSnow || getBlock(pos) instanceof BlockLiquid || getBlock(pos) instanceof BlockVine || getBlock(pos) instanceof BlockSnow || getBlock(pos) instanceof BlockTallGrass || getBlock(pos) instanceof BlockFire || getBlock(pos) instanceof BlockDynamicLiquid || getBlock(pos) instanceof BlockStaticLiquid) {
            return true;
        }
        return false;
    }

    public boolean isObsidianOrBedrock(BlockPos pos) {
        if (isValidBlock(getBlock(pos))) {
            return true;
        }
        return false;
    }

    public boolean isValidBlock(Block block) {
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK;
    }

    public Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    public boolean itemNullCheck() {
        if (InventoryUtil.findHotbarBlock(ItemPickaxe.class) == -1 || InventoryUtil.findHotbarBlock(BlockAnvil.class) == -1) {
            return true;
        }
        return false;
    }

}
