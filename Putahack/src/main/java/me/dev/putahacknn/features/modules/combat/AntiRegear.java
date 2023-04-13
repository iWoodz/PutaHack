package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.mixin.mixins.accessors.ICPacketPlayer;
import me.dev.putahacknn.util.InventoryUtil;
import me.dev.putahacknn.util.MathUtil;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiRegear extends Module {
    public final Setting<Integer> range = this.register(new Setting<>("Range", 5, 0, 8));
    public final Setting<Boolean> autoSwap = this.register(new Setting<>("AutoSwap", true));
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    public float yaw, pitch;
//china code incoming
    public AntiRegear() {
        super("AntiRegear", "AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", Category.COMBAT, true, false, false);
    }

    @Override
    public void onDisable() {
        if (this.rotate.getValue()) {
            this.yaw = AntiRegear.mc.player.rotationYaw;
            this.pitch = AntiRegear.mc.player.rotationPitch;
        }
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (this.getBlock() != null) {
            int oldSlot = AntiRegear.mc.player.inventory.currentItem;
            int pickSlot = InventoryUtil.findHotbarBlock(ItemPickaxe.class);
            if (this.autoSwap.getValue() && pickSlot != -1) {
                AntiRegear.mc.player.inventory.currentItem = pickSlot;
            }
            if (this.rotate.getValue()) {
                Vec3d vec = new Vec3d((double)this.getBlock().getPos().getX() + 0.5, (double)(this.getBlock().getPos().getY() - 1), (double)this.getBlock().getPos().getZ() + 0.5);
                float[] rotations = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), vec);
                this.yaw = rotations[0];
                this.pitch = rotations[1];
            }
            AntiRegear.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.getBlock().getPos(), EnumFacing.SOUTH));
            AntiRegear.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.getBlock().getPos(), EnumFacing.SOUTH));
            AntiRegear.mc.player.inventory.currentItem = oldSlot;
        }
        if (this.rotate.getValue() && this.getBlock() == null) {
            this.yaw = AntiRegear.mc.player.rotationYaw;
            this.pitch = AntiRegear.mc.player.rotationPitch;
        }
    }// now, me personally

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (this.rotate.getValue() && event.getPacket() instanceof CPacketPlayer) {
            ((ICPacketPlayer) event.getPacket()).setYaw(this.yaw);
            ((ICPacketPlayer) event.getPacket()).setPitch(this.pitch);
        }
    }

    public TileEntity getBlock() {
        TileEntity out = null;
        for (TileEntity entity : AntiRegear.mc.world.loadedTileEntityList) {
            if (!(entity instanceof TileEntityShulkerBox) || !(entity.getDistanceSq(AntiRegear.mc.player.posX, AntiRegear.mc.player.posY, AntiRegear.mc.player.posZ) <= (double)(this.range.getValue() * this.range.getValue()))) continue;
            out = entity;
        }
        return out;
    }
}