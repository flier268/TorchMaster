package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterRangeRenderSessionTest
{
    @Test
    void shouldRenderRequiresPlayerThatIsNotSneaking()
    {
        assertFalse(TorchmasterRangeRenderSession.shouldRender(false, false));
        assertFalse(TorchmasterRangeRenderSession.shouldRender(true, true));
        assertTrue(TorchmasterRangeRenderSession.shouldRender(true, false));
    }
}
