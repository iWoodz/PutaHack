package me.dev.putahacknn.mixin;

import me.dev.putahacknn.PutaHacknn;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class PutaHacknnLoader
        implements IFMLLoadingPlugin {
    private static boolean isObfuscatedEnvironment = false;

    public PutaHacknnLoader() {
        PutaHacknn.LOGGER.info("\n\nLoading mixins by a racist");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.putahacknn.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        PutaHacknn.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

