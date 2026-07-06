package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterClientEventAdapterTest
{
    @Test
    void endTickPhaseOnlyAcceptsEnd()
    {
        assertTrue(TorchmasterClientEventAdapter.isEndTickPhase("END"));
        assertFalse(TorchmasterClientEventAdapter.isEndTickPhase("START"));
        assertFalse(TorchmasterClientEventAdapter.isEndTickPhase("Post"));
    }

    @Test
    void renderStageOnlyAcceptsAfterTranslucentBlocks()
    {
        assertTrue(TorchmasterClientEventAdapter.isAfterTranslucentStage("AFTER_TRANSLUCENT_BLOCKS"));
        assertFalse(TorchmasterClientEventAdapter.isAfterTranslucentStage("AFTER_ENTITIES"));
    }

    @Test
    void forgePoseStackCopyDescriptorTracksModernBranch()
    {
        assertTrue(TorchmasterClientEventAdapter.shouldCopyForgePoseStack(true));
        assertFalse(TorchmasterClientEventAdapter.shouldCopyForgePoseStack(false));
    }
}
