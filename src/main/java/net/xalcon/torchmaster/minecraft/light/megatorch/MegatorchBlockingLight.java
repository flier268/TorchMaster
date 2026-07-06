package net.xalcon.torchmaster.minecraft.light.megatorch;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.domain.LightDefinition;
import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;

public class MegatorchBlockingLight implements MinecraftBlockingLight
{
    private BlockPos pos;
    private final boolean diamondBase;

    public MegatorchBlockingLight(BlockPos pos)
    {
        this(pos, false);
    }

    public MegatorchBlockingLight(BlockPos pos, boolean diamondBase)
    {
        this.pos = pos;
        this.diamondBase = diamondBase;
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
    public boolean cleanupCheck(World level)
    {
        //? if >=1.21.11 {
        /*return level.isInBuildLimit(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockMegaTorch.get();
        *///?} else {
        return level.canSetBlock(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockMegaTorch.get();
        //?}
    }

    @Override
    public String displayName()
    {
        return LightDefinition.MEGA_TORCH.displayName();
    }

    @Override
    public boolean blocksNaturalSpawnsOnly()
    {
        return diamondBase;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public boolean hasDiamondBase()
    {
        return diamondBase;
    }
}
