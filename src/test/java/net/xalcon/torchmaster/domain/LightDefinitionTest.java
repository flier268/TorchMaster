package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LightDefinitionTest
{
    @Test
    void megaTorchKeepsLegacyKeyAndTextMetadata()
    {
        LightDefinition definition = LightDefinition.MEGA_TORCH;

        assertEquals(LightKind.MEGA_TORCH, definition.kind());
        assertEquals("MT_1_64_-2", definition.key(new BlockPosView(1, 64, -2)));
        assertEquals("Mega Torch", definition.displayName());
        assertEquals("block.torchmaster.megatorch", definition.blockTranslationKey());
    }

    @Test
    void dreadLampKeepsLegacyKeyAndTextMetadata()
    {
        LightDefinition definition = LightDefinition.DREAD_LAMP;

        assertEquals(LightKind.DREAD_LAMP, definition.kind());
        assertEquals("DL_1_64_-2", definition.key(new BlockPosView(1, 64, -2)));
        assertEquals("Dread Lamp", definition.displayName());
        assertEquals("block.torchmaster.dreadlamp", definition.blockTranslationKey());
    }
}
