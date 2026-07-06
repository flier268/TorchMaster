package net.xalcon.torchmaster.minecraft.spawn;

import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.EventResultPort;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;

public final class MinecraftSpawnEventContext
{
    public enum Kind
    {
        ENTITY,
        PHANTOM,
        VILLAGE_SIEGE
    }

    private final Kind kind;
    private final EntityTypeKey entityType;
    private final Vec3View position;
    private final SpawnReason spawnReason;
    private final EventResultPort currentResult;

    private MinecraftSpawnEventContext(Kind kind, EntityTypeKey entityType, Vec3View position, SpawnReason spawnReason, EventResultPort currentResult)
    {
        this.kind = kind;
        this.entityType = entityType;
        this.position = position;
        this.spawnReason = spawnReason;
        this.currentResult = currentResult;
    }

    public static MinecraftSpawnEventContext entity(EntityTypeKey entityType, Vec3View position, SpawnReason spawnReason, EventResultPort currentResult)
    {
        return new MinecraftSpawnEventContext(Kind.ENTITY, entityType, position, spawnReason, currentResult);
    }

    public static MinecraftSpawnEventContext phantom(EntityTypeKey entityType, Vec3View position, SpawnReason spawnReason, EventResultPort currentResult)
    {
        return new MinecraftSpawnEventContext(Kind.PHANTOM, entityType, position, spawnReason, currentResult);
    }

    public static MinecraftSpawnEventContext villageSiege(Vec3View position, EventResultPort currentResult)
    {
        return new MinecraftSpawnEventContext(Kind.VILLAGE_SIEGE, null, position, null, currentResult);
    }

    public Kind kind()
    {
        return kind;
    }

    public EntityTypeKey entityType()
    {
        return entityType;
    }

    public Vec3View position()
    {
        return position;
    }

    public SpawnReason spawnReason()
    {
        return spawnReason;
    }

    public EventResultPort currentResult()
    {
        return currentResult;
    }
}
