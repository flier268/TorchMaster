package net.xalcon.torchmaster.platform;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.ModRegistry;

import java.util.Collection;

public class ForgePlatformHelper extends AbstractForgePlatformHelper
{
    @Override
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(ModRegistry.itemMegaTorch.get()))
                .displayItems((parameters, output) -> itemsToShow.forEach(itemRef -> output.accept(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public void registerCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((CreativeModeTabEvent.Register event) ->
                event.registerCreativeModeTab(new ResourceLocation(Constants.MOD_ID, name), builder -> builder
                        .title(Component.translatable("itemGroup." + name))
                        .icon(() -> new ItemStack(ModRegistry.itemMegaTorch.get()))
                        .displayItems((parameters, output) -> itemsToShow.forEach(itemRef -> output.accept(new ItemStack(itemRef.get()))))));
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.of(supplier::create, blocks).build(null);
    }
}
