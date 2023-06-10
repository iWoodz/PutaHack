package dev.starstruck.module.render;

import dev.starstruck.listener.bus.Listener;
import dev.starstruck.listener.event.render.EventArmAnimationSpeed;
import dev.starstruck.module.Module;
import dev.starstruck.module.ModuleCategory;
import dev.starstruck.setting.Setting;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author aesthetical
 * @since 06/05/23
 *
 * @see dev.starstruck.mixin.mixins.render.item.MixinItemRenderer
 * @see EntityLivingBase#getArmSwingAnimationEnd()
 */
public class ViewModel extends Module {
    public final Setting<Double> translateX = new Setting<>(0.0, 0.01, -5.0, 5.0, "TranslateX");
    public final Setting<Double> translateY = new Setting<>(0.0, 0.01, -5.0, 5.0, "TranslateY");
    public final Setting<Double> translateZ = new Setting<>(0.0, 0.01, -5.0, 5.0, "TranslateZ");
    public final Setting<Double> scaleX = new Setting<>(1.0, 0.01, -5.0, 5.0, "ScaleX");
    public final Setting<Double> scaleY = new Setting<>(1.0, 0.01, -5.0, 5.0, "ScaleY");
    public final Setting<Double> scaleZ = new Setting<>(1.0, 0.01, -5.0, 5.0, "ScaleZ");

    private final Setting<Integer> armSpeed = new Setting<>(14, 1, 20, "ArmSpeed");

    public ViewModel() {
        super("ViewModel", "Changes how items render in your hand", ModuleCategory.RENDER);
    }

    @Listener
    public void onArmAnimationSpeed(EventArmAnimationSpeed event) {
        // default is 6, 20 (max) - 14 (default) = 6
        event.setSpeed(armSpeed.getMax().intValue() - armSpeed.getValue());
        event.cancel();
    }
}
