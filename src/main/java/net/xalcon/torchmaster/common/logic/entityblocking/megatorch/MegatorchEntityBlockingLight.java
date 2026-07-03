package net.xalcon.torchmaster.common.logic.entityblocking.megatorch;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.logic.DistanceLogics;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;

public class MegatorchEntityBlockingLight implements IEntityBlockingLight
{
    public static final VoxelShape SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private BlockPos pos;
    private boolean diamondBase;

    public MegatorchEntityBlockingLight(BlockPos pos)
    {
        this(pos, false);
    }

    public MegatorchEntityBlockingLight(BlockPos pos, boolean diamondBase)
    {
        this.pos = pos;
        this.diamondBase = diamondBase;
    }

    @Override
    public boolean shouldBlockEntity(Entity entity, BlockPos pos)
    {
        if(this.diamondBase)
            return false;

        return Torchmaster.MegaTorchFilterRegistry.containsEntity(entity.getType().getRegistryName())
            && DistanceLogics.Cubic.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), this.pos, TorchmasterConfig.GENERAL.megaTorchRadius.get());
    }

    @Override
    public boolean shouldBlockEntity(EntityType<?> entityType, BlockPos pos)
    {
        if(this.diamondBase)
            return false;

        return Torchmaster.MegaTorchFilterRegistry.containsEntity(entityType.getRegistryName())
            && DistanceLogics.Cubic.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), this.pos, TorchmasterConfig.GENERAL.megaTorchRadius.get());
    }

    @Override
    public boolean shouldBlockNaturalSpawnPosition(BlockPos pos)
    {
        return this.diamondBase
            && DistanceLogics.Cubic.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), this.pos, TorchmasterConfig.GENERAL.megaTorchRadius.get());
    }

    @Override
    public boolean shouldBlockNaturalSpawnChunk(int chunkX, int chunkZ)
    {
        if(!this.diamondBase)
            return false;

        int chunkRadius = (TorchmasterConfig.GENERAL.megaTorchRadius.get() + 15) >> 4;
        int torchChunkX = this.pos.getX() >> 4;
        int torchChunkZ = this.pos.getZ() >> 4;
        return Math.abs(chunkX - torchChunkX) <= chunkRadius
            && Math.abs(chunkZ - torchChunkZ) <= chunkRadius;
    }

    @Override
    public String getLightSerializerKey()
    {
        return MegatorchSerializer.SERIALIZER_KEY;
    }

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     *
     * @param world the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    @Override
    public boolean cleanupCheck(World world)
    {
        return world.isBlockLoaded(this.pos) && world.getBlockState(pos).getBlock() != ModBlocks.blockMegaTorch;
    }

    @Override
    public String getName()
    {
        return "Mega Torch";
    }

    @Override
    public BlockPos getPos()
    {
        return pos;
    }

    public boolean hasDiamondBase()
    {
        return diamondBase;
    }
}
