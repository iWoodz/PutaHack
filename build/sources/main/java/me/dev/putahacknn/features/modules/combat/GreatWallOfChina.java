package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.BlockUtil;
import me.dev.putahacknn.util.EntityUtil;
import me.dev.putahacknn.util.InventoryUtil;
import me.dev.putahacknn.util.Timer;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GreatWallOfChina extends Module {

    public GreatWallOfChina() {
        super("GreatWallOfChina", "a wall so great even dbear's insults wont get through it", Category.COMBAT, true, false, false);
    }

    public enum Mode {
        Sand,
        Anvil,
        Both
    }

    public final Setting<Integer> delay = this.register(new Setting("Delay", 15, 0, 100));
    public final Setting<Integer> blocksPerPlace = this.register(new Setting("Blocks Per Place", 4, 0, 20));
    public final Setting<Mode> mode = this.register(new Setting("Mode", Mode.Anvil));
    public final Setting<Float> targetRange = this.register(new Setting("Target Range", 6.0f, 0.0f, 10.0f));
    public final Setting<Boolean> swing = this.register(new Setting("Swing", false));
    public final Setting<Boolean> rotate = this.register(new Setting("Rotate", true));
    public Timer timer = new Timer();
    public int placed;

    @Override
    public void onUpdate() {
        placed = 0;
        EntityLivingBase target = EntityUtil.getTarget(targetRange.getValue());
        if (target != null) {
            BlockPos entityPos = new BlockPos(target.posX, target.posY, target.posZ);
            if (!hasPlacedAnvil(entityPos)) {
                placeBlocks(entityPos);
            }
        }
    }

    public boolean hasPlacedAnvil(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityFallingBlock) {
                if (((EntityFallingBlock) entity).getBlock() == Blocks.ANVIL || ((EntityFallingBlock) entity).getBlock() == Blocks.SAND) {
                    if (entity.posX == pos.getX() && entity.posZ == pos.getZ() && entity.posY < pos.getY() + 2) {
                        return true;
                    }
                }
            }
        }
        if (BlockUtil.getBlock(pos) == Blocks.ANVIL || BlockUtil.getBlock(pos) == Blocks.SAND) {
            return true;
        }
        return false;
    }

    public void placeBlocks(BlockPos pos) {
        if (timer.passedMs(delay.getValue())) {
            if (BlockUtil.getBlock(pos) != Blocks.ANVIL || BlockUtil.getBlock(pos) != Blocks.SAND) {
                if (BlockUtil.hasNeighbour(pos.up().up())) {
                    placeBlock(pos.up().up(), false);
                } else {
                    placeBlock(pos.south(), true);
                    placeBlock(pos.south().up(), true);
                    placeBlock(pos.south().up().up(), true);
                }
            }
            timer.reset();
        }
    }

    public void switchToSlot(int slot) {
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public void placeBlock(BlockPos pos, boolean helping) {
        int sandSlot = InventoryUtil.findHotbarBlock(BlockSand.class);
        int anvilSlot = InventoryUtil.findHotbarBlock(BlockAnvil.class);
        int oldSlot = mc.player.inventory.currentItem;
        int slot = -1;
        if (mode.getValue() == Mode.Sand && sandSlot != -1) {
            slot = sandSlot;
        } else if (mode.getValue() == Mode.Anvil && anvilSlot != -1) {
            slot = anvilSlot;
        } else if (mode.getValue() == Mode.Both) {
            slot = sandSlot != -1 ? sandSlot : anvilSlot;
        }
        if (placed < blocksPerPlace.getValue() && slot != -1 && BlockUtil.canPlaceBlock(pos) && !BlockUtil.intersectsWithEntity(pos) && BlockUtil.hasNeighbour(pos)) {
            switchToSlot(!helping ? slot : InventoryUtil.findHotbarBlock(BlockObsidian.class));
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), true, null, swing.getValue());
            mc.player.inventory.currentItem = oldSlot;
            placed++;
        }
    }

}
