package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.BlockUtil;
import me.dev.putahacknn.util.EntityUtil;
import me.dev.putahacknn.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class TrapHead extends Module {
    private final Setting<Float> range = register(new Setting("Range", Float.valueOf(5.0F), Float.valueOf(1.0F), Float.valueOf(6.0F)));
    public EntityPlayer target;

    public TrapHead() {
        super("TrapHead", "Trap Head", Module.Category.COMBAT, true, false, false);
    }


    public void onTick() {
        if (fullNullCheck())
            return;
        this.target = getTarget(((Float) this.range.getValue()).floatValue());
        if (this.target == null)
            return;
        BlockPos people = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1)
            return;
        int old = mc.player.inventory.currentItem;
        if (getBlock(people.add(0, 2, 0)).getBlock() == Blocks.AIR)
            if (getBlock(people.add(1, 2, 0)).getBlock() != Blocks.AIR || getBlock(people.add(0, 2, 1)).getBlock() != Blocks.AIR || getBlock(people.add(-1, 2, 0)).getBlock() != Blocks.AIR || getBlock(people.add(0, 2, -1)).getBlock() != Blocks.AIR) {
                switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                switchToSlot(old);
            } else if (getBlock(people.add(1, 1, 0)).getBlock() != Blocks.AIR) {
                switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(1, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                switchToSlot(old);
            } else if (getBlock(people.add(-1, 1, 0)).getBlock() != Blocks.AIR) {
                switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(-1, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                switchToSlot(old);
            } else if (getBlock(people.add(0, 1, 1)).getBlock() != Blocks.AIR) {
                switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, 1), EnumHand.MAIN_HAND, false, true, false);
                switchToSlot(old);
            } else if (getBlock(people.add(0, 1, -1)).getBlock() != Blocks.AIR) {
                switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, -1), EnumHand.MAIN_HAND, false, true, false);
                switchToSlot(old);
            }
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity) player, range))
                continue;
            if (PutaHacknn.friendManager.isFriend(player.getName()) || mc.player.posY - player.posY >= 5.0D)
                continue;
            if (target == null) {
                target = player;
                distance = EntityUtil.mc.player.getDistanceSq((Entity) player);
                continue;
            }
            if (EntityUtil.mc.player.getDistanceSq((Entity) player) >= distance)
                continue;
            target = player;
            distance = EntityUtil.mc.player.getDistanceSq((Entity) player);
        }
        return target;
    }

    private void switchToSlot(int slot) {
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }
}
