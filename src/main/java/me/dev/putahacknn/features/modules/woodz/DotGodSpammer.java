package me.dev.putahacknn.features.modules.woodz;

import me.dev.putahacknn.event.events.BlockEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class DotGodSpammer extends Module {

    public DotGodSpammer() {
        super("DotGodSpammer", "flood chat wit da cheat on baby", Module.Category.PLAYER, false, false, false);
    }

    public static String walkMessage = "I just moved {blocks} meters like a butterfly thanks to DotGod.CC!";
    public static String breakMessage = "I just broke {amount} {name} thanks to DotGod.CC!";
    public static String eatMessage = "I just ate {amount} {name} thanks to DotGod.CC!";
    private final Setting<Boolean> move = register(new Setting("Move", false));
    private final Setting<Boolean> breakBlock = register(new Setting("Break", false));
    private final Setting<Boolean> eat = register(new Setting("Eat", false));
    private final Setting<Double> delay = register(new Setting("Delay", 1d, 1d, 20d));
    public static int blockBrokeDelay = 0;
    static int blockPlacedDelay = 0;
    static int jumpDelay = 0;
    static int attackDelay = 0;
    static int eattingDelay = 0;
    static long lastPositionUpdate;
    static double lastPositionX;
    static double lastPositionY;
    static double lastPositionZ;
    private static double speed;
    String heldItem = "";
    int blocksPlaced = 0;
    int blocksBroken = 0;
    int eaten = 0;

    @Override
    public void onUpdate() {
        blockBrokeDelay++;
        blockPlacedDelay++;
        jumpDelay++;
        attackDelay++;
        eattingDelay++;
        heldItem = mc.player.getHeldItemMainhand().getDisplayName();
        if (move.getValue()) {
            if (lastPositionUpdate + (5000L * delay.getValue()) < System.currentTimeMillis()) {
                double d0 = lastPositionX - mc.player.lastTickPosX;
                double d2 = lastPositionY - mc.player.lastTickPosY;
                double d3 = lastPositionZ - mc.player.lastTickPosZ;
                speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
                if (!(speed <= 1) && !(speed > 5000)) {
                    String walkAmount = new DecimalFormat("0").format(speed);
                    mc.player.sendChatMessage(walkMessage.replace("{blocks}", walkAmount));
                }
                lastPositionUpdate = System.currentTimeMillis();
                lastPositionX = mc.player.lastTickPosX;
                lastPositionY = mc.player.lastTickPosY;
                lastPositionZ = mc.player.lastTickPosZ;
            }
        }
    }

    @SubscribeEvent
    public void onItemUse(LivingEntityUseItemEvent event) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        if (event.getEntity() == mc.player) {
            if (event.getItem().getItem() instanceof ItemFood || event.getItem().getItem() instanceof ItemAppleGold) {
                eaten++;
                if (eattingDelay >= 300 * delay.getValue()) {
                    if (eat.getValue() && eaten > randomNum) {
                        mc.player.sendChatMessage
                                (eatMessage.replace("{amount}", eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
                        eaten = 0;
                        eattingDelay = 0;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent event) {
        blocksBroken++;
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        if (blockBrokeDelay >= 300 * delay.getValue()) {
            if (breakBlock.getValue() && blocksBroken > randomNum) {
                String msg = breakMessage
                        .replace("{amount}", blocksBroken + "")
                        .replace("{name}", mc.world.getBlockState(event.pos).getBlock().getLocalizedName());
                mc.player.sendChatMessage(msg);
            }
            blocksBroken = 0;
            blockBrokeDelay = 0;
        }
    }
}
