package me.dev.putahacknn.features.modules.combat;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.manager.ModuleManager;
import me.dev.putahacknn.util.BlockUtil;
import me.dev.putahacknn.util.EntityUtil;
import me.dev.putahacknn.util.RotationUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class PistonCrystal extends Module {

    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    public Setting<Boolean> blockPlayer = this.register(new Setting<Boolean>("TrapPlayer", true));
    public Setting<Boolean> antiWeakness = this.register(new Setting<Boolean>("AntiWeakness", false));
    public Setting<Double> enemyRange = this.register(new Setting<Double>("Range", 5.9, 0.0, 6.0));
    public Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("BlocksPerTick", 4, 0, 20));
    public Setting<Integer> startDelay = this.register(new Setting<Integer>("StartDelay", 4, 0, 20));
    public Setting<Integer> trapDelay = this.register(new Setting<Integer>("TrapDelay", 4, 0, 20));
    public Setting<Integer> pistonDelay = this.register(new Setting<Integer>("PistonDelay", 2, 0, 20));
    public Setting<Integer> crystalDelay = this.register(new Setting<Integer>("CrystalDelay", 2, 0, 20));
    public Setting<Integer> hitDelay = this.register(new Setting<Integer>("HitDelay", 2, 0, 20));
    public Setting<BreakModes> breakMode = this.register(new Setting<BreakModes>("Break Mode", BreakModes.swing));
    private boolean isSneaking = false;
    private boolean firstRun = false;
    private boolean noMaterials = false;
    private boolean hasMoved = false;
    private boolean isHole = true;
    private boolean enoughSpace = true;
    private int oldSlot = -1;
    private int[] slot_mat;
    private int[] delayTable;
    private int stage;
    private int delayTimeTicks;
    private structureTemp toPlace;
    int[][] disp_surblock = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
    Double[][] sur_block;
    private int stuck = 0;
    boolean broken;
    boolean brokenCrystalBug;
    boolean brokenRedstoneTorch;
    public static ModuleManager moduleManager;
    private static PistonCrystal instance;
    private EntityPlayer closestTarget;
    double[] coordsD;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    public PistonCrystal() {
        super("PistonCrystal", "Use Pistons and Crystals to pvp.", Category.COMBAT, true, false, false);
        instance = this;
    }

    public static PistonCrystal getInstance() {
        if (instance == null) {
            instance = new PistonCrystal();
        }
        return instance;
    }

    @Override
    public void onEnable() {
        this.coordsD = new double[3];
        this.delayTable = new int[]{this.startDelay.getValue(), this.trapDelay.getValue(), this.pistonDelay.getValue(), this.crystalDelay.getValue(), this.hitDelay.getValue()};
        this.toPlace = new structureTemp(0.0, 0, null);
        boolean b = true;
        this.firstRun = true;
        this.isHole = true;
        boolean b2 = false;
        this.brokenRedstoneTorch = false;
        this.brokenCrystalBug = false;
        this.broken = false;
        this.hasMoved = false;
        this.slot_mat = new int[]{-1, -1, -1, -1, -1};
        boolean stage = false;
        this.stuck = 0;
        this.delayTimeTicks = 0;
        this.stage = 0;
        if (mc.player == null) {
            this.disable();
            return;
        }
        this.oldSlot = mc.player.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return;
        }
        if (this.isSneaking) {
            mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        if (this.oldSlot != mc.player.inventory.currentItem && this.oldSlot != -1) {
            mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
        this.noMaterials = false;
        this.firstRun = true;
    }

    @Override
    public void onUpdate() {
        if (mc.player == null) {
            this.disable();
            return;
        }
        if (this.firstRun) {
            this.closestTarget = EntityUtil.getClosestEnemy(this.enemyRange.getValue());
            if (this.closestTarget == null) {
                return;
            }
            this.firstRun = false;
            if (this.getMaterialsSlot()) {
                if (this.is_in_hole()) {
                    this.enoughSpace = this.createStructure();
                } else {
                    this.isHole = false;
                }
            } else {
                this.noMaterials = true;
            }
        } else {
            if (this.delayTable == null) {
                return;
            }
            if (this.delayTimeTicks < this.delayTable[this.stage]) {
                ++this.delayTimeTicks;
                return;
            }
            this.delayTimeTicks = 0;
        }
        if (this.noMaterials || !this.isHole || !this.enoughSpace || this.hasMoved) {
            this.disable();
            return;
        }
        if (this.trapPlayer()) {
            if (this.stage == 1) {
                BlockPos targetPos = this.compactBlockPos(this.stage);
                this.placeBlock(targetPos, this.stage, this.toPlace.offsetX, this.toPlace.offsetZ);
                ++this.stage;
            } else if (this.stage == 2) {
                BlockPos targetPosPiston = this.compactBlockPos(this.stage - 1);
                if (!(this.get_block(targetPosPiston.getX(), targetPosPiston.getY(), targetPosPiston.getZ()) instanceof BlockPistonBase)) {
                    --this.stage;
                } else {
                    BlockPos targetPos2 = this.compactBlockPos(this.stage);
                    if (this.placeBlock(targetPos2, this.stage, this.toPlace.offsetX, this.toPlace.offsetZ)) {
                        ++this.stage;
                    }
                }
            } else if (this.stage == 3) {
                for (Entity t : mc.world.loadedEntityList) {
                    if (!(t instanceof EntityEnderCrystal) || (int)t.posX != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x || (int)t.posZ != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z) continue;
                    --this.stage;
                    break;
                }
                if (this.stage == 3) {
                    BlockPos targetPos = this.compactBlockPos(this.stage);
                    this.placeBlock(targetPos, this.stage, this.toPlace.offsetX, this.toPlace.offsetZ);
                    ++this.stage;
                }
            } else if (this.stage == 4) {
                this.breakCrystal();
            }
        }
    }

    public void breakCrystal() {
        Object crystal = null;
        for (Object t : mc.world.loadedEntityList) {
            if (!(t instanceof EntityEnderCrystal) || (((Entity)t).posX != (double)((int)((Entity)t).posX) && (int)((Entity)t).posX != (int)this.closestTarget.posX || (int)((double)((int)((Entity)t).posX) - 0.1) != (int)this.closestTarget.posX && (int)((double)((int)((Entity)t).posX) + 0.1) != (int)this.closestTarget.posX || (int)((Entity)t).posZ != (int)this.closestTarget.posZ) && (((Entity)t).posZ != (double)((int)((Entity)t).posZ) && (int)((Entity)t).posZ != (int)this.closestTarget.posZ || (int)((double)((int)((Entity)t).posZ) - 0.1) != (int)this.closestTarget.posZ && (int)((double)((int)((Entity)t).posZ) + 0.1) != (int)this.closestTarget.posZ || (int)((Entity)t).posX != (int)this.closestTarget.posX)) continue;
            crystal = t;
        }
        if (this.broken && crystal == null) {
            boolean n = false;
            this.stuck = 0;
            this.stage = 0;
            this.broken = false;
        }
        if (crystal != null) {
            this.breakCrystalPiston((Entity)crystal);
            this.broken = true;
        } else if (++this.stuck >= 35) {
            boolean found = false;
            for (Entity t2 : mc.world.loadedEntityList) {
                if (!(t2 instanceof EntityEnderCrystal) || (int)t2.posX != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x || (int)t2.posZ != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z) continue;
                found = true;
                break;
            }
            if (!found) {
                BlockPos offsetPosPist = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + 2));
                BlockPos pos = new BlockPos(this.closestTarget.getPositionVector()).add(offsetPosPist.getX(), offsetPosPist.getY(), offsetPosPist.getZ());
                if (this.brokenRedstoneTorch && this.get_block(pos.getX(), pos.getY(), pos.getZ()) instanceof BlockAir) {
                    this.stage = 1;
                    this.brokenRedstoneTorch = false;
                } else {
                    EnumFacing side = BlockUtil.getPlaceableSide(pos);
                    if (side != null) {
                        if (this.rotate.getValue()) {
                            BlockPos neighbour = pos.offset(side);
                            EnumFacing opposite = side.getOpposite();
                            Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 1.0, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
                            RotationUtil.faceVector(hitVec, false);
                        }
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, side));
                        mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
                        this.brokenRedstoneTorch = true;
                    }
                }
            } else {
                boolean ext = false;
                for (Entity t3 : mc.world.loadedEntityList) {
                    if (!(t3 instanceof EntityEnderCrystal) || (int)t3.posX != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x || (int)t3.posZ != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z) continue;
                    ext = true;
                    break;
                }
                boolean n3 = false;
                this.stuck = 0;
                this.stage = 0;
                this.brokenCrystalBug = false;
                if (ext) {
                    this.breakCrystalPiston(null);
                    this.brokenCrystalBug = true;
                }
            }
        }
    }

    public BlockPos compactBlockPos(int step) {
        BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + step - 1));
        return new BlockPos(this.closestTarget.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
    }

    private void breakCrystalPiston(Entity crystal) {
        if (this.antiWeakness.getValue()) {
            mc.player.inventory.currentItem = this.slot_mat[4];
        }
        if (this.rotate.getValue()) {
            this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)mc.player);
        }
        if (this.breakMode.getValue().equals((Object)BreakModes.swing)) {
            this.breakCrystal(crystal);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        } else if (this.breakMode.getValue().equals(BreakModes.packet)) {
            mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        if (this.rotate.getValue()) {
            resetRotation();
        }
    }

    private boolean trapPlayer() {
        int i = 0;
        int blockPlaced = 0;
        if (this.toPlace.to_place.size() <= 0 || this.toPlace.supportBlock <= 0) {
            this.stage = this.stage == 0 ? 1 : this.stage;
            return true;
        }
        do {
            BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(i));
            BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
            if (this.placeBlock(targetPos, 0, 0.0, 0.0)) {
                ++blockPlaced;
            }
            if (blockPlaced != this.blocksPerTick.getValue()) continue;
            return false;
        } while (++i < this.toPlace.supportBlock);
        this.stage = this.stage == 0 ? 1 : this.stage;
        return true;
    }

    private boolean placeBlock(BlockPos pos, int step, double offsetX, double offsetZ) {
        Block block = mc.world.getBlockState(pos).getBlock();
        EnumFacing side = BlockUtil.getPlaceableSide(pos);
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5 + offsetX, 1.0, 0.5 + offsetZ).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
        if (mc.player.inventory.getStackInSlot(this.slot_mat[step]) != ItemStack.EMPTY) {
            if (mc.player.inventory.currentItem != this.slot_mat[step]) {
                int n = mc.player.inventory.currentItem = this.slot_mat[step] == 11 ? mc.player.inventory.currentItem : this.slot_mat[step];
            }
            if (!this.isSneaking && BlockUtil.blackList.contains((Object)neighbourBlock) || BlockUtil.shulkerList.contains((Object)neighbourBlock)) {
                mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
                this.isSneaking = true;
            }
            if (this.rotate.getValue() || step == 1) {
                Vec3d positionHit = hitVec;
                if (!this.rotate.getValue() && step == 1) {
                    positionHit = new Vec3d(mc.player.posX + offsetX, mc.player.posY, mc.player.posZ + offsetZ);
                }
                RotationUtil.faceVector(positionHit, false);
            }
            EnumHand handSwing = EnumHand.MAIN_HAND;
            if (this.slot_mat[step] == 11) {
                handSwing = EnumHand.OFF_HAND;
            }
            mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, handSwing);
            mc.player.swingArm(handSwing);
            return true;
        }
        return false;
    }

    private boolean getMaterialsSlot() {
        if (mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
            this.slot_mat[2] = 11;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (stack.getItem() instanceof ItemEndCrystal) {
                this.slot_mat[2] = i;
                continue;
            }
            if (this.antiWeakness.getValue() && stack.getItem() instanceof ItemSword) {
                this.slot_mat[4] = i;
                continue;
            }
            if (!(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                this.slot_mat[0] = i;
                continue;
            }
            if (block instanceof BlockPistonBase) {
                this.slot_mat[1] = i;
                continue;
            }
            if (!(block instanceof BlockRedstoneTorch) && block != Blocks.REDSTONE_BLOCK) continue;
            this.slot_mat[3] = i;
        }
        int count = 0;
        for (int val : this.slot_mat) {
            if (val == -1) continue;
            ++count;
        }
        return count == 4 + (this.antiWeakness.getValue() != false ? 1 : 0);
    }

    private boolean is_in_hole() {
        this.sur_block = new Double[][]{{this.closestTarget.posX + 1.0, this.closestTarget.posY, this.closestTarget.posZ}, {this.closestTarget.posX - 1.0, this.closestTarget.posY, this.closestTarget.posZ}, {this.closestTarget.posX, this.closestTarget.posY, this.closestTarget.posZ + 1.0}, {this.closestTarget.posX, this.closestTarget.posY, this.closestTarget.posZ - 1.0}};
        return !(this.get_block(this.sur_block[0][0], this.sur_block[0][1], this.sur_block[0][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[1][0], this.sur_block[1][1], this.sur_block[1][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[2][0], this.sur_block[2][1], this.sur_block[2][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[3][0], this.sur_block[3][1], this.sur_block[3][2]) instanceof BlockAir);
    }

    private boolean createStructure() {
        structureTemp addedStructure = new structureTemp(Double.MAX_VALUE, 0, null);
        int i = 0;
        int[] arrn = new int[]{(int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ};
        int[] meCord = arrn;
        if ((double)meCord[1] - this.closestTarget.posY > -1.0) {
            for (Double[] cord_b : this.sur_block) {
                double d = 0;
                double[] crystalCords = new double[]{cord_b[0], cord_b[1] + 1.0, cord_b[2]};
                BlockPos positionCrystal = new BlockPos(crystalCords[0], crystalCords[1], crystalCords[2]);
                double distance_now = mc.player.getDistance(crystalCords[0], crystalCords[1], crystalCords[2]);
                if (d < addedStructure.distance && (positionCrystal.getY() != meCord[1] || meCord[0] != positionCrystal.getX() || Math.abs(meCord[2] - positionCrystal.getZ()) > 3 && meCord[2] != positionCrystal.getZ() || Math.abs(meCord[0] - positionCrystal.getX()) > 3)) {
                    cord_b[1] = cord_b[1] + 1.0;
                    if (this.get_block(crystalCords[0], crystalCords[1], crystalCords[2]) instanceof BlockAir) {
                        double[] pistonCord = new double[]{crystalCords[0] + (double)this.disp_surblock[i][0], crystalCords[1], crystalCords[2] + (double)this.disp_surblock[i][2]};
                        Block blockPiston = this.get_block(pistonCord[0], pistonCord[1], pistonCord[2]);
                        if ((blockPiston instanceof BlockAir || blockPiston instanceof BlockPistonBase) && this.someoneInCoords(pistonCord[0], pistonCord[1], pistonCord[2])) {
                            boolean join;
                            boolean b = false;
                            if (!this.rotate.getValue() || ((int)pistonCord[0] == meCord[0] ? this.closestTarget.posZ > mc.player.posZ != this.closestTarget.posZ > pistonCord[2] || Math.abs((int)this.closestTarget.posZ - (int)mc.player.posZ) == 1 : (int)pistonCord[2] != meCord[2] || (this.closestTarget.posX > mc.player.posX != this.closestTarget.posX > pistonCord[0] || Math.abs((int)this.closestTarget.posX - (int)mc.player.posX) == 1) && (Math.abs((int)this.closestTarget.posX - (int)mc.player.posX) <= 1 || pistonCord[0] > this.closestTarget.posX != (double)meCord[0] > this.closestTarget.posX))) {
                                b = true;
                            }
                            if (join = b) {
                                boolean enter;
                                boolean b2 = false;
                                if (!this.rotate.getValue() || (meCord[0] == (int)this.closestTarget.posX || meCord[2] == (int)this.closestTarget.posZ ? mc.player.getDistance(crystalCords[0], crystalCords[1], crystalCords[2]) <= 3.5 || meCord[0] == (int)crystalCords[0] || meCord[2] == (int)crystalCords[2] : meCord[0] != (int)pistonCord[0] || Math.abs((int)this.closestTarget.posZ - (int)mc.player.posZ) == 1 || meCord[2] == (int)pistonCord[2] && Math.abs((int)this.closestTarget.posZ - (int)mc.player.posZ) != 1)) {
                                    b2 = true;
                                }
                                if (enter = b2) {
                                    int[] poss = null;
                                    for (int[] possibilites : this.disp_surblock) {
                                        double[] coordinatesTemp = new double[]{cord_b[0] + (double)this.disp_surblock[i][0] + (double)possibilites[0], cord_b[1], cord_b[2] + (double)this.disp_surblock[i][2] + (double)possibilites[2]};
                                        int[] torchCoords = new int[]{(int)coordinatesTemp[0], (int)coordinatesTemp[1], (int)coordinatesTemp[2]};
                                        int[] crystalCoords = new int[]{(int)crystalCords[0], (int)crystalCords[1], (int)crystalCords[2]};
                                        if (!(this.get_block(coordinatesTemp[0], coordinatesTemp[1], coordinatesTemp[2]) instanceof BlockAir) || torchCoords[0] == crystalCoords[0] && torchCoords[1] == crystalCoords[1] && crystalCoords[2] == torchCoords[2] || !this.someoneInCoords(coordinatesTemp[0], coordinatesTemp[1], coordinatesTemp[2])) continue;
                                        poss = possibilites;
                                        break;
                                    }
                                    if (poss != null) {
                                        float offsetZ;
                                        float offsetX;
                                        ArrayList<Vec3d> toPlaceTemp = new ArrayList<Vec3d>();
                                        int supportBlock = 0;
                                        if (this.get_block(cord_b[0] + (double)this.disp_surblock[i][0], cord_b[1] - 1.0, cord_b[2] + (double)this.disp_surblock[i][2]) instanceof BlockAir) {
                                            toPlaceTemp.add(new Vec3d((double)(this.disp_surblock[i][0] * 2), (double)this.disp_surblock[i][1], (double)(this.disp_surblock[i][2] * 2)));
                                            ++supportBlock;
                                        }
                                        if (this.get_block(cord_b[0] + (double)this.disp_surblock[i][0] + (double)poss[0], cord_b[1] - 1.0, cord_b[2] + (double)this.disp_surblock[i][2] + (double)poss[2]) instanceof BlockAir) {
                                            toPlaceTemp.add(new Vec3d((double)(this.disp_surblock[i][0] * 2 + poss[0]), (double)this.disp_surblock[i][1], (double)(this.disp_surblock[i][2] * 2 + poss[2])));
                                            ++supportBlock;
                                        }
                                        toPlaceTemp.add(new Vec3d((double)(this.disp_surblock[i][0] * 2), (double)(this.disp_surblock[i][1] + 1), (double)(this.disp_surblock[i][2] * 2)));
                                        toPlaceTemp.add(new Vec3d((double)this.disp_surblock[i][0], (double)(this.disp_surblock[i][1] + 1), (double)this.disp_surblock[i][2]));
                                        toPlaceTemp.add(new Vec3d((double)(this.disp_surblock[i][0] * 2 + poss[0]), (double)(this.disp_surblock[i][1] + 1), (double)(this.disp_surblock[i][2] * 2 + poss[2])));
                                        if (this.disp_surblock[i][0] != 0) {
                                            float f = offsetX = this.rotate.getValue() != false ? (float)this.disp_surblock[i][0] / 2.0f : (float)this.disp_surblock[i][0];
                                            offsetZ = this.rotate.getValue() ? (mc.player.getDistanceSq(pistonCord[0], pistonCord[1], pistonCord[2] + 0.5) > mc.player.getDistanceSq(pistonCord[0], pistonCord[1], pistonCord[2] - 0.5) ? -0.5f : 0.5f) : (float)this.disp_surblock[i][2];
                                        } else {
                                            float f = offsetZ = this.rotate.getValue() != false ? (float)this.disp_surblock[i][2] / 2.0f : (float)this.disp_surblock[i][2];
                                            offsetX = this.rotate.getValue() ? (mc.player.getDistanceSq(pistonCord[0] + 0.5, pistonCord[1], pistonCord[2]) > mc.player.getDistanceSq(pistonCord[0] - 0.5, pistonCord[1], pistonCord[2]) ? -0.5f : 0.5f) : (float)this.disp_surblock[i][0];
                                        }
                                        addedStructure.replaceValues(distance_now, supportBlock, toPlaceTemp, -1, offsetX, offsetZ);
                                    }
                                }
                            }
                        }
                    }
                }
                ++i;
            }
            if (addedStructure.to_place != null) {
                if (this.blockPlayer.getValue()) {
                    Vec3d valuesStart = addedStructure.to_place.get(addedStructure.supportBlock + 1);
                    int[] valueBegin = new int[]{(int)(-valuesStart.x), (int)valuesStart.y, (int)(-valuesStart.z)};
                    addedStructure.to_place.add(0, new Vec3d(0.0, 2.0, 0.0));
                    addedStructure.to_place.add(0, new Vec3d((double)valueBegin[0], (double)(valueBegin[1] + 1), (double)valueBegin[2]));
                    addedStructure.to_place.add(0, new Vec3d((double)valueBegin[0], (double)valueBegin[1], (double)valueBegin[2]));
                    addedStructure.supportBlock += 3;
                }
                this.toPlace = addedStructure;
                return true;
            }
        }
        return false;
    }

    private boolean someoneInCoords(double x, double y, double z) {
        int xCheck = (int)x;
        int yCheck = (int)y;
        int zCheck = (int)z;
        for (EntityPlayer player : mc.world.playerEntities) {
            if ((int)player.posX != xCheck || (int)player.posZ != zCheck || (int)player.posY < yCheck - 1 || (int)player.posY > yCheck + 1) continue;
            return false;
        }
        return true;
    }

    private Block get_block(double x, double y, double z) {
        return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        double yaw = Math.atan2(dirz /= len, dirx /= len);
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;
        return new double[]{yaw += 90.0, pitch};
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private void breakCrystal(Entity crystal) {
        mc.playerController.attackEntity((EntityPlayer)mc.player, crystal);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        Object packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)yaw;
            ((CPacketPlayer)packet).pitch = (float)pitch;
        }
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    private static enum BreakModes {
        packet,
        swing;
    }

    static class structureTemp {
        public double distance;
        public int supportBlock;
        public List<Vec3d> to_place;
        public int direction;
        public float offsetX;
        public float offsetZ;

        public structureTemp(double distance, int supportBlock, List<Vec3d> to_place) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = -1;
        }

        public void replaceValues(double distance, int supportBlock, List<Vec3d> to_place, int direction, float offsetX, float offsetZ) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = direction;
            this.offsetX = offsetX;
            this.offsetZ = offsetZ;
        }
    }
}
