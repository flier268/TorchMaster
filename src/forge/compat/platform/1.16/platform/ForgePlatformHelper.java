package net.xalcon.torchmaster.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.xalcon.torchmaster.ModRegistry;

import java.util.Collection;

public class ForgePlatformHelper extends AbstractForgePlatformHelper
{
    @Override
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return new CreativeModeTab(name) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(ModRegistry.itemMegaTorch.get());
            }
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.of(() -> supplier.create(BlockPos.ZERO, null), blocks).build(null);
    }
}
