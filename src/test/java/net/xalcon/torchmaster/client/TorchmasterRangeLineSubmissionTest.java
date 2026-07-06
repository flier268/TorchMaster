package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TorchmasterRangeLineSubmissionTest
{
    @Test
    void coordinatorKeepsSharedLineGeometryInvariants()
    {
        TorchmasterRangeBoxes.Box box = TorchmasterRangeBoxes.rangeBox(new BlockPos(1, 2, 3), 2);

        assertEquals(12, TorchmasterRangeLineSubmitter.submittedLineCount(box));
        assertEquals(TorchmasterLineBoxRenderer.LINE_WIDTH, TorchmasterRangeLineSubmitter.lineWidth(), 0.0001F);
    }
}
