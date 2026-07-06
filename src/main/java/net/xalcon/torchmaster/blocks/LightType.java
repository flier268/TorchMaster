package net.xalcon.torchmaster.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.domain.LightDefinition;
import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.light.megatorch.MegatorchBlockingLight;
import net.xalcon.torchmaster.minecraft.light.dreadlamp.DreadLampBlockingLight;

import java.util.function.Function;

public enum LightType
{
    MegaTorch(
            LightDefinition.MEGA_TORCH,
            Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            new Vec3d(.5f, 1f, .5f),
            MegatorchBlockingLight::new
    ),
    DreadLamp(
            LightDefinition.DREAD_LAMP,
            Block.createCuboidShape(1, 1, 1, 15, 15, 15),
            new Vec3d(.5f, .3f, .5f),
            DreadLampBlockingLight::new
    );

    private final LightDefinition definition;
    private final VoxelShape shape;
    private final Vec3d flameOffset;
    private final Function<BlockPos, MinecraftBlockingLight> lightFactory;

    LightType(LightDefinition definition, VoxelShape shape, Vec3d flameOffset, Function<BlockPos, MinecraftBlockingLight> lightFactory)
    {
        this.definition = definition;
        this.shape = shape;
        this.flameOffset = flameOffset;
        this.lightFactory = lightFactory;
    }

    public static LightType forKind(LightKind kind)
    {
        switch (kind) {
            case MEGA_TORCH:
                return MegaTorch;
            case DREAD_LAMP:
                return DreadLamp;
            default:
                throw new IllegalArgumentException("Unsupported light kind: " + kind);
        }
    }

    public LightDefinition definition()
    {
        return definition;
    }

    public LightKind kind()
    {
        return definition.kind();
    }

    public VoxelShape shape()
    {
        return shape;
    }

    public Vec3d flameOffset()
    {
        return flameOffset;
    }

    public String key(BlockPos pos)
    {
        return definition.key(MinecraftAdapterViews.blockPos(pos));
    }

    public MinecraftBlockingLight createLight(BlockPos pos)
    {
        return lightFactory.apply(pos);
    }

    public String displayName()
    {
        return definition.displayName();
    }

    public String blockTranslationKey()
    {
        return definition.blockTranslationKey();
    }

    public boolean matchesBlock(Block block)
    {
        switch (this) {
            case MegaTorch:
                return block == TorchmasterContent.blockMegaTorch.get();
            case DreadLamp:
                return block == TorchmasterContent.blockDreadLamp.get();
            default:
                return false;
        }
    }
}
