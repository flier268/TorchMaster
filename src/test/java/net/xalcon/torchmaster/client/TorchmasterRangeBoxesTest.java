package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TorchmasterRangeBoxesTest
{
    @Test
    void rangeBoxExpandsAroundCenter()
    {
        TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.rangeBox(new BlockPos(10, 20, 30), 3);

        assertBox(box, 7, 17, 27, 14, 24, 34);
    }

    @Test
    void rangeBoxSupportsRadiusZero()
    {
        TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.rangeBox(new BlockPos(2, 4, 6), 0);

        assertBox(box, 2, 4, 6, 3, 5, 7);
    }

    @Test
    void rangeBoxSupportsNegativeCoordinates()
    {
        TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.rangeBox(new BlockPos(-10, -20, -30), 2);

        assertBox(box, -12, -22, -32, -7, -17, -27);
    }

    @Test
    void sampleBoxIsOneBlockWide()
    {
        TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.sampleBox(new BlockPos(-1, 0, 5));

        assertBox(box, -1, 0, 5, 0, 1, 6);
    }

    private static void assertBox(TorchmasterRangeBoxes.Box box, double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        assertEquals(minX, box.minX, 0.0D);
        assertEquals(minY, box.minY, 0.0D);
        assertEquals(minZ, box.minZ, 0.0D);
        assertEquals(maxX, box.maxX, 0.0D);
        assertEquals(maxY, box.maxY, 0.0D);
        assertEquals(maxZ, box.maxZ, 0.0D);
    }
}
