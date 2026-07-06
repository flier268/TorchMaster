package net.xalcon.torchmaster.client;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterConfigDraftTest
{
    @Test
    void mapsEntryListsInScreenOrder()
    {
        TorchmasterConfigDraft draft = TorchmasterConfigDraft.fromEntries(
                Arrays.asList(5, 255, 16, 10, 64, 65),
                Arrays.asList(true, false, true),
                Arrays.asList(
                        Collections.singletonList("+minecraft:zombie"),
                        Collections.singletonList("-minecraft:squid")));

        assertEquals(5, draft.feralFlareTickRate());
        assertTrue(draft.aggressiveSpawnChecks());
        assertEquals(Collections.singletonList("-minecraft:squid"), draft.dreadLampEntityBlockListOverrides());
    }

    @Test
    void rejectsUnexpectedEntryCounts()
    {
        assertThrows(IllegalArgumentException.class, () -> TorchmasterConfigDraft.fromEntries(
                Collections.singletonList(5),
                Arrays.asList(true, false, true),
                Arrays.asList(Collections.emptyList(), Collections.emptyList())));
    }
}
