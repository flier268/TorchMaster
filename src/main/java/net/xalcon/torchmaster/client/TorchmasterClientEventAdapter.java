package net.xalcon.torchmaster.client;

import java.util.Objects;
//? if fabric {
import net.xalcon.torchmaster.TorchmasterContent;
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
//? if >=1.21.11
//import net.minecraft.client.render.BlockRenderLayer;
//? if >=1.16 && <1.21.11
import net.minecraft.client.render.RenderLayer;
//?}
//? if forge && >=1.19 {
/*import net.minecraft.client.util.math.MatrixStack;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
*///?} else if forge && >=1.15 {
/*import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
*///?} else if forge {
/*import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
*///?}
//? if neoforge {
/*import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
*///?}

public final class TorchmasterClientEventAdapter
{
    private TorchmasterClientEventAdapter()
    {
    }

    public static boolean isEndTickPhase(String phaseName)
    {
        return Objects.equals("END", phaseName);
    }

    public static boolean isAfterTranslucentStage(String stageName)
    {
        return Objects.equals("AFTER_TRANSLUCENT_BLOCKS", stageName);
    }

    public static boolean shouldCopyForgePoseStack(boolean minecraft1206OrNewer)
    {
        return minecraft1206OrNewer;
    }

    public static void onFabricEndClientTick()
    {
        TorchmasterClientLifecycle.onEndClientTick();
    }

    //? if fabric {
    public static void initializeFabricClient()
    {
        configureFabricRenderLayers();
        TorchmasterClientLifecycle.installLightScreenOpener();
        registerFabricClientTick();
        registerFabricRangeRenderer();
    }

    private static void configureFabricRenderLayers()
    {
        //? if >=1.21.11 {
        /*BlockRenderLayerMap.putBlock(TorchmasterContent.blockDreadLamp.get(), BlockRenderLayer.CUTOUT);
        *///?} else if >=1.16 {
        BlockRenderLayerMap.INSTANCE.putBlock(TorchmasterContent.blockDreadLamp.get(), RenderLayer.getCutout());
        //?}
    }

    private static void registerFabricClientTick()
    {
        //? if >=1.16 {
        ClientTickEvents.END_CLIENT_TICK.register(client -> onFabricEndClientTick());
        //?} else {
        /*ClientTickCallback.EVENT.register(client -> onFabricEndClientTick());
        *///?}
    }

    private static void registerFabricRangeRenderer()
    {
        //? if >=1.21.11 {
        /*WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            TorchmasterClientLifecycle.renderCurrentRange(context.matrices(), context.consumers());
        });
        *///?} else if >=1.16 {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (context.matrixStack() != null) {
                TorchmasterClientLifecycle.renderCurrentRange(context.matrixStack());
            }
        });
        //?}
    }
    //?}

    //? if forge && >=1.19 {
    /*public static void onForgeEvent(Event event)
    {
        if (event instanceof TickEvent.ClientTickEvent) {
            onForgeClientTick((TickEvent.ClientTickEvent) event);
        } else if (event instanceof RenderLevelStageEvent) {
            onForgeRenderLevelStage((RenderLevelStageEvent) event);
        }
    }

    public static void onForgeClientTick(TickEvent.ClientTickEvent event)
    {
        if (!isEndTickPhase(event.phase.name())) {
            return;
        }
        TorchmasterClientLifecycle.onEndClientTick();
    }

    public static void onForgeRenderLevelStage(RenderLevelStageEvent event)
    {
        if (!isAfterTranslucentStage(event.getStage().name())) {
            return;
        }
        //? if >=1.20.6 {
        MatrixStack poseStack = new MatrixStack();
        poseStack.multiplyPositionMatrix(event.getPoseStack());
        TorchmasterClientLifecycle.renderCurrentRange(poseStack);
        //?} else {
        /^TorchmasterClientLifecycle.renderCurrentRange(event.getPoseStack());^/
        //?}
    }
    *///?} else if forge && >=1.15 {
    /*public static void onForgeEvent(Event event)
    {
        if (event instanceof TickEvent.ClientTickEvent) {
            onForgeClientTick((TickEvent.ClientTickEvent) event);
        } else if (event instanceof RenderWorldLastEvent) {
            onForgeRenderWorldLast((RenderWorldLastEvent) event);
        }
    }

    public static void onForgeClientTick(TickEvent.ClientTickEvent event)
    {
        if (!isEndTickPhase(event.phase.name())) {
            return;
        }
        TorchmasterClientLifecycle.onEndClientTick();
    }

    public static void onForgeRenderWorldLast(RenderWorldLastEvent event)
    {
        TorchmasterClientLifecycle.renderCurrentRange(event.getMatrixStack());
    }
    *///?} else if forge {
    /*public static void onForgeEvent(Event event)
    {
        if (event instanceof TickEvent.ClientTickEvent) {
            onForgeClientTick((TickEvent.ClientTickEvent) event);
        } else if (event instanceof RenderWorldLastEvent) {
            onForgeRenderWorldLast((RenderWorldLastEvent) event);
        }
    }

    public static void onForgeClientTick(TickEvent.ClientTickEvent event)
    {
        if (!isEndTickPhase(event.phase.name())) {
            return;
        }
        TorchmasterClientLifecycle.onEndClientTick();
    }

    public static void onForgeRenderWorldLast(RenderWorldLastEvent event)
    {
        TorchmasterClientLifecycle.renderCurrentRange();
    }
    *///?}

    //? if neoforge {
    /*public static void onNeoForgeEvent(Event event)
    {
        if (event instanceof ClientTickEvent.Post) {
            onNeoForgeClientTick((ClientTickEvent.Post) event);
        //? if >=1.21.8 {
        } else if (event instanceof RenderLevelStageEvent.AfterTranslucentBlocks) {
            onNeoForgeRenderLevelStage((RenderLevelStageEvent.AfterTranslucentBlocks) event);
        //?} else {
        /^} else if (event instanceof RenderLevelStageEvent) {^/
        /^    onNeoForgeRenderLevelStage((RenderLevelStageEvent) event);^/
        //?}
        }
    }

    public static void onNeoForgeClientTick(ClientTickEvent.Post event)
    {
        TorchmasterClientLifecycle.onEndClientTick();
    }

    //? if >=1.21.8 {
    public static void onNeoForgeRenderLevelStage(RenderLevelStageEvent.AfterTranslucentBlocks event)
    {
        TorchmasterClientLifecycle.renderCurrentRange(event.getPoseStack());
    }
    //?} else {
    /^public static void onNeoForgeRenderLevelStage(RenderLevelStageEvent event)^/
    /^{^/
    /^    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {^/
    /^        return;^/
    /^    }^/
    /^    TorchmasterClientLifecycle.renderCurrentRange(event.getPoseStack());^/
    /^}^/
    //?}
    *///?}
}
