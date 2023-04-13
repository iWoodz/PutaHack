package me.dev.putahacknn.features.gui.components.items.buttons;

import me.dev.putahacknn.features.gui.PutaHacknnGui;
import me.dev.putahacknn.features.gui.components.Component;
import me.dev.putahacknn.features.gui.components.items.Item;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.RenderUtil;
import me.dev.putahacknn.util.Util;
import me.dev.putahacknn.PutaHacknn;
import me.dev.putahacknn.features.modules.client.ClickGui;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class Button
        extends Item {
    private boolean state;

    public Button(String name) {
        super(name);
        this.height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? PutaHacknn.colorManager.getColorWithAlpha(PutaHacknn.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : PutaHacknn.colorManager.getColorWithAlpha(PutaHacknn.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? ColorUtil.toARGB(110, 110, 110, 40) : -2007673515));
        PutaHacknn.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 2.0f - (float) PutaHacknnGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        Util.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : PutaHacknnGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
    }
}

