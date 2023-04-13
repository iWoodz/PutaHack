package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.features.modules.Module;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class CritRender extends Module {

    public CritRender() {
        super("CritRender", "renders crit particles on a crystal you're breaking", Category.RENDER, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            if (((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
                mc.effectRenderer.emitParticleAtEntity(Objects.requireNonNull(((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world)), EnumParticleTypes.CRIT);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttacKEntity(AttackEntityEvent event) {
        if (event.getEntity() instanceof EntityEnderCrystal) {
            mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.CRIT);
        }
    }

}
