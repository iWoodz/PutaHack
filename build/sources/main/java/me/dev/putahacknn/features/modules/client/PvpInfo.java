package me.dev.putahacknn.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.modules.combat.AutoCrystalRewrite;
import me.dev.putahacknn.features.modules.combat.Surround;
import me.dev.putahacknn.features.setting.Bind;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.EntityUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class PvpInfo extends Module {

    public PvpInfo() {
        super("PvpInfo", "JAJAJAJAJJAJAJAAJJAJAAJ :vvvvvvvvvvvvvvvvvvvvvvvvv XDDDDDDDDDDDDDDDDDDDDDDDD", Category.CLIENT, true, false, false);
    }

    public final Setting<Integer> x = this.register(new Setting("X", 2, 0, 1000));
    public final Setting<Integer> y = this.register(new Setting("Y", 10, 0, 600));
    public final Setting<Integer> red = this.register(new Setting("Red", 150, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 50, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting("Alpha", 255, 0, 255));
    public final Setting<Boolean> textShadow = this.register(new Setting("Text Shadow", false));
    public final Setting<Boolean> shortNames = this.register(new Setting("Short Names", true));
    public final Setting<Bind> surroundBind = this.register(new Setting("Surround Bind", new Bind(-1)));
    public final Setting<Bind> autoCrystalBind = this.register(new Setting("Auto Crystal Bind", new Bind(-1)));
    public boolean surroundOn;// in case you want to use a different ca or surround but still have it show up
    public boolean autoCrystalOn;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (mc.player != null && mc.world != null) {
            if (surroundBind.getValue().getKey() != -1 && Keyboard.getEventKeyState() && Keyboard.getEventKey() == surroundBind.getValue().getKey()) {
                surroundOn = !surroundOn;
            }
            if (autoCrystalBind.getValue().getKey() != -1 &&Keyboard.getEventKeyState() && Keyboard.getEventKey() == autoCrystalBind.getValue().getKey()) {
                autoCrystalOn = !autoCrystalOn;
            }
        }
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        int color = new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()).hashCode();
        int ping = EntityUtil.getPing(mc.player);
        PutaHacknn.textManager.drawStringCustomFont(ChatFormatting.RESET + (shortNames.getValue() ? "AC: " : "AutoCrystal: ") + ((autoCrystalOn || AutoCrystalRewrite.INSTANCE.isEnabled()) ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"), x.getValue(), y.getValue(), color, textShadow.getValue());
        PutaHacknn.textManager.drawStringCustomFont(ChatFormatting.RESET + (shortNames.getValue() ? "FTP: " : "Surround: ") + ((Surround.INSTANCE.isEnabled() || surroundOn) ? ChatFormatting.GREEN + "ON" : ChatFormatting.RED + "OFF"), x.getValue(), y.getValue() + 9, color, textShadow.getValue());
        PutaHacknn.textManager.drawStringCustomFont(ChatFormatting.RESET + "Ping: " + (ping == -1 ? "NOT-CALCULATED" : (ping <= 70 ? ChatFormatting.GREEN + "" + ping : ping <= 110 ? ChatFormatting.YELLOW + "" + ping : ChatFormatting.RED + "" + ping)), x.getValue(), y.getValue() + 18, color, textShadow.getValue());
        PutaHacknn.textManager.drawStringCustomFont(ChatFormatting.RESET + (shortNames.getValue() ? "SFTY: " : "Safety: ") + (EntityUtil.isInHole(mc.player) ? ChatFormatting.GREEN + (shortNames.getValue() ? "+" : "SAFE") : ChatFormatting.RED + (shortNames.getValue() ? "-" : "UNSAFE")), x.getValue(), y.getValue() + 27, color, textShadow.getValue());
    }

}
