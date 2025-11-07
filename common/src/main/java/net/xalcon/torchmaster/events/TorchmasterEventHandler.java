package net.xalcon.torchmaster.events;

import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
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

        var level = entity.level();
        var entityType = entity.getType();

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockEntityType(entityType, entity.level(), location, spawnType))
            {
                container.setResult(EventResult.DENY);
                Torchmaster.LOG.debug("Blocking spawn of {}", EntityType.getKey(entityType));
            }
            else
            {
                Torchmaster.LOG.debug("Allowed spawn of {}", EntityType.getKey(entityType));
            }
        });
    }

    public static void onPlayerSpawnPhantoms(Player player, final Vec3 location, final EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        Torchmaster.LOG.debug("PlayerSpawnPhantoms - Pos: {}/{}/{}", location.x, location.y, location.z);

        // If aggressive spawn checks are disabled, check if other mods already explicitly allowed the spawn
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW)
            return;

        var level = player.level();

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockEntityType(EntityType.PHANTOM, level, player.position(), EntitySpawnReason.NATURAL))
            {
                container.setResult(EventResult.DENY);
                Torchmaster.LOG.debug("Blocking spawn of {}", EntityType.getKey(EntityType.PHANTOM));
            }
            else
            {
                Torchmaster.LOG.debug("Allowed spawn of {}", EntityType.getKey(EntityType.PHANTOM));
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
            if(reg.shouldBlockVillageZombieRaid(attemptedSpawnPos))
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

    public static void onPatrolMemberSpawn(ServerLevel level, Vec3 location, boolean leader, Entity entity, EventResultContainer container) {

        var config = Services.PLATFORM.getConfig();
        Torchmaster.LOG.debug("PatrolMemberSpawn - Pos: {}/{}/{}", location.x, location.y, location.z);

        // If aggressive spawn checks are disabled, check if other mods already explicitly allowed the spawn
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW)
            return;

        var entityType = entity.getType();

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockEntityType(entityType, level, location, EntitySpawnReason.NATURAL))
            {
                container.setResult(EventResult.DENY);
                Torchmaster.LOG.debug("Blocking spawn of {}, (Leader: {})", EntityType.getKey(entityType), leader);
            }
            else
            {
                Torchmaster.LOG.debug("Allowed spawn of {}, (Leader: {})", EntityType.getKey(entityType), leader);
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
