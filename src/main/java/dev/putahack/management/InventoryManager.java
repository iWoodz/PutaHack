package dev.putahack.management;

import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.network.EventPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketHeldItemChange;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class InventoryManager {

    /**
     * The minecraft game instance
     */
    private final Minecraft mc = Minecraft.getMinecraft();

    /**
     * The server slot
     */
    private int serverSlot = -1;

    @Listener
    public void onPacketIn(EventPacket.In event) {
        if (event.getPacket() instanceof SPacketHeldItemChange) {
            serverSlot = ((SPacketHeldItemChange) event.getPacket())
                    .getHeldItemHotbarIndex();
        }
    }

    @Listener
    public void onPacketOut(EventPacket.Out event) {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            serverSlot = ((CPacketHeldItemChange) event.getPacket())
                    .getSlotId();
        }
    }

    /**
     * Syncs the client slot with the server slot
     */
    public void sync() {
        if (serverSlot != mc.player.inventory.currentItem) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(
                    mc.player.inventory.currentItem));
        }
    }

    public int getServerSlot() {
        return serverSlot;
    }

    public ItemStack getStack(EntityPlayer player) {
        return serverSlot == -1
                ? player.getHeldItemMainhand()
                : player.inventory.mainInventory.get(serverSlot);
    }
}
