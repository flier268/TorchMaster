package net.xalcon.torchmaster;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.xalcon.torchmaster.platform.FabricClientRenderHelper;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer {
    public void onInitializeClient()
    {
        FabricClientRenderHelper.configureBlockRenderLayers();
    }
}
