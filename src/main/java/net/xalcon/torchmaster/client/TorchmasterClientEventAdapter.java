package net.xalcon.torchmaster.client;

import java.util.Objects;
//? if forge && >=1.19 {
/*import net.minecraft.client.util.math.MatrixStack;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
*///?} else if forge && >=1.15 {
/*import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
*///?} else if forge {
/*import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
*///?}
//? if neoforge {
/*import net.neoforged.neoforge.client.event.ClientTickEvent;
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

    //? if forge && >=1.19 {
    /*public static void onForgeClientTick(TickEvent.ClientTickEvent event)
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
    /*public static void onForgeClientTick(TickEvent.ClientTickEvent event)
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
    /*public static void onForgeClientTick(TickEvent.ClientTickEvent event)
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
    /*public static void onNeoForgeClientTick(ClientTickEvent.Post event)
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
