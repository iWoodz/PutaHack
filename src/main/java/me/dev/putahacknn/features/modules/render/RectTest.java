package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.Render2DEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import me.dev.putahacknn.util.ColorUtil;
import me.dev.putahacknn.util.RenderUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RectTest extends Module {

    public final Setting<Integer> x = this.register(new Setting("X", 456, 0, 1000));
    public final Setting<Integer> y = this.register(new Setting("Y", 400, 0, 600));
    public final Setting<Integer> red = this.register(new Setting("Red", 145, 0, 255));
    public final Setting<Integer> green = this.register(new Setting("Green", 120, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting("Blue", 225, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting("Alpha", 40, 0, 255));
    public RectTest() {
        super("RectTest", "test rectangle", Module.Category.RENDER, true, false, false);
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        RenderUtil.drawRect(x.getValue(), y.getValue(), x.getValue() + 50, y.getValue() + 25, ColorUtil.toARGB(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()));
    }
}