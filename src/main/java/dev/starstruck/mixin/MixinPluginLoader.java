package dev.starstruck.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author aesthetical
 * @since 06/04/23
 */
@Name("MixinPluginLoader")
@MCVersion("1.12.2")
public class MixinPluginLoader implements IFMLLoadingPlugin {

    private static final Logger logger = LogManager.getLogger(
            "Mixins");

    /**
     * If future client is detected loaded with the client
     */
    public static boolean futureClient;

    public MixinPluginLoader() {
        logger.info("chinaware.cc.wtf.vip.eu is loading mixins...");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.starstruck.json");

        try {
            Class.forName("net.futureclient.loader.launch.launchwrapper.LaunchWrapperEntryPoint");
            futureClient = true;
            logger.info("Detected Future client for compatibility");
        } catch (ClassNotFoundException ignored) {
        }
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
