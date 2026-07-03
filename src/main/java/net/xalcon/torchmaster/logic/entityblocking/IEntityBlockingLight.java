package net.xalcon.torchmaster.logic.entityblocking;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
//? if >=1.21.2 {
/*import net.minecraft.world.entity.EntitySpawnReason;
*///?} else {
import net.minecraft.world.entity.MobSpawnType;
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IEntityBlockingLight
{
    //boolean shouldBlockEntity(Entity entity, Level level, EntitySpawnReason spawnType);
    //? if >=1.21.2 {
    /*boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, EntitySpawnReason spawnType);
    *///?} else {
    boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, MobSpawnType spawnType);
    //?}
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
