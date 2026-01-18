package net.xalcon.torchmaster.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;

public interface EntityBlocker
{
    String getIdentifier();
    Identifier getType();
    boolean shouldBlockEntitySpawn(Entity entity, Level level, EntitySpawnReason spawnType);
    boolean shouldBlockVillageSiege(Level level, BlockPos pos);
    boolean shouldBlockVillageRaid(Level level, BlockPos pos);
}
