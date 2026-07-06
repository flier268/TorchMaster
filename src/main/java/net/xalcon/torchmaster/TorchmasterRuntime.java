package net.xalcon.torchmaster;

import net.xalcon.torchmaster.compat.VanillaCompat;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.platform.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class TorchmasterRuntime
{
    public static final EntityFilterList MegaTorchFilterRegistry = new EntityFilterList(Constants.MOD_ID + ":entity_filter/mega_torch");
    public static final EntityFilterList DreadLampFilterRegistry = new EntityFilterList(Constants.MOD_ID + ":entity_filter/dread_lamp");

    public static final Logger LOG = LogManager.getLogger(Constants.MOD_NAME);

    public static ITorchmasterConfig getConfig()
    {
        return Services.PLATFORM.getConfig();
    }

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        if(System.getProperty("torchmaster.enableDebugLogging", "0").equals("1"))
        {
            Configurator.setLevel(Constants.MOD_NAME, org.apache.logging.log4j.Level.DEBUG);
        }
        else
        {
            Configurator.setLevel(Constants.MOD_NAME, org.apache.logging.log4j.Level.INFO);
        }

        LOG.info("Initializing Torchmaster for platform {}", Services.PLATFORM.getPlatformName());
        LOG.info("Debug Logging Enabled: {}", LOG.isDebugEnabled());
        LOG.debug("If you can see this while the system property torchmaster.enableDebugLogging is not set to 1, report this on github!");
        TorchmasterContent.initialize();
    }

    public static void onWorldLoaded()
    {
        DreadLampFilterRegistry.clear();
        MegaTorchFilterRegistry.clear();

        VanillaCompat.registerDreadLampEntities(DreadLampFilterRegistry);
        VanillaCompat.registerTorchEntities(MegaTorchFilterRegistry);

        DreadLampFilterRegistry.applyListOverrides(TorchmasterRuntime.getConfig().getDreadLampEntityBlockListOverrides());
        MegaTorchFilterRegistry.applyListOverrides(TorchmasterRuntime.getConfig().getMegaTorchEntityBlockListOverrides());
    }
}
