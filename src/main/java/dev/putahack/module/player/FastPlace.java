package dev.putahack.module.player;

import dev.putahack.PutaHack;
import dev.putahack.listener.bus.Listener;
import dev.putahack.listener.event.player.EventUpdate;
import dev.putahack.mixin.mixins.client.IMinecraft;
import dev.putahack.module.Module;
import dev.putahack.module.ModuleCategory;
import dev.putahack.setting.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

import java.util.HashSet;
import java.util.Set;

/**
 * @author aesthetical
 * @since 06/09/23
 */
public class FastPlace extends Module {
    private final Set<Item> whitelist = new HashSet<>();

    private final Setting<Integer> speed = new Setting<>(
            4, 0, 4, "Speed");
    private final Setting<Boolean> ghostFix = new Setting<>(true, "GhostFix");

    public FastPlace() {
        super("FastPlace", "Places items faster", ModuleCategory.PLAYER);

        whitelist.add(Items.EXPERIENCE_BOTTLE);
        whitelist.add(Items.END_CRYSTAL);
        whitelist.add(Item.getItemFromBlock(Blocks.OBSIDIAN));
    }

    @Listener
    public void onUpdate(EventUpdate event) {
        if (isHoldingWhitelisted()) {
            ((IMinecraft) mc).setRightClickDelayTimer(
                    speed.getMax().intValue() - speed.getValue());

            if (ghostFix.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                EnumHand hand = mc.player.getActiveHand();
                if (hand == null) hand = EnumHand.MAIN_HAND;
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(hand));
            }
        }
    }

    private boolean isHoldingWhitelisted() {
        ItemStack stack = PutaHack.get().getInventory()
                .getStack(mc.player);
        return !stack.isEmpty()
                && whitelist.contains(stack.getItem());
    }

    public Set<Item> getWhitelist() {
        return whitelist;
    }
}
