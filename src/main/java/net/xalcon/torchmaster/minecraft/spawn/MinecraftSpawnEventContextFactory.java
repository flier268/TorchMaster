package net.xalcon.torchmaster.minecraft.spawn;

import net.minecraft.entity.EntityType;
//? if >=1.16.5
import net.minecraft.entity.SpawnReason;
//? if <1.16.5
//import net.minecraft.entity.SpawnType;
import net.minecraft.util.math.Vec3d;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnReasonAdapter;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.EventResultPort;
import net.xalcon.torchmaster.port.Vec3View;

public final class MinecraftSpawnEventContextFactory
{
    private MinecraftSpawnEventContextFactory()
    {
    }

    //? if fabric && forge && >=1.21.2 {
    /*public static MinecraftSpawnEventContext entity(EntityType<?> entityType, Vec3 location, EntitySpawnReason spawnType, EventResultContainer container)
    *///?} else {
    //? if >=1.16.5
    public static MinecraftSpawnEventContext entity(EntityType<?> entityType, Vec3d location, SpawnReason spawnType, EventResultContainer container)
    //? if <1.16.5
    //public static MinecraftSpawnEventContext entity(EntityType<?> entityType, Vec3d location, SpawnType spawnType, EventResultContainer container)
    //?}
    {
        return entity(
                MinecraftAdapterViews.entityTypeKey(EntityType.getId(entityType)),
                MinecraftAdapterViews.vec3(location),
                MinecraftSpawnReasonAdapter.toPort(spawnType),
                MinecraftSpawnEventContainers.portResult(container));
    }

    //? if fabric && forge && >=1.21.2 {
    /*public static MinecraftSpawnEventContext phantom(Vec3 location, EntitySpawnReason spawnType, EventResultContainer container)
    *///?} else {
    //? if >=1.16.5
    public static MinecraftSpawnEventContext phantom(Vec3d location, SpawnReason spawnType, EventResultContainer container)
    //? if <1.16.5
    //public static MinecraftSpawnEventContext phantom(Vec3d location, SpawnType spawnType, EventResultContainer container)
    //?}
    {
        return phantom(
                MinecraftAdapterViews.entityTypeKey(EntityType.getId(EntityType.PHANTOM)),
                MinecraftAdapterViews.vec3(location),
                MinecraftSpawnReasonAdapter.toPort(spawnType),
                MinecraftSpawnEventContainers.portResult(container));
    }

    public static MinecraftSpawnEventContext villageSiege(Vec3d attemptedSpawnPos, EventResultContainer container)
    {
        return villageSiege(
                MinecraftAdapterViews.vec3(attemptedSpawnPos),
                MinecraftSpawnEventContainers.portResult(container));
    }

    static MinecraftSpawnEventContext entity(EntityTypeKey entityType, Vec3View position, net.xalcon.torchmaster.port.SpawnReason spawnReason, EventResultPort currentResult)
    {
        return MinecraftSpawnEventContext.entity(entityType, position, spawnReason, currentResult);
    }

    static MinecraftSpawnEventContext phantom(EntityTypeKey entityType, Vec3View position, net.xalcon.torchmaster.port.SpawnReason spawnReason, EventResultPort currentResult)
    {
        return MinecraftSpawnEventContext.phantom(entityType, position, spawnReason, currentResult);
    }

    static MinecraftSpawnEventContext villageSiege(Vec3View position, EventResultPort currentResult)
    {
        return MinecraftSpawnEventContext.villageSiege(position, currentResult);
    }
}
