package net.xalcon.torchmaster.content;

import net.xalcon.torchmaster.domain.LightKind;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterContentDefinitionsTest
{
    @Test
    void blockDefinitionsContainExistingContentIds()
    {
        List<String> ids = TorchmasterContentDefinitions.blocks().stream()
                .map(TorchmasterContentDefinitions.BlockDefinition::id)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(
                TorchmasterContentDefinitions.MEGA_TORCH,
                TorchmasterContentDefinitions.DREAD_LAMP,
                TorchmasterContentDefinitions.FERAL_FLARE_LANTERN,
                TorchmasterContentDefinitions.INVISIBLE_LIGHT), ids);
    }

    @Test
    void creativeTabItemOrderMatchesLegacyRegistrationOrder()
    {
        assertEquals(Arrays.asList(
                TorchmasterContentDefinitions.MEGA_TORCH,
                TorchmasterContentDefinitions.DREAD_LAMP,
                TorchmasterContentDefinitions.FERAL_FLARE_LANTERN,
                TorchmasterContentDefinitions.FROZEN_PEARL), TorchmasterContentDefinitions.creativeTabItemIds());
    }

    @Test
    void creativeBlockItemsAreDerivedFromBlockDefinitions()
    {
        List<String> creativeBlockItems = TorchmasterContentDefinitions.blocks().stream()
                .filter(TorchmasterContentDefinitions.BlockDefinition::hasCreativeTabItem)
                .map(TorchmasterContentDefinitions.BlockDefinition::id)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(
                TorchmasterContentDefinitions.MEGA_TORCH,
                TorchmasterContentDefinitions.DREAD_LAMP,
                TorchmasterContentDefinitions.FERAL_FLARE_LANTERN), creativeBlockItems);
        assertTrue(TorchmasterContentDefinitions.creativeTabItemIds().containsAll(creativeBlockItems));
    }

    @Test
    void entityBlockingBlocksUseDomainLightKinds()
    {
        assertEquals(LightKind.MEGA_TORCH, TorchmasterContentDefinitions.MEGA_TORCH_BLOCK.lightKind());
        assertEquals(LightKind.DREAD_LAMP, TorchmasterContentDefinitions.DREAD_LAMP_BLOCK.lightKind());
    }
}
