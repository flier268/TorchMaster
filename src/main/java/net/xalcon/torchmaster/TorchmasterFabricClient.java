package net.xalcon.torchmaster;

//? if fabric && >=1.21.11 {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer {
    public void onInitializeClient()
    {
        BlockRenderLayerMap.putBlock(ModRegistry.blockDreadLamp.get(), ChunkSectionLayer.CUTOUT);
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
        registerConfigKey();
    }

    private static void registerConfigKey()
    {
        try {
            Class.forName("net.xalcon.torchmaster.TorchmasterFabricConfigClient")
                    .getMethod("register")
                    .invoke(null);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
*///?} else if fabric && >=1.21.8 {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer {
    public void onInitializeClient()
    {
        BlockRenderLayerMap.putBlock(ModRegistry.blockDreadLamp.get(), ChunkSectionLayer.CUTOUT);
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
        registerConfigKey();
    }

    private static void registerConfigKey()
    {
        try {
            Class.forName("net.xalcon.torchmaster.TorchmasterFabricConfigClient")
                    .getMethod("register")
                    .invoke(null);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
*///?} else if fabric && >=1.21.2 {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer {
    public void onInitializeClient()
    {
        BlockRenderLayerMap.INSTANCE.putBlock(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
        registerConfigKey();
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (Minecraft.getInstance().level != null && context.matrixStack() != null) {
                TorchmasterLightRangeRenderer.render(Minecraft.getInstance().level, context.camera(), context.matrixStack());
            }
        });
    }

    private static void registerConfigKey()
    {
        try {
            Class.forName("net.xalcon.torchmaster.TorchmasterFabricConfigClient")
                    .getMethod("register")
                    .invoke(null);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
*///?} else if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer {
    public void onInitializeClient()
    {
        BlockRenderLayerMap.INSTANCE.putBlock(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
        registerConfigKey();
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (Minecraft.getInstance().level != null && context.matrixStack() != null) {
                TorchmasterLightRangeRenderer.render(Minecraft.getInstance().level, context.camera(), context.matrixStack());
            }
        });
    }

    private static void registerConfigKey()
    {
        try {
            Class.forName("net.xalcon.torchmaster.TorchmasterFabricConfigClient")
                    .getMethod("register")
                    .invoke(null);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
//?}
