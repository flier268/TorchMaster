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
        TorchmasterConfigEntries.Collector collector = TorchmasterConfigEntries.collector();
        collector.addInt("5");
        collector.addInt("255");
        collector.addInt("16");
        collector.addInt("10");
        collector.addInt("64");
        collector.addInt("65");
        collector.addBoolean(true);
        collector.addBoolean(false);
        collector.addBoolean(true);
        collector.addList("+minecraft:zombie");
        collector.addList("-minecraft:squid");

        TorchmasterConfigDraft draft = collector.toDraft();

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
