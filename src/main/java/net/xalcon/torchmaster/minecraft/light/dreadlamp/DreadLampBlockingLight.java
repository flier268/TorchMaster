package net.xalcon.torchmaster.minecraft.light.dreadlamp;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.domain.LightDefinition;
import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;

public class DreadLampBlockingLight implements MinecraftBlockingLight
{
    public static final VoxelShape SHAPE = Block.createCuboidShape(1, 1, 1, 15, 15, 15);
    private final BlockPos pos;

    public DreadLampBlockingLight(BlockPos pos)
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

    public boolean cleanupCheck(World level)
    {
        //? if >=1.21.11 {
        /*return level.isInBuildLimit(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockDreadLamp.get();
        *///?} else {
        return level.canSetBlock(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockDreadLamp.get();
        //?}
    }

    @Override
    public String displayName()
    {
        return LightDefinition.DREAD_LAMP.displayName();
    }

    public BlockPos getPos()
    {
        return pos;
    }
}
