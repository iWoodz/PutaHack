package dev.starstruck.mixin.mixins.client;

import dev.starstruck.Starstruck;
import dev.starstruck.listener.event.input.EventKeyInput;
import dev.starstruck.listener.event.input.EventMouseInput;
import dev.starstruck.listener.event.render.EventLimitFramerate;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author aesthetical
 * @since 06/04/23
 */
@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "init", at = @At("TAIL"))
    public void hookInit$tail(CallbackInfo info) {
        try {
            Starstruck.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "runTickKeyboard", at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z",
            shift = Shift.AFTER,
            remap = false))
    public void hookRunTickKeyboard(CallbackInfo info) {
        if (Keyboard.getEventKeyState()) {
            int keyCode = Keyboard.getEventKey() == 0
                    ? Keyboard.getEventCharacter() + 256
                    : Keyboard.getEventKey();
            Starstruck.getBus().dispatch(
                    new EventKeyInput(keyCode));
        }
    }

    @Inject(method = "runTickMouse", at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/input/Mouse;getEventButtonState()Z",
            ordinal = 1,
            shift = Shift.AFTER,
            remap = false))
    public void hookRunTickMouse(CallbackInfo info) {
        if (Mouse.getEventButtonState()) Starstruck.getBus().dispatch(
                new EventMouseInput(Mouse.getEventButton()));
    }

    @Inject(method = "getLimitFramerate", at = @At(value = "RETURN"), cancellable = true)
    public void hookGetLimitFramerate(CallbackInfoReturnable<Integer> info) {
        EventLimitFramerate event = new EventLimitFramerate(info.getReturnValueI());
        if (Starstruck.getBus().dispatch(event)) info.setReturnValue(event.getFps());
    }
}
