package net.xalcon.torchmaster;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlockEntity;
import net.xalcon.torchmaster.blocks.InvisibleLightBlock;
import net.xalcon.torchmaster.content.ContentRegistration;
import net.xalcon.torchmaster.content.ContentRegistrar;
import net.xalcon.torchmaster.platform.RegistryObject;

public class TorchmasterContent
{
    public static RegistryObject<EntityBlockingLightBlock> blockMegaTorch;
    public static RegistryObject<EntityBlockingLightBlock> blockDreadLamp;

    public static RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern;
    public static RegistryObject<BlockEntityType<FeralFlareLanternBlockEntity>> tileFeralFlareLantern;
    public static RegistryObject<InvisibleLightBlock> blockInvisibleLight;

    public static RegistryObject<Item> itemMegaTorch;
    public static RegistryObject<Item> itemDreadLamp;
    public static RegistryObject<Item> itemFeralFlareLantern;
    public static RegistryObject<Item> itemFrozenPearl;

    private TorchmasterContent()
    {
    }

    public static void initialize()
    {
        applyRegistration(ContentRegistrar.INSTANCE.register());
    }

    static void applyRegistration(ContentRegistration registration)
    {
        blockMegaTorch = registration.blockMegaTorch();
        blockDreadLamp = registration.blockDreadLamp();
        blockFeralFlareLantern = registration.blockFeralFlareLantern();
        tileFeralFlareLantern = registration.tileFeralFlareLantern();
        blockInvisibleLight = registration.blockInvisibleLight();
        itemMegaTorch = registration.itemMegaTorch();
        itemDreadLamp = registration.itemDreadLamp();
        itemFeralFlareLantern = registration.itemFeralFlareLantern();
        itemFrozenPearl = registration.itemFrozenPearl();
    }
}
