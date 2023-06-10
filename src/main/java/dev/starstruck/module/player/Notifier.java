package dev.starstruck.module.player;

import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.network.EventPacket;
import dev.starstruck.listener.event.player.EventDeath;
import dev.starstruck.listener.event.player.EventPop;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;

import static java.lang.String.format;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class Notifier extends Module {
    /**
     * The value of getType() on the SPacketSpawnObject packet for an ender pearl
     */
    private static final int PEARL_TYPE = 65;

    private final Setting<Boolean> pearls = new Setting<>(true, "Pearls");
    private final Setting<Boolean> pops = new Setting<>(true, "Pops");

    public Notifier() {
        super("Notifier", "Notifies you of shit", ModuleCategory.PLAYER);
    }

    @Listener
    public void onPacketIn(EventPacket.In event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            SPacketSpawnObject packet = event.getPacket();
            if (packet.getType() != PEARL_TYPE || !pearls.getValue()) return;

            double x = packet.getX();
            double y = packet.getY();
            double z = packet.getZ();

            // see Entity#getHorizontalFacing
            EnumFacing facing = EnumFacing.byHorizontalIndex(
                    MathHelper.floor(
                            (double) (packet.getYaw() * 4.0F / 360.0F) + 0.5D) & 3);

            // this is turbo shit but it works fairly well
            Entity closestEntity = mc.world.playerEntities.stream()
                    .filter((p) -> p.getDistanceSq(x, y, z) <= 1.5 * 1.5)
                    .min(Comparator.comparingDouble((p) -> p.getDistanceSq(x, y, z)))
                    .orElse(null);

            if (closestEntity != null) {
                print((closestEntity.equals(mc.player)
                        ? "You"
                        : closestEntity.getName())
                        + " threw an ender pearl heading " + facing.name().toLowerCase());
            } else {
                print(format("An ender pearl was thrown at XYZ: %.1f, %.1f, %.1f heading %s",
                        x, y, z, facing.name().toLowerCase()));
            }
        }
    }

    @Listener
    public void onPop(EventPop event) {
        if (!pops.getValue()) return;

        print(event.getPlayer().hashCode(), (event.getPlayer().equals(mc.player)
                ? "You have"
                : event.getPlayer().getName() + " has")
                + " popped " + event.getPops() + " totem"
                + (event.getPops() != 1 ? "s" : ""));
    }

    @Listener
    public void onDeath(EventDeath event) {
        if (!pops.getValue() || event.getPops() == -1) return;

        print(event.getPlayer().hashCode(), (event.getPlayer().equals(mc.player)
                ? "You"
                : event.getPlayer().getName())
                + " died after popping "
                + event.getPops()
                + " totem" + (event.getPops() != 1 ? "s" : ""));
    }
}
