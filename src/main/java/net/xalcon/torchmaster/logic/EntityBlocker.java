package net.xalcon.torchmaster.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
//? if >=1.21.2 {
/*import net.minecraft.world.entity.EntitySpawnReason;
*///?} else {
import net.minecraft.world.entity.MobSpawnType;
//?}
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface EntityBlocker
{
    String getIdentifier();
    //? if >=1.21.11 {
    /*Identifier getType();
    *///?} else {
    ResourceLocation getType();
    //?}
    //? if >=1.21.2 {
    /*boolean shouldBlockEntitySpawn(Entity entity, Level level, EntitySpawnReason spawnType);
    *///?} else {
    boolean shouldBlockEntitySpawn(Entity entity, Level level, MobSpawnType spawnType);
    //?}
    boolean shouldBlockVillageSiege(Level level, BlockPos pos);
    boolean shouldBlockVillageRaid(Level level, BlockPos pos);
}
