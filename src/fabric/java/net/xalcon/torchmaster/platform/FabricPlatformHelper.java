package net.xalcon.torchmaster.platform;

//? if >=1.19.3
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
//? if >=1.21.11
//import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
//? if >=1.17 <1.19.3
//import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
//? if <1.19.3
//import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
//? if >=1.19.3
import net.minecraft.text.Text;
//? if >=1.19.3 <1.20
//import net.minecraft.util.Identifier;
//? if <1.19.3 && >=1.16
//import net.minecraft.util.collection.DefaultedList;
//? if <1.16
//import net.minecraft.util.DefaultedList;
//? if <1.19.3
//import net.minecraft.util.math.BlockPos;
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
    public ItemGroup createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        //? if >=1.19.3 {
        //? if >=1.20 {
        return FabricItemGroup.builder()
        //?} else {
        /*return FabricItemGroup.builder(new Identifier(Constants.MOD_ID, name))
        *///?}
                .displayName(Text.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .entries((parameters, output) -> itemsToShow.forEach(itemRef -> output.add(new ItemStack(itemRef.get()))))
                .build();
        //?} else {
        /*((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
        return new ItemGroup(ItemGroup.GROUPS.length - 1, name) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(TorchmasterContent.itemMegaTorch.get());
            }

            @Override
            public void appendStacks(DefaultedList<ItemStack> stacks) {
                itemsToShow.forEach(itemRef -> stacks.add(new ItemStack(itemRef.get())));
            }
        };
        *///?}
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        //? if >=1.21.11 {
        /*return FabricBlockEntityTypeBuilder.create(supplier::create, blocks).build();
        *///?} else if >=1.19.3 {
        return BlockEntityType.Builder.create(supplier::create, blocks).build(null);
        //?} else if >=1.17 {
        /*return FabricBlockEntityTypeBuilder.create(supplier::create, blocks).build();
        *///?} else {
        /*return BlockEntityType.Builder.create(() -> supplier.create(BlockPos.ORIGIN, blocks[0].getDefaultState()), blocks).build(null);
        *///?}
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return CONFIG;
    }
}
