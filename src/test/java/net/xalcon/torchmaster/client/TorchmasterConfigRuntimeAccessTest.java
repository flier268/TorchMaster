package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.config.ITorchmasterConfig;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterConfigRuntimeAccessTest
{
    @Test
    void runtimeFacadeUsesSuppliedConfigAndReload()
    {
        ITorchmasterConfig config = new TestConfig();
        AtomicBoolean reloaded = new AtomicBoolean(false);
        TorchmasterConfigScreenController.ConfigRuntime runtime = TorchmasterConfigRuntimeAccess.from(() -> config, () -> reloaded.set(true));

        assertSame(config, runtime.config());

        runtime.reload();

        assertTrue(reloaded.get());
    }

    private static final class TestConfig implements ITorchmasterConfig
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
        public java.util.List<String> getMegaTorchEntityBlockListOverrides() { return Collections.emptyList(); }
        public java.util.List<String> getDreadLampEntityBlockListOverrides() { return Collections.emptyList(); }
    }
}
