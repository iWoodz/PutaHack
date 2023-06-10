package dev.starstruck.management;

import dev.starstruck.Starstruck;
import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.network.EventPacket;
import dev.starstruck.listener.event.player.EventDeath;
import dev.starstruck.listener.event.player.EventPop;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class PopManager {

    /**
     * The minecraft game instance
     */
    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * The OpCode value from a SPacketEntityStatus packet signifying a totem pop
     */
    private static final byte POP_OPCODE = 35;

    /**
     * A map of the player id and the amount of times they have popped
     * x <= 0 reset/no totems popped
     */
    private final Map<Integer, Integer> pops = new ConcurrentHashMap<>();

    @Listener(receiveCanceled = true)
    public void onPacketIn(EventPacket.In event) {
        if (event.getPacket() instanceof SPacketEntityMetadata) {
            SPacketEntityMetadata packet = event.getPacket();
            Entity entity = mc.world.getEntityByID(packet.getEntityId());
            if (!(entity instanceof EntityPlayer)) return;

            EntityPlayer player = (EntityPlayer) entity;

            if (player.isDead || player.getHealth() <= 0.0f) {
                int totems = pops.getOrDefault(packet.getEntityId(), -1);
                Starstruck.getBus().dispatch(new EventDeath(player, totems));
                if (totems != -1) pops.put(packet.getEntityId(), 0);
            }

        } else if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == POP_OPCODE) {
                Entity entity = packet.getEntity(mc.world);
                if (!(entity instanceof EntityPlayer)) return;

                int totems = pops.merge(entity.getEntityId(), 1, Integer::sum);
                Starstruck.getBus().dispatch(new EventPop((EntityPlayer) entity, totems));
            }
        }
    }
}
