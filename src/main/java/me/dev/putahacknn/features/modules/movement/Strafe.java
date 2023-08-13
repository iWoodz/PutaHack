package me.dev.putahacknn.features.modules.movement;

import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.MoveEvent;
import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.event.events.UpdateWalkingPlayerEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.MovementUtil;
import me.dev.putahacknn.util.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Strafe extends Module
{
    private static Strafe INSTANCE;
    private final Setting<Mode> mode;
    private final Setting<Boolean> useTimer;
    private final Setting<Float> timerFactor;
    private final Setting<Boolean> boost;
    private final Setting<Boolean> hypixel;
    private final Setting<Boolean> strict;
    private final Setting<Boolean> disableOnSneak;
    private int strafeStage;
    public int hopStage;
    private double horizontal;
    private double currentSpeed;
    private double prevMotion;
    private boolean oddStage;
    private int state;
    private double aacSpeed;
    private int aacCounter;
    private int aacState;
    private int ticksPassed;
    private double maxVelocity;
    private final Timer velocityTimer;
    private final Timer setbackTimer;
    private int lowHopStage;
    private double lowHopSpeed;
    private boolean even;
    private boolean forceGround;

    public Strafe() {
        super("StrafeRewrite", "vrooooom", Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.STRAFE));
        this.useTimer = (Setting<Boolean>)this.register(new Setting("UseTimer", true));
        this.timerFactor = (Setting<Float>)this.register(new Setting("Factor", 1.0f, 0.1f, 10.0f));
        this.boost = (Setting<Boolean>)this.register(new Setting("Boost", false, v -> this.mode.getValue() == Mode.STRAFE || this.mode.getValue() == Mode.STRAFESTRICT));
        this.hypixel = (Setting<Boolean>)this.register(new Setting("Hypixel", false));
        this.strict = (Setting<Boolean>)this.register(new Setting("Strict", false, v -> this.mode.getValue() == Mode.STRAFE));
        this.disableOnSneak = (Setting<Boolean>)this.register(new Setting("DisableOnSneak", false));
        this.strafeStage = 1;
        this.currentSpeed = 0.0;
        this.prevMotion = 0.0;
        this.oddStage = false;
        this.state = 4;
        this.aacSpeed = 0.2873;
        this.aacState = 4;
        this.ticksPassed = 0;
        this.maxVelocity = 0.0;
        this.velocityTimer = new Timer();
        this.setbackTimer = new Timer();
        Strafe.INSTANCE = this;
    }

    public static Strafe getInstance() {
        if (Strafe.INSTANCE == null) {
            Strafe.INSTANCE = new Strafe();
        }
        return Strafe.INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (Strafe.mc.player == null || Strafe.mc.world == null) {
            return;
        }
        if (this.disableOnSneak.getValue() && Strafe.mc.player.isSneaking()) {
            return;
        }
        if (this.mode.getValue() == Mode.STRAFE || (this.mode.getValue() == Mode.LOWHOP && this.useTimer.getValue())) {
            PutaHacknn.timerManager.setTimer(1.08f + 0.008f * this.timerFactor.getValue());
        }
        else if (this.mode.getValue() != Mode.STRAFESTRICT) {
            PutaHacknn.timerManager.reset();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() == Mode.STRAFE || this.mode.getValue() == Mode.STRAFESTRICT || this.mode.getValue() == Mode.LOWHOP) {
            final double dX = Strafe.mc.player.posX - Strafe.mc.player.prevPosX;
            final double dZ = Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ;
            this.prevMotion = Math.sqrt(dX * dX + dZ * dZ);
        }
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && this.forceGround) {
            this.forceGround = false;
            ((CPacketPlayer) event.getPacket()).onGround = true;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            PutaHacknn.timerManager.reset();
            this.currentSpeed = 0.0;
            this.state = 4;
            this.aacSpeed = 0.2873;
            this.aacState = 4;
            this.prevMotion = 0.0;
            this.aacCounter = 0;
            this.maxVelocity = 0.0;
            this.setbackTimer.reset();
            this.lowHopStage = 4;
        }
        else if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion velocity = event.getPacket();
            this.maxVelocity = Math.sqrt(velocity.getMotionX() * velocity.getMotionX() + velocity.getMotionZ() * velocity.getMotionZ());
            this.velocityTimer.reset();
        }
    }

    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (Strafe.mc.player == null || Strafe.mc.world == null) {
            return;
        }
        if (this.disableOnSneak.getValue() && Strafe.mc.player.isSneaking()) {
            return;
        }
        switch (this.mode.getValue()) {
            case STRAFE: {
                if (this.state != 1 || Strafe.mc.player.moveForward == 0.0f || Strafe.mc.player.moveStrafing == 0.0f) {
                    if (this.state == 2 && (Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f)) {
                        double jumpSpeed = 0.0;
                        if (Strafe.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                            jumpSpeed += (Strafe.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                        }
                        event.setY(Strafe.mc.player.motionY = (this.hypixel.getValue() ? 0.3999999463558197 : 0.3999) + jumpSpeed);
                        this.currentSpeed *= (this.oddStage ? 1.6835 : 1.395);
                    }
                    else if (this.state == 3) {
                        final double adjustedMotion = 0.66 * (this.prevMotion - this.getBaseMotionSpeed());
                        this.currentSpeed = this.prevMotion - adjustedMotion;
                        this.oddStage = !this.oddStage;
                    }
                    else {
                        final List<AxisAlignedBB> collisionBoxes = (List<AxisAlignedBB>) Strafe.mc.world.getCollisionBoxes(Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0));
                        if ((collisionBoxes.size() > 0 || Strafe.mc.player.collidedVertically) && this.state > 0) {
                            this.state = ((Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f) ? 1 : 0);
                        }
                        this.currentSpeed = this.prevMotion - this.prevMotion / 159.0;
                    }
                }
                else {
                    this.currentSpeed = 1.35 * this.getBaseMotionSpeed() - 0.01;
                }
                this.currentSpeed = Math.max(this.currentSpeed, this.getBaseMotionSpeed());
                if (this.maxVelocity > 0.0 && this.boost.getValue() && !this.velocityTimer.hasPassed(75.0) && !Strafe.mc.player.collidedHorizontally) {
                    this.currentSpeed = Math.max(this.currentSpeed, this.maxVelocity);
                }
                else if (this.strict.getValue()) {
                    this.currentSpeed = Math.min(this.currentSpeed, 0.433);
                }
                double forward = Strafe.mc.player.movementInput.moveForward;
                double strafe = Strafe.mc.player.movementInput.moveStrafe;
                float yaw = Strafe.mc.player.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                }
                else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            yaw += ((forward > 0.0) ? -45 : 45);
                        }
                        else if (strafe < 0.0) {
                            yaw += ((forward > 0.0) ? 45 : -45);
                        }
                        strafe = 0.0;
                        if (forward > 0.0) {
                            forward = 1.0;
                        }
                        else if (forward < 0.0) {
                            forward = -1.0;
                        }
                    }
                    event.setX(forward * this.currentSpeed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * this.currentSpeed * Math.sin(Math.toRadians(yaw + 90.0f)));
                    event.setZ(forward * this.currentSpeed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * this.currentSpeed * Math.cos(Math.toRadians(yaw + 90.0f)));
                }
                if (Strafe.mc.player.moveForward == 0.0f && Strafe.mc.player.moveStrafing == 0.0f) {
                    return;
                }
                ++this.state;
                break;
            }
            case STRAFESTRICT: {
                ++this.aacCounter;
                this.aacCounter %= 5;
                if (this.aacCounter != 0) {
                    PutaHacknn.timerManager.reset();
                }
                else if (MovementUtil.isPlayerMoving()) {
                    PutaHacknn.timerManager.setTimer(1.3f);
                    final EntityPlayerSP player = Strafe.mc.player;
                    player.motionX *= 1.0199999809265137;
                    final EntityPlayerSP player2 = Strafe.mc.player;
                    player2.motionZ *= 1.0199999809265137;
                }
                if (Strafe.mc.player.onGround && MovementUtil.isPlayerMoving()) {
                    this.aacState = 2;
                }
                if (this.round(Strafe.mc.player.posY - (int) Strafe.mc.player.posY, 3) == this.round(0.138, 3)) {
                    final EntityPlayerSP player3 = Strafe.mc.player;
                    player3.motionY -= 0.08;
                    event.setY(event.getY() - 0.09316090325960147);
                    final EntityPlayerSP player4 = Strafe.mc.player;
                    player4.posY -= 0.09316090325960147;
                }
                if (this.aacState == 1 && (Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f)) {
                    this.aacState = 2;
                    this.aacSpeed = 1.38 * this.getBaseMotionSpeed() - 0.01;
                }
                else if (this.aacState == 2) {
                    this.aacState = 3;
                    event.setY(Strafe.mc.player.motionY = 0.399399995803833);
                    this.aacSpeed *= 2.149;
                }
                else if (this.aacState == 3) {
                    this.aacState = 4;
                    final double adjustedMotion = 0.66 * (this.prevMotion - this.getBaseMotionSpeed());
                    this.aacSpeed = this.prevMotion - adjustedMotion;
                }
                else {
                    if (Strafe.mc.world.getCollisionBoxes((Entity) Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || Strafe.mc.player.collidedVertically) {
                        this.aacState = 1;
                    }
                    this.aacSpeed = this.prevMotion - this.prevMotion / 159.0;
                }
                this.aacSpeed = Math.max(this.aacSpeed, this.getBaseMotionSpeed());
                if (this.maxVelocity > 0.0 && this.boost.getValue() && !this.velocityTimer.hasPassed(75.0) && !Strafe.mc.player.collidedHorizontally) {
                    this.aacSpeed = Math.max(this.aacSpeed, this.maxVelocity);
                }
                else {
                    this.aacSpeed = Math.min(this.aacSpeed, (this.ticksPassed > 25) ? 0.449 : 0.433);
                }
                float forward2 = Strafe.mc.player.movementInput.moveForward;
                float strafe2 = Strafe.mc.player.movementInput.moveStrafe;
                float yaw2 = Strafe.mc.player.rotationYaw;
                ++this.ticksPassed;
                if (this.ticksPassed > 50) {
                    this.ticksPassed = 0;
                }
                if (forward2 == 0.0f && strafe2 == 0.0f) {
                    event.setX(0.0);
                    event.setZ(0.0);
                }
                else if (forward2 != 0.0f) {
                    if (strafe2 >= 1.0f) {
                        yaw2 += ((forward2 > 0.0f) ? -45 : 45);
                        strafe2 = 0.0f;
                    }
                    else if (strafe2 <= -1.0f) {
                        yaw2 += ((forward2 > 0.0f) ? 45 : -45);
                        strafe2 = 0.0f;
                    }
                    if (forward2 > 0.0f) {
                        forward2 = 1.0f;
                    }
                    else if (forward2 < 0.0f) {
                        forward2 = -1.0f;
                    }
                }
                final double cos = Math.cos(Math.toRadians(yaw2 + 90.0f));
                final double sin = Math.sin(Math.toRadians(yaw2 + 90.0f));
                event.setX(forward2 * this.aacSpeed * cos + strafe2 * this.aacSpeed * sin);
                event.setZ(forward2 * this.aacSpeed * sin - strafe2 * this.aacSpeed * cos);
                if (forward2 == 0.0f && strafe2 == 0.0f) {
                    event.setX(0.0);
                    event.setZ(0.0);
                    break;
                }
                break;
            }
            case LOWHOP: {
                if (!this.setbackTimer.hasPassed(100.0)) {
                    return;
                }
                double jumpSpeed = 0.0;
                if (Strafe.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    jumpSpeed += (Strafe.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                }
                if (this.round(Strafe.mc.player.posY - (int) Strafe.mc.player.posY, 3) == this.round(0.4, 3)) {
                    event.setY(Strafe.mc.player.motionY = 0.31 + jumpSpeed);
                }
                else if (this.round(Strafe.mc.player.posY - (int) Strafe.mc.player.posY, 3) == this.round(0.71, 3)) {
                    event.setY(Strafe.mc.player.motionY = 0.04 + jumpSpeed);
                }
                else if (this.round(Strafe.mc.player.posY - (int) Strafe.mc.player.posY, 3) == this.round(0.75, 3)) {
                    event.setY(Strafe.mc.player.motionY = -0.2 - jumpSpeed);
                }
                else if (this.round(Strafe.mc.player.posY - (int) Strafe.mc.player.posY, 3) == this.round(0.55, 3)) {
                    event.setY(Strafe.mc.player.motionY = -0.14 + jumpSpeed);
                }
                else if (this.round(Strafe.mc.player.posY - (int) Strafe.mc.player.posY, 3) == this.round(0.41, 3)) {
                    event.setY(Strafe.mc.player.motionY = -0.2 + jumpSpeed);
                }
                if (this.lowHopStage == 1 && (Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f)) {
                    this.lowHopSpeed = 1.35 * this.getBaseMotionSpeed() - 0.01;
                }
                else if (this.lowHopStage == 2 && (Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f)) {
                    event.setY(Strafe.mc.player.motionY = (this.checkHeadspace() ? 0.2 : 0.3999) + jumpSpeed);
                    this.lowHopSpeed *= (this.even ? 1.5685 : 1.3445);
                }
                else if (this.lowHopStage == 3) {
                    final double dV = 0.66 * (this.prevMotion - this.getBaseMotionSpeed());
                    this.lowHopSpeed = this.prevMotion - dV;
                    this.even = !this.even;
                }
                else {
                    if (Strafe.mc.player.onGround && this.lowHopStage > 0) {
                        this.lowHopStage = ((Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f) ? 1 : 0);
                    }
                    this.lowHopSpeed = this.prevMotion - this.prevMotion / 159.0;
                }
                this.lowHopSpeed = Math.max(this.lowHopSpeed, this.getBaseMotionSpeed());
                float forward3 = Strafe.mc.player.movementInput.moveForward;
                float strafe3 = Strafe.mc.player.movementInput.moveStrafe;
                if (forward3 == 0.0f && strafe3 == 0.0f) {
                    event.setX(0.0);
                    event.setZ(0.0);
                }
                else if (forward3 != 0.0 && strafe3 != 0.0) {
                    forward3 *= (float)Math.sin(0.7853981633974483);
                    strafe3 *= (float)Math.cos(0.7853981633974483);
                }
                event.setX(forward3 * this.lowHopSpeed * -Math.sin(Math.toRadians(Strafe.mc.player.rotationYaw)) + strafe3 * this.lowHopSpeed * Math.cos(Math.toRadians(Strafe.mc.player.rotationYaw)));
                event.setZ(forward3 * this.lowHopSpeed * Math.cos(Math.toRadians(Strafe.mc.player.rotationYaw)) - strafe3 * this.lowHopSpeed * -Math.sin(Math.toRadians(Strafe.mc.player.rotationYaw)));
                if (Strafe.mc.player.moveForward == 0.0f && Strafe.mc.player.moveStrafing == 0.0f) {
                    return;
                }
                ++this.lowHopStage;
                break;
            }
        }
    }

    private boolean checkHeadspace() {
        return Strafe.mc.world.getCollisionBoxes((Entity) Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, 0.21, 0.0)).size() > 0;
    }

    @Override
    public void onEnable() {
        if (Strafe.mc.player == null || Strafe.mc.world == null) {
            this.toggle();
            return;
        }
        this.maxVelocity = 0.0;
        this.hopStage = 1;
        this.lowHopStage = 4;
        if (this.mode.getValue() == Mode.STRAFE) {
            this.state = 4;
            this.currentSpeed = this.getBaseMotionSpeed();
            this.prevMotion = 0.0;
        }
    }

    @Override
    public void onDisable() {
        if (Strafe.mc.player == null || Strafe.mc.world == null) {
            return;
        }
        PutaHacknn.timerManager.reset();
    }

    private double getBaseMotionSpeed() {
        double baseSpeed = (this.mode.getValue() == Mode.STRAFE || this.mode.getValue() == Mode.STRAFESTRICT || this.mode.getValue() == Mode.LOWHOP) ? 0.2873 : 0.272;
        if (Strafe.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int amplifier = Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1.0);
        }
        return baseSpeed;
    }

    private double round(final double value, final int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() != Mode.NONE) {
            return this.mode.currentEnumName();
        }
        return null;
    }

    static {
        Strafe.INSTANCE = new Strafe();
    }

    public enum Mode
    {
        NONE,
        STRAFE,
        STRAFESTRICT,
        LOWHOP;
    }
}
