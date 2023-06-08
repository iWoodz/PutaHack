package dev.putahack.mixin.entity;

import com.mojang.authlib.GameProfile;
import dev.putahack.PutaHack;
import dev.putahack.listener.event.EventStage;
import dev.putahack.listener.event.player.EventItemSlowdown;
import dev.putahack.listener.event.player.EventUpdate;
import dev.putahack.listener.event.player.EventWalkingUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.spongepowered.asm.lib.Opcodes.PUTFIELD;

/**
 * @author aesthetical
 * @since 06/04/23
 */
@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow @Final public NetHandlerPlayClient connection;

    @Shadow private boolean serverSprintState;
    @Shadow private boolean serverSneakState;

    @Shadow private double lastReportedPosX;
    @Shadow private double lastReportedPosY;
    @Shadow private double lastReportedPosZ;

    @Shadow private float lastReportedYaw;
    @Shadow private float lastReportedPitch;

    @Shadow private int positionUpdateTicks;

    @Shadow private boolean prevOnGround;
    @Shadow private boolean autoJumpEnabled;

    @Shadow public MovementInput movementInput;

    @Shadow protected Minecraft mc;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "onUpdate", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/AbstractClientPlayer;onUpdate()V",
            shift = Shift.AFTER))
    public void hookOnUpdate(CallbackInfo info) {
        PutaHack.getBus().dispatch(new EventUpdate());
    }

    @Inject(method = "onLivingUpdate", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;sprintToggleTimer:I",
            ordinal = 1,
            opcode = PUTFIELD,
            shift = Shift.AFTER))
    public void hookOnLivingUpdate$sprintToggleTimer(CallbackInfo info) {
        PutaHack.getBus().dispatch(new EventItemSlowdown(movementInput));
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void hookOnUpdateWalkingPlayer$Head(CallbackInfo info) {
        EventWalkingUpdate event = new EventWalkingUpdate(EventStage.PRE,
                posX, getEntityBoundingBox().minY, posZ, rotationYaw, rotationPitch, onGround);
        PutaHack.getBus().dispatch(event);
        if (!event.isCanceled()) return;

        // do not send vanilla packets
        info.cancel();

        boolean sprinting = this.isSprinting();
        if (sprinting != serverSprintState) {
            connection.sendPacket(new CPacketEntityAction(this,
                    sprinting ? Action.START_SPRINTING : Action.STOP_SPRINTING));
            serverSprintState = sprinting;
        }

        boolean sneaking = this.isSneaking();
        if (sneaking != serverSneakState) {
            connection.sendPacket(new CPacketEntityAction(this,
                    sneaking ? Action.START_SNEAKING : Action.STOP_SNEAKING));
            serverSneakState = sneaking;
        }

        if (mc.getRenderViewEntity() == (EntityPlayerSP) (Object) this) {
            double d0 = event.getX() - lastReportedPosX;
            double d1 = event.getY() - lastReportedPosY;
            double d2 = event.getZ() - lastReportedPosZ;
            double d3 = event.getYaw() - lastReportedYaw;
            double d4 = event.getPitch() - lastReportedPitch;

            ++positionUpdateTicks;

            boolean moved = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || positionUpdateTicks >= 20;
            boolean rotated = d3 != 0.0D || d4 != 0.0D;

            if (isRiding()) {
                connection.sendPacket(new PositionRotation(motionX, -999.0, motionZ,
                        event.getYaw(), event.getPitch(), event.isOnGround()));
                moved = false;
            } else if (moved && rotated) {
                connection.sendPacket(new PositionRotation(event.getX(), event.getY(), event.getZ(),
                        event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (moved) {
                connection.sendPacket(new Position(
                        event.getX(), event.getY(), event.getZ(), event.isOnGround()));
            } else if (rotated) {
                connection.sendPacket(new Rotation(
                        event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (prevOnGround != event.isOnGround()) {
                connection.sendPacket(new CPacketPlayer(event.isOnGround()));
            }

            if (moved) {
                lastReportedPosX = event.getX();
                lastReportedPosY = event.getY();
                lastReportedPosZ = event.getZ();
                positionUpdateTicks = 0;
            }

            if (rotated) {
                lastReportedYaw = event.getYaw();
                lastReportedPitch = event.getPitch();
            }

            prevOnGround = onGround;
            autoJumpEnabled = mc.gameSettings.autoJump;

            PutaHack.getBus().dispatch(new EventWalkingUpdate(EventStage.POST,
                    posX, getEntityBoundingBox().minY, posZ, rotationYaw, rotationPitch, onGround));
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("TAIL"))
    public void hookOnUpdateWalkingPlayer$Tail(CallbackInfo info) {
        PutaHack.getBus().dispatch(new EventWalkingUpdate(EventStage.POST,
                posX, getEntityBoundingBox().minY, posZ, rotationYaw, rotationPitch, onGround));
    }
}
