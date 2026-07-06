package net.xalcon.torchmaster.events;

import net.minecraft.entity.Entity;
//? if >=1.16.5
import net.minecraft.entity.SpawnReason;
//? if <1.16.5
//import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlocker;
import net.xalcon.torchmaster.minecraft.spawn.MinecraftSpawnEventHooks;

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
        MinecraftSpawnEventHooks.onCheckSpawn(spawnType, entity, location, container);
    }

    public static void onPlayerSpawnPhantoms(PlayerEntity player, final Vec3d location, final EventResultContainer container)
    {
        MinecraftSpawnEventHooks.onPlayerSpawnPhantoms(player, location, container);
    }

    public static void onVillageSiege(World level, Vec3d attemptedSpawnPos, EventResultContainer container)
    {
        MinecraftSpawnEventHooks.onVillageSiege(level, attemptedSpawnPos, container);
    }

    public static void onServerLevelTickEnd(MinecraftServer server, BooleanSupplier haveTime)
    {
        MinecraftSpawnBlocker.tickStores(server);
    }
}
