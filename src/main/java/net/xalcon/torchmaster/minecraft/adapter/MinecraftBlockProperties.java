package net.xalcon.torchmaster.minecraft.adapter;

//? if >=1.20 {
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    public static AbstractBlock.Settings megaTorch() {
        return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static AbstractBlock.Settings dreadLamp() {
        return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static AbstractBlock.Settings feralFlareLantern() {
        return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static AbstractBlock.Settings invisibleLight() {
        return AbstractBlock.Settings.create()
                .luminance(state -> 15)
                .noCollision()
                .replaceable()
                .air();
    }

    private static AbstractBlock.Settings wood(MapColorRef color) {
        return AbstractBlock.Settings.create().mapColor(color.mapColor());
    }

    private static AbstractBlock.Settings decoration(MapColorRef color) {
        return AbstractBlock.Settings.create().mapColor(color.mapColor());
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        private MapColor mapColor() {
            switch (this) {
                case BLACK:
                    return MapColor.BLACK;
                case YELLOW:
                default:
                    return MapColor.YELLOW;
            }
        }
    }
}
//?} else if fabric && >=1.18 {
/*import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    public static AbstractBlock.Settings megaTorch() {
        return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static AbstractBlock.Settings dreadLamp() {
        return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static AbstractBlock.Settings feralFlareLantern() {
        return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static AbstractBlock.Settings invisibleLight() {
        return AbstractBlock.Settings.of(Material.AIR)
                .luminance(state -> 15)
                .noCollision()
                .air();
    }

    private static AbstractBlock.Settings wood(MapColorRef color) {
        return AbstractBlock.Settings.of(Material.WOOD, color.mapColor());
    }

    private static AbstractBlock.Settings decoration(MapColorRef color) {
        return AbstractBlock.Settings.of(Material.DECORATION, color.mapColor());
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        private MapColor mapColor() {
            switch (this) {
                case BLACK:
                    return MapColor.BLACK;
                case YELLOW:
                default:
                    return MapColor.YELLOW;
            }
        }
    }
}
*///?} else if fabric && >=1.16.5 {
/*import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    public static Block.Settings megaTorch() {
        return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
    }

    public static Block.Settings dreadLamp() {
        return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
    }

    public static Block.Settings feralFlareLantern() {
        return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
    }

    public static Block.Settings invisibleLight() {
        return FabricBlockSettings.of(Material.AIR)
                .lightLevel(15)
                .noCollision()
                .build();
    }

    private static FabricBlockSettings wood(MapColorRef color) {
        return FabricBlockSettings.of(Material.WOOD, color.mapColor());
    }

    private static FabricBlockSettings decoration(MapColorRef color) {
        return FabricBlockSettings.of(Material.DECORATION, color.mapColor());
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        private MapColor mapColor() {
            switch (this) {
                case BLACK:
                    return MapColor.BLACK;
                case YELLOW:
                default:
                    return MapColor.YELLOW;
            }
        }
    }
}
*///?} else if fabric {
/*import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    public static Block.Settings megaTorch() {
        return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
    }

    public static Block.Settings dreadLamp() {
        return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
    }

    public static Block.Settings feralFlareLantern() {
        return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
    }

    public static Block.Settings invisibleLight() {
        return FabricBlockSettings.of(Material.AIR)
                .lightLevel(15)
                .noCollision()
                .build();
    }

    private static FabricBlockSettings wood(MapColorRef color) {
        return FabricBlockSettings.of(Material.WOOD, color.materialColor());
    }

    private static FabricBlockSettings decoration(MapColorRef color) {
        //? if >=1.15.2
        return FabricBlockSettings.of(Material.SUPPORTED, color.materialColor())
        //? if <1.15.2
        //return FabricBlockSettings.of(Material.PART, color.materialColor())
        ;
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        private MaterialColor materialColor() {
            switch (this) {
                case BLACK:
                    return MaterialColor.BLACK;
                case YELLOW:
                default:
                    return MaterialColor.YELLOW;
            }
        }
    }
}
*///?} else if >=1.17 {
/*import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    public static Block.Settings megaTorch() {
        return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static Block.Settings dreadLamp() {
        return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static Block.Settings feralFlareLantern() {
        return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static Block.Settings invisibleLight() {
        return Block.Settings.of(Material.AIR)
                .luminance(state -> 15)
                .noCollision();
    }

    private static Block.Settings wood(MapColorRef color) {
        return Block.Settings.of(Material.WOOD, color.mapColor());
    }

    private static Block.Settings decoration(MapColorRef color) {
        return Block.Settings.of(Material.DECORATION, color.mapColor());
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        private MapColor mapColor() {
            switch (this) {
                case BLACK:
                    return MapColor.BLACK;
                case YELLOW:
                default:
                    return MapColor.YELLOW;
            }
        }
    }
}
*///?} else if >=1.16.5 {
/*import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    public static Block.Settings megaTorch() {
        return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static Block.Settings dreadLamp() {
        return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static Block.Settings feralFlareLantern() {
        return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
    }

    public static Block.Settings invisibleLight() {
        return Block.Settings.of(Material.AIR)
                .luminance(state -> 15)
                .noCollision();
    }

    private static Block.Settings wood(MapColorRef color) {
        return Block.Settings.of(Material.WOOD, color.mapColor());
    }

    private static Block.Settings decoration(MapColorRef color) {
        return Block.Settings.of(Material.DECORATION, color.mapColor());
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        private MapColor mapColor() {
            switch (this) {
                case BLACK:
                    return MapColor.BLACK;
                case YELLOW:
                default:
                    return MapColor.YELLOW;
            }
        }
    }
}
*///?} else {
/*import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    public static Block.Settings megaTorch() {
        return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15);
    }

    public static Block.Settings dreadLamp() {
        return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15);
    }

    public static Block.Settings feralFlareLantern() {
        return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .lightLevel(15);
    }

    public static Block.Settings invisibleLight() {
        return Block.Settings.of(Material.AIR)
                .lightLevel(15)
                .noCollision();
    }

    private static Block.Settings wood(MapColorRef color) {
        return Block.Settings.of(Material.WOOD, color.materialColor());
    }

    private static Block.Settings decoration(MapColorRef color) {
        //? if >=1.15.2
        return Block.Settings.of(Material.SUPPORTED, color.materialColor())
        //? if <1.15.2
        //return Block.Settings.of(Material.PART, color.materialColor())
        ;
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        private MaterialColor materialColor() {
            switch (this) {
                case BLACK:
                    return MaterialColor.BLACK;
                case YELLOW:
                default:
                    return MaterialColor.YELLOW;
            }
        }
    }
}
*///?}
