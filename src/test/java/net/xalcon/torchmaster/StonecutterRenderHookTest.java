package net.xalcon.torchmaster;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StonecutterRenderHookTest
{
    @Test
    void activeVersionHasRangeRenderHook() throws Exception
    {
        //? if forge && >=1.19 {
        /*assertStaticMethod(
                "net.xalcon.torchmaster.TorchmasterForgeClient",
                "onRenderLevelStage",
                "net.minecraftforge.client.event.RenderLevelStageEvent");
        *///?} elif forge && >=1.18 {
        /*assertStaticMethod(
                "net.xalcon.torchmaster.TorchmasterForgeClient",
                "onRenderLevelLast",
                "net.minecraftforge.client.event.RenderLevelLastEvent");
        *///?} elif forge {
        /*assertStaticMethod(
                "net.xalcon.torchmaster.TorchmasterForgeClient",
                "onRenderWorldLast",
                "net.minecraftforge.client.event.RenderWorldLastEvent");
        *///?} elif neoforge && >=1.21.8 {
        /*assertStaticMethod(
                "net.xalcon.torchmaster.TorchmasterNeoforgeClient",
                "onRenderLevelStage",
                "net.neoforged.neoforge.client.event.RenderLevelStageEvent$AfterTranslucentBlocks");
        *///?} elif neoforge {
        /*assertStaticMethod(
                "net.xalcon.torchmaster.TorchmasterNeoforgeClient",
                "onRenderLevelStage",
                "net.neoforged.neoforge.client.event.RenderLevelStageEvent");
        *///?} elif fabric && >=1.21.8 {
        /*assertStaticMethod(
                "net.xalcon.torchmaster.mixin.LevelRendererMixin",
                "renderRange",
                "net.minecraft.client.Camera");
        *///?} elif fabric {
        assertClientInitializer("net.xalcon.torchmaster.TorchmasterFabricClient");
        //?}
    }

    private static void assertStaticMethod(String className, String methodName, String parameterClassName) throws Exception
    {
        Class<?> owner = Class.forName(className);
        Class<?> parameter = Class.forName(parameterClassName);
        Method method = owner.getDeclaredMethod(methodName, parameter);

        assertTrue(Modifier.isStatic(method.getModifiers()), className + "#" + methodName + " must be static");
    }

    private static void assertClientInitializer(String className) throws Exception
    {
        Class<?> owner = Class.forName(className);
        Method method = owner.getDeclaredMethod("onInitializeClient");

        assertTrue(Modifier.isPublic(method.getModifiers()), className + "#onInitializeClient must be public");
    }
}
