package net.xalcon.torchmaster.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
//? if >=1.21.2 {
/*import net.minecraft.world.entity.EntitySpawnReason;
*///?} else {
import net.minecraft.world.entity.MobSpawnType;
//?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.core.SpawnBlockingRules;
import net.xalcon.torchmaster.minecraft.MinecraftConfigView;
import net.xalcon.torchmaster.minecraft.MinecraftEventResultAdapter;
import net.xalcon.torchmaster.minecraft.MinecraftSpawnReasonAdapter;
import net.xalcon.torchmaster.platform.Services;

import java.util.function.BooleanSupplier;

public class TorchmasterEventHandler
{
    //? if >=1.21.2 {
    /*public static void onCheckSpawn(final EntitySpawnReason spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    *///?} else {
    public static void onCheckSpawn(final MobSpawnType spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    //?}
    {
        ITorchmasterConfig config = Services.PLATFORM.getConfig();
        Torchmaster.LOG.debug("CheckSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}", spawnType, EntityType.getKey(entity.getType()), location.x, location.y, location.z);

        if(SpawnBlockingRules.shouldSkipEntitySpawnCheck(
                MinecraftSpawnReasonAdapter.toPort(spawnType),
                MinecraftEventResultAdapter.toPort(container.getResult()),
                new MinecraftConfigView(config)))
            return;

        //? if >=1.21.2 {
        /*Level level = entity.level();
        *///?} else {
        Level level = entity.getCommandSenderWorld();
        //?}
        EntityType<?> entityType = entity.getType();

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockEntityType(entityType,
                    //? if >=1.21.2 {
                    /*entity.level()
                    *///?} else {
                    entity.getCommandSenderWorld()
                    //?}
                    , location, spawnType))
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
        ITorchmasterConfig config = Services.PLATFORM.getConfig();
        Torchmaster.LOG.debug("PlayerSpawnPhantoms - Pos: {}/{}/{}", location.x, location.y, location.z);

        if(SpawnBlockingRules.shouldSkipPhantomSpawnCheck(
                MinecraftEventResultAdapter.toPort(container.getResult()),
                new MinecraftConfigView(config)))
            return;

        //? if >=1.21.2 {
        /*Level level = player.level();
        *///?} else {
        Level level = player.getCommandSenderWorld();
        //?}

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockEntityType(EntityType.PHANTOM, level, player.position(),
                    //? if >=1.21.2 {
                    /*EntitySpawnReason.NATURAL
                    *///?} else {
                    MobSpawnType.NATURAL
                    //?}
            ))
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
        ITorchmasterConfig config = Services.PLATFORM.getConfig();
        Torchmaster.LOG.debug("VillageSiegeEvent - Pos: {}", attemptedSpawnPos);
        if(SpawnBlockingRules.shouldSkipVillageSiegeCheck(
                MinecraftEventResultAdapter.toPort(container.getResult()),
                new MinecraftConfigView(config)))
            return;

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

    public static void onServerLevelTickEnd(MinecraftServer server, BooleanSupplier haveTime)
    {
        for(ServerLevel level : server.getAllLevels())
        {
            Torchmaster.getRegistryForLevel(level).ifPresent(reg -> reg.onGlobalTick(level));
        }
    }
}
