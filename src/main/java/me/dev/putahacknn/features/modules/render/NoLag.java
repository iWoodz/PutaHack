package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.PacketEvent;
import me.dev.putahacknn.event.events.RenderEntityModelEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/**
 * Credits: By Woodz
 */
public class NoLag
        extends Module {
    public final Setting<Boolean> scoreBoards;
    public final Setting<Boolean> tnt;
    public final Setting<Boolean> skulls;
    public final Setting<Boolean> antiSpam = this.register(new Setting<>("AntiSpam", true));
    public final Setting<Boolean> parrots;
    public static NoLag INSTANCE;
    public final Setting<Boolean> antiPopLag = this.register(new Setting<>("AntiPopLag", true));
    public final Setting<Boolean> glowing;

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive receive) {
        String string;
    }

    @Override
    public void onTick() {
        if (!this.glowing.getValue()) {
            return;
        }
        for (Entity entity : NoLag.mc.world.loadedEntityList) {
            if (!entity.isGlowing()) continue;
            entity.setGlowing(false);
        }
    }

    public NoLag() {
        super("NoLag", "Removes several things that may cause fps drops", Module.Category.RENDER, true, false, false);
        this.skulls = this.register(new Setting<>("WitherSkulls", false));
        this.tnt = this.register(new Setting<>("PrimedTNT", false));
        this.scoreBoards = this.register(new Setting<>("ScoreBoards", true));
        this.glowing = this.register(new Setting<>("GlowingEntities", true));
        this.parrots = this.register(new Setting<>("Parrots", true));
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderEntity(RenderEntityModelEvent renderEntityModelEvent) {
        if (renderEntityModelEvent.entity != null) {
            if (this.skulls.getValue() && renderEntityModelEvent.entity instanceof EntityWitherSkull) {
                renderEntityModelEvent.setCanceled(true);
            }
            if (this.tnt.getValue() && renderEntityModelEvent.entity instanceof EntityTNTPrimed) {
                renderEntityModelEvent.setCanceled(true);
            }
            if (this.parrots.getValue() && renderEntityModelEvent.entity instanceof EntityParrot) {
                renderEntityModelEvent.setCanceled(true);
            }
        }
    }
}
