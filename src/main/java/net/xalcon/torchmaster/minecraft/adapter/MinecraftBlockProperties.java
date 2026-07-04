package net.xalcon.torchmaster.minecraft.adapter;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
//? if >=1.20 {
import net.minecraft.world.level.material.MapColor;
//?} else {
/*import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
*///?}

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    public static BlockBehaviour.Properties megaTorch() {
        return wood(MapColorRef.YELLOW)
                .sound(SoundType.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(state -> 15);
    }

    public static BlockBehaviour.Properties dreadLamp() {
        return wood(MapColorRef.BLACK)
                .sound(SoundType.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(state -> 15);
    }

    public static BlockBehaviour.Properties feralFlareLantern() {
        return decoration(MapColorRef.YELLOW)
                .sound(SoundType.LANTERN)
                .strength(1.0f, 1.0f)
                .lightLevel(state -> 15);
    }

    public static BlockBehaviour.Properties invisibleLight() {
        BlockBehaviour.Properties properties =
                //? if >=1.20 {
                BlockBehaviour.Properties.of()
                //?} else {
                /*BlockBehaviour.Properties.of(Material.AIR)
                *///?}
                        .lightLevel(state -> 15)
                        //? if >=1.21.9 {
                        /*.noCollision()
                        *///?} else {
                        .noCollission()
                        //?}
                        //? if >=1.20
                        .replaceable()
                        .air();
        return properties;
    }

    private static BlockBehaviour.Properties wood(MapColorRef color) {
        //? if >=1.20 {
        return BlockBehaviour.Properties.of().mapColor(color.mapColor());
        //?} else {
        /*return BlockBehaviour.Properties.of(Material.WOOD, color.materialColor());
        *///?}
    }

    private static BlockBehaviour.Properties decoration(MapColorRef color) {
        //? if >=1.20 {
        return BlockBehaviour.Properties.of().mapColor(color.mapColor());
        //?} else {
        /*return BlockBehaviour.Properties.of(Material.DECORATION, color.materialColor());
        *///?}
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        //? if >=1.20 {
        private MapColor mapColor() {
            switch (this) {
                case BLACK:
                    return MapColor.COLOR_BLACK;
                case YELLOW:
                default:
                    return MapColor.COLOR_YELLOW;
            }
        }
        //?} else {
        /*private MaterialColor materialColor() {
            switch (this) {
                case BLACK:
                    return MaterialColor.COLOR_BLACK;
                case YELLOW:
                default:
                    return MaterialColor.COLOR_YELLOW;
            }
        }
        *///?}
    }
}
