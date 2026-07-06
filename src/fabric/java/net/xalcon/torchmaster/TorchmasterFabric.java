package net.xalcon.torchmaster;

import net.fabricmc.api.ModInitializer;
import net.xalcon.torchmaster.network.FabricLightSettingsNetworking;

public class TorchmasterFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        TorchmasterRuntime.init();
        FabricLightSettingsNetworking.registerServer();
        TorchmasterRuntime.onWorldLoaded();
    }


}
