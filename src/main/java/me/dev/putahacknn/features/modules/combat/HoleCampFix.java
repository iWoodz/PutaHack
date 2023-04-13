package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.modules.woodz.BeepBoop;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.BlockUtil;
import me.dev.putahacknn.util.DamageUtil;
import me.dev.putahacknn.util.EntityUtil;
import me.dev.putahacknn.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleCampFix extends Module {

    public HoleCampFix() {
        super("HoleCampFix", "holecamper is in a safe hole? fixed", Category.COMBAT, true, false, false);
        INSTANCE = this;
    }

    public static HoleCampFix INSTANCE;
    public final Setting<CrystalMode> crystalMode = this.register(new Setting("Crystal Mode", CrystalMode.Offhand));
    public final Setting<Boolean> blockRotate = this.register(new Setting("Block Rotate", false));
    public BlockPos lastHitPos;
    public boolean hasHit;

    public enum CrystalMode {
        Silent,
        Offhand
    }

    @Override
    public void onUpdate() {
        if (mc.player != null && mc.world != null) {
            EntityLivingBase target = EntityUtil.getTarget(10.0f);
            if (target == null || mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                return;
            }
            BlockPos cevPos = new BlockPos(target.getPositionVector()).up().up();
            if (!BlockUtil.canPlaceCrystal(cevPos, true, false) && BlockUtil.getBlock(cevPos) == Blocks.OBSIDIAN) {
                return;
            }
            if (lastHitPos == null || !lastHitPos.equals(cevPos)) {
                hasHit = false;
                lastHitPos = cevPos;
            }
            int crystalSlot = InventoryUtil.findHotbarBlock(ItemEndCrystal.class);
            int obsidianSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int oldSlot = mc.player.inventory.currentItem;
            if (crystalMode.getValue() == CrystalMode.Offhand) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(cevPos, EnumFacing.DOWN, EnumHand.OFF_HAND, 0.5f, 0.5f, 0.5f));
            } else if (crystalSlot != -1) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(crystalSlot));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(cevPos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
                mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
            }
            if (!BeepBoop.INSTANCE.isEnabled()) {
                BeepBoop.INSTANCE.enable();
            }
            if (!hasHit) {
                BeepBoop.INSTANCE.hitPos(cevPos, EnumFacing.DOWN);
                hasHit = true;
            }
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderCrystal && DamageUtil.calculateDamage(entity, target) >= 7.0f) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                }
            }
            if (!intersectsWithCrystal(cevPos.up()) && BlockUtil.canPlaceBlock(cevPos) && BlockUtil.hasNeighbour(cevPos)) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(obsidianSlot));
                BlockUtil.placeBlock(cevPos, true, true, blockRotate.getValue());
                mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
            } else if (!BlockUtil.hasNeighbour(cevPos)) {
                placeHelpingBlocks(cevPos, obsidianSlot);
            }
        }
    }

    public void placeHelpingBlocks(BlockPos pos, int slot) {
        int oldSlot = mc.player.inventory.currentItem;
        if (!BlockUtil.hasNeighbour(pos)) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (BlockUtil.canPlaceBlock(pos.offset(facing)) && BlockUtil.hasNeighbour(pos.offset(facing))) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                    BlockUtil.placeBlock(pos.offset(facing), true, true, blockRotate.getValue());
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
                    break;
                }
            }
        }
    }

    public static boolean intersectsWithCrystal(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        BeepBoop.INSTANCE.minedPos = null;
    }
}
