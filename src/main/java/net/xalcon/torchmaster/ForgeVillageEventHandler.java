package net.xalcon.torchmaster;

//? if forge && >=1.19 {
/*import net.minecraftforge.event.village.VillageSiegeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnEventContainers;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeVillageEventHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onVillageSiegeEvent(VillageSiegeEvent event)
    {
        EventResultContainer container = MinecraftSpawnEventContainers.defaultContainer();

        SpawnEventBridge.onVillageSiege(event.getLevel(), event.getAttemptedSpawnPos(), container);

        if(MinecraftSpawnEventContainers.denies(container))
            event.setCanceled(true);
    }
}
*///?} else if forge {
/*import net.minecraftforge.event.village.VillageSiegeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.SpawnEventBridge;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnEventContainers;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ForgeVillageEventHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onVillageSiegeEvent(VillageSiegeEvent event)
    {
        EventResultContainer container = MinecraftSpawnEventContainers.defaultContainer();

        SpawnEventBridge.onVillageSiege(event.getWorld(), event.getAttemptedSpawnPos(), container);

        if(MinecraftSpawnEventContainers.denies(container))
            event.setCanceled(true);
    }
}
*///?}
