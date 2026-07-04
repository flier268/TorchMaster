package net.xalcon.torchmaster.minecraft.light.megatorch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;

public class MegatorchBlockingLight implements MinecraftBlockingLight
{
    public static final VoxelShape SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private BlockPos pos;

    public MegatorchBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public String getLightSerializerType()
    {
        return MegatorchSerializer.SERIALIZER_KEY;
    }

    @Override
    public LightKind kind()
    {
        return LightKind.MEGA_TORCH;
    }

    @Override
    public BlockPosView position()
    {
        return MinecraftAdapterViews.blockPos(pos);
    }

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     *
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    public boolean cleanupCheck(Level level)
    {
        return level.isLoaded(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockMegaTorch.get();
    }

    @Override
    public String displayName()
    {
        return "Mega Torch";
    }

    public BlockPos getPos()
    {
        return pos;
    }
}
