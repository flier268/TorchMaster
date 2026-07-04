package net.xalcon.torchmaster;

//? if >=1.19.3 {
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.create(Registries.BLOCK, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.create(Registries.ITEM, Constants.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    //? if >=1.20
    private static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
//?} else {
    /*private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.create(Registry.BLOCK, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.create(Registry.ITEM, Constants.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.create(Registry.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
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
    private static CreativeModeTab tab;

    public static void initialize()
    {
        List<RegistryObject<Item>> creativeTabItems = new ArrayList<>();
        //? if <1.19.3
        //tab = Services.PLATFORM.createCreativeModeTab(Constants.MOD_ID, creativeTabItems);

        /**
         Mega Torch
         */
        blockMegaTorch = BLOCKS.register("megatorch", () -> new EntityBlockingLightBlock(
                MinecraftBlockProperties.megaTorch(),
                LightType.MegaTorch));
        itemMegaTorch = fromBlock(blockMegaTorch);
        creativeTabItems.add(itemMegaTorch);

        /**
         Dread Lamp
         */
        blockDreadLamp = BLOCKS.register("dreadlamp", () -> new EntityBlockingLightBlock(
                MinecraftBlockProperties.dreadLamp(),
                LightType.DreadLamp));
        itemDreadLamp = fromBlock(blockDreadLamp);
        creativeTabItems.add(itemDreadLamp);

        /**
         Feral Flare Lantern
         */
        blockFeralFlareLantern = BLOCKS.register("feral_flare_lantern", () -> new FeralFlareLanternBlock(
                MinecraftBlockProperties.feralFlareLantern())
        );
        tileFeralFlareLantern = BLOCK_ENTITIES.register(blockFeralFlareLantern.getId().getPath(),
                () -> Services.PLATFORM.createBlockEntityType(FeralFlareLanternBlockEntity::new, blockFeralFlareLantern.get()));
        itemFeralFlareLantern = fromBlock(blockFeralFlareLantern);
        creativeTabItems.add(itemFeralFlareLantern);

        itemFrozenPearl = ITEMS.register("frozen_pearl", () -> new FrozenPearlItem(itemProperties()));
        creativeTabItems.add(itemFrozenPearl);

        blockInvisibleLight = BLOCKS.register("invisible_light", () -> new InvisibleLightBlock(
                MinecraftBlockProperties.invisibleLight())
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

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block, Consumer<Item.Properties> propertiesConfig) {
        Item.Properties properties = itemProperties();
        propertiesConfig.accept(properties);
        return ITEMS.register(block.getId().getPath(), () -> new TMItemBlock(block.get(), properties));
    }

    private static Item.Properties itemProperties() {
        Item.Properties properties = new Item.Properties();
        //? if <1.19.3
        //properties.tab(tab);
        return properties;
    }
}
