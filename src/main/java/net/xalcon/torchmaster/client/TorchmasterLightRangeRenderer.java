package net.xalcon.torchmaster.client;

//? if <1.15 {
/*import com.mojang.blaze3d.platform.GlStateManager;
*///?}
import net.minecraft.client.MinecraftClient;
//? if >=1.21.11
//import net.minecraft.client.gl.RenderPipelines;
//? if <1.15
/*import net.minecraft.client.render.BufferBuilder;*/
import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.RenderLayer;
//? if >=1.21.11
//import net.minecraft.client.render.RenderSetup;
//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
//? if >=1.15
import net.minecraft.client.render.VertexConsumerProvider;
//? if <1.15
/*import net.minecraft.client.render.Tessellator;*/
//? if <1.15
/*import net.minecraft.client.render.VertexFormats;*/
import net.minecraft.client.render.WorldRenderer;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
//? if <1.15
/*import net.minecraft.util.math.Vec3d;*/
import net.minecraft.world.World;

public final class TorchmasterLightRangeRenderer
{
    //? if >=1.21.11
    //private static final RenderLayer LINE_LAYER = RenderLayer.of("torchmaster_lines", RenderSetup.builder(RenderPipelines.LINES).build());
    private static final float RANGE_RED = 0.15F;
    private static final float RANGE_GREEN = 0.85F;
    private static final float RANGE_BLUE = 1.0F;
    private static final float RANGE_ALPHA = 0.85F;
    private static final float LINE_WIDTH = 2.0F;
    private static final float SAMPLE_RED = 0.25F;
    private static final float SAMPLE_GREEN = 1.0F;
    private static final float SAMPLE_BLUE = 0.45F;
    private static final float SAMPLE_ALPHA = 0.42F;

    private TorchmasterLightRangeRenderer()
    {
    }

    //? if >=1.15 {
    public static void render(World level, Camera camera, MatrixStack poseStack)
    {
        VertexConsumerProvider.Immediate bufferSource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        renderAndFlush(level, camera, poseStack, bufferSource);
    }
    //?} else {
    /*public static void render(World level, Camera camera)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null || minecraft.player.isSneaking()) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.lineWidth(LINE_WIDTH);
        buffer.begin(1, VertexFormats.POSITION_COLOR);
        for (TorchmasterLightRangeDisplay.RangeSnapshot snapshot : TorchmasterLightRangeDisplay.snapshots(level)) {
            renderRangeBox(snapshot.pos, snapshot.radius, camera, buffer);
            for (BlockPos pos : snapshot.randomAirBlocks) {
                TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.sampleBox(pos);
                renderLineBox(camera, buffer,
                        box.minX, box.minY, box.minZ,
                        box.maxX, box.maxY, box.maxZ,
                        SAMPLE_RED, SAMPLE_GREEN, SAMPLE_BLUE, SAMPLE_ALPHA);
            }
        }
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
    }
    *///?}

    //? if >=1.15 {
    public static void renderAndFlush(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider.Immediate bufferSource)
    {
        render(level, camera, poseStack, bufferSource);
        flushLines(bufferSource);
    }

    public static void render(World level, Camera camera, MatrixStack poseStack, VertexConsumerProvider bufferSource)
    {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft.player == null || minecraft.player.isSneaking()) {
            return;
        }

        //? if >=1.21.11 {
        /*VertexConsumer lineBuffer = bufferSource.getBuffer(LINE_LAYER);
        *///?} else {
        VertexConsumer lineBuffer = bufferSource.getBuffer(RenderLayer.getLines());
        //?}

        poseStack.push();
        //? if >=1.21.11 {
        /*poseStack.translate(-camera.getCameraPos().x, -camera.getCameraPos().y, -camera.getCameraPos().z);
        *///?} else {
        poseStack.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
        //?}
        for (TorchmasterLightRangeDisplay.RangeSnapshot snapshot : TorchmasterLightRangeDisplay.snapshots(level)) {
            renderBox(TorchmasterRangeBoxes.rangeBox(snapshot.pos, snapshot.radius), poseStack, lineBuffer,
                    RANGE_RED, RANGE_GREEN, RANGE_BLUE, RANGE_ALPHA);
            for (BlockPos pos : snapshot.randomAirBlocks) {
                TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.sampleBox(pos);
                //? if >=1.21.11 {
                /*renderLineBox(poseStack, lineBuffer,
                        box.minX, box.minY, box.minZ,
                        box.maxX, box.maxY, box.maxZ,
                        SAMPLE_RED, SAMPLE_GREEN, SAMPLE_BLUE, SAMPLE_ALPHA);
                *///?} else {
                WorldRenderer.drawBox(poseStack, lineBuffer,
                        box.minX, box.minY, box.minZ,
                        box.maxX, box.maxY, box.maxZ,
                        SAMPLE_RED, SAMPLE_GREEN, SAMPLE_BLUE, SAMPLE_ALPHA);
                //?}
            }
        }
        poseStack.pop();
    }

    private static void flushLines(VertexConsumerProvider.Immediate bufferSource)
    {
        //? if >=1.21.11 {
        /*bufferSource.draw(LINE_LAYER);
        *///?} else {
        bufferSource.draw(RenderLayer.getLines());
        //?}
    }

    private static void renderBox(TorchmasterRangeBoxes.Box box, MatrixStack poseStack, VertexConsumer lineBuffer, float red, float green, float blue, float alpha)
    {
        //? if >=1.21.11 {
        /*renderLineBox(poseStack, lineBuffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
        *///?} else {
        WorldRenderer.drawBox(poseStack, lineBuffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
        //?}
    }
    //?} else {
    /*private static void renderRangeBox(BlockPos center, int radius, Camera camera, BufferBuilder buffer)
    {
        TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.rangeBox(center, radius);
        renderLineBox(camera, buffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, RANGE_RED, RANGE_GREEN, RANGE_BLUE, RANGE_ALPHA);
    }

    private static void renderLineBox(Camera camera, BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        Vec3d cameraPos = camera.getPos();
        minX -= cameraPos.x;
        minY -= cameraPos.y;
        minZ -= cameraPos.z;
        maxX -= cameraPos.x;
        maxY -= cameraPos.y;
        maxZ -= cameraPos.z;

        line(buffer, minX, minY, minZ, maxX, minY, minZ, red, green, blue, alpha);
        line(buffer, maxX, minY, minZ, maxX, minY, maxZ, red, green, blue, alpha);
        line(buffer, maxX, minY, maxZ, minX, minY, maxZ, red, green, blue, alpha);
        line(buffer, minX, minY, maxZ, minX, minY, minZ, red, green, blue, alpha);
        line(buffer, minX, maxY, minZ, maxX, maxY, minZ, red, green, blue, alpha);
        line(buffer, maxX, maxY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        line(buffer, maxX, maxY, maxZ, minX, maxY, maxZ, red, green, blue, alpha);
        line(buffer, minX, maxY, maxZ, minX, maxY, minZ, red, green, blue, alpha);
        line(buffer, minX, minY, minZ, minX, maxY, minZ, red, green, blue, alpha);
        line(buffer, maxX, minY, minZ, maxX, maxY, minZ, red, green, blue, alpha);
        line(buffer, maxX, minY, maxZ, maxX, maxY, maxZ, red, green, blue, alpha);
        line(buffer, minX, minY, maxZ, minX, maxY, maxZ, red, green, blue, alpha);
    }

    private static void line(BufferBuilder buffer, double startX, double startY, double startZ, double endX, double endY, double endZ, float red, float green, float blue, float alpha)
    {
        buffer.vertex(startX, startY, startZ).color(red, green, blue, alpha).next();
        buffer.vertex(endX, endY, endZ).color(red, green, blue, alpha).next();
    }
    *///?}

    //? if >=1.21.11 {
    /*private static void renderLineBox(MatrixStack poseStack, VertexConsumer lineBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        line(poseStack, lineBuffer, minX, minY, minZ, maxX, minY, minZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, maxX, minY, minZ, maxX, minY, maxZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, maxX, minY, maxZ, minX, minY, maxZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, minX, minY, maxZ, minX, minY, minZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, minX, maxY, minZ, maxX, maxY, minZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, maxX, maxY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, maxX, maxY, maxZ, minX, maxY, maxZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, minX, maxY, maxZ, minX, maxY, minZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, minX, minY, minZ, minX, maxY, minZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, maxX, minY, minZ, maxX, maxY, minZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, maxX, minY, maxZ, maxX, maxY, maxZ, red, green, blue, alpha);
        line(poseStack, lineBuffer, minX, minY, maxZ, minX, maxY, maxZ, red, green, blue, alpha);
    }

    private static void line(MatrixStack poseStack, VertexConsumer lineBuffer, double startX, double startY, double startZ, double endX, double endY, double endZ, float red, float green, float blue, float alpha)
    {
        MatrixStack.Entry entry = poseStack.peek();
        float normalX = (float)(endX - startX);
        float normalY = (float)(endY - startY);
        float normalZ = (float)(endZ - startZ);
        lineBuffer.vertex(entry, (float)startX, (float)startY, (float)startZ)
                .color(red, green, blue, alpha)
                .normal(entry, normalX, normalY, normalZ)
                .lineWidth(LINE_WIDTH);
        lineBuffer.vertex(entry, (float)endX, (float)endY, (float)endZ)
                .color(red, green, blue, alpha)
                .normal(entry, normalX, normalY, normalZ)
                .lineWidth(LINE_WIDTH);
    }
    *///?} elif fabric && forge && >=1.21.9 {
    /*private static void renderLineBox(PoseStack poseStack, VertexConsumer lineBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        ShapeRenderer.renderLineBox(poseStack.last(), lineBuffer, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
    }
    *///?} elif fabric && forge && >=1.21.2 {
    /*private static void renderLineBox(PoseStack poseStack, VertexConsumer lineBuffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        ShapeRenderer.renderLineBox(poseStack, lineBuffer, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
    }
    *///?}
}
