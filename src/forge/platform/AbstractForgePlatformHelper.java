package net.xalcon.torchmaster.platform;

import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

public abstract class AbstractForgePlatformHelper implements IPlatformHelper
{
    private static final ITorchmasterConfig CONFIG = TorchmasterTomlConfig.load(FMLPaths.CONFIGDIR.get().resolve("torchmaster.toml"));

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return CONFIG;
    }
}
