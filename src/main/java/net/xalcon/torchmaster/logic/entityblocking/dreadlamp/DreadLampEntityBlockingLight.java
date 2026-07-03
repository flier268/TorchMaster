package net.xalcon.torchmaster.logic.entityblocking.dreadlamp;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.adapter.BlockPosView;
import net.xalcon.torchmaster.core.LightKind;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.minecraft.MinecraftAdapterViews;

public class DreadLampEntityBlockingLight implements IEntityBlockingLight
{
    public static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 15, 15);
    private final BlockPos pos;

    public DreadLampEntityBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public String getLightSerializerType()
    {
        return DreadLampSerializer.SERIALIZER_KEY;
    }

    @Override
    public LightKind kind()
    {
        return LightKind.DREAD_LAMP;
    }

    @Override
    public BlockPosView position()
    {
        return MinecraftAdapterViews.blockPos(pos);
    }

    public boolean cleanupCheck(Level level)
    {
        return level.isLoaded(this.pos) && level.getBlockState(pos).getBlock() != ModRegistry.blockDreadLamp.get();
    }

    @Override
    public String displayName()
    {
        return "Dread Lamp";
    }

    public BlockPos getPos()
    {
        return pos;
    }
}
