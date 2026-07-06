package net.xalcon.torchmaster;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.xalcon.torchmaster.client.TorchmasterClientEventAdapter;
import net.xalcon.torchmaster.network.FabricLightSettingsNetworking;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        TorchmasterClientEventAdapter.initializeFabricClient();
        FabricLightSettingsNetworking.registerClient();
    }
}
