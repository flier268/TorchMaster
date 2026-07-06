package net.xalcon.torchmaster.minecraft.adapter;

//? if fabric && <1.18
/*import net.fabricmc.fabric.api.block.FabricBlockSettings;*/
//? if >=1.20 || (fabric && >=1.18)
import net.minecraft.block.AbstractBlock;
//? if <1.20 && !(fabric && >=1.18)
//import net.minecraft.block.Block;
//? if >=1.16.5
import net.minecraft.block.MapColor;
//? if <1.20
//import net.minecraft.block.Material;
//? if <1.16.5
//import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;

public final class MinecraftBlockProperties {
    private MinecraftBlockProperties() {
    }

    //? if >=1.20 || (fabric && >=1.18)
    public static AbstractBlock.Settings megaTorch() {
    //? if <1.20 && !(fabric && >=1.18)
    //public static Block.Settings megaTorch() {
        //? if fabric && <1.18 {
        /*return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
        *///?} else if >=1.16.5 {
        return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
        //?} else {
        /*return wood(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15);
        *///?}
    }

    //? if >=1.20 || (fabric && >=1.18)
    public static AbstractBlock.Settings dreadLamp() {
    //? if <1.20 && !(fabric && >=1.18)
    //public static Block.Settings dreadLamp() {
        //? if fabric && <1.18 {
        /*return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
        *///?} else if >=1.16.5 {
        return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
        //?} else {
        /*return wood(MapColorRef.BLACK)
                .sounds(BlockSoundGroup.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(15);
        *///?}
    }

    //? if >=1.20 || (fabric && >=1.18)
    public static AbstractBlock.Settings feralFlareLantern() {
    //? if <1.20 && !(fabric && >=1.18)
    //public static Block.Settings feralFlareLantern() {
        //? if fabric && <1.18 {
        /*return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .lightLevel(15)
                .build();
        *///?} else if >=1.16.5 {
        return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .luminance(state -> 15);
        //?} else {
        /*return decoration(MapColorRef.YELLOW)
                .sounds(BlockSoundGroup.LANTERN)
                .strength(1.0f, 1.0f)
                .lightLevel(15);
        *///?}
    }

    //? if >=1.20 || (fabric && >=1.18)
    public static AbstractBlock.Settings invisibleLight() {
    //? if <1.20 && !(fabric && >=1.18)
    //public static Block.Settings invisibleLight() {
        //? if >=1.20 {
        return AbstractBlock.Settings.create()
                .luminance(state -> 15)
                .noCollision()
                .replaceable()
                .air()
                ;
        //?} else if fabric && >=1.18 {
        /*return AbstractBlock.Settings.of(Material.AIR)
                .luminance(state -> 15)
                .noCollision()
                .air();
        *///?} else if fabric {
        /*return FabricBlockSettings.of(Material.AIR)
                .lightLevel(15)
                .noCollision()
                .build();
        *///?} else if >=1.16.5 {
        /*return Block.Settings.of(Material.AIR)
                .luminance(state -> 15)
                .noCollision();
        *///?} else {
        /*return Block.Settings.of(Material.AIR)
                .lightLevel(15)
                .noCollision();
        *///?}
    }

    //? if fabric && <1.18
    /*private static FabricBlockSettings wood(MapColorRef color) {*/
    //? if >=1.20 || (fabric && >=1.18)
    private static AbstractBlock.Settings wood(MapColorRef color) {
    //? if <1.20 && !fabric
    //private static Block.Settings wood(MapColorRef color) {
        //? if >=1.20
        return AbstractBlock.Settings.create().mapColor(color.mapColor());
        //? if fabric && >=1.18 <1.20
        //return AbstractBlock.Settings.of(Material.WOOD, color.mapColor());
        //? if fabric && >=1.16.5 <1.18
        //return FabricBlockSettings.of(Material.WOOD, color.mapColor());
        //? if fabric && <1.16.5
        //return FabricBlockSettings.of(Material.WOOD, color.materialColor());
        //? if >=1.16.5 <1.20 && !fabric
        //return Block.Settings.of(Material.WOOD, color.mapColor());
        //? if <1.16.5 && !fabric
        //return Block.Settings.of(Material.WOOD, color.materialColor());
    }

    //? if fabric && <1.18
    /*private static FabricBlockSettings decoration(MapColorRef color) {*/
    //? if >=1.20 || (fabric && >=1.18)
    private static AbstractBlock.Settings decoration(MapColorRef color) {
    //? if <1.20 && !fabric
    //private static Block.Settings decoration(MapColorRef color) {
        //? if >=1.20
        return AbstractBlock.Settings.create().mapColor(color.mapColor());
        //? if fabric && >=1.18 <1.20
        //return AbstractBlock.Settings.of(Material.DECORATION, color.mapColor());
        //? if fabric && >=1.16.5 <1.18
        //return FabricBlockSettings.of(Material.DECORATION, color.mapColor());
        //? if >=1.16.5 <1.20 && !fabric
        //return Block.Settings.of(Material.DECORATION, color.mapColor());
        //? if fabric && >=1.15.2 <1.16.5
        //return FabricBlockSettings.of(Material.SUPPORTED, color.materialColor());
        //? if fabric && <1.15.2
        //return FabricBlockSettings.of(Material.PART, color.materialColor());
        //? if >=1.15.2 <1.16.5 && !fabric
        //return Block.Settings.of(Material.SUPPORTED, color.materialColor());
        //? if <1.15.2 && !fabric
        //return Block.Settings.of(Material.PART, color.materialColor());
    }

    private enum MapColorRef {
        YELLOW,
        BLACK;

        //? if >=1.16.5 {
        private MapColor mapColor() {
            switch (this) {
                case BLACK:
                    return MapColor.BLACK;
                case YELLOW:
                default:
                    return MapColor.YELLOW;
            }
        }
        //?} else {
        /*private MaterialColor materialColor() {
            switch (this) {
                case BLACK:
                    return MaterialColor.BLACK;
                case YELLOW:
                default:
                    return MaterialColor.YELLOW;
            }
        }
        *///?}
    }
}
