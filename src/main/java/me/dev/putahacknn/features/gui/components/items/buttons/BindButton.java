package me.dev.putahacknn.features.gui.components.items.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.putahacknn.features.gui.PutaHacknnGui;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.RenderUtil;
import me.dev.putahacknn.util.Util;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.modules.client.ClickGui;
import me.dev.putahacknn.features.setting.Bind;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class BindButton
        extends Button {
    private final Setting setting;
    public boolean isListening;

    public BindButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int color = ColorUtil.toARGB(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 255);
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515) : (!this.isHovering(mouseX, mouseY) ? PutaHacknn.colorManager.getColorWithAlpha(PutaHacknn.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : PutaHacknn.colorManager.getColorWithAlpha(PutaHacknn.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())));
        if (this.isListening) {
            PutaHacknn.textManager.drawStringWithShadow("Press a Key...", this.x + 2.3f, this.y - 1.7f - (float) PutaHacknnGui.getClickGui().getTextOffset(), -1);
        } else {
            PutaHacknn.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), this.x + 2.3f, this.y - 1.7f - (float) PutaHacknnGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            Util.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.setting.setValue(bind);
            this.onMouseClick();
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }
}

