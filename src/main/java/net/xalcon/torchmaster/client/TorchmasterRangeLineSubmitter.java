package net.xalcon.torchmaster.client;

//? if <1.15
/*import net.minecraft.client.render.BufferBuilder;*/
import net.minecraft.client.render.Camera;
//? if >=1.15
import net.minecraft.client.render.VertexConsumer;
//? if >=1.15
import net.minecraft.client.util.math.MatrixStack;

final class TorchmasterRangeLineSubmitter
{
    private TorchmasterRangeLineSubmitter()
    {
    }

    //? if >=1.15 {
    static void submitBox(MatrixStack poseStack, VertexConsumer lineBuffer, TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
    {
        //? if >=1.21.11 {
        /*TorchmasterLatestLineSubmitter.submitBox(poseStack, lineBuffer, box, style);
        *///?} else {
        TorchmasterWorldRendererLineSubmitter.submitBox(poseStack, lineBuffer, box, style);
        //?}
    }
    //?} else {
    /*static void submitBox(Camera camera, BufferBuilder buffer, TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
    {
        TorchmasterLegacyLineSubmitter.submitBox(camera, buffer, box, style);
    }
    *///?}

    static int submittedLineCount(TorchmasterRangeBoxes.Box box)
    {
        return TorchmasterLineBoxRenderer.lines(box).length;
    }

    static float lineWidth()
    {
        return TorchmasterLineBoxRenderer.LINE_WIDTH;
    }
}
