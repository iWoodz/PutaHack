package dev.putahack.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

import static org.spongepowered.asm.mixin.MixinEnvironment.Option.HOT_SWAP;

/**
 * @author aesthetical
 * @since 06/04/23
 */
@Name("MixinPluginLoader")
@MCVersion("1.12.2")
public class MixinPluginLoader implements IFMLLoadingPlugin {

    public MixinPluginLoader() {
        LogManager.getLogger("Mixins").info("chinaware.cc.wtf.vip.eu is loading mixins...");
        MixinBootstrap.init();
        MixinEnvironment.getCurrentEnvironment().setOption(HOT_SWAP, true);
        Mixins.addConfiguration("mixins.putahack.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
