package net.xalcon.torchmaster;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
//? if >=1.19.3
import net.minecraft.registry.RegistryKeys;
//? if <1.19.3
//import net.minecraft.util.registry.Registry;
import net.xalcon.torchmaster.blocks.*;
import net.xalcon.torchmaster.items.FrozenPearlItem;
import net.xalcon.torchmaster.items.TMItemBlock;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftBlockProperties;
import net.xalcon.torchmaster.platform.RegistrationProvider;
import net.xalcon.torchmaster.platform.RegistryObject;
import net.xalcon.torchmaster.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("DanglingJavadoc")
public class TorchmasterContent
{
    //? if >=1.19.3 {
    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.create(RegistryKeys.BLOCK, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.create(RegistryKeys.ITEM, Constants.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.create(RegistryKeys.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    //? if >=1.20
    private static final RegistrationProvider<ItemGroup> CREATIVE_MODE_TABS = RegistrationProvider.create(RegistryKeys.ITEM_GROUP, Constants.MOD_ID);
//?} else if >=1.15 {
    /*private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.create(Registry.BLOCK, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.create(Registry.ITEM, Constants.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.create(Registry.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    *///?} else {
    /*private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.create(Registry.BLOCK, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.create(Registry.ITEM, Constants.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.create(Registry.BLOCK_ENTITY, Constants.MOD_ID);
    *///?}

    public static RegistryObject<EntityBlockingLightBlock> blockMegaTorch;
    public static RegistryObject<EntityBlockingLightBlock> blockDreadLamp;

    public static RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern;
    public static RegistryObject<BlockEntityType<FeralFlareLanternBlockEntity>> tileFeralFlareLantern;
    public static RegistryObject<InvisibleLightBlock> blockInvisibleLight;

    public static RegistryObject<Item> itemMegaTorch;
    public static RegistryObject<Item> itemDreadLamp;
    public static RegistryObject<Item> itemFeralFlareLantern;
    public static RegistryObject<Item> itemFrozenPearl;

    private TorchmasterContent() { }
    private static ItemGroup tab;

    public static void initialize()
    {
        List<RegistryObject<Item>> creativeTabItems = new ArrayList<>();
        //? if <1.19.3
        //tab = Services.PLATFORM.createCreativeModeTab(Constants.MOD_ID, creativeTabItems);

        /**
         Mega Torch
         */
        blockMegaTorch = BLOCKS.register("megatorch", () -> new EntityBlockingLightBlock(
                blockProperties("megatorch", MinecraftBlockProperties.megaTorch()),
                LightType.MegaTorch));
        itemMegaTorch = fromBlock(blockMegaTorch);
        creativeTabItems.add(itemMegaTorch);

        /**
         Dread Lamp
         */
        blockDreadLamp = BLOCKS.register("dreadlamp", () -> new EntityBlockingLightBlock(
                blockProperties("dreadlamp", MinecraftBlockProperties.dreadLamp()),
                LightType.DreadLamp));
        itemDreadLamp = fromBlock(blockDreadLamp);
        creativeTabItems.add(itemDreadLamp);

        /**
         Feral Flare Lantern
         */
        blockFeralFlareLantern = BLOCKS.register("feral_flare_lantern", () -> new FeralFlareLanternBlock(
                blockProperties("feral_flare_lantern", MinecraftBlockProperties.feralFlareLantern()))
        );
        tileFeralFlareLantern = BLOCK_ENTITIES.register(blockFeralFlareLantern.getId().getPath(),
                () -> Services.PLATFORM.createBlockEntityType(FeralFlareLanternBlockEntity::new, blockFeralFlareLantern.get()));
        itemFeralFlareLantern = fromBlock(blockFeralFlareLantern);
        creativeTabItems.add(itemFeralFlareLantern);

        itemFrozenPearl = ITEMS.register("frozen_pearl", () -> new FrozenPearlItem(itemProperties("frozen_pearl")));
        creativeTabItems.add(itemFrozenPearl);

        blockInvisibleLight = BLOCKS.register("invisible_light", () -> new InvisibleLightBlock(
                blockProperties("invisible_light", MinecraftBlockProperties.invisibleLight()))
        );

        /**
         Creative Mode Tab
         */
        //? if >=1.20
        CREATIVE_MODE_TABS.register(Constants.MOD_ID, () -> Services.PLATFORM.createCreativeModeTab(Constants.MOD_ID, creativeTabItems));
        //? if forge && >=1.19.3 <1.20
        //Services.PLATFORM.registerCreativeModeTab(Constants.MOD_ID, creativeTabItems);
    }

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return fromBlock(block, i -> {});
    }

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block, Consumer<Item.Settings> propertiesConfig) {
        Item.Settings properties = blockItemProperties(block.getId().getPath());
        propertiesConfig.accept(properties);
        return ITEMS.register(block.getId().getPath(), () -> new TMItemBlock(block.get(), properties));
    }

    private static <P> P blockProperties(String name, P properties) {
        //? if >=1.21.2
        //((net.minecraft.block.AbstractBlock.Settings) properties).registryKey(net.minecraft.registry.RegistryKey.of(RegistryKeys.BLOCK, resourceLocation(name)));
        return properties;
    }

    private static Item.Settings blockItemProperties(String name) {
        Item.Settings properties = itemProperties(name);
        //? if >=1.21.2
        //properties.useBlockPrefixedTranslationKey();
        return properties;
    }

    private static Item.Settings itemProperties(String name) {
        Item.Settings properties = itemProperties();
        //? if >=1.21.2
        //properties.registryKey(net.minecraft.registry.RegistryKey.of(RegistryKeys.ITEM, resourceLocation(name)));
        return properties;
    }

    //? if >=1.21.2 {
    /*private static net.minecraft.util.Identifier resourceLocation(String name) {
        return net.minecraft.util.Identifier.of(Constants.MOD_ID, name);
    }
    *///?}

    private static Item.Settings itemProperties() {
        Item.Settings properties = new Item.Settings();
        //? if <1.19.3
        //properties.group(tab);
        return properties;
    }
}
