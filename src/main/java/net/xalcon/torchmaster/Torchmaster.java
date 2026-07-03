package net.xalcon.torchmaster;

//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.xalcon.torchmaster.compat.VanillaCompat;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.logic.entityblocking.FilteredLightManager;
import net.xalcon.torchmaster.logic.entityblocking.IBlockingLightManager;
import net.xalcon.torchmaster.platform.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Optional;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class Torchmaster
{
    public static final EntityFilterList MegaTorchFilterRegistry = new EntityFilterList(id("entity_filter/mega_torch"));
    public static final EntityFilterList DreadLampFilterRegistry = new EntityFilterList(id("entity_filter/dread_lamp"));

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
        ModRegistry.initialize();
    }

    public static Optional<IBlockingLightManager> getRegistryForLevel(Level level)
    {
        if(level instanceof ServerLevel)
        {
            ServerLevel serverLevel = (ServerLevel)level;
            //? if >=1.21.11 {
            /*String dimensionIdentifier = level.dimension().identifier().toDebugFileName();
*///?} elif >=1.17 {
            String dimensionIdentifier = level.dimension().location().toDebugFileName();
//?} else {
            /*String dimensionIdentifier = level.dimension().location().toString().replace(':', '_').replace('/', '_');
            *///?}
            //? if >=1.21.5 {
            /*return Optional.of(serverLevel.getDataStorage().computeIfAbsent(FilteredLightManager.type("torchmaster_lights_" + dimensionIdentifier)));
*///?} elif >=1.21 {
            return Optional.of(serverLevel.getDataStorage().computeIfAbsent(FilteredLightManager.Factory, "torchmaster_lights_" + dimensionIdentifier));
//?} elif >=1.17 {
            /*return Optional.of(serverLevel.getDataStorage().computeIfAbsent(FilteredLightManager::load, FilteredLightManager::new, "torchmaster_lights_" + dimensionIdentifier));
*///?} else {
            /*return Optional.of(serverLevel.getDataStorage().computeIfAbsent(FilteredLightManager::new, "torchmaster_lights_" + dimensionIdentifier));
            *///?}
        }
        return Optional.empty();
    }

    //? if >=1.21.11 {
    /*private static Identifier id(String path)
    *///?} else {
    private static ResourceLocation id(String path)
    //?}
    {
        //? if >=1.21.11 {
        /*return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    *///?} elif >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
	//?} else {
        /*return new ResourceLocation(Constants.MOD_ID, path);
        *///?}
    }

    public static void onWorldLoaded()
    {
        DreadLampFilterRegistry.clear();
        MegaTorchFilterRegistry.clear();

        VanillaCompat.registerDreadLampEntities(DreadLampFilterRegistry);
        VanillaCompat.registerTorchEntities(MegaTorchFilterRegistry);

        DreadLampFilterRegistry.applyListOverrides(Torchmaster.getConfig().getDreadLampEntityBlockListOverrides());
        MegaTorchFilterRegistry.applyListOverrides(Torchmaster.getConfig().getMegaTorchEntityBlockListOverrides());
    }
}
