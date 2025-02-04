package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.Render3DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.modules.client.ClickGui;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.RenderUtil;
import me.dev.putahacknn.util.RenderUtill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BurrowESP extends Module {

    private static BurrowESP INSTANCE = new BurrowESP();

    public Setting<Integer> range = register(new Setting("Range", 20, 5, 50));
    public Setting<Boolean> self = register(new Setting("Self", true));
    public Setting<Boolean> text = register(new Setting("Text", false));
    public Setting<String> textString = register(new Setting("TextString", "BURROW", v -> this.text.getValue()));
    public Setting<Boolean> rainbow = register(new Setting("Rainbow", false));
    public Setting<Integer> red = register(new Setting("Red", 145, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> green = register(new Setting("Green", 120, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> blue = register(new Setting("Blue", 225, 0, 255, v -> !this.rainbow.getValue()));
    public Setting<Integer> alpha = register(new Setting("Alpha", 30, 0, 255));
    public Setting<Integer> outlineAlpha = register(new Setting("OL-Alpha", 120, 0, 255));

    private final List<BlockPos> posList = new ArrayList<>();

    private final RenderUtill renderUtill = new RenderUtill();

    public BurrowESP() {
        super("BurrowESP", "renders a cube in the foot hitbox of the opp", Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static BurrowESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BurrowESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onTick() {
        posList.clear();
        for (EntityPlayer player : mc.world.playerEntities) {
            BlockPos blockPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY + 0.2), Math.floor(player.posZ));
            if ((mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= this.range.getValue()) {

                if (!(blockPos.distanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= 1.5) || this.self.getValue()) {
                    posList.add(blockPos);
                }


            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (BlockPos blockPos : posList) {
            String s = textString.getValue().toUpperCase();
            if (this.text.getValue()) {
                this.renderUtill.drawText(blockPos, s, rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), outlineAlpha.getValue()));
            }
            RenderUtil.drawBoxESP(blockPos, rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), outlineAlpha.getValue()), 1.5F, true, true, alpha.getValue());
        }
    }
}
