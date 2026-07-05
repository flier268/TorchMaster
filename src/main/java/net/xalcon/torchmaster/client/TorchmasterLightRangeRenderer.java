//? if >=1.16 {
package net.xalcon.torchmaster.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
//? if >=1.21.2 {
/*import net.minecraft.client.renderer.ShapeRenderer;
*///?}
//? if >=1.21.11 {
/*
import net.minecraft.world.phys.shapes.Shapes;
*///?}
//? if >=1.21.11 {
/*import net.minecraft.client.renderer.rendertype.RenderTypes;
*///?} else {
import net.minecraft.client.renderer.RenderType;
//?}
//? if <1.21.2 {
import net.minecraft.client.renderer.LevelRenderer;
//?}
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class TorchmasterLightRangeRenderer
{
    private static final float RANGE_RED = 0.15F;
    private static final float RANGE_GREEN = 0.85F;
    private static final float RANGE_BLUE = 1.0F;
    private static final float RANGE_ALPHA = 0.85F;
    private static final float SAMPLE_RED = 0.25F;
    private static final float SAMPLE_GREEN = 1.0F;
    private static final float SAMPLE_BLUE = 0.45F;
    private static final float SAMPLE_ALPHA = 0.42F;

    private TorchmasterLightRangeRenderer()
    {
    }

    public static void render(Level level, Camera camera, PoseStack poseStack)
    {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        renderAndFlush(level, camera, poseStack, bufferSource);
    }

    public static void renderAndFlush(Level level, Camera camera, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource)
    {
        render(level, camera, poseStack, bufferSource);
        flushLines(bufferSource);
    }

    public static void render(Level level, Camera camera, PoseStack poseStack, MultiBufferSource bufferSource)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.player.isShiftKeyDown()) {
            return;
        }

        //? if >=1.21.11 {
        /*VertexConsumer lineBuffer = bufferSource.getBuffer(RenderTypes.lines());
        *///?} else {
        VertexConsumer lineBuffer = bufferSource.getBuffer(RenderType.lines());
        //?}

        poseStack.pushPose();
        //? if >=1.21.11 {
        /*poseStack.translate(-camera.position().x, -camera.position().y, -camera.position().z);
        *///?} else {
        poseStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
        //?}
        for (TorchmasterLightRangeDisplay.RangeSnapshot snapshot : TorchmasterLightRangeDisplay.snapshots(level)) {
            renderRangeBox(snapshot.pos, snapshot.radius, poseStack, lineBuffer);
            for (BlockPos pos : snapshot.randomAirBlocks) {
                //? if >=1.21.2 {
                /*renderLineBox(poseStack, lineBuffer,
                        pos.getX(), pos.getY(), pos.getZ(),
                        pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1,
                        SAMPLE_RED, SAMPLE_GREEN, SAMPLE_BLUE, SAMPLE_ALPHA);
                *///?} else {
                LevelRenderer.renderLineBox(poseStack, lineBuffer,
                        pos.getX(), pos.getY(), pos.getZ(),
                        pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1,
                        SAMPLE_RED, SAMPLE_GREEN, SAMPLE_BLUE, SAMPLE_ALPHA);
                //?}
            }
        }
        poseStack.popPose();
    }

    private static void flushLines(MultiBufferSource.BufferSource bufferSource)
    {
        //? if >=1.21.11 {
        /*bufferSource.endBatch(RenderTypes.lines());
        *///?} else {
        bufferSource.endBatch(RenderType.lines());
        //?}
    }

    private static void renderRangeBox(BlockPos center, int radius, PoseStack poseStack, VertexConsumer lineBuffer)
    {
        double minX = center.getX() - radius;
        double minY = center.getY() - radius;
        double minZ = center.getZ() - radius;
        double maxX = center.getX() + radius + 1;
        double maxY = center.getY() + radius + 1;
        double maxZ = center.getZ() + radius + 1;

        //? if >=1.21.2 {
        /*renderLineBox(poseStack, lineBuffer, minX, minY, minZ, maxX, maxY, maxZ, RANGE_RED, RANGE_GREEN, RANGE_BLUE, RANGE_ALPHA);
        *///?} else {
        LevelRenderer.renderLineBox(poseStack, lineBuffer, minX, minY, minZ, maxX, maxY, maxZ, RANGE_RED, RANGE_GREEN, RANGE_BLUE, RANGE_ALPHA);
        //?}
    }

    //? if >=1.21.11 {
    /*private static void renderLineBox(PoseStack poseStack, VertexConsumer lineBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        ShapeRenderer.renderShape(poseStack, lineBuffer, Shapes.box(minX, minY, minZ, maxX, maxY, maxZ), 0.0, 0.0, 0.0, color(red, green, blue), alpha);
    }

    private static int color(float red, float green, float blue)
    {
        return ((int)(red * 255.0F) << 16) | ((int)(green * 255.0F) << 8) | (int)(blue * 255.0F);
    }
    *///?} elif >=1.21.9 {
    /*private static void renderLineBox(PoseStack poseStack, VertexConsumer lineBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        ShapeRenderer.renderLineBox(poseStack.last(), lineBuffer, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
    }
    *///?} elif >=1.21.2 {
    /*private static void renderLineBox(PoseStack poseStack, VertexConsumer lineBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        ShapeRenderer.renderLineBox(poseStack, lineBuffer, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
    }
    *///?}
}
//?}
