package net.xalcon.torchmaster.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.xalcon.torchmaster.EntityFilterList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TorchmasterTomlConfig implements ITorchmasterConfig
{
    private static final String LEGACY_FABRIC_CONFIG_NAME = "torchmaster-config";
    private static final Pattern LEGACY_STRING_VALUE = Pattern.compile("\"([^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\"");

    private final Path configFile;
    private int feralFlareTickRate;
    private int feralFlareLanternLightCountHardcap;
    private int feralFlareRadius;
    private int feralFlareMinLightLevel;
    private int dreadLampRadius;
    private int megaTorchRadius;
    private boolean aggressiveSpawnChecks;
    private boolean blockOnlyNaturalSpawns;
    private boolean blockVillageSieges;
    private List<String> megaTorchEntityBlockListOverrides;
    private List<String> dreadLampEntityBlockListOverrides;

    private TorchmasterTomlConfig(
            Path configFile,
            int feralFlareTickRate,
            int feralFlareLanternLightCountHardcap,
            int feralFlareRadius,
            int feralFlareMinLightLevel,
            int dreadLampRadius,
            int megaTorchRadius,
            boolean aggressiveSpawnChecks,
            boolean blockOnlyNaturalSpawns,
            boolean blockVillageSieges,
            List<String> megaTorchEntityBlockListOverrides,
            List<String> dreadLampEntityBlockListOverrides)
    {
        this.configFile = configFile;
        this.feralFlareTickRate = feralFlareTickRate;
        this.feralFlareLanternLightCountHardcap = feralFlareLanternLightCountHardcap;
        this.feralFlareRadius = feralFlareRadius;
        this.feralFlareMinLightLevel = feralFlareMinLightLevel;
        this.dreadLampRadius = dreadLampRadius;
        this.megaTorchRadius = megaTorchRadius;
        this.aggressiveSpawnChecks = aggressiveSpawnChecks;
        this.blockOnlyNaturalSpawns = blockOnlyNaturalSpawns;
        this.blockVillageSieges = blockVillageSieges;
        this.megaTorchEntityBlockListOverrides = immutableCopy(megaTorchEntityBlockListOverrides);
        this.dreadLampEntityBlockListOverrides = immutableCopy(dreadLampEntityBlockListOverrides);
    }

    private static List<String> immutableCopy(List<String> values)
    {
        return Collections.unmodifiableList(new ArrayList<>(values));
    }

    public static TorchmasterTomlConfig load(Path configFile)
    {
        try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).sync().build()) {
            config.load();
            migrateLegacyFabricConfig(config, configFile);
            applyDefaults(config);
            config.save();

            return new TorchmasterTomlConfig(
                    configFile,
                    readInt(config, TorchmasterConfigSchema.FERAL_FLARE_TICK_RATE),
                    readInt(config, TorchmasterConfigSchema.FERAL_FLARE_LANTERN_LIGHT_COUNT_HARDCAP),
                    readInt(config, TorchmasterConfigSchema.FERAL_FLARE_RADIUS),
                    readInt(config, TorchmasterConfigSchema.FERAL_FLARE_MIN_LIGHT_LEVEL),
                    readInt(config, TorchmasterConfigSchema.DREAD_LAMP_RADIUS),
                    readInt(config, TorchmasterConfigSchema.MEGA_TORCH_RADIUS),
                    readBoolean(config, TorchmasterConfigSchema.AGGRESSIVE_SPAWN_CHECKS),
                    readBoolean(config, TorchmasterConfigSchema.BLOCK_ONLY_NATURAL_SPAWNS),
                    readBoolean(config, TorchmasterConfigSchema.BLOCK_VILLAGE_SIEGES),
                    readList(config, TorchmasterConfigSchema.MEGA_TORCH_ENTITY_BLOCK_LIST_OVERRIDES),
                    readList(config, TorchmasterConfigSchema.DREAD_LAMP_ENTITY_BLOCK_LIST_OVERRIDES));
        }
    }

    public synchronized void save(
            int feralFlareTickRate,
            int feralFlareLanternLightCountHardcap,
            int feralFlareRadius,
            int feralFlareMinLightLevel,
            int dreadLampRadius,
            int megaTorchRadius,
            boolean aggressiveSpawnChecks,
            boolean blockOnlyNaturalSpawns,
            boolean blockVillageSieges,
            List<String> megaTorchEntityBlockListOverrides,
            List<String> dreadLampEntityBlockListOverrides)
    {
        try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).sync().build()) {
            config.load();
            applyDefaults(config);

            setInt(config, TorchmasterConfigSchema.FERAL_FLARE_TICK_RATE, feralFlareTickRate);
            setInt(config, TorchmasterConfigSchema.FERAL_FLARE_LANTERN_LIGHT_COUNT_HARDCAP, feralFlareLanternLightCountHardcap);
            setInt(config, TorchmasterConfigSchema.FERAL_FLARE_RADIUS, feralFlareRadius);
            setInt(config, TorchmasterConfigSchema.FERAL_FLARE_MIN_LIGHT_LEVEL, feralFlareMinLightLevel);
            setInt(config, TorchmasterConfigSchema.DREAD_LAMP_RADIUS, dreadLampRadius);
            setInt(config, TorchmasterConfigSchema.MEGA_TORCH_RADIUS, megaTorchRadius);
            setBoolean(config, TorchmasterConfigSchema.AGGRESSIVE_SPAWN_CHECKS, aggressiveSpawnChecks);
            setBoolean(config, TorchmasterConfigSchema.BLOCK_ONLY_NATURAL_SPAWNS, blockOnlyNaturalSpawns);
            setBoolean(config, TorchmasterConfigSchema.BLOCK_VILLAGE_SIEGES, blockVillageSieges);
            setList(config, TorchmasterConfigSchema.MEGA_TORCH_ENTITY_BLOCK_LIST_OVERRIDES, megaTorchEntityBlockListOverrides);
            setList(config, TorchmasterConfigSchema.DREAD_LAMP_ENTITY_BLOCK_LIST_OVERRIDES, dreadLampEntityBlockListOverrides);

            config.save();
            reloadFrom(config);
        }
    }

    private static void migrateLegacyFabricConfig(CommentedFileConfig config, Path configFile)
    {
        Path parent = configFile.getParent();
        if (parent == null) {
            return;
        }

        Path legacyConfig = legacyFabricConfigFile(parent);
        if (legacyConfig == null) {
            return;
        }

        String legacyContent;
        try {
            legacyContent = new String(Files.readAllBytes(legacyConfig), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            return;
        }

        migrateLegacyInt(config, legacyContent, "feralFlareLanternTickRate", TorchmasterConfigSchema.FERAL_FLARE_TICK_RATE);
        migrateLegacyInt(config, legacyContent, "feralFlareLanternLightHardcap", TorchmasterConfigSchema.FERAL_FLARE_LANTERN_LIGHT_COUNT_HARDCAP);
        migrateLegacyInt(config, legacyContent, "feralFlareLanternRadius", TorchmasterConfigSchema.FERAL_FLARE_RADIUS);
        migrateLegacyInt(config, legacyContent, "feralFlareLanternMinLightLevel", TorchmasterConfigSchema.FERAL_FLARE_MIN_LIGHT_LEVEL);
        migrateLegacyInt(config, legacyContent, "dreadLampRadius", TorchmasterConfigSchema.DREAD_LAMP_RADIUS);
        migrateLegacyInt(config, legacyContent, "megaTorchRadius", TorchmasterConfigSchema.MEGA_TORCH_RADIUS);
        migrateLegacyBoolean(config, legacyContent, "blockOnlyNaturalSpawns", TorchmasterConfigSchema.BLOCK_ONLY_NATURAL_SPAWNS);
        migrateLegacyBoolean(config, legacyContent, "blockVillageSieges", TorchmasterConfigSchema.BLOCK_VILLAGE_SIEGES);
        migrateLegacyList(config, legacyContent, "megaTorchEntityBlockListOverrides", TorchmasterConfigSchema.MEGA_TORCH_ENTITY_BLOCK_LIST_OVERRIDES);
        migrateLegacyList(config, legacyContent, "dreadLampEntityBlockListOverrides", TorchmasterConfigSchema.DREAD_LAMP_ENTITY_BLOCK_LIST_OVERRIDES);
    }

    private static Path legacyFabricConfigFile(Path configDir)
    {
        Path json5 = configDir.resolve(LEGACY_FABRIC_CONFIG_NAME + ".json5");
        if (Files.exists(json5)) {
            return json5;
        }

        Path json = configDir.resolve(LEGACY_FABRIC_CONFIG_NAME + ".json");
        if (Files.exists(json)) {
            return json;
        }

        return null;
    }

    private static void migrateLegacyInt(CommentedFileConfig config, String content, String legacyKey, TorchmasterConfigSchema.IntValue target)
    {
        String path = TorchmasterConfigSchema.path(target);
        if (config.get(path) instanceof Number) {
            return;
        }

        Matcher matcher = Pattern.compile(quotedKey(legacyKey) + "\\s*:\\s*(-?\\d+)").matcher(content);
        if (matcher.find()) {
            config.set(path, Integer.parseInt(matcher.group(1)));
        }
    }

    private static void migrateLegacyBoolean(CommentedFileConfig config, String content, String legacyKey, TorchmasterConfigSchema.BooleanValue target)
    {
        String path = TorchmasterConfigSchema.path(target);
        if (config.get(path) instanceof Boolean) {
            return;
        }

        Matcher matcher = Pattern.compile(quotedKey(legacyKey) + "\\s*:\\s*(true|false)").matcher(content);
        if (matcher.find()) {
            config.set(path, Boolean.parseBoolean(matcher.group(1)));
        }
    }

    private static void migrateLegacyList(CommentedFileConfig config, String content, String legacyKey, TorchmasterConfigSchema.ListValue target)
    {
        String path = TorchmasterConfigSchema.path(target);
        if (config.get(path) instanceof List<?>) {
            return;
        }

        Matcher matcher = Pattern.compile(quotedKey(legacyKey) + "\\s*:\\s*\\[(.*?)]", Pattern.DOTALL).matcher(content);
        if (!matcher.find()) {
            return;
        }

        List<String> values = new ArrayList<>();
        Matcher valueMatcher = LEGACY_STRING_VALUE.matcher(matcher.group(1));
        while (valueMatcher.find()) {
            values.add(valueMatcher.group(1));
        }
        config.set(path, values);
    }

    private static String quotedKey(String key)
    {
        return "\"\\Q" + key + "\\E\"";
    }

    private static void applyDefaults(CommentedFileConfig config)
    {
        for (TorchmasterConfigSchema.BooleanValue value : TorchmasterConfigSchema.BOOLEAN_VALUES) {
            String path = TorchmasterConfigSchema.path(value);
            if (!(config.get(path) instanceof Boolean)) {
                config.set(path, value.defaultValue());
            }
            config.setComment(path, comment(value));
        }

        for (TorchmasterConfigSchema.IntValue value : TorchmasterConfigSchema.INT_VALUES) {
            String path = TorchmasterConfigSchema.path(value);
            int normalized = value.normalize(config.get(path));
            config.set(path, normalized);
            config.setComment(path, comment(value));
        }

        for (TorchmasterConfigSchema.ListValue value : TorchmasterConfigSchema.LIST_VALUES) {
            String path = TorchmasterConfigSchema.path(value);
            config.set(path, value.normalize(config.get(path), EntityFilterList::IsValidFilterString));
            config.setComment(path, comment(value));
        }
    }

    private static String comment(TorchmasterConfigSchema.ConfigValue value)
    {
        return String.join(System.lineSeparator(), value.comments()) + System.lineSeparator() + "Translation: " + value.translationKey();
    }

    private static boolean readBoolean(CommentedFileConfig config, TorchmasterConfigSchema.BooleanValue value)
    {
        Boolean configured = config.get(TorchmasterConfigSchema.path(value));
        return configured != null ? configured : value.defaultValue();
    }

    private static int readInt(CommentedFileConfig config, TorchmasterConfigSchema.IntValue value)
    {
        return value.normalize(config.get(TorchmasterConfigSchema.path(value)));
    }

    private static List<String> readList(CommentedFileConfig config, TorchmasterConfigSchema.ListValue value)
    {
        return value.normalize(config.get(TorchmasterConfigSchema.path(value)), EntityFilterList::IsValidFilterString);
    }

    private static void setBoolean(CommentedFileConfig config, TorchmasterConfigSchema.BooleanValue value, boolean configured)
    {
        config.set(TorchmasterConfigSchema.path(value), configured);
        config.setComment(TorchmasterConfigSchema.path(value), comment(value));
    }

    private static void setInt(CommentedFileConfig config, TorchmasterConfigSchema.IntValue value, int configured)
    {
        config.set(TorchmasterConfigSchema.path(value), value.normalize(configured));
        config.setComment(TorchmasterConfigSchema.path(value), comment(value));
    }

    private static void setList(CommentedFileConfig config, TorchmasterConfigSchema.ListValue value, List<String> configured)
    {
        config.set(TorchmasterConfigSchema.path(value), value.normalize(configured, EntityFilterList::IsValidFilterString));
        config.setComment(TorchmasterConfigSchema.path(value), comment(value));
    }

    private void reloadFrom(CommentedFileConfig config)
    {
        feralFlareTickRate = readInt(config, TorchmasterConfigSchema.FERAL_FLARE_TICK_RATE);
        feralFlareLanternLightCountHardcap = readInt(config, TorchmasterConfigSchema.FERAL_FLARE_LANTERN_LIGHT_COUNT_HARDCAP);
        feralFlareRadius = readInt(config, TorchmasterConfigSchema.FERAL_FLARE_RADIUS);
        feralFlareMinLightLevel = readInt(config, TorchmasterConfigSchema.FERAL_FLARE_MIN_LIGHT_LEVEL);
        dreadLampRadius = readInt(config, TorchmasterConfigSchema.DREAD_LAMP_RADIUS);
        megaTorchRadius = readInt(config, TorchmasterConfigSchema.MEGA_TORCH_RADIUS);
        aggressiveSpawnChecks = readBoolean(config, TorchmasterConfigSchema.AGGRESSIVE_SPAWN_CHECKS);
        blockOnlyNaturalSpawns = readBoolean(config, TorchmasterConfigSchema.BLOCK_ONLY_NATURAL_SPAWNS);
        blockVillageSieges = readBoolean(config, TorchmasterConfigSchema.BLOCK_VILLAGE_SIEGES);
        megaTorchEntityBlockListOverrides = immutableCopy(readList(config, TorchmasterConfigSchema.MEGA_TORCH_ENTITY_BLOCK_LIST_OVERRIDES));
        dreadLampEntityBlockListOverrides = immutableCopy(readList(config, TorchmasterConfigSchema.DREAD_LAMP_ENTITY_BLOCK_LIST_OVERRIDES));
    }

    @Override
    public int getFeralFlareTickRate()
    {
        return feralFlareTickRate;
    }

    @Override
    public int getFeralFlareLanternLightCountHardcap()
    {
        return feralFlareLanternLightCountHardcap;
    }

    @Override
    public int getFeralFlareRadius()
    {
        return feralFlareRadius;
    }

    @Override
    public int getFeralFlareMinLightLevel()
    {
        return feralFlareMinLightLevel;
    }

    @Override
    public int getDreadLampRadius()
    {
        return dreadLampRadius;
    }

    @Override
    public int getMegaTorchRadius()
    {
        return megaTorchRadius;
    }

    @Override
    public boolean getAggressiveSpawnChecks()
    {
        return aggressiveSpawnChecks;
    }

    @Override
    public boolean getBlockOnlyNaturalSpawns()
    {
        return blockOnlyNaturalSpawns;
    }

    @Override
    public boolean getBlockVillageSieges()
    {
        return blockVillageSieges;
    }

    @Override
    public List<String> getMegaTorchEntityBlockListOverrides()
    {
        return new ArrayList<>(megaTorchEntityBlockListOverrides);
    }

    @Override
    public List<String> getDreadLampEntityBlockListOverrides()
    {
        return new ArrayList<>(dreadLampEntityBlockListOverrides);
    }
}
