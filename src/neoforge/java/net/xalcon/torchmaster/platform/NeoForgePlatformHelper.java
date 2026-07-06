package net.xalcon.torchmaster.platform;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
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
        //? if >=1.21.9 {
        return !FMLLoader.getCurrent().isProduction();
        //?} else {
        /*return !FMLLoader.isProduction();
        *///?}
    }

    @Override
    public ItemGroup createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return ItemGroup.create(null, -1)
                .displayName(Text.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(TorchmasterContent.itemMegaTorch.get()))
                .entries((parameters, output) -> itemsToShow.forEach(itemRef -> output.add(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        //? if >=1.21.11 {
        return new BlockEntityType<>(supplier::create, blocks);
        //?} else {
        /*return BlockEntityType.Builder.create(supplier::create, blocks).build(null);
        *///?}
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return CONFIG;
    }
}
