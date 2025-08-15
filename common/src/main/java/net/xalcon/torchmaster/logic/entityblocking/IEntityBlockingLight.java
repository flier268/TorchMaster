package net.xalcon.torchmaster.logic.entityblocking;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IEntityBlockingLight
{
    //boolean shouldBlockEntity(Entity entity, Level level, EntitySpawnReason spawnType);
    boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, EntitySpawnReason spawnType);
    boolean shouldBlockVillageZombieRaid(Vec3 pos);

    String getLightSerializerType();
    String getDisplayName();
    BlockPos getPos();

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    boolean cleanupCheck(Level level);
}
