package net.xalcon.torchmaster.minecraft.light.dreadlamp;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;

public class DreadLampBlockingLight implements MinecraftBlockingLight
{
    public static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 15, 15);
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

    public boolean cleanupCheck(Level level)
    {
        return level.isLoaded(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockDreadLamp.get();
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
