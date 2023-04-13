package me.dev.putahacknn.features.modules.render;

import me.dev.putahacknn.event.events.PerspectiveEvent;
import me.dev.putahacknn.features.modules.Module;
import me.dev.putahacknn.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AspectRatio extends Module {

    public AspectRatio() {
        super("AspectRatio", "ratio aspect", Category.RENDER, true, false, false);
    }

    public final Setting<Float> aspectRatio = this.register(new Setting("Aspect Ratio", 1.0f, 0.0f, 3.0f));

    @SubscribeEvent
    public void onPerspectiveChange(PerspectiveEvent event) {
        event.setAspectRatio(aspectRatio.getValue());
    }

}
