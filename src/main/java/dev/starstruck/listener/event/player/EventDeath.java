package dev.starstruck.listener.event.player;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author aesthetical
 * @since 06/10/23
 */
public class EventDeath {
    private final EntityPlayer player;
    private final int pops;

    public EventDeath(EntityPlayer player, int pops) {
        this.player = player;
        this.pops = pops;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public int getPops() {
        return pops;
    }
}