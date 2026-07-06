package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class TorchmasterForge extends AbstractTorchmasterForge
{
    public TorchmasterForge()
    {
        MinecraftForge.EVENT_BUS.addListener(TorchmasterForge::loadComplete);
    }

    private static void loadComplete(Event event)
    {
        TorchmasterForgeLifecycleAdapter.onForgeEvent(event);
    }
}
