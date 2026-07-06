package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterConfigScreenControllerTest
{
    @TempDir
    Path tempDir;

    @Test
    void saveActionCollectsRowsIntoDraftAndReloadsRuntime()
    {
        java.util.List<TorchmasterConfigWidgetRows.Row> rows = new java.util.ArrayList<>();
        TorchmasterConfigScreenController controller = new TorchmasterConfigScreenController(rows);
        addValidRows(rows);
        TorchmasterTomlConfig config = TorchmasterTomlConfig.load(tempDir.resolve("torchmaster.toml"));
        Runtime runtime = new Runtime(config);

        TorchmasterConfigScreenController.ActionOutcome outcome = controller.runAction(TorchmasterConfigScreenActions.Action.SAVE, runtime);

        assertSame(TorchmasterConfigScreenController.ActionOutcome.NONE, outcome);
        assertTrue(runtime.reloaded);
        assertSame(config, runtime.refreshedRangeConfig);
        assertEquals(5, config.getFeralFlareTickRate());
        assertEquals(65, config.getMegaTorchRadius());
        assertTrue(config.getAggressiveSpawnChecks());
        assertFalse(config.getBlockOnlyNaturalSpawns());
        assertFalse(config.getRestrictLightSettingsToOwner());
        assertEquals(Collections.singletonList("-minecraft:squid"), config.getDreadLampEntityBlockListOverrides());
        assertEquals("screen.torchmaster.config.saved", controller.status().translationKey());
        assertEquals(0xFF55FF55, controller.statusColor());
    }

    @Test
    void saveActionReportsUnsupportedConfigWithoutReloading()
    {
        TorchmasterConfigScreenController controller = new TorchmasterConfigScreenController();
        Runtime runtime = new Runtime(new UnsupportedConfig());

        TorchmasterConfigScreenController.ActionOutcome outcome = controller.runAction(TorchmasterConfigScreenActions.Action.SAVE, runtime);

        assertSame(TorchmasterConfigScreenController.ActionOutcome.NONE, outcome);
        assertFalse(runtime.reloaded);
        assertSame(null, runtime.refreshedRangeConfig);
        assertEquals("screen.torchmaster.config.unsupported", controller.status().translationKey());
        assertEquals(0xFFFF5555, controller.statusColor());
    }

    @Test
    void resetAndDoneReturnScreenLifecycleOutcomes()
    {
        TorchmasterConfigScreenController controller = new TorchmasterConfigScreenController();

        assertSame(TorchmasterConfigScreenController.ActionOutcome.REBUILD_WIDGETS,
                controller.runAction(TorchmasterConfigScreenActions.Action.RESET, new Runtime(new UnsupportedConfig())));
        assertEquals(0, controller.scrollOffset());
        assertEquals("screen.torchmaster.config.reverted", controller.status().translationKey());
        assertEquals(0xFFFFFF55, controller.statusColor());

        assertSame(TorchmasterConfigScreenController.ActionOutcome.CLOSE_SCREEN,
                controller.runAction(TorchmasterConfigScreenActions.Action.DONE, new Runtime(new UnsupportedConfig())));
    }

    @Test
    void readOnlyActionRejectsSaveAndReset()
    {
        TorchmasterConfigScreenController controller = new TorchmasterConfigScreenController();
        controller.setReadOnly(true);

        assertSame(TorchmasterConfigScreenController.ActionOutcome.NONE,
                controller.runAction(TorchmasterConfigScreenActions.Action.SAVE, new Runtime(new UnsupportedConfig())));
        assertEquals("screen.torchmaster.config.readOnly", controller.status().translationKey());
        assertEquals(0xFFFF5555, controller.statusColor());

        assertSame(TorchmasterConfigScreenController.ActionOutcome.NONE,
                controller.runAction(TorchmasterConfigScreenActions.Action.RESET, new Runtime(new UnsupportedConfig())));
    }

    private static void addValidRows(java.util.List<TorchmasterConfigWidgetRows.Row> rows)
    {
        rows.add(Row.integer("5"));
        rows.add(Row.integer("255"));
        rows.add(Row.integer("16"));
        rows.add(Row.integer("10"));
        rows.add(Row.integer("64"));
        rows.add(Row.integer("65"));
        rows.add(Row.bool(true));
        rows.add(Row.bool(false));
        rows.add(Row.bool(true));
        rows.add(Row.bool(false));
        rows.add(Row.list("+minecraft:zombie"));
        rows.add(Row.list("-minecraft:squid"));
    }

    private static final class Row extends TorchmasterConfigWidgetRows.Row
    {
        private final String intValue;
        private final Boolean booleanValue;
        private final String listValue;

        private Row(TorchmasterConfigEntries.EntryDefinition definition, String intValue, Boolean booleanValue, String listValue)
        {
            super(definition, 0);
            this.intValue = intValue;
            this.booleanValue = booleanValue;
            this.listValue = listValue;
        }

        static Row integer(String value)
        {
            return new Row(TorchmasterConfigEntries.EntryDefinition.integer("test.int", 0), value, null, null);
        }

        static Row bool(boolean value)
        {
            return new Row(TorchmasterConfigEntries.EntryDefinition.bool("test.bool", value), null, value, null);
        }

        static Row list(String value)
        {
            return new Row(TorchmasterConfigEntries.EntryDefinition.list("test.list", Collections.emptyList()), null, null, value);
        }

        @Override
        void setPosition(TorchmasterConfigScreenLayout layout, int fieldX, int y)
        {
            this.y = y;
        }

        @Override
        void setVisible(boolean visible)
        {
            this.visible = visible;
        }

        @Override
        void setEditable(boolean editable)
        {
        }

        @Override
        TorchmasterConfigEntries.ReadResult read(TorchmasterConfigEntries.Collector collector)
        {
            if (intValue != null) {
                return collector.addInt(intValue);
            }
            if (booleanValue != null) {
                return collector.addBoolean(booleanValue);
            }
            return collector.addList(listValue);
        }
    }

    private static final class Runtime implements TorchmasterConfigScreenController.ConfigRuntime
    {
        private final ITorchmasterConfig config;
        private boolean reloaded;
        private ITorchmasterConfig refreshedRangeConfig;

        private Runtime(ITorchmasterConfig config)
        {
            this.config = config;
        }

        @Override
        public ITorchmasterConfig config()
        {
            return config;
        }

        @Override
        public void reload()
        {
            reloaded = true;
        }

        @Override
        public void refreshRangeDisplay(ITorchmasterConfig config)
        {
            refreshedRangeConfig = config;
        }
    }

    private static final class UnsupportedConfig implements ITorchmasterConfig
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
        public java.util.List<String> getMegaTorchEntityBlockListOverrides() { return Collections.emptyList(); }
        public java.util.List<String> getDreadLampEntityBlockListOverrides() { return Collections.emptyList(); }
    }
}
