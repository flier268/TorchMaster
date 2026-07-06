package net.xalcon.torchmaster.client;

//? if >=1.15 <1.21.11 {
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
//?}

final class TorchmasterWorldRendererLineSubmitter
{
    private TorchmasterWorldRendererLineSubmitter()
    {
    }

    //? if >=1.15 <1.21.11 {
    static void submitBox(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
    {
        WorldRenderer.drawBox(poseStack, lineBuffer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, style.red, style.green, style.blue, style.alpha);
    }
    //?}
}
