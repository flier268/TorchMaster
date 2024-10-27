package net.xalcon.torchmaster.compat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.logic.entityblocking.EntityBlockingEventHandler;

public class MCARebornCompat
{
    public static void apply()
    {
        if(FMLLoader.getLoadingModList().getMods().stream().anyMatch(mod -> mod.getModId().equals("mca")))
        {
            Torchmaster.Log.info("MCA Reborn detected, applying compat");
            MinecraftForge.EVENT_BUS.addListener(MCARebornCompat::onEntityJoinLevel);
        }
    }

    private static void onEntityJoinLevel(EntityJoinLevelEvent event)
    {
        if(event.getLevel() instanceof ServerLevel && !event.loadedFromDisk())
        {
            var type = EntityType.getKey(event.getEntity().getType()).toString();
            if(type.equals("mca:male_zombie_villager") || type.equals("mca:female_zombie_villager"))
            {
                var log = TorchmasterConfig.GENERAL.logSpawnChecks.get();
                if(!EntityBlockingEventHandler.shouldBlockEntity(event.getEntity(), event.getEntity().getOnPos()))
                {
                    if (log) Torchmaster.Log.debug("[MCA Compat] Allowing spawn of {}", EntityType.getKey(event.getEntity().getType()));
                    return;
                }

                if (log) Torchmaster.Log.debug("[MCA Compat] Blocking spawn of {}", EntityType.getKey(event.getEntity().getType()));
                event.setCanceled(true);
            }
        }
    }
}
