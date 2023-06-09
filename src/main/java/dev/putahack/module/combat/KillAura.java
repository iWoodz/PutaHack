package dev.putahack.module.combat;

import dev.putahack.PutaHack;
import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.EventStage;
import dev.putahack.listener.event.player.EventWalkingUpdate;
import dev.putahack.mixin.mixins.entity.IEntityLivingBase;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;
import dev.putahack.util.math.rotate.BodyPart;
import dev.putahack.util.math.rotate.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author aesthetical
 * @since 06/07/23
 *
 * @see IEntityLivingBase
 * @see EntityLivingBase
 */
public class KillAura extends Module {
    private final Setting<Mode> mode = new Setting<>(Mode.SINGLE, "Mode");
    private final Setting<Priority> priority = new Setting<>(Priority.DISTANCE, "Priority");

    private final Setting<Double> range = new Setting<>(
            4.0, 0.01, 1.0, 6.0, "Range");
    private final Setting<Double> wallRange = new Setting<>(
            3.0, 0.01, 1.0, 6.0, "WallRange");

    private final Setting<Boolean> tpsSync = new Setting<>(true, "TPSSync");

    private final Setting<Boolean> rotate = new Setting<>(true, "Rotate");
    private final Setting<Double> rotateSpeed = new Setting<>(
            rotate::getValue, 85.0, 0.01, 1.0, 100.0, "RotateSpeed");
    private final Setting<BodyPart> bodyPart = new Setting<>(
            rotate::getValue, BodyPart.HEAD, "Part");
    private final Setting<Boolean> raytrace = new Setting<>(true, "Raytrace");

    private final Setting<Boolean> swing = new Setting<>(true, "Swing");
    private final Setting<Weapon> weapon = new Setting<>(Weapon.SWITCH, "Weapon");

    private float[] previousRotations;
    private EntityLivingBase target;

    public KillAura() {
        super("KillAura", "Attacks entities around you", ModuleCategory.COMBAT);
    }

    @Listener(receiveCanceled = true)
    public void onWalkingUpdate(EventWalkingUpdate event) {

        if (!isValidTarget(target) || mode.getValue() == Mode.SWITCH) {
            target = (EntityLivingBase) mc.world.loadedEntityList
                    .stream()
                    .filter((e) -> e instanceof EntityLivingBase && isValidTarget((EntityLivingBase) e))
                    .min(Comparator.comparingDouble((e) -> priority.getValue().apply((EntityLivingBase) e)))
                    .orElse(null);

            // gay
            if (target == null
                    && mc.pointedEntity instanceof EntityLivingBase
                    && isValidTarget((EntityLivingBase) mc.pointedEntity)) {

                target = (EntityLivingBase) mc.pointedEntity;
            }
        }

        if (target == null) return;

        if (rotate.getValue()) {
            float[] rotations = RotationUtils.entity(target, bodyPart.getValue());
            PutaHack.get().getRotations().spoof(rotations);
        }

        if (raytrace.getValue()) {
            // TODO: customized raycast util (would need for ca and shit anyway)
        }

        if (event.getStage() == EventStage.POST) {
            if (getAttackStrength() < 1.0f) return;

            mc.playerController.attackEntity(mc.player, target);
            if (swing.getValue()) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            } else {
                mc.player.connection.sendPacket(
                        new CPacketAnimation(EnumHand.MAIN_HAND));
            }
        }
    }

    private boolean isValidTarget(EntityLivingBase t) {
        if (t == null
                || t.isDead
                || t.getHealth() <= 0.0f
                || t.isPlayerSleeping()
                || t.equals(mc.player)) return false;

        double distance = Math.pow(mc.player.canEntityBeSeen(t)
                ? range.getValue() : wallRange.getValue(), 2);

        return mc.player.getDistanceSq(t) <= distance;
    }

    private float getAttackStrength() {
        float strength = (((IEntityLivingBase) mc.player)
                .getTicksSinceLastSwing() + 1.0f) / mc.player.getCooldownPeriod();
        if (tpsSync.getValue()) {
            // TODO: tick manager
        }

        return strength;
    }

    public enum Mode {
        SINGLE, SWITCH
    }

    public enum Priority {
        DISTANCE((x) -> mc.player.getDistanceSq(x)),
        HEALTH((x) -> (double) x.getHealth()),
        ARMOR((x) -> (double) x.getTotalArmorValue());

        private final Function<EntityLivingBase, Double> sort;

        Priority(Function<EntityLivingBase, Double> sort) {
            this.sort = sort;
        }

        public double apply(EntityLivingBase entity) {
            return sort.apply(entity);
        }
    }

    public enum Weapon {
        NONE, SWITCH, SWORD
    }
}
