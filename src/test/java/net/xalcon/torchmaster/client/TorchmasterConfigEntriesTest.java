package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.config.ITorchmasterConfig;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterConfigEntriesTest
{
    @Test
    void definitionsKeepScreenSaveOrder()
    {
        List<TorchmasterConfigEntries.EntryDefinition> entries = TorchmasterConfigEntries.fromConfig(config());

        assertEquals("screen.torchmaster.config.feralFlareTickRate", entries.get(0).translationKey());
        assertEquals(TorchmasterConfigEntries.EntryType.INTEGER, entries.get(0).type());
        assertEquals("screen.torchmaster.config.aggressiveSpawnChecks", entries.get(6).translationKey());
        assertEquals(TorchmasterConfigEntries.EntryType.BOOLEAN, entries.get(6).type());
        assertEquals("screen.torchmaster.config.restrictLightSettingsToOwner", entries.get(9).translationKey());
        assertEquals(TorchmasterConfigEntries.EntryType.BOOLEAN, entries.get(9).type());
        assertEquals("screen.torchmaster.config.dreadLampEntityBlockListOverrides", entries.get(11).translationKey());
        assertEquals(TorchmasterConfigEntries.EntryType.LIST, entries.get(11).type());
    }

    @Test
    void collectorBuildsDraftInEntryOrder()
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
        collector.addBoolean(false);
        collector.addList("+minecraft:zombie");
        collector.addList("-minecraft:squid");

        TorchmasterConfigDraft draft = collector.toDraft();

        assertEquals(5, draft.feralFlareTickRate());
        assertTrue(draft.aggressiveSpawnChecks());
        assertFalse(draft.restrictLightSettingsToOwner());
        assertEquals(Collections.singletonList("-minecraft:squid"), draft.dreadLampEntityBlockListOverrides());
    }

    @Test
    void collectorRejectsInvalidIntAndFilter()
    {
        assertFalse(TorchmasterConfigEntries.collector().addInt("not-a-number").isSuccess());
        assertFalse(TorchmasterConfigEntries.collector().addList("minecraft:zombie").isSuccess());
        assertTrue(TorchmasterConfigEntries.collector().addList("+minecraft:zombie, -minecraft:squid").isSuccess());
    }

    @Test
    void parseListTrimsEmptyValues()
    {
        assertEquals(Arrays.asList("+minecraft:zombie", "-minecraft:squid"),
                TorchmasterConfigEntries.parseList(" +minecraft:zombie, , -minecraft:squid "));
    }

    private static ITorchmasterConfig config()
    {
        return new ITorchmasterConfig()
        {
            public int getFeralFlareTickRate() { return 5; }
            public int getFeralFlareLanternLightCountHardcap() { return 255; }
            public int getFeralFlareRadius() { return 16; }
            public int getFeralFlareMinLightLevel() { return 10; }
            public int getDreadLampRadius() { return 64; }
            public int getMegaTorchRadius() { return 65; }
            public boolean getAggressiveSpawnChecks() { return true; }
            public boolean getBlockOnlyNaturalSpawns() { return false; }
            public boolean getBlockVillageSieges() { return true; }
            public boolean getRestrictLightSettingsToOwner() { return true; }
            public List<String> getMegaTorchEntityBlockListOverrides() { return Collections.singletonList("+minecraft:zombie"); }
            public List<String> getDreadLampEntityBlockListOverrides() { return Collections.singletonList("-minecraft:squid"); }
        };
    }
}
