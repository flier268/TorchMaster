package net.xalcon.torchmaster;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//? if >=1.16
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if <1.16
//import net.fabricmc.fabric.api.event.client.ClientTickCallback;
//? if >=1.21.11 {
/*import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
*///?} else if >=1.16 {
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
//?}
import net.minecraft.client.MinecraftClient;
//? if >=1.21.11
//import net.minecraft.client.render.BlockRenderLayer;
//? if >=1.16 && <1.21.11
import net.minecraft.client.render.RenderLayer;
import net.xalcon.torchmaster.client.TorchmasterLightRangeDisplay;
import net.xalcon.torchmaster.client.TorchmasterLightRangeRenderer;
import net.xalcon.torchmaster.client.TorchmasterLightScreen;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        configureRenderLayers();
        TorchmasterClientBridge.setLightScreenOpener(TorchmasterLightScreen::open);
        registerClientTick();
        registerRangeRenderer();
    }

    private static void configureRenderLayers()
    {
        //? if >=1.21.11 {
        /*BlockRenderLayerMap.putBlock(TorchmasterContent.blockDreadLamp.get(), BlockRenderLayer.CUTOUT);
        *///?} else if >=1.16 {
        BlockRenderLayerMap.INSTANCE.putBlock(TorchmasterContent.blockDreadLamp.get(), RenderLayer.getCutout());
        //?}
    }

    private static void registerClientTick()
    {
        //? if >=1.16 {
        ClientTickEvents.END_CLIENT_TICK.register(client -> TorchmasterLightRangeDisplay.tick());
        //?} else {
        /*ClientTickCallback.EVENT.register(client -> TorchmasterLightRangeDisplay.tick());
        *///?}
    }

    private static void registerRangeRenderer()
    {
        //? if >=1.21.11 {
        /*WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            if (minecraft.world != null) {
                TorchmasterLightRangeRenderer.render(minecraft.world, minecraft.gameRenderer.getCamera(), context.matrices(), context.consumers());
            }
        });
        *///?} else if >=1.16 {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            if (minecraft.world != null && context.matrixStack() != null) {
                TorchmasterLightRangeRenderer.render(minecraft.world, context.camera(), context.matrixStack());
            }
        });
        //?}
    }
}
//?}
