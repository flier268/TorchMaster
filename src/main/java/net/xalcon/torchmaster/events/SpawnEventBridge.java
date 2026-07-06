package net.xalcon.torchmaster.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
//? if >=1.16.5
import net.minecraft.entity.SpawnReason;
//? if <1.16.5
//import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.domain.SpawnBlockingRules;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftConfigView;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEventResultAdapter;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlocker;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnReasonAdapter;
import net.xalcon.torchmaster.platform.Services;

import java.util.function.BooleanSupplier;

public class SpawnEventBridge
{
    //? if fabric && forge && >=1.21.2 {
    /*public static void onCheckSpawn(final EntitySpawnReason spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    *///?} else {
    //? if >=1.16.5
    public static void onCheckSpawn(final SpawnReason spawnType, final Entity entity, final Vec3d location, final EventResultContainer container)
    //? if <1.16.5
    //public static void onCheckSpawn(final SpawnType spawnType, final Entity entity, final Vec3d location, final EventResultContainer container)
    //?}
    {
        ITorchmasterConfig config = Services.PLATFORM.getConfig();
        TorchmasterRuntime.LOG.debug("CheckSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}", spawnType, EntityType.getId(entity.getType()), location.x, location.y, location.z);

        if(SpawnBlockingRules.shouldSkipEntitySpawnCheck(
                MinecraftSpawnReasonAdapter.toPort(spawnType),
                MinecraftEventResultAdapter.toPort(container.getResult()),
                new MinecraftConfigView(config)))
            return;

        //? if fabric && forge && >=1.21.2 {
        /*Level level = entity.level();
        *///?} else {
        World level = entity.getEntityWorld();
        //?}
        if(MinecraftSpawnBlocker.shouldBlockEntity(
                level,
                entity.getType(),
                location,
                MinecraftSpawnReasonAdapter.toPort(spawnType)))
        {
            container.setResult(EventResult.DENY);
            TorchmasterRuntime.LOG.debug("Blocking spawn of {}", EntityType.getId(entity.getType()));
        }
        else
        {
            TorchmasterRuntime.LOG.debug("Allowed spawn of {}", EntityType.getId(entity.getType()));
        }
    }

    public static void onPlayerSpawnPhantoms(PlayerEntity player, final Vec3d location, final EventResultContainer container)
    {
        ITorchmasterConfig config = Services.PLATFORM.getConfig();
        TorchmasterRuntime.LOG.debug("PlayerSpawnPhantoms - Pos: {}/{}/{}", location.x, location.y, location.z);

        if(SpawnBlockingRules.shouldSkipPhantomSpawnCheck(
                MinecraftEventResultAdapter.toPort(container.getResult()),
                new MinecraftConfigView(config)))
            return;

        //? if fabric && forge && >=1.21.2 {
        /*Level level = player.level();
        *///?} else {
        World level = player.getEntityWorld();
        //?}

        if(MinecraftSpawnBlocker.shouldBlockEntity(
                level,
                EntityType.PHANTOM,
                //? if >=1.21.11
                //player.getEntityPos()
                //? if <1.21.11
                player.getPos()
                ,
                MinecraftSpawnReasonAdapter.toPort(
                //? if fabric && forge && >=1.21.2 {
                /*EntitySpawnReason.NATURAL
                *///?} else {
                //? if >=1.16.5
                SpawnReason.NATURAL
                //? if <1.16.5
                //SpawnType.NATURAL
                //?}
                )))
        {
            container.setResult(EventResult.DENY);
            TorchmasterRuntime.LOG.debug("Blocking spawn of {}", EntityType.getId(EntityType.PHANTOM));
        }
        else
        {
            TorchmasterRuntime.LOG.debug("Allowed spawn of {}", EntityType.getId(EntityType.PHANTOM));
        }
    }

    public static void onVillageSiege(World level, Vec3d attemptedSpawnPos, EventResultContainer container)
    {
        ITorchmasterConfig config = Services.PLATFORM.getConfig();
        TorchmasterRuntime.LOG.debug("VillageSiegeEvent - Pos: {}", attemptedSpawnPos);
        if(SpawnBlockingRules.shouldSkipVillageSiegeCheck(
                MinecraftEventResultAdapter.toPort(container.getResult()),
                new MinecraftConfigView(config)))
            return;

        if(MinecraftSpawnBlocker.shouldBlockVillageSiege(level, attemptedSpawnPos))
        {
            container.setResult(EventResult.DENY);
            TorchmasterRuntime.LOG.debug("Blocking village siege @ {}", attemptedSpawnPos);
        }
        else
        {
            TorchmasterRuntime.LOG.debug("Allowed village siege @ {}", attemptedSpawnPos);
        }
    }

    public static void onServerLevelTickEnd(MinecraftServer server, BooleanSupplier haveTime)
    {
        MinecraftSpawnBlocker.tickStores(server);
    }
}
