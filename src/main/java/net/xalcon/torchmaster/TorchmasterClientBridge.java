package net.xalcon.torchmaster;

//? if >=1.19.4
import net.minecraft.registry.RegistryKey;
//? if >=1.16.5 <1.19.4
/*import net.minecraft.util.registry.RegistryKey;*/
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    //? if >=1.16.5
    public static void openLightScreen(BlockPos pos, RegistryKey<World> dimension, LightType lightType)
    //? if <1.16.5
    //public static void openLightScreen(BlockPos pos, Object dimension, LightType lightType)
    {
        lightScreenOpener.open(pos, dimension, lightType);
    }

    @FunctionalInterface
    public interface LightScreenOpener
    {
        //? if >=1.16.5
        void open(BlockPos pos, RegistryKey<World> dimension, LightType lightType);
        //? if <1.16.5
        //void open(BlockPos pos, Object dimension, LightType lightType);
    }
}
