package dev.starstruck.module.combat;

import com.google.common.collect.Lists;
import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.player.EventUpdate;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import dev.starstruck.util.timing.Timer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author aesthetical
 * @since 06/05/23
 */
public class AutoArmor extends Module {

    /**
     * A list of enchantments to factor into a "good" armor piece
     */
    private static final List<Enchantment> ENCHANTMENTS = Lists.newArrayList(
            Enchantments.UNBREAKING, Enchantments.MENDING, Enchantments.AQUA_AFFINITY);

    private final Setting<Priority> priority = new Setting<>(Priority.BLAST, "Priority");
    private final Setting<Double> delay = new Setting<>(0.1, 0.01, 0.0, 2.0, "Delay");

    private final Timer timer = new Timer();
    private final int[] bestArmorSlots = new int[4];

    public AutoArmor() {
        super("AutoArmor",
                "Automatically swaps to the best piece of armor available",
                ModuleCategory.COMBAT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Arrays.fill(bestArmorSlots, -1);
    }

    @Listener
    public void onUpdate(EventUpdate event) {
        for (int i = 0; i < 36; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemArmor)) continue;

            int index = ((ItemArmor) itemStack.getItem()).armorType.getIndex();

            float score = getStackScore(itemStack);
            float otherScore = 0.0f;

            ItemStack armorStack = mc.player.inventory.armorInventory.get(index);
            if (!armorStack.isEmpty()) {
                otherScore = getStackScore(armorStack);
            }

            if (score > otherScore) bestArmorSlots[index] = i;
        }

        if (!timer.hasPassed((long) (delay.getValue() * 1000.0), false)) return;

        for (int i = 0; i < bestArmorSlots.length; ++i) {
            int armorSlot = bestArmorSlots[i];
            if (armorSlot == -1) continue;

            int windowId = mc.player.openContainer.windowId;
            int packetSlot = armorSlot < 9 ? armorSlot + 36 : armorSlot;

            boolean slotEmpty = mc.player.inventory.armorInventory.get(i).isEmpty();
            if (slotEmpty) {
                mc.playerController.windowClick(windowId,
                        packetSlot, 0, ClickType.QUICK_MOVE, mc.player);
            } else {
                mc.playerController.windowClick(windowId,
                        packetSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(windowId,
                        8 - i, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(windowId,
                        packetSlot, 0, ClickType.PICKUP, mc.player);
            }

            timer.resetTime();
            bestArmorSlots[i] = -1;

            break;
        }
    }

    /**
     * Gets the "score" for an item stack
     * @param itemStack the item stack
     * @return the score for the armor piece
     */
    private float getStackScore(ItemStack itemStack) {
        float score = 0.0f;
        if (itemStack.getItem() instanceof ItemArmor) {
            ItemArmor armor = ((ItemArmor) itemStack.getItem());
            score = (float) armor.damageReduceAmount + armor.toughness;
        }

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(itemStack);
        if (!enchants.isEmpty()) {
            for (Enchantment enchantment : enchants.keySet()) {
                int level = enchants.get(enchantment);
                if (enchantment.equals(priority.getValue().getEnchantment())) {
                    score += (level * 2.0f);
                }

                if (ENCHANTMENTS.contains(enchantment)) score += (float) level * 0.75f;
            }
        }

        return score;
    }

    public enum Priority {
        BLAST(Enchantments.BLAST_PROTECTION),
        PROT(Enchantments.PROTECTION);

        private final Enchantment enchantment;

        Priority(Enchantment enchantment) {
            this.enchantment = enchantment;
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }
    }
}
