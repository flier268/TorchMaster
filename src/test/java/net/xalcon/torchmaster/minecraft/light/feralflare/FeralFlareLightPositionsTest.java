package net.xalcon.torchmaster.minecraft.light.feralflare;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeralFlareLightPositionsTest {
    @Test
    void encodesAndDecodesMultipleChildLights() {
        BlockPos origin = new BlockPos(100, 64, -100);
        List<BlockPos> childLights = Arrays.asList(
                new BlockPos(96, 70, -90),
                new BlockPos(101, 63, -101));

        int[] encoded = FeralFlareLightPositions.encode(origin, childLights);
        List<BlockPos> decoded = FeralFlareLightPositions.decode(origin, encoded);

        assertEquals(childLights, decoded);
    }

    @Test
    void encodesEmptyList() {
        BlockPos origin = new BlockPos(0, 64, 0);

        int[] encoded = FeralFlareLightPositions.encode(origin, Collections.emptyList());
        List<BlockPos> decoded = FeralFlareLightPositions.decode(origin, encoded);

        assertArrayEquals(new int[0], encoded);
        assertTrue(decoded.isEmpty());
    }

    @Test
    void keepsSignedByteOffsetRange() {
        BlockPos origin = new BlockPos(0, 0, 0);
        List<BlockPos> childLights = Collections.singletonList(new BlockPos(-128, 127, -1));

        int[] encoded = FeralFlareLightPositions.encode(origin, childLights);
        List<BlockPos> decoded = FeralFlareLightPositions.decode(origin, encoded);

        assertEquals(childLights, decoded);
    }
}
