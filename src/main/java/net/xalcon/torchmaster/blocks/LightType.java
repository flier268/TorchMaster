package net.xalcon.torchmaster.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.light.megatorch.MegatorchBlockingLight;
import net.xalcon.torchmaster.minecraft.light.dreadlamp.DreadLampBlockingLight;

import java.util.function.Function;

public enum LightType
{
    MegaTorch(
            Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            new Vec3d(.5f, 1f, .5f),
            pos -> "MT_" +pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
            MegatorchBlockingLight::new
    ),
    DreadLamp(
            Block.createCuboidShape(1, 1, 1, 15, 15, 15),
            new Vec3d(.5f, .3f, .5f),
            pos -> "DL_" + pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
            DreadLampBlockingLight::new
    );

    LightType(VoxelShape shape, Vec3d flameOffset, Function<BlockPos, String> keyFactory, Function<BlockPos, MinecraftBlockingLight> lightFactory)
    {
        Shape = shape;
        FlameOffset = flameOffset;
        KeyFactory = keyFactory;
        LightFactory = lightFactory;
    }

    public final VoxelShape Shape;
    public final Vec3d FlameOffset;
    public final Function<BlockPos, String> KeyFactory;
    public final Function<BlockPos, MinecraftBlockingLight> LightFactory;
}
