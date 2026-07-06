package net.xalcon.torchmaster.platform;

//? if forge && >=1.20 {
/*import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.xalcon.torchmaster.TorchmasterContent;

import java.util.Collection;

public class ForgePlatformHelper extends AbstractForgePlatformHelper
{
    @Override
    public ItemGroup createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return ItemGroup.builder()
                .displayName(Text.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .entries((parameters, output) -> itemsToShow.forEach(itemRef -> output.add(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.create(supplier::create, blocks).build(null);
    }
}
*///?} else if forge && >=1.19.3 {
/*import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.TorchmasterContent;

import java.util.Collection;

public class ForgePlatformHelper extends AbstractForgePlatformHelper
{
    @Override
    public ItemGroup createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return ItemGroup.create(ItemGroup.Row.TOP, 0)
                .displayName(Text.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .entries((parameters, output) -> itemsToShow.forEach(itemRef -> output.add(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public void registerCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((CreativeModeTabEvent.Register event) ->
                event.registerCreativeModeTab(new Identifier(Constants.MOD_ID, name), builder -> builder
                        .displayName(Text.translatable("itemGroup." + name))
                        .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                        .entries((parameters, output) -> itemsToShow.forEach(itemRef -> output.add(new ItemStack(itemRef.get()))))));
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.create(supplier::create, blocks).build(null);
    }
}
*///?} else if forge && >=1.17 {
/*import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.xalcon.torchmaster.TorchmasterContent;

import java.util.Collection;

public class ForgePlatformHelper extends AbstractForgePlatformHelper
{
    @Override
    public ItemGroup createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return new ItemGroup(name) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(TorchmasterContent.itemMegaTorch.get());
            }
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.create(supplier::create, blocks).build(null);
    }
}
*///?} else if forge {
/*import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.TorchmasterContent;

import java.util.Collection;

public class ForgePlatformHelper extends AbstractForgePlatformHelper
{
    @Override
    public ItemGroup createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return new ItemGroup(name) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(TorchmasterContent.itemMegaTorch.get());
            }
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.create(() -> supplier.create(BlockPos.ORIGIN, blocks[0].getDefaultState()), blocks).build(null);
    }
}
*///?}
