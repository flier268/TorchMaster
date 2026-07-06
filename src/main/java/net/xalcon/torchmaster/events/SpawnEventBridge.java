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
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlocker;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventContext;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventContextFactory;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventRuntime;

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
        //? if fabric && forge && >=1.21.2 {
        /*Level level = entity.level();
        *///?} else {
        World level = entity.getEntityWorld();
        //?}
        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.entity(entity.getType(), location, spawnType, container);
        MinecraftSpawnEventRuntime.applyDeny(container, MinecraftSpawnEventRuntime.shouldDenyEntitySpawn(context,
                () -> MinecraftSpawnBlocker.shouldBlockEntity(level, entity.getType(), location, context.spawnReason())));
    }

    public static void onPlayerSpawnPhantoms(PlayerEntity player, final Vec3d location, final EventResultContainer container)
    {
        //? if fabric && forge && >=1.21.2 {
        /*Level level = player.level();
        *///?} else {
        World level = player.getEntityWorld();
        //?}

        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.phantom(location,
                //? if fabric && forge && >=1.21.2 {
                /*EntitySpawnReason.NATURAL
                *///?} else {
                //? if >=1.16.5
                SpawnReason.NATURAL
                //? if <1.16.5
                //SpawnType.NATURAL
                //?}
                , container);
        MinecraftSpawnEventRuntime.applyDeny(container, MinecraftSpawnEventRuntime.shouldDenyPhantomSpawn(context,
                () -> MinecraftSpawnBlocker.shouldBlockEntity(
                        level,
                        EntityType.PHANTOM,
                        //? if >=1.21.11
                        //player.getEntityPos()
                        //? if <1.21.11
                        player.getPos()
                        ,
                        context.spawnReason())));
    }

    public static void onVillageSiege(World level, Vec3d attemptedSpawnPos, EventResultContainer container)
    {
        MinecraftSpawnEventContext context = MinecraftSpawnEventContextFactory.villageSiege(attemptedSpawnPos, container);
        MinecraftSpawnEventRuntime.applyDeny(container, MinecraftSpawnEventRuntime.shouldDenyVillageSiege(context,
                () -> MinecraftSpawnBlocker.shouldBlockVillageSiege(level, attemptedSpawnPos)));
    }

    public static void onServerLevelTickEnd(MinecraftServer server, BooleanSupplier haveTime)
    {
        MinecraftSpawnBlocker.tickStores(server);
    }
}
