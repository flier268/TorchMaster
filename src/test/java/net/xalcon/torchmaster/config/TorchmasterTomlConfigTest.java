package net.xalcon.torchmaster.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchmasterTomlConfigTest
{
    @TempDir
    Path tempDir;

    @Test
    void createsMissingConfigWithDefaults() throws Exception
    {
        Path configFile = tempDir.resolve("torchmaster.toml");

        ITorchmasterConfig config = TorchmasterTomlConfig.load(configFile);

        assertTrue(Files.exists(configFile));
        assertEquals(64, config.getMegaTorchRadius());
        assertEquals(64, config.getDreadLampRadius());
        assertEquals(16, config.getFeralFlareRadius());
        assertEquals(5, config.getFeralFlareTickRate());
        assertEquals(10, config.getFeralFlareMinLightLevel());
        assertEquals(255, config.getFeralFlareLanternLightCountHardcap());
        assertTrue(config.getBlockOnlyNaturalSpawns());
        assertTrue(config.getBlockVillageSieges());
        assertTrue(config.getRestrictLightSettingsToOwner());
        assertEquals(Collections.emptyList(), config.getMegaTorchEntityBlockListOverrides());

        String content = readString(configFile);
        assertTrue(content.contains("[General]"));
        assertTrue(content.contains("megaTorchRadius = 64"));
        assertTrue(content.contains("feralFlareLanternLightCountHardcap = 255"));
        assertTrue(content.contains("restrictLightSettingsToOwner = true"));
        assertFalse(content.contains("frozenPearlDurability"));
    }

    @Test
    void readsExistingForgeStyleKeys() throws Exception
    {
        Path configFile = tempDir.resolve("torchmaster.toml");
        writeString(configFile,
                "[General]\n"
                        + "blockOnlyNaturalSpawns = false\n"
                        + "blockVillageSieges = false\n"
                        + "megaTorchRadius = 12\n"
                        + "dreadLampRadius = 13\n"
                        + "feralFlareRadius = 14\n"
                        + "feralFlareTickRate = 15\n"
                        + "feralFlareMinLightLevel = 9\n"
                        + "feralFlareLanternLightCountHardcap = 16\n"
                        + "aggressiveSpawnChecks = true\n"
                        + "restrictLightSettingsToOwner = false\n"
                        + "megaTorchEntityBlockListOverrides = [\"+minecraft:zombie\"]\n"
                        + "dreadLampEntityBlockListOverrides = [\"-minecraft:squid\"]\n");

        ITorchmasterConfig config = TorchmasterTomlConfig.load(configFile);

        assertEquals(12, config.getMegaTorchRadius());
        assertEquals(13, config.getDreadLampRadius());
        assertEquals(14, config.getFeralFlareRadius());
        assertEquals(15, config.getFeralFlareTickRate());
        assertEquals(9, config.getFeralFlareMinLightLevel());
        assertEquals(16, config.getFeralFlareLanternLightCountHardcap());
        assertTrue(config.getAggressiveSpawnChecks());
        assertFalse(config.getRestrictLightSettingsToOwner());
        assertEquals(Collections.singletonList("+minecraft:zombie"), config.getMegaTorchEntityBlockListOverrides());
        assertEquals(Collections.singletonList("-minecraft:squid"), config.getDreadLampEntityBlockListOverrides());
    }

    @Test
    void resetsOutOfRangeIntegersToDefaults() throws Exception
    {
        Path configFile = tempDir.resolve("torchmaster.toml");
        writeString(configFile,
                "[General]\n"
                        + "megaTorchRadius = -1\n"
                        + "feralFlareRadius = 0\n"
                        + "feralFlareMinLightLevel = 99\n");

        ITorchmasterConfig config = TorchmasterTomlConfig.load(configFile);

        assertEquals(64, config.getMegaTorchRadius());
        assertEquals(16, config.getFeralFlareRadius());
        assertEquals(10, config.getFeralFlareMinLightLevel());

        String content = readString(configFile);
        assertTrue(content.contains("megaTorchRadius = 64"));
        assertTrue(content.contains("feralFlareRadius = 16"));
        assertTrue(content.contains("feralFlareMinLightLevel = 10"));
    }

    @Test
    void filtersInvalidBlockListOverrides() throws Exception
    {
        Path configFile = tempDir.resolve("torchmaster.toml");
        writeString(configFile,
                "[General]\n"
                        + "megaTorchEntityBlockListOverrides = [\"+minecraft:zombie\", \"minecraft:creeper\", \"-minecraft:skeleton\", 7]\n"
                        + "dreadLampEntityBlockListOverrides = [\"-minecraft:squid\", \"+bad.path:entity\"]\n");

        ITorchmasterConfig config = TorchmasterTomlConfig.load(configFile);

        assertEquals(Arrays.asList("+minecraft:zombie", "-minecraft:skeleton"), config.getMegaTorchEntityBlockListOverrides());
        assertEquals(Collections.singletonList("-minecraft:squid"), config.getDreadLampEntityBlockListOverrides());
    }

    @Test
    void returnedBlockListOverridesDoNotMutateConfig() throws Exception
    {
        Path configFile = tempDir.resolve("torchmaster.toml");
        writeString(configFile,
                "[General]\n"
                        + "megaTorchEntityBlockListOverrides = [\"+minecraft:zombie\"]\n");

        ITorchmasterConfig config = TorchmasterTomlConfig.load(configFile);

        config.getMegaTorchEntityBlockListOverrides().add("-minecraft:zombie");

        assertEquals(Collections.singletonList("+minecraft:zombie"), config.getMegaTorchEntityBlockListOverrides());
    }

    @Test
    void migratesLegacyFabricOwoConfig() throws Exception
    {
        Path configFile = tempDir.resolve("torchmaster.toml");
        writeString(tempDir.resolve("torchmaster-config.json5"),
                "{\n"
                        + "  \"feralFlareLanternTickRate\": 2,\n"
                        + "  \"feralFlareLanternLightHardcap\": 17,\n"
                        + "  \"feralFlareLanternRadius\": 24,\n"
                        + "  \"feralFlareLanternMinLightLevel\": 7,\n"
                        + "  \"dreadLampRadius\": 44,\n"
                        + "  \"megaTorchRadius\": 45,\n"
                        + "  \"blockOnlyNaturalSpawns\": false,\n"
                        + "  \"blockVillageSieges\": false,\n"
                        + "  \"megaTorchEntityBlockListOverrides\": [\"+minecraft:zombie\"],\n"
                        + "  \"dreadLampEntityBlockListOverrides\": [\"-minecraft:squid\"]\n"
                        + "}\n");

        ITorchmasterConfig config = TorchmasterTomlConfig.load(configFile);

        assertEquals(2, config.getFeralFlareTickRate());
        assertEquals(17, config.getFeralFlareLanternLightCountHardcap());
        assertEquals(24, config.getFeralFlareRadius());
        assertEquals(7, config.getFeralFlareMinLightLevel());
        assertEquals(44, config.getDreadLampRadius());
        assertEquals(45, config.getMegaTorchRadius());
        assertFalse(config.getBlockOnlyNaturalSpawns());
        assertFalse(config.getBlockVillageSieges());
        assertEquals(Collections.singletonList("+minecraft:zombie"), config.getMegaTorchEntityBlockListOverrides());
        assertEquals(Collections.singletonList("-minecraft:squid"), config.getDreadLampEntityBlockListOverrides());

        String content = readString(configFile);
        assertTrue(content.contains("feralFlareTickRate = 2"));
        assertTrue(content.contains("feralFlareRadius = 24"));
    }

    @Test
    void existingTomlValuesWinOverLegacyFabricConfig() throws Exception
    {
        Path configFile = tempDir.resolve("torchmaster.toml");
        writeString(configFile,
                "[General]\n"
                        + "feralFlareTickRate = 15\n");
        writeString(tempDir.resolve("torchmaster-config.json"),
                "{\n"
                        + "  \"feralFlareLanternTickRate\": 2\n"
                        + "}\n");

        ITorchmasterConfig config = TorchmasterTomlConfig.load(configFile);

        assertEquals(15, config.getFeralFlareTickRate());
    }

    private static String readString(Path path) throws Exception
    {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static void writeString(Path path, String content) throws Exception
    {
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
    }
}
