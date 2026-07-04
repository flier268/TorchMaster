package net.xalcon.torchmaster.platform;

//? if neoforge && >=1.21.9 {
/*import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

import java.util.Collection;

public class NeoForgePlatformHelper implements IPlatformHelper
{
    private static final ITorchmasterConfig CONFIG = TorchmasterTomlConfig.load(FMLPaths.CONFIGDIR.get().resolve("torchmaster.toml"));

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .displayItems((parameters, output) -> itemsToShow.forEach(itemRef -> output.accept(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return new BlockEntityType<>(supplier::create, blocks);
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return CONFIG;
    }
}
*///?} else if neoforge && >=1.21.2 {
/*import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

import java.util.Collection;

public class NeoForgePlatformHelper implements IPlatformHelper
{
    private static final ITorchmasterConfig CONFIG = TorchmasterTomlConfig.load(FMLPaths.CONFIGDIR.get().resolve("torchmaster.toml"));

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .displayItems((parameters, output) -> itemsToShow.forEach(itemRef -> output.accept(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return new BlockEntityType<>(supplier::create, blocks);
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return CONFIG;
    }
}
*///?} else if neoforge {
/*import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

import java.util.Collection;

public class NeoForgePlatformHelper implements IPlatformHelper
{
    private static final ITorchmasterConfig CONFIG = TorchmasterTomlConfig.load(FMLPaths.CONFIGDIR.get().resolve("torchmaster.toml"));

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .displayItems((parameters, output) -> itemsToShow.forEach(itemRef -> output.accept(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.of(supplier::create, blocks).build(null);
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return CONFIG;
    }
}
*///?}
