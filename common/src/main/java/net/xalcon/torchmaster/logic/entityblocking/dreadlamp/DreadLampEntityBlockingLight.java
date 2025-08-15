package net.xalcon.torchmaster.logic.entityblocking.dreadlamp;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.logic.IDistanceLogic;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.platform.Services;

public class DreadLampEntityBlockingLight implements IEntityBlockingLight
{
    public static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 15, 15);
    private final BlockPos pos;

    public DreadLampEntityBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, EntitySpawnReason spawnType)
    {
        return Torchmaster.DreadLampFilterRegistry.containsEntity(EntityType.getKey(entityType))
            && IDistanceLogic.Cubic.isPositionInRange(pos.x, pos.y, pos.z, this.pos, Services.PLATFORM.getConfig().getDreadLampRadius());
    }

    @Override
    public boolean shouldBlockVillageZombieRaid(Vec3 pos)
    {
        return false;
    }

    @Override
    public String getLightSerializerType()
    {
        return DreadLampSerializer.SERIALIZER_KEY;
    }

    @Override
    public boolean cleanupCheck(Level level)
    {
        return level.isLoaded(this.pos) && level.getBlockState(pos).getBlock() != ModRegistry.blockDreadLamp.get();
    }

    @Override
    public String getDisplayName()
    {
        return "Dread Lamp";
    }

    @Override
    public BlockPos getPos()
    {
        return pos;
    }
}
