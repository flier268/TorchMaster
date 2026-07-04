package net.xalcon.torchmaster.platform;

//? if fabric && >=1.21.2 {
/*import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

import java.util.Collection;

public class FabricPlatformHelper implements IPlatformHelper {
    private static final ITorchmasterConfig CONFIG = TorchmasterTomlConfig.load(FabricLoader.getInstance().getConfigDir().resolve("torchmaster.toml"));

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return FabricItemGroup.builder()
                .title(Component.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .displayItems((parameters, output) -> itemsToShow.forEach(itemRef -> output.accept(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return FabricBlockEntityTypeBuilder.create(supplier::create, blocks).build();
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return CONFIG;
    }
}
*///?} else if fabric && >=1.20 {
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

import java.util.Collection;

public class FabricPlatformHelper implements IPlatformHelper {
    private static final ITorchmasterConfig CONFIG = TorchmasterTomlConfig.load(FabricLoader.getInstance().getConfigDir().resolve("torchmaster.toml"));

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return FabricItemGroup.builder()
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
//?} else if fabric {
/*import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

import java.util.Collection;

public class FabricPlatformHelper implements IPlatformHelper {
    private static final ITorchmasterConfig CONFIG = TorchmasterTomlConfig.load(FabricLoader.getInstance().getConfigDir().resolve("torchmaster.toml"));

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return FabricItemGroupBuilder.create(new ResourceLocation(Constants.MOD_ID, name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .appendItems(stacks -> itemsToShow.forEach(itemRef -> stacks.add(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return FabricBlockEntityTypeBuilder.create(supplier::create, blocks).build(null);
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return CONFIG;
    }
}
*///?}
