package net.xalcon.torchmaster.minecraft.content;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
//? if >=1.19.3
import net.minecraft.registry.RegistryKeys;
//? if <1.19.3
//import net.minecraft.util.registry.Registry;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlockEntity;
import net.xalcon.torchmaster.blocks.InvisibleLightBlock;
import net.xalcon.torchmaster.content.ContentRegistration;
import net.xalcon.torchmaster.content.ContentRegistrar;
import net.xalcon.torchmaster.content.TorchmasterContentDefinitions;
import net.xalcon.torchmaster.platform.RegistrationProvider;
import net.xalcon.torchmaster.platform.RegistryObject;
import net.xalcon.torchmaster.platform.Services;

import java.util.ArrayList;
import java.util.List;

public class MinecraftContentRegistrar implements ContentRegistrar
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

    @Override
    public ContentRegistration register()
    {
        List<RegistryObject<Item>> creativeTabItems = new ArrayList<>();
        //? if <1.19.3
        //MinecraftContentFactory.setLegacyCreativeTab(Services.PLATFORM.createCreativeModeTab(Constants.MOD_ID, creativeTabItems));

        RegistryObject<EntityBlockingLightBlock> blockMegaTorch = BLOCKS.register(
                TorchmasterContentDefinitions.MEGA_TORCH_BLOCK.id(),
                () -> MinecraftContentFactory.entityBlockingLight(TorchmasterContentDefinitions.MEGA_TORCH_BLOCK));
        RegistryObject<Item> itemMegaTorch = registerBlockItem(blockMegaTorch);
        creativeTabItems.add(itemMegaTorch);

        RegistryObject<EntityBlockingLightBlock> blockDreadLamp = BLOCKS.register(
                TorchmasterContentDefinitions.DREAD_LAMP_BLOCK.id(),
                () -> MinecraftContentFactory.entityBlockingLight(TorchmasterContentDefinitions.DREAD_LAMP_BLOCK));
        RegistryObject<Item> itemDreadLamp = registerBlockItem(blockDreadLamp);
        creativeTabItems.add(itemDreadLamp);

        RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern = BLOCKS.register(
                TorchmasterContentDefinitions.FERAL_FLARE_LANTERN_BLOCK.id(),
                () -> MinecraftContentFactory.feralFlareLantern(TorchmasterContentDefinitions.FERAL_FLARE_LANTERN_BLOCK));
        RegistryObject<BlockEntityType<FeralFlareLanternBlockEntity>> tileFeralFlareLantern = BLOCK_ENTITIES.register(
                blockFeralFlareLantern.getId().getPath(),
                () -> Services.PLATFORM.createBlockEntityType(FeralFlareLanternBlockEntity::new, blockFeralFlareLantern.get()));
        RegistryObject<Item> itemFeralFlareLantern = registerBlockItem(blockFeralFlareLantern);
        creativeTabItems.add(itemFeralFlareLantern);

        RegistryObject<Item> itemFrozenPearl = ITEMS.register(
                TorchmasterContentDefinitions.FROZEN_PEARL_ITEM.id(),
                () -> MinecraftContentFactory.frozenPearl(TorchmasterContentDefinitions.FROZEN_PEARL_ITEM));
        creativeTabItems.add(itemFrozenPearl);

        RegistryObject<InvisibleLightBlock> blockInvisibleLight = BLOCKS.register(
                TorchmasterContentDefinitions.INVISIBLE_LIGHT_BLOCK.id(),
                () -> MinecraftContentFactory.invisibleLight(TorchmasterContentDefinitions.INVISIBLE_LIGHT_BLOCK));

        registerCreativeModeTab(creativeTabItems);

        return new ContentRegistration(
                blockMegaTorch,
                blockDreadLamp,
                blockFeralFlareLantern,
                tileFeralFlareLantern,
                blockInvisibleLight,
                itemMegaTorch,
                itemDreadLamp,
                itemFeralFlareLantern,
                itemFrozenPearl);
    }

    private static <B extends Block> RegistryObject<Item> registerBlockItem(RegistryObject<B> block)
    {
        return ITEMS.register(block.getId().getPath(), () -> MinecraftContentFactory.blockItem(block));
    }

    private static void registerCreativeModeTab(List<RegistryObject<Item>> creativeTabItems)
    {
        //? if >=1.20
        CREATIVE_MODE_TABS.register(Constants.MOD_ID, () -> Services.PLATFORM.createCreativeModeTab(Constants.MOD_ID, creativeTabItems));
        //? if forge && >=1.19.3 <1.20
        //Services.PLATFORM.registerCreativeModeTab(Constants.MOD_ID, creativeTabItems);
    }
}
