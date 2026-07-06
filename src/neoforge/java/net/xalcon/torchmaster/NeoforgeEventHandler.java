package net.xalcon.torchmaster;

import net.minecraft.util.math.Vec3d;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent;
import net.neoforged.neoforge.event.village.VillageSiegeEvent;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventContainers;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventHooks;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoforgeEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCheckSpawn(MobSpawnEvent.PositionCheck event)
    {
        EventResultContainer container = NeoforgeSpawnEventResults.positionCheckContainer(event.getResult());

        var spawnType = event.getSpawnType();
        var entity = event.getEntity();
        var pos = new Vec3d(event.getX(), event.getY(), event.getZ());
        MinecraftSpawnEventHooks.onCheckSpawn(spawnType, entity, pos, container);

        event.setResult(NeoforgeSpawnEventResults.toPositionCheck(container));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDoSpecialSpawn(MobSpawnEvent.PositionCheck event)
    {
        EventResultContainer container = NeoforgeSpawnEventResults.positionCheckContainer(event.getResult());

        var spawnType = event.getSpawnType();
        var entity = event.getEntity();
        var pos = new Vec3d(event.getX(), event.getY(), event.getZ());
        MinecraftSpawnEventHooks.onCheckSpawn(spawnType, entity, pos, container);

        event.setResult(NeoforgeSpawnEventResults.toPositionCheck(container));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerSpawnPhantomsEvent(PlayerSpawnPhantomsEvent event)
    {
        EventResultContainer container = NeoforgeSpawnEventResults.phantomContainer(event.getResult());

        var player = event.getEntity();
        var pos = new Vec3d(player.getX(), player.getY(), player.getZ());
        MinecraftSpawnEventHooks.onPlayerSpawnPhantoms(player, pos, container);

        event.setResult(NeoforgeSpawnEventResults.toPhantom(container));
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onVillageSiegeEvent(VillageSiegeEvent event)
    {
        EventResultContainer container = MinecraftSpawnEventContainers.defaultContainer();

        MinecraftSpawnEventHooks.onVillageSiege(event.getLevel(), event.getAttemptedSpawnPos(), container);

        if(MinecraftSpawnEventContainers.denies(container))
            event.setCanceled(true);
    }
}
