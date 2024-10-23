package net.xalcon.torchmaster.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.platform.Services;

import java.util.function.BooleanSupplier;

public class TorchmasterEventHandler
{
    private static boolean isPlayerTriggered(EntitySpawnReason spawnType)
    {
        switch(spawnType)
        {
            case BREEDING:
            case DISPENSER:
            case BUCKET:
            case CONVERSION:
            case SPAWN_ITEM_USE:
            case TRIGGERED:
            case COMMAND:
            case EVENT:
            case TRIAL_SPAWNER:
            case LOAD:
            case DIMENSION_TRAVEL:
                return true;
            case NATURAL:
            case CHUNK_GENERATION:
            case PATROL:
            case SPAWNER:
            case STRUCTURE:
            case MOB_SUMMONED:
            case REINFORCEMENT:
            case JOCKEY:
            default:
                return false;
        }
    }

    private static boolean isNaturalSpawn(EntitySpawnReason spawnType)
    {
        switch(spawnType)
        {
            case NATURAL:
            case CHUNK_GENERATION:
            case PATROL: // Patrol can be considered natural
            default:
                return true;
            case BREEDING:
            case CONVERSION:
            case BUCKET:
            case DISPENSER:
            case SPAWNER:
            case STRUCTURE:
            case MOB_SUMMONED:
            case JOCKEY:
            case REINFORCEMENT:
            case TRIGGERED:
            case SPAWN_ITEM_USE:
            case COMMAND:
            case EVENT:
            case LOAD:
            case TRIAL_SPAWNER:
            case DIMENSION_TRAVEL:
                return false;
        }
    }

    public static void onCheckSpawn(final EntitySpawnReason spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        Torchmaster.LOG.debug("CheckSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}", spawnType, EntityType.getKey(entity.getType()), location.x, location.y, location.z);

        // Check if the spawn was intentional (i.e. player invoked), we dont block those
        if(isPlayerTriggered(spawnType))
            return;

        // If aggressive spawn checks are disabled, check if other mods already explicitly allowed the spawn
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW)
            return;

        // Check if we should block artificial spawns
        if(config.getBlockOnlyNaturalSpawns() && !isNaturalSpawn(spawnType))
            return;

        var level = entity.getCommandSenderWorld();

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockEntity(entity, entity.getCommandSenderWorld(), spawnType))
            {
                container.setResult(EventResult.DENY);
                Torchmaster.LOG.debug("Blocking spawn of {}", EntityType.getKey(entity.getType()));
                //event.getEntity().addTag("torchmaster_removed_spawn");
            }
            else
            {
                Torchmaster.LOG.debug("Allowed spawn of {}", EntityType.getKey(entity.getType()));
            }
        });
    }

    public static void onVillageSiege(Level level, Vec3 attemptedSpawnPos, EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        if(!config.getBlockVillageSieges()) return;
        Torchmaster.LOG.debug("VillageSiegeEvent - Pos: {}", attemptedSpawnPos);
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW) return;

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockVillagePillagerSiege(attemptedSpawnPos))
            {
                container.setResult(EventResult.DENY);
                Torchmaster.LOG.debug("Blocking village siege @ {}", attemptedSpawnPos);
            }
            else
            {
                Torchmaster.LOG.debug("Allowed village siege @ {}", attemptedSpawnPos);
            }
        });
    }

    public static void onServerLevelTickEnd(MinecraftServer server, BooleanSupplier haveTime)
    {
        for(ServerLevel level : server.getAllLevels())
        {
            Torchmaster.getRegistryForLevel(level).ifPresent(reg -> reg.onGlobalTick(level));
        }
    }
}
