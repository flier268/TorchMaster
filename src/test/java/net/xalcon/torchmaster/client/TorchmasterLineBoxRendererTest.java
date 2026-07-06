package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterLineBoxRendererTest
{
    @Test
    void boxProducesTwelveEdges()
    {
        TorchmasterLineBoxRenderer.Line[] lines = TorchmasterLineBoxRenderer.lines(TorchmasterRangeBoxes.sampleBox(new BlockPos(1, 2, 3)));

        assertEquals(12, lines.length);
        assertLine(lines[0], 1, 2, 3, 2, 2, 3);
        assertLine(lines[11], 1, 2, 4, 1, 3, 4);
    }

    @Test
    void linesPreserveNegativeCoordinates()
    {
        TorchmasterLineBoxRenderer.Line[] lines = TorchmasterLineBoxRenderer.lines(TorchmasterRangeBoxes.rangeBox(new BlockPos(-2, -3, -4), 0));

        assertLine(lines[0], -2, -3, -4, -1, -3, -4);
        assertLine(lines[6], -1, -2, -3, -2, -2, -3);
    }

    @Test
    void stylesKeepRenderColorsAndLineWidth()
    {
        assertStyle(TorchmasterLineBoxRenderer.RANGE_STYLE, 0.15F, 0.85F, 1.0F, 0.85F);
        assertStyle(TorchmasterLineBoxRenderer.SAMPLE_STYLE, 0.25F, 1.0F, 0.45F, 0.42F);
        assertTrue(TorchmasterLineBoxRenderer.LINE_WIDTH > 0.0F);
    }

    private static void assertLine(TorchmasterLineBoxRenderer.Line line, double startX, double startY, double startZ, double endX, double endY, double endZ)
    {
        assertEquals(startX, line.startX, 0.0D);
        assertEquals(startY, line.startY, 0.0D);
        assertEquals(startZ, line.startZ, 0.0D);
        assertEquals(endX, line.endX, 0.0D);
        assertEquals(endY, line.endY, 0.0D);
        assertEquals(endZ, line.endZ, 0.0D);
    }

    private static void assertStyle(TorchmasterLineBoxRenderer.Style style, float red, float green, float blue, float alpha)
    {
        assertEquals(red, style.red, 0.0F);
        assertEquals(green, style.green, 0.0F);
        assertEquals(blue, style.blue, 0.0F);
        assertEquals(alpha, style.alpha, 0.0F);
    }
}
