package me.dev.putahacknn.features.modules.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import me.dev.putahacknn.features.command.Command;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MoverType;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

public class FakePlayer
        extends Module {
    private EntityOtherPlayerMP fakePlayer;

    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing", Module.Category.PLAYER, false, false, false);
    }

    public final Setting<String> name = this.register(new Setting("Name", "no bitches?"));
    public final Setting<Boolean> move = this.register(new Setting("Move", false));
    public final Setting<Float> speed = this.register(new Setting("Speed", 1.0f, 0.0f, 10.0f));
    public final Setting<Boolean> copyInv = this.register(new Setting("Copy Inv", true));

    @Override
    public void onLogout() {
        if (this.isEnabled()) {
            this.disable();
        }
    }

    @Override
    public void onUpdate() {
        if (fakePlayer != null) {
            Random random = new Random();
            fakePlayer.moveForward = mc.player.moveForward + (random.nextInt(5) / 10F) * speed.getValue();
            fakePlayer.moveStrafing = mc.player.moveStrafing + (random.nextInt(5) / 10F) * speed.getValue();
            if (copyInv.getValue()) {
                fakePlayer.inventory.copyInventory(mc.player.inventory);
            }
            if (move.getValue()) {
                travel(fakePlayer.moveStrafing * speed.getValue(), fakePlayer.moveVertical * speed.getValue(), fakePlayer.moveForward * speed.getValue());
            }
            fakePlayer.setHealth(mc.player.getHealth());
            fakePlayer.setAbsorptionAmount(mc.player.getAbsorptionAmount());
        }
    }

    public void travel(float strafe, float vertical, float forward) {
        if (fakePlayer != null) {
            double d0 = fakePlayer.posY;
            float f1 = 0.8F;
            float f2 = 0.02F;
            float f3 = (float) EnchantmentHelper.getDepthStriderModifier(fakePlayer);
            if (f3 > 3.0F) {
                f3 = 3.0F;
            }
            if (!fakePlayer.onGround) {
                f3 *= 0.5F;
            }
            if (f3 > 0.0F) {
                f1 += (0.54600006F - f1) * f3 / 3.0F;
                f2 += (fakePlayer.getAIMoveSpeed() - f2) * f3 / 4.0F;
            }
            fakePlayer.moveRelative(strafe, vertical, forward, f2);
            fakePlayer.move(MoverType.SELF, fakePlayer.motionX, fakePlayer.motionY, fakePlayer.motionZ);
            fakePlayer.motionX *= (double) f1;
            fakePlayer.motionY *= 0.800000011920929D;
            fakePlayer.motionZ *= (double) f1;
            if (!fakePlayer.hasNoGravity()) {
                fakePlayer.motionY -= 0.02D;
            }
            if (fakePlayer.collidedHorizontally && fakePlayer.isOffsetPositionInLiquid(fakePlayer.motionX, fakePlayer.motionY + 0.6000000238418579D - fakePlayer.posY + d0, fakePlayer.motionZ)) {
                fakePlayer.motionY = 0.30000001192092896D;
            }
        }
    }

    public static String getUuid(String name) {
        JsonParser parser = new JsonParser();
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if (UUIDJson.isEmpty()) {
                return "invalid name";
            }
            JsonObject UUIDObject = (JsonObject) parser.parse(UUIDJson);
            return FakePlayer.reformatUuid(UUIDObject.get("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private static String reformatUuid(String uuid) {
        String longUuid = "";
        longUuid = longUuid + uuid.substring(1, 9) + "-";
        longUuid = longUuid + uuid.substring(9, 13) + "-";
        longUuid = longUuid + uuid.substring(13, 17) + "-";
        longUuid = longUuid + uuid.substring(17, 21) + "-";
        longUuid = longUuid + uuid.substring(21, 33);
        return longUuid;
    }

    @Override
    public void onEnable() {
        if (FakePlayer.fullNullCheck()) {
            this.disable();
            return;
        }
        if (fakePlayer != null) {
            this.fakePlayer = null;
        }
        if (FakePlayer.mc.player != null) {
            try {
                this.fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.fromString(FakePlayer.getUuid(name.getValue())), name.getValue()));
            } catch (Exception e) {
                this.fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03"), name.getValue()));
                Command.sendMessage("Failed to load uuid, setting another one.");
            }
            Command.sendMessage(String.format("%s has been spawned.", name.getValue()));
            this.fakePlayer.copyLocationAndAnglesFrom(FakePlayer.mc.player);
            this.fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
            FakePlayer.mc.world.addEntityToWorld(-100, this.fakePlayer);
        }
    }

    @Override
    public void onDisable() {
        if (FakePlayer.mc.world != null && FakePlayer.mc.player != null) {
            super.onDisable();
            FakePlayer.mc.world.removeEntity(this.fakePlayer);
        }
    }
}

