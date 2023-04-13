package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.util.*;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.Timer;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.List;

public class Surround extends Module {


    public Surround() {
        super("Surround", "Surrounds yourself with obsidian to prevent crystal damage", Category.COMBAT, true, false, false);
        INSTANCE = this;
    }

    public static Surround INSTANCE;
    private final Timer delayTimer = new Timer();
    public final Setting<Boolean> allowNon1x1 = this.register(new Setting("Allow non 1x1", true));
    public final Setting<Boolean> centre = this.register(new Setting("Centre", false));
    public final Setting<Integer> delayTicks = this.register(new Setting("Tick Delay", 0, 0, 10));
    public final Setting<Integer> blocksPerTick = this.register(new Setting("Blocks Per Tick", 16, 1, 20));
    public final Setting<Boolean> onlyOnStop = this.register(new Setting("OnStop", false));
    public final Setting<Boolean> disableOnJump = this.register(new Setting("Disable On Jump", true));
    public final Setting<Boolean> onlyOnSneak = this.register(new Setting("Only on Sneak", false));
    public final Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
    public final Setting<Boolean> destroyCrystal = this.register(new Setting("Destroy Stuck Crystal", true));
    public final Setting<Boolean> destroyAboveCrystal = this.register(new Setting("Destroy Above Crystal", true));
    public final Setting<Boolean> entityExtend = this.register(new Setting("Entity Extend", true));
    public final Setting<Boolean> swingArm = this.register(new Setting("Swing Arm", true));
    public final Setting<Boolean> fake = this.register(new Setting("Fake", false));
    ArrayList<BlockPos> blockChanged = new ArrayList<>();
    boolean hasPlaced;
    int y;

    int getSlot() {
        int slot = InventoryUtil.findFirstBlockSlot(Blocks.OBSIDIAN.getClass(), 0, 8);
        if (slot == -1) {
            slot = InventoryUtil.findFirstBlockSlot(Blocks.ENDER_CHEST.getClass(), 0, 8);
        }
        return slot;
    }

    @Override
    public void onEnable() {
        y = (int) Math.floor(mc.player.posY);
    }

    @Override
    public void onUpdate() {
        if (fake.getValue()) {
            return;
        }
        try {
            if (fullNullCheck() || onlyOnStop.getValue() && (mc.player.motionX != 0 || mc.player.motionY != 0 || mc.player.motionZ != 0) || (onlyOnSneak.getValue() && !mc.gameSettings.keyBindSneak.isPressed()))
                return;
            if (disableOnJump.getValue() && Math.abs(Math.abs(y) - Math.abs(mc.player.posY)) >= 0.4 && !isOnNonFullBlock()) { // enough for jump to cause unless under a block
                disable();
                return;
            }
            if (delayTimer.getPassedTimeMs() / 50L >= delayTicks.getValue()) {
                delayTimer.reset();

                int blocksPlaced = 0;

                hasPlaced = false;

                List<BlockPos> offsetPattern = this.getOffsets();
                int maxSteps = offsetPattern.size();
                int blockSlot = this.getSlot();
                if (blockSlot == -1)
                    return;

                int offsetSteps = 0;
                if (centre.getValue() && !allowNon1x1.getValue()) {
                    PlayerUtil.centerPlayer(mc.player.getPositionVector());
                }
                while (blocksPlaced <= blocksPerTick.getValue()) {
                    if (offsetSteps >= maxSteps) {
                        break;
                    }
                    BlockPos targetPos = offsetPattern.get(offsetSteps++);
                    if (BlockUtil.getBlock(targetPos) == Blocks.ANVIL) {
                        extendOnPos(targetPos);
                    }
                    if (intersectsWithEntityRealBB(targetPos) && canPlaceBlockNoEntityCheck(targetPos)) {
                        if (entityExtend.getValue()) {
                            doExtendOnPos(targetPos);
                        }
                    }
                    if (blockChanged.contains(targetPos))
                        continue;

                    mc.world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(targetPos), null);
                    boolean foundSomeone = false;
                    for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(targetPos).shrink(0.015))) {
                        if (entity instanceof EntityPlayer) {
                            foundSomeone = true;
                            break;
                        }
                        if (entity instanceof EntityEnderCrystal && destroyCrystal.getValue()) {
                            if (rotate.getValue()) {
                                RotationUtil.faceVector(new Vec3d(entity.getPosition()).add(0.5, 0, 0.5), true);
                            }
                            mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                        }
                    }
                    if (destroyAboveCrystal.getValue()) {
                        for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                            if (entity instanceof EntityEnderCrystal) {
                                if (sameBlockPos(entity.getPosition(), targetPos)) {
                                    if (rotate.getValue()) {
                                        RotationUtil.faceVector(new Vec3d(entity.getPosition()).add(0.5, 0, 0.5), true);
                                    }
                                    mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                                }
                            }
                        }
                    }
                    if (foundSomeone) {
                        continue;
                    }
                    if (!mc.world.getBlockState(targetPos).getMaterial().isReplaceable())
                        continue;
                    int oldSlot = mc.player.inventory.currentItem;
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(blockSlot));
                    if (BlockUtil.place(targetPos, EnumHand.MAIN_HAND, rotate.getValue(), false, swingArm.getValue())) {
                        if (centre.getValue()) {
                            PlayerUtil.centerPlayer(mc.player.getPositionVector());
                        }
                        blocksPlaced++;
                    }
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                }
            }
            blockChanged.clear();
        } catch (Exception ignored) {

        }
    }

    boolean sameBlockPos(BlockPos first, BlockPos second) {
        if (first == null || second == null)
            return false;
        return first.getX() == second.getX() && first.getY() == second.getY() + 2 && first.getZ() == second.getZ();
    }

    public boolean isOnNonFullBlock() {
        if (mc.world.getBlockState(new BlockPos(mc.player.getPositionVector())).getBlock() instanceof BlockEnderChest || BlockUtil.getBlock(new BlockPos(mc.player.getPositionVector())) instanceof BlockChest) {
            return true;
        }
        return false;
    }

    public void extendOnPos(BlockPos pos) {
        BlockPos[] dynamicOffset3 = {pos.north(), pos.east(), pos.south(), pos.west()};
        for (BlockPos posC : dynamicOffset3) {
            if (canPlaceBlock(posC) && !intersectsWithEntity(posC)) {
                placeBlocks(posC);
            }
        }
    }

    public void doExtendOnPos(BlockPos pos) {
        if (!intersectsWithSelf(pos) && intersectsWithEntityNoChecks(pos) && canPlaceBlockNoEntityCheck(pos)) {
            BlockPos[] dynamicOffset = {pos.north(), pos.east(), pos.south(), pos.west()};
            for (BlockPos posA : dynamicOffset) {
                if (canPlaceBlock(posA) && !intersectsWithEntityNoChecks(posA)) {
                    doExtendBlocks(posA);
                } else if (intersectsWithEntityNoChecks(posA)) {
                    BlockPos[] dynamicOffset2 = {posA.north(), posA.east(), posA.south(), posA.west()};
                    for (BlockPos posB : dynamicOffset2) {
                        if (canPlaceBlock(posB) && !intersectsWithEntityNoChecks(posB)) {
                            doExtendBlocks(posB);
                        } else if (intersectsWithEntityNoChecks(posB)) {
                            BlockPos[] dynamicOffset3 = {posB.north(), posB.east(), posB.south(), posB.west()};
                            for (BlockPos posC : dynamicOffset3) {
                                if (canPlaceBlock(posC) && !intersectsWithEntityNoChecks(posC)) {
                                    doExtendBlocks(posC);
                                } else if (intersectsWithEntityNoChecks(posC)) {
                                    BlockPos[] dynamicOffset4 = {posC.north(), posC.east(), posC.south(), posC.west()};
                                    for (BlockPos posD : dynamicOffset4) {
                                        if (canPlaceBlock(posD) && !intersectsWithEntityNoChecks(posD)) {
                                            doExtendBlocks(posD);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void doExtendBlocks(BlockPos pos) {
        if (BlockUtil.hasNeighbour(pos)) {
            placeBlocks(pos);
        } else {
            placeHelpingBlocks(pos);
            placeBlocks(pos);
        }
    }

    public void placeHelpingBlocks(BlockPos pos) {
        if (!BlockUtil.hasNeighbour(pos)) {
            if (BlockUtil.canPlaceBlock(pos.north()) && BlockUtil.hasNeighbour(pos.north()) && !intersectsWithEntityRealBB(pos.north())) {
                placeBlocks(pos.north());
            } else if (BlockUtil.canPlaceBlock(pos.east()) && BlockUtil.hasNeighbour(pos.east()) && !intersectsWithEntityRealBB(pos.east())) {
                placeBlocks(pos.east());
            } else if (BlockUtil.canPlaceBlock(pos.south()) && BlockUtil.hasNeighbour(pos.south()) && !intersectsWithEntityRealBB(pos.south())) {
                placeBlocks(pos.south());
            } else if (BlockUtil.canPlaceBlock(pos.west()) && BlockUtil.hasNeighbour(pos.west()) && !intersectsWithEntityRealBB(pos.west())) {
                placeBlocks(pos.west());
            } else if (BlockUtil.canPlaceBlock(pos.up()) && BlockUtil.hasNeighbour(pos.up()) && !intersectsWithEntityRealBB(pos.up())) {
                placeBlocks(pos.up());
            } else if (BlockUtil.canPlaceBlock(pos.down()) && BlockUtil.hasNeighbour(pos.down()) && !intersectsWithEntityRealBB(pos.down())) {
                placeBlocks(pos.down());
            }
        }
    }

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityXPOrb) continue;
            if (entity instanceof EntityItem) continue;
            if (entity instanceof EntityEnderCrystal) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox().shrink(0.0005))) return true;
        }
        return false;
    }

    private boolean intersectsWithEntityRealBB(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityXPOrb) continue;
            if (entity instanceof EntityItem) continue;
            if (entity instanceof EntityEnderCrystal) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox().shrink(0.015))) return true;
        }
        return false;
    }

    private boolean intersectsWithEntityNoChecks(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityXPOrb) continue;
            if (entity instanceof EntityItem) continue;
            if (entity instanceof EntityEnderCrystal) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox().shrink(0.01))) return true;
        }
        return false;
    }

    private boolean intersectsWithSelf(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity != mc.player) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }

    public boolean canPlaceBlock(BlockPos pos) {
        if (BlockUtil.getBlock(pos) instanceof BlockAir || BlockUtil.getBlock(pos) instanceof BlockLiquid) {
            if (!intersectsWithEntity(pos)) {
                return true;
            }
        }
        return false;
    }

    public boolean canPlaceBlockNoEntityCheck(BlockPos pos) {
        if (BlockUtil.getBlock(pos) instanceof BlockAir || BlockUtil.getBlock(pos) instanceof BlockLiquid) {
            return true;
        }
        return false;
    }

    List<BlockPos> getOffsets() {
        BlockPos playerPos = this.getPlayerPos();
        ArrayList<BlockPos> offsets = new ArrayList<BlockPos>();
        if (this.allowNon1x1.getValue()) {
            int z;
            int x;
            double decimalX = Math.abs(mc.player.posX) - Math.floor(Math.abs(mc.player.posX));
            double decimalZ = Math.abs(mc.player.posZ) - Math.floor(Math.abs(mc.player.posZ));
            int lengthXPos = this.calcLength(decimalX, false);
            int lengthXNeg = this.calcLength(decimalX, true);
            int lengthZPos = this.calcLength(decimalZ, false);
            int lengthZNeg = this.calcLength(decimalZ, true);
            ArrayList<BlockPos> tempOffsets = new ArrayList<BlockPos>();
            offsets.addAll(this.getOverlapPos());
            for (x = 1; x < lengthXPos + 1; ++x) {
                tempOffsets.add(this.addToPlayer(playerPos, x, 0.0, 1 + lengthZPos));
                tempOffsets.add(this.addToPlayer(playerPos, x, 0.0, -(1 + lengthZNeg)));
            }
            for (x = 0; x <= lengthXNeg; ++x) {
                tempOffsets.add(this.addToPlayer(playerPos, -x, 0.0, 1 + lengthZPos));
                tempOffsets.add(this.addToPlayer(playerPos, -x, 0.0, -(1 + lengthZNeg)));
            }
            for (z = 1; z < lengthZPos + 1; ++z) {
                tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 0.0, z));
                tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 0.0, z));
            }
            for (z = 0; z <= lengthZNeg; ++z) {
                tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 0.0, -z));
                tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 0.0, -z));
            }
            for (BlockPos pos : tempOffsets) {
                if (getDown(pos)) {
                    offsets.add(pos.add(0, -1, 0));
                }
                offsets.add(pos);
            }
        } else {
            offsets.add(playerPos.add(0, -1, 0));
            for (int[] surround : new int[][]{
                    {1, 0},
                    {0, 1},
                    {-1, 0},
                    {0, -1}
            }) {
                if (getDown(playerPos.add(surround[0], 0, surround[1])))
                    offsets.add(playerPos.add(surround[0], -1, surround[1]));

                offsets.add(playerPos.add(surround[0], 0, surround[1]));
            }
        }
        return offsets;
    }

    public static boolean getDown(BlockPos pos) {

        for (EnumFacing e : EnumFacing.values())
            if (!mc.world.isAirBlock(pos.add(e.getDirectionVec())))
                return false;

        return true;

    }

    int calcOffset(double dec) {
        return dec >= 0.7 ? 1 : (dec <= 0.3 ? -1 : 0);
    }

    BlockPos addToPlayer(BlockPos playerPos, double x, double y, double z) {
        if (playerPos.getX() < 0) {
            x = -x;
        }
        if (playerPos.getY() < 0) {
            y = -y;
        }
        if (playerPos.getZ() < 0) {
            z = -z;
        }
        return playerPos.add(x, y, z);
    }

    List<BlockPos> getOverlapPos() {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        double decimalX = mc.player.posX - Math.floor(mc.player.posX);
        double decimalZ = mc.player.posZ - Math.floor(mc.player.posZ);
        int offX = this.calcOffset(decimalX);
        int offZ = this.calcOffset(decimalZ);
        positions.add(this.getPlayerPos());
        for (int x = 0; x <= Math.abs(offX); ++x) {
            for (int z = 0; z <= Math.abs(offZ); ++z) {
                int properX = x * offX;
                int properZ = z * offZ;
                positions.add(this.getPlayerPos().add(properX, -1, properZ));
            }
        }
        return positions;
    }


    int calcLength(double decimal, boolean negative) {
        if (negative) {
            return decimal <= 0.3 ? 1 : 0;
        }
        return decimal >= 0.7 ? 1 : 0;
    }

    BlockPos getPlayerPos() {
        double decimalPoint = mc.player.posY - Math.floor(mc.player.posY);
        return new BlockPos(mc.player.posX, decimalPoint > 0.8 ? Math.floor(mc.player.posY) + 1.0 : Math.floor(mc.player.posY), mc.player.posZ);
    }

    public void placeBlocks(BlockPos pos) {
        int oldSlot = mc.player.inventory.currentItem;
        if (getSlot() != -1 && !intersectsWithEntity(pos) && !intersectsWithSelf(pos) && canPlaceBlock(pos)) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlot()));
            BlockUtil.place(pos, EnumHand.MAIN_HAND, rotate.getValue(), false, swingArm.getValue());
            mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        }
    }
}

