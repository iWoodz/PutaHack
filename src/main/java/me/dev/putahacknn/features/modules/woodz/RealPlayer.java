package me.dev.putahacknn.features.modules.woodz;

import com.mojang.authlib.GameProfile;
import me.dev.putahacknn.event.events.ClientEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class RealPlayer extends Module {

    public RealPlayer() {
        super("RealPlayer", "even dbear will be confused", Category.WOODZ, true, false, false);
    }

    private final Setting<Boolean> record = this.register(new Setting<>("Record", false));
    private final Setting<Boolean> play = this.register(new Setting<>("Play", false));
    public final Setting<Boolean> loop = this.register(new Setting("Loop", true));
    private final Queue<Location> recordedPositions = new LinkedList<>();
    private EntityOtherPlayerMP fake;

    @Override
    public void onUpdate() {
        if (record.getValue()) {
            Location location = new Location(mc.player.posX, mc.player.posY, mc.player.posZ);
            location.moveForward = mc.player.moveForward;
            location.moveStrafe = mc.player.moveStrafing;
            location.moveVertical = mc.player.moveVertical;
            location.yaw = mc.player.rotationYaw;
            location.pitch = mc.player.rotationPitch;
            recordedPositions.add(location);
        }

        if (fake == null)
            return;

        fake.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));

        if (play.getValue() && fake != null) {
            Location loc = recordedPositions.poll();
            if (loc != null) {
                fake.setLocationAndAngles(loc.posX, loc.posY, loc.posZ, loc.yaw, loc.pitch);
                fake.rotationYawHead = loc.yaw;
                travel(fake, loc.moveStrafe, loc.moveVertical, loc.moveForward);
            } else if (!loop.getValue()) {
                play.setValue(false);
            }
        }
    }

    public void travel(EntityLivingBase entity, float strafe, float vertical, float forward) {
        double d0 = entity.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = (float) EnchantmentHelper.getDepthStriderModifier(entity);

        if (f3 > 3.0F) {
            f3 = 3.0F;
        }

        if (!entity.onGround) {
            f3 *= 0.5F;
        }

        if (f3 > 0.0F) {
            f1 += (0.54600006F - f1) * f3 / 3.0F;
            f2 += (entity.getAIMoveSpeed() - f2) * f3 / 3.0F;
        }

        entity.moveRelative(strafe, vertical, forward, f2);
        entity.move(MoverType.SELF, entity.motionX, entity.motionY, entity.motionZ);
        entity.motionX *= (double) f1;
        entity.motionY *= 0.800000011920929D;
        entity.motionZ *= (double) f1;

        if (!entity.hasNoGravity()) {
            entity.motionY -= 0.02D;
        }

        if (entity.collidedHorizontally && entity.isOffsetPositionInLiquid(entity.motionX, entity.motionY + 0.6000000238418579D - entity.posY + d0, entity.motionZ)) {
            entity.motionY = 0.30000001192092896D;
        }
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            setEnabled(false);
            return;
        }
        if (fake == null) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), "im a real player guys");
            fake = new EntityOtherPlayerMP(mc.world, profile);
            fake.inventory.copyInventory(mc.player.inventory);
            fake.copyLocationAndAnglesFrom(mc.player);
            fake.setHealth(mc.player.getHealth());
            fake.onGround = mc.player.onGround;
            mc.world.addEntityToWorld(-999, fake);
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (fake != null) {
            mc.world.removeEntity(fake);
            recordedPositions.clear();
            fake = null;
        }
    }

    private static class Location {
        double posX, posY, posZ;
        float moveForward;
        float moveStrafe;
        float moveVertical;
        float yaw;
        float pitch;

        public Location(double posX, double posY, double posZ) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }


        public void setMoveForward(float moveForward) {
            this.moveForward = moveForward;
        }

        public void setMoveStrafe(float moveStrafe) {
            this.moveStrafe = moveStrafe;
        }

        public void setMoveVertical(float moveVertical) {
            this.moveVertical = moveVertical;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }
    }

}
