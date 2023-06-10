package dev.starstruck.module.combat;

import com.google.common.collect.Lists;
import dev.starstruck.Starstruck;
import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.network.EventPacket;
import dev.starstruck.listener.event.player.EventWalkingUpdate;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import dev.starstruck.util.math.rotate.RotationUtils;
import dev.starstruck.util.world.PlaceUtils;
import dev.starstruck.util.world.WorldUtils;
import dev.starstruck.util.world.holder.Placement;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class Burrow extends Module {

    /**
     * A whitelist of blocks we can burrow with
     */
    private static final List<Block> whitelist = Lists.newArrayList(
            Blocks.OBSIDIAN, Blocks.ENDER_CHEST, Blocks.CHEST);

    /**
     * Packetlogged from the vanilla game when a jump is done
     * This *might* not work for jump boost potions, so i'll add compatibility with that
     */
    private static final double[] vanillaJump = {
            0.41999998688698,
            0.7531999805211997,
            1.00133597911214,
            1.16610926093821 };

    private final Setting<Mode> mode = new Setting<>(Mode.CLIP, "Mode");
    private final Setting<Double> clip = new Setting<>(
            2.5, 0.01, -10.0, 10.0, "Clip");
    private final Setting<Boolean> rotate = new Setting<>(true, "Rotate");
    private final Setting<Boolean> swing = new Setting<>(true, "Swing");
    private final Setting<Boolean> robo = new Setting<>(false, "Robo");

    private boolean burrowing;
    private double startY;

    public Burrow() {
        super("Burrow",
                "Abuses NCP to lag you into a block",
                ModuleCategory.COMBAT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (mc.world == null || mc.player == null) {
            setState(false);
            return;
        }

        startY = mc.player.posY;

        if (mode.getValue() == Mode.BYPASS) {
            print("I wouldn't recommend using mode 'BYPASS' cause it prolly doesn't work, but you do you");
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (mc.player != null) Starstruck.get().getInventory().sync();
        startY = -1.0;

        burrowing = false;
    }

    @Listener
    public void onWalkingUpdate(EventWalkingUpdate event) {

        // if we have jumped, don't do shit & toggle off
        if (startY < mc.player.posY || mc.player.posY <= 0.0) {
            setState(false);
            return;
        }

        // dont burrow if we cant
        if (!hasSpaceToBurrow() || !mc.player.onGround) return;

        burrow();
        if (!robo.getValue()) setState(false);
    }

    @Listener
    public void onPacketIn(EventPacket.In event) {
        if (event.getPacket() instanceof SPacketBlockChange) {
            SPacketBlockChange packet = event.getPacket();
            if (!packet.getBlockPosition().equals(posAt()) || !robo.getValue()) return;

            if (packet.getBlockState()
                    .getMaterial().isReplaceable()
                    && mc.player.onGround) burrow();
        }
    }

    private void burrow() {

        // dont burrow twice in a row (this is useful especially for robo mode)
        if (burrowing) return;
        burrowing = true;

        // from my understanding, burrow works because of how NCP's lagback system works
        // you use complete vanilla jump - which NCP and UpdatedNCP will accept
        // then you place the block server-side and NCP will let that happen because you are out of
        // the block, so you don't fail phase. once the block is placed, send a move packet that
        // will cause NCP to flag, which will lag you back to your last onGround position - inside the block
        // most burrow patches use the bukkit MoveEvent to check once the player moves if they're in a block
        // which can be abused by the 0.0624 value - only problem is that it doesn't flag NCP...

        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemBlock) {
                ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
                if (whitelist.contains(itemBlock.getBlock())) {
                    slot = i;
                    break;
                }
            }
        }

        if (slot == -1) return;

        Placement placement = PlaceUtils.getPlacementAt(posAt());
        if (placement == null) return;

        if (rotate.getValue()) {
            float[] rotations = RotationUtils.block(
                    placement.getPos(), placement.getFacing());
            Starstruck.get().getRotations().spoofInstant(rotations);
        }

        double posY = mc.player.posY; // getDynamicY();

        for (double y : vanillaJump) {
            mc.player.connection.sendPacket(new Position(
                    mc.player.posX, posY + y, mc.player.posZ, false));
        }

        mc.player.connection.sendPacket(
                new CPacketHeldItemChange(slot));

        Vec3d hitVec = PlaceUtils.getBasicHitVec(placement);

        // see PlayerControllerMP#processRightClickBlock
        float facingX = (float) (hitVec.x - (double) placement.getPos().getX());
        float facingY = (float) (hitVec.y - (double) placement.getPos().getY());
        float facingZ = (float) (hitVec.z - (double) placement.getPos().getZ());

        // we use a packet instead because vanilla will block the placement due to our bb colliding
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placement.getPos(),
                placement.getFacing(),
                EnumHand.MAIN_HAND,
                facingX, facingY, facingZ));

        if (swing.getValue()) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
        } else {
            mc.player.connection.sendPacket(
                    new CPacketAnimation(EnumHand.MAIN_HAND));
        }

        if (mode.getValue() == Mode.CLIP) {
            mc.player.connection.sendPacket(new Position(
                    mc.player.posX,
                    posY + clip.getValue(),
                    mc.player.posZ, false));
        } else {
            // idek how to do this part, might ask Doogie and see if he'll be nice and gimme

            mc.player.connection.sendPacket(new Position(
                    mc.player.posX + 2,
                    posY,
                    mc.player.posZ - 2, true));
        }

        Starstruck.get().getInventory().sync();
        burrowing = false;
    }

    private double getDynamicY() {
        // i have no clue if this even works
        // the idea is to prevent burrow not working on top of echests
        // perry did something similar in perry fobus but ill just make it myself

        BlockPos down = posAt().down();
        IBlockState state = mc.world.getBlockState(down);

        if (state.getMaterial().isReplaceable()) return mc.player.posY;

        AxisAlignedBB bb = state.getCollisionBoundingBox(mc.world, down);
        if (bb == null) return mc.player.posY;

        return bb.maxY;
    }

    private boolean hasSpaceToBurrow() {
        BlockPos at = posAt();
        for (int i = 0; i < 3; ++i) {
            if (!WorldUtils.isReplaceable(at)) return false;
            at = at.up();
        }
        return true;
    }

    private BlockPos posAt() {
        return new BlockPos(mc.player.getPositionVector());
    }

    public enum Mode {
        CLIP, BYPASS
    }
}
