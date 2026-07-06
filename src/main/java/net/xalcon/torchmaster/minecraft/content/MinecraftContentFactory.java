package net.xalcon.torchmaster.minecraft.content;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
//? if >=1.19.3
import net.minecraft.registry.RegistryKeys;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.blocks.InvisibleLightBlock;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.blocks.MegaTorchBlock;
import net.xalcon.torchmaster.content.TorchmasterContentDefinitions;
import net.xalcon.torchmaster.items.FrozenPearlItem;
import net.xalcon.torchmaster.items.TMItemBlock;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftBlockProperties;
import net.xalcon.torchmaster.platform.RegistryObject;

public final class MinecraftContentFactory
{
    private static ItemGroup legacyCreativeTab;

    private MinecraftContentFactory()
    {
    }

    public static void setLegacyCreativeTab(ItemGroup tab)
    {
        legacyCreativeTab = tab;
    }

    public static EntityBlockingLightBlock entityBlockingLight(TorchmasterContentDefinitions.BlockDefinition definition)
    {
        LightType lightType = LightType.forKind(definition.lightKind());
        switch (lightType.kind()) {
            case MEGA_TORCH:
                return new MegaTorchBlock(blockProperties(definition.id(), MinecraftBlockProperties.megaTorch()), lightType);
            case DREAD_LAMP:
                return new EntityBlockingLightBlock(blockProperties(definition.id(), MinecraftBlockProperties.dreadLamp()), lightType);
            default:
                throw new IllegalArgumentException("Block definition has no entity blocking light type: " + definition.id());
        }
    }

    public static FeralFlareLanternBlock feralFlareLantern(TorchmasterContentDefinitions.BlockDefinition definition)
    {
        return new FeralFlareLanternBlock(blockProperties(definition.id(), MinecraftBlockProperties.feralFlareLantern()));
    }

    public static InvisibleLightBlock invisibleLight(TorchmasterContentDefinitions.BlockDefinition definition)
    {
        return new InvisibleLightBlock(blockProperties(definition.id(), MinecraftBlockProperties.invisibleLight()));
    }

    public static <B extends net.minecraft.block.Block> Item blockItem(RegistryObject<B> block)
    {
        return new TMItemBlock(block.get(), blockItemProperties(block.getId().getPath()));
    }

    public static FrozenPearlItem frozenPearl(TorchmasterContentDefinitions.ItemDefinition definition)
    {
        return new FrozenPearlItem(itemProperties(definition.id()));
    }

    private static <P> P blockProperties(String name, P properties)
    {
        //? if >=1.21.2
        //((net.minecraft.block.AbstractBlock.Settings) properties).registryKey(net.minecraft.registry.RegistryKey.of(RegistryKeys.BLOCK, resourceLocation(name)));
        return properties;
    }

    private static Item.Settings blockItemProperties(String name)
    {
        Item.Settings properties = itemProperties(name);
        //? if >=1.21.2
        //properties.useBlockPrefixedTranslationKey();
        return properties;
    }

    private static Item.Settings itemProperties(String name)
    {
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

    private static Item.Settings itemProperties()
    {
        Item.Settings properties = new Item.Settings();
        //? if <1.19.3
        //properties.group(legacyCreativeTab);
        return properties;
    }
}
