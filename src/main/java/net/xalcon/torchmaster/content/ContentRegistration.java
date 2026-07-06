package net.xalcon.torchmaster.content;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlockEntity;
import net.xalcon.torchmaster.blocks.InvisibleLightBlock;
import net.xalcon.torchmaster.platform.RegistryObject;

public final class ContentRegistration
{
    private final RegistryObject<EntityBlockingLightBlock> blockMegaTorch;
    private final RegistryObject<EntityBlockingLightBlock> blockDreadLamp;
    private final RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern;
    private final RegistryObject<BlockEntityType<FeralFlareLanternBlockEntity>> tileFeralFlareLantern;
    private final RegistryObject<InvisibleLightBlock> blockInvisibleLight;
    private final RegistryObject<Item> itemMegaTorch;
    private final RegistryObject<Item> itemDreadLamp;
    private final RegistryObject<Item> itemFeralFlareLantern;
    private final RegistryObject<Item> itemFrozenPearl;

    public ContentRegistration(
            RegistryObject<EntityBlockingLightBlock> blockMegaTorch,
            RegistryObject<EntityBlockingLightBlock> blockDreadLamp,
            RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern,
            RegistryObject<BlockEntityType<FeralFlareLanternBlockEntity>> tileFeralFlareLantern,
            RegistryObject<InvisibleLightBlock> blockInvisibleLight,
            RegistryObject<Item> itemMegaTorch,
            RegistryObject<Item> itemDreadLamp,
            RegistryObject<Item> itemFeralFlareLantern,
            RegistryObject<Item> itemFrozenPearl)
    {
        this.blockMegaTorch = blockMegaTorch;
        this.blockDreadLamp = blockDreadLamp;
        this.blockFeralFlareLantern = blockFeralFlareLantern;
        this.tileFeralFlareLantern = tileFeralFlareLantern;
        this.blockInvisibleLight = blockInvisibleLight;
        this.itemMegaTorch = itemMegaTorch;
        this.itemDreadLamp = itemDreadLamp;
        this.itemFeralFlareLantern = itemFeralFlareLantern;
        this.itemFrozenPearl = itemFrozenPearl;
    }

    public RegistryObject<EntityBlockingLightBlock> blockMegaTorch()
    {
        return blockMegaTorch;
    }

    public RegistryObject<EntityBlockingLightBlock> blockDreadLamp()
    {
        return blockDreadLamp;
    }

    public RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern()
    {
        return blockFeralFlareLantern;
    }

    public RegistryObject<BlockEntityType<FeralFlareLanternBlockEntity>> tileFeralFlareLantern()
    {
        return tileFeralFlareLantern;
    }

    public RegistryObject<InvisibleLightBlock> blockInvisibleLight()
    {
        return blockInvisibleLight;
    }

    public RegistryObject<Item> itemMegaTorch()
    {
        return itemMegaTorch;
    }

    public RegistryObject<Item> itemDreadLamp()
    {
        return itemDreadLamp;
    }

    public RegistryObject<Item> itemFeralFlareLantern()
    {
        return itemFeralFlareLantern;
    }

    public RegistryObject<Item> itemFrozenPearl()
    {
        return itemFrozenPearl;
    }
}
