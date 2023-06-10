package dev.starstruck.module.player;

import dev.starstruck.Starstruck;
import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.player.EventWalkingUpdate;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import dev.starstruck.util.math.rotate.RotationUtils;
import dev.starstruck.util.timing.Timer;
import dev.starstruck.util.world.PlaceUtils;
import dev.starstruck.util.world.holder.Placement;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class Scaffold extends Module {
    private final Setting<Double> delay = new Setting<>(50.0, 0.01, 0.0, 500.0, "Delay");
    private final Setting<Boolean> rotate = new Setting<>(true, "Rotate");
    private final Setting<Swap> swap = new Setting<>(Swap.LITE_SPOOF, "Swap");
    private final Setting<Boolean> swing = new Setting<>(true, "Swing");
    private final Setting<Boolean> tower = new Setting<>(true, "Tower");

    private final Timer placeTimer = new Timer();
    private float[] rotations;
    private int oldSlot = -1;

    public Scaffold() {
        super("Scaffold", "Places blocks rapidly at your feet", ModuleCategory.PLAYER);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        rotations = null;

        if (mc.player != null) {
            if (swap.getValue() == Swap.HOLD) {
                if (oldSlot != -1) {
                    mc.player.inventory.currentItem = oldSlot;
                }

            } else {
                Starstruck.get().getInventory().sync();
            }
        }

        oldSlot = -1;
    }

    @Listener
    public void onWalkingUpdate(EventWalkingUpdate event) {

        Placement placement = PlaceUtils.getPlacementAt(
                new BlockPos(mc.player.getPositionVector()).down());

        if (rotate.getValue() && rotations != null) {
            Starstruck.get().getRotations().spoof(rotations);
        }

        if (placement == null) return;

        rotations = RotationUtils.blocK(
                placement.getPos(), placement.getFacing());

        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return;

        if (placeTimer.hasPassed(delay.getValue().longValue(), false)) {

            switch (swap.getValue()) {
                case MANUAL:
                    if (!(mc.player.inventory.getCurrentItem().getItem() instanceof ItemBlock)) {
                        return;
                    }
                    break;

                case LITE_SPOOF:
                case LITE:
                    if (Starstruck.get().getInventory().getServerSlot() != slot) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                    }
                    break;

                case HOLD:
                    mc.player.inventory.currentItem = slot;
                    break;
            }

            EnumActionResult result = mc.playerController.processRightClickBlock(mc.player,
                    mc.world,
                    placement.getPos(),
                    placement.getFacing(),
                    PlaceUtils.getBasicHitVec(placement),
                    EnumHand.MAIN_HAND);
            if (result == EnumActionResult.SUCCESS) {
                placeTimer.resetTime();

                if (swing.getValue()) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                } else {
                    mc.player.connection.sendPacket(
                            new CPacketAnimation(EnumHand.MAIN_HAND));
                }

                if (tower.getValue() && mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (mc.player.onGround && mc.player.motionY < 0.1) {
                        mc.player.motionY = 0.41999998688697815;
                    } else if (mc.player.motionY <= 0.16477328182606651) {
                        mc.player.motionY = 0.41999998688697815;
                    }
                }

                if (swap.getValue() == Swap.LITE) {
                    Starstruck.get().getInventory().sync();
                }
            }
        }
    }

    public enum Swap {
        MANUAL, LITE, LITE_SPOOF, HOLD
    }
}
