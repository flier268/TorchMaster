package net.xalcon.torchmaster;

//? if forge && >=1.19 {
/*import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class TorchmasterForge extends AbstractTorchmasterForge
{
    public TorchmasterForge() {
        MinecraftForge.EVENT_BUS.addListener(TorchmasterForge::loadComplete);
    }

    private static void loadComplete(LevelEvent.Load event)
    {
        TorchmasterRuntime.onWorldLoaded();
    }

}
*///?} else if forge {
/*import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class TorchmasterForge extends AbstractTorchmasterForge
{
    public TorchmasterForge() {
        MinecraftForge.EVENT_BUS.addListener(TorchmasterForge::loadComplete);
    }

    private static void loadComplete(WorldEvent.Load event)
    {
        TorchmasterRuntime.onWorldLoaded();
    }

}
*///?}
