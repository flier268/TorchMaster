package net.xalcon.torchmaster;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.xalcon.torchmaster.blocks.LightType;

public final class TorchmasterClientBridge
{
    private static LightScreenOpener lightScreenOpener = (pos, dimension, lightType) -> {
    };

    private TorchmasterClientBridge()
    {
    }

    public static void setLightScreenOpener(LightScreenOpener opener)
    {
        lightScreenOpener = opener;
    }

    public static void openLightScreen(BlockPos pos, ResourceKey<Level> dimension, LightType lightType)
    {
        lightScreenOpener.open(pos, dimension, lightType);
    }

    @FunctionalInterface
    public interface LightScreenOpener
    {
        void open(BlockPos pos, ResourceKey<Level> dimension, LightType lightType);
    }
}
