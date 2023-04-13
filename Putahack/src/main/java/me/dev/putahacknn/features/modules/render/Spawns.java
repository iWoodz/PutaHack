package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.Render3DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.awt.Color;

public class Spawns extends Module {

    public Spawns() {
        super("Spawns", "why is dbear's cps so low", Category.RENDER, true, false, false);
        this.register(red);
        this.register(green);
        this.register(blue);
        this.register(alpha);
    }

    public static Setting<Integer> red = new Setting("Red", 255, 0, 255);
    public static Setting<Integer> green = new Setting("Green", 255, 0, 255);
    public static Setting<Integer> blue = new Setting("Blue", 255, 0, 255);
    public static Setting<Integer> alpha = new Setting("Alpha", 255, 0, 255);
    public static HashMap<UUID, Thingering> thingers = new HashMap<>();
    public static Color color8 = new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        for (final Entity entity : Spawns.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && !Spawns.thingers.containsKey(entity.getUniqueID())) {
                Spawns.thingers.put(entity.getUniqueID(), new Thingering(this, entity));
                Spawns.thingers.get(entity.getUniqueID()).starTime = System.currentTimeMillis();
            }
        }
    }

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        for (final Map.Entry<UUID, Thingering> entry : Spawns.thingers.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue().starTime < 1500) {
                float opacity = Float.intBitsToFloat(Float.floatToIntBits(1.2886874E38f) ^ 0x7EC1E66F);
                final long time = System.currentTimeMillis();
                final long duration = time - entry.getValue().starTime;
                if (duration < ((long)1500)) {
                    opacity = 1.0f - duration / 1500.0f;
                }
                drawCircle(entry.getValue().entity, event.getPartialTicks(), 0.5D, (System.currentTimeMillis() - entry.getValue().starTime) / 100.0f, opacity);
            }
        }
    }

    public static void drawCircle(final Entity entity, final float partialTicks, final double rad, final float plusY, final float alpha) {
        final Color color = new Color(color8.getRed(), color8.getGreen(), color8.getBlue());
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(Float.intBitsToFloat(Float.floatToIntBits(0.8191538f) ^ 0x7F11B410));
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - Spawns.mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - Spawns.mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - Spawns.mc.getRenderManager().viewerPosZ;
        final float r = Float.intBitsToFloat(Float.floatToIntBits(3180.4917f) ^ 0x7EC6475F) * color.getRed();
        final float g = Float.intBitsToFloat(Float.floatToIntBits(4554.3037f) ^ 0x7E0ED2EF) * color.getGreen();
        final float b = Float.intBitsToFloat(Float.floatToIntBits(29994.996f) ^ 0x7D6AD57F) * color.getBlue();
        for (int i = 0; i <= 90; ++i) {
            GL11.glColor4f(r, g, b, alpha);
            GL11.glVertex3d(x + rad * Math.cos(i * Double.longBitsToDouble(Double.doubleToLongBits(0.038923223119235344) ^ 0x7FBACC45F0F011C7L) / Double.longBitsToDouble(Double.doubleToLongBits(0.010043755046771538) ^ 0x7FC211D1FBA3AC6BL)), y + plusY / Float.intBitsToFloat(Float.floatToIntBits(0.13022153f) ^ 0x7F2558CB), z + rad * Math.sin(i * Double.longBitsToDouble(Double.doubleToLongBits(0.012655047216797511) ^ 0x7F90CB18FB234FBFL) / Double.longBitsToDouble(Double.doubleToLongBits(0.00992417958121009) ^ 0x7FC2D320D5ED6BD3L)));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public class Thingering {
        public Entity entity;
        public long starTime;
        public Spawns this$0;

        public Thingering(final Spawns this$0, final Entity entity) {
            this.this$0 = this$0;
            this.entity = entity;
            this.starTime = ((long)1417733199 ^ 0x5480E44FL);
        }
    }

}
