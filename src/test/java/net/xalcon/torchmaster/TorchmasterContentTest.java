package net.xalcon.torchmaster;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
//? if >=1.19.3
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlockEntity;
import net.xalcon.torchmaster.blocks.InvisibleLightBlock;
import net.xalcon.torchmaster.content.ContentRegistration;
import net.xalcon.torchmaster.platform.RegistryObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class TorchmasterContentTest
{
    @Test
    void applyRegistrationBackfillsLegacyFacadeFields()
    {
        RegistryObject<EntityBlockingLightBlock> blockMegaTorch = fake("megatorch");
        RegistryObject<EntityBlockingLightBlock> blockDreadLamp = fake("dreadlamp");
        RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern = fake("feral_flare_lantern");
        RegistryObject<BlockEntityType<FeralFlareLanternBlockEntity>> tileFeralFlareLantern = fake("feral_flare_lantern");
        RegistryObject<InvisibleLightBlock> blockInvisibleLight = fake("invisible_light");
        RegistryObject<Item> itemMegaTorch = fake("megatorch");
        RegistryObject<Item> itemDreadLamp = fake("dreadlamp");
        RegistryObject<Item> itemFeralFlareLantern = fake("feral_flare_lantern");
        RegistryObject<Item> itemFrozenPearl = fake("frozen_pearl");

        TorchmasterContent.applyRegistration(new ContentRegistration(
                blockMegaTorch,
                blockDreadLamp,
                blockFeralFlareLantern,
                tileFeralFlareLantern,
                blockInvisibleLight,
                itemMegaTorch,
                itemDreadLamp,
                itemFeralFlareLantern,
                itemFrozenPearl));

        assertSame(blockMegaTorch, TorchmasterContent.blockMegaTorch);
        assertSame(blockDreadLamp, TorchmasterContent.blockDreadLamp);
        assertSame(blockFeralFlareLantern, TorchmasterContent.blockFeralFlareLantern);
        assertSame(tileFeralFlareLantern, TorchmasterContent.tileFeralFlareLantern);
        assertSame(blockInvisibleLight, TorchmasterContent.blockInvisibleLight);
        assertSame(itemMegaTorch, TorchmasterContent.itemMegaTorch);
        assertSame(itemDreadLamp, TorchmasterContent.itemDreadLamp);
        assertSame(itemFeralFlareLantern, TorchmasterContent.itemFeralFlareLantern);
        assertSame(itemFrozenPearl, TorchmasterContent.itemFrozenPearl);
    }

    private static <T> RegistryObject<T> fake(String id)
    {
        return new RegistryObject<T>() {
            @Override
            //? if >=1.19.3
            public RegistryKey<T> getResourceKey()
            //? if <1.19.3
            //public Object getResourceKey()
            {
                return null;
            }

            @Override
            public Identifier getId()
            {
                //? if >=1.21 {
                return Identifier.of(Constants.MOD_ID, id);
                //?} else {
                /*return new Identifier(Constants.MOD_ID, id);
                *///?}
            }

            @Override
            public T get()
            {
                return null;
            }
        };
    }
}
