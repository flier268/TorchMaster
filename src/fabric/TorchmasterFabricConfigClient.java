package net.xalcon.torchmaster;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.xalcon.torchmaster.client.TorchmasterLightRangeDisplay;

@Environment(EnvType.CLIENT)
public final class TorchmasterFabricConfigClient
{
    private TorchmasterFabricConfigClient()
    {
    }

    public static void register()
    {
        ClientTickEvents.END_CLIENT_TICK.register(client -> TorchmasterLightRangeDisplay.tick());
    }
}
