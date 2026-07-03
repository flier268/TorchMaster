package net.xalcon.torchmaster.logic.entityblocking.megatorch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
//? if >=1.21.2 {
/*import net.minecraft.world.entity.EntitySpawnReason;
*///?} else {
import net.minecraft.world.entity.MobSpawnType;
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.core.LightKind;
import net.xalcon.torchmaster.core.LightRules;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.minecraft.MinecraftAdapterViews;
import net.xalcon.torchmaster.platform.Services;

public class MegatorchEntityBlockingLight implements IEntityBlockingLight
{
    public static final VoxelShape SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private BlockPos pos;

    public MegatorchEntityBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    //? if >=1.21.2 {
    /*public boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, EntitySpawnReason spawnType)
    *///?} else {
    public boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, MobSpawnType spawnType)
    //?}
    {
        return LightRules.blocksEntity(
                LightKind.MEGA_TORCH,
                MinecraftAdapterViews.entityFilter(Torchmaster.MegaTorchFilterRegistry),
                MinecraftAdapterViews.entityTypeKey(EntityType.getKey(entityType)),
                MinecraftAdapterViews.vec3(pos),
                MinecraftAdapterViews.blockPos(this.pos),
                Services.PLATFORM.getConfig().getMegaTorchRadius());
    }

    @Override
    public boolean shouldBlockVillageZombieRaid(Vec3 pos)
    {
        return LightRules.blocksVillageSiege(
                LightKind.MEGA_TORCH,
                MinecraftAdapterViews.vec3(pos),
                MinecraftAdapterViews.blockPos(this.pos),
                Services.PLATFORM.getConfig().getMegaTorchRadius());
    }

    @Override
    public String getLightSerializerType()
    {
        return MegatorchSerializer.SERIALIZER_KEY;
    }

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     *
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    @Override
    public boolean cleanupCheck(Level level)
    {
        return level.isLoaded(this.pos) && level.getBlockState(pos).getBlock() != ModRegistry.blockMegaTorch.get();
    }

    @Override
    public String getDisplayName()
    {
        return "Mega Torch";
    }

    @Override
    public BlockPos getPos()
    {
        return pos;
    }
}
