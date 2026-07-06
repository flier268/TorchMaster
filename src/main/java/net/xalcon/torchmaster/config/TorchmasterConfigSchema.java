package net.xalcon.torchmaster.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class TorchmasterConfigSchema
{
    public static final String SECTION_GENERAL = "General";

    public static final BooleanValue BEGINNER_TOOLTIPS = new BooleanValue(
            "beginnerTooltips",
            true,
            "Show additional information in the tooltip of certain items and blocks",
            "torchmaster.config.beginnerTooltips.description");
    public static final BooleanValue BLOCK_ONLY_NATURAL_SPAWNS = new BooleanValue(
            "blockOnlyNaturalSpawns",
            true,
            "By default, mega torches only block natural spawns (i.e. from low light levels). Setting this to false will also block spawns from spawners",
            "torchmaster.config.blockOnlyNaturalSpawns.description");
    public static final BooleanValue LYCANITES_MOBS_BLOCK_ALL = new BooleanValue(
            "lycanitesMobsBlockAll",
            true,
            "If this setting is enabled, the mega torch will block all natural spawn attempts from Lycanites Mobs in its radius",
            "torchmaster.config.lycanitesMobsBlockAll.description");
    public static final BooleanValue BLOCK_VILLAGE_SIEGES = new BooleanValue(
            "blockVillageSieges",
            true,
            "If this setting is enabled, the mega torch will block village sieges from zombies",
            "torchmaster.config.villagesiege.description");
    public static final BooleanValue RESTRICT_LIGHT_SETTINGS_TO_OWNER = new BooleanValue(
            "restrictLightSettingsToOwner",
            true,
            "If this setting is enabled, per-light settings can only be changed by the player who placed the light or by operators",
            "torchmaster.config.restrictLightSettingsToOwner.description");
    public static final IntValue MEGA_TORCH_RADIUS = new IntValue(
            "megaTorchRadius",
            64,
            0,
            Integer.MAX_VALUE,
            "The radius of the mega torch in each direction (cube) with the torch at its center",
            "torchmaster.config.megaTorchRadius.description");
    public static final IntValue DREAD_LAMP_RADIUS = new IntValue(
            "dreadLampRadius",
            64,
            0,
            Integer.MAX_VALUE,
            "The radius of the dread lamp in each direction (cube) with the torch at its center",
            "torchmaster.config.dreadLamp.description");
    public static final ListValue MEGA_TORCH_ENTITY_BLOCK_LIST_OVERRIDES = new ListValue(
            "megaTorchEntityBlockListOverrides",
            Collections.emptyList(),
            Arrays.asList(
                    "Use this setting to override the internal lists for entity blocking",
                    "You can use this to block more entities or even allow certain entities to still spawn",
                    "The + prefix will add the entity to the list, effectivly denying its spawns",
                    "The - prefix will remove the entity from the list (if necessary), effectivly allowing its spawns",
                    "Note: Each entry needs to be put in quotes! Multiple Entries should be separated by comma.",
                    "      The whole also needs to be enclosed by [ and ] respectively",
                    "Block zombies: [\"+minecraft:zombie\"]",
                    "Allow creepers: [\"-minecraft:creeper\"],",
                    "Multiple Entries: [\"+minecraft:pig\", \"-gaia:dryad\"]"),
            "torchmaster.config.megaTorch.blockListOverrides.description");
    public static final ListValue DREAD_LAMP_ENTITY_BLOCK_LIST_OVERRIDES = new ListValue(
            "dreadLampEntityBlockListOverrides",
            Collections.emptyList(),
            Arrays.asList(
                    "Same as the mega torch block list override, just for the dread lamp",
                    "Block squid: [\"+minecraft:squid\"]",
                    "Allow pigs: [\"-minecraft:pig\"]"),
            "torchmaster.config.dreadLamp.blockListOverrides.description");
    public static final IntValue FERAL_FLARE_RADIUS = new IntValue(
            "feralFlareRadius",
            16,
            1,
            127,
            "The radius in which the feral flare should try to place lights",
            "torchmaster.config.feralFlareRadius.description");
    public static final IntValue FERAL_FLARE_TICK_RATE = new IntValue(
            "feralFlareTickRate",
            5,
            1,
            Integer.MAX_VALUE,
            "Controls how often the flare should try to place lights. 1 means every tick, 10 every 10th tick, etc",
            "torchmaster.config.feralFlareTickRate.description");
    public static final IntValue FERAL_FLARE_MIN_LIGHT_LEVEL = new IntValue(
            "feralFlareMinLightLevel",
            10,
            0,
            15,
            "The target minimum light level to place lights for",
            "torchmaster.config.feralFlareMinLightLevel.description");
    public static final IntValue FERAL_FLARE_LANTERN_LIGHT_COUNT_HARDCAP = new IntValue(
            "feralFlareLanternLightCountHardcap",
            255,
            0,
            Short.MAX_VALUE,
            "The maximum amount of invisble lights a feral flare lantern can place. Set to 0 to disable light placement.\n"
                    + "Warning: Setting this value too high in conjunction with the feralFlareMinLightLevel and Radius can lead to world corruption!\n"
                    + "(Badly compressed packet error)",
            "torchmaster.config.feralFlareLanternLightCountHardcap.description");
    public static final BooleanValue AGGRESSIVE_SPAWN_CHECKS = new BooleanValue(
            "aggressiveSpawnChecks",
            false,
            "Configures the spawn check to be more aggressive, effectivly overriding the CheckSpawn results of other mods",
            "torchmaster.config.aggressiveSpawnChecks.description");

    public static final List<BooleanValue> BOOLEAN_VALUES = Arrays.asList(
            BEGINNER_TOOLTIPS,
            BLOCK_ONLY_NATURAL_SPAWNS,
            LYCANITES_MOBS_BLOCK_ALL,
            BLOCK_VILLAGE_SIEGES,
            RESTRICT_LIGHT_SETTINGS_TO_OWNER,
            AGGRESSIVE_SPAWN_CHECKS);
    public static final List<IntValue> INT_VALUES = Arrays.asList(
            MEGA_TORCH_RADIUS,
            DREAD_LAMP_RADIUS,
            FERAL_FLARE_RADIUS,
            FERAL_FLARE_TICK_RATE,
            FERAL_FLARE_MIN_LIGHT_LEVEL,
            FERAL_FLARE_LANTERN_LIGHT_COUNT_HARDCAP);
    public static final List<ListValue> LIST_VALUES = Arrays.asList(
            MEGA_TORCH_ENTITY_BLOCK_LIST_OVERRIDES,
            DREAD_LAMP_ENTITY_BLOCK_LIST_OVERRIDES);

    private TorchmasterConfigSchema()
    {
    }

    public static String path(ConfigValue value)
    {
        return SECTION_GENERAL + "." + value.key();
    }

    public interface ConfigValue
    {
        String key();
        List<String> comments();
        String translationKey();
    }

    public static final class BooleanValue implements ConfigValue
    {
        private final String key;
        private final boolean defaultValue;
        private final List<String> comments;
        private final String translationKey;

        public BooleanValue(String key, boolean defaultValue, String comment, String translationKey)
        {
            this(key, defaultValue, Collections.singletonList(comment), translationKey);
        }

        public BooleanValue(String key, boolean defaultValue, List<String> comments, String translationKey)
        {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments;
            this.translationKey = translationKey;
        }

        @Override
        public String key()
        {
            return key;
        }

        public boolean defaultValue()
        {
            return defaultValue;
        }

        @Override
        public List<String> comments()
        {
            return comments;
        }

        @Override
        public String translationKey()
        {
            return translationKey;
        }
    }

    public static final class IntValue implements ConfigValue
    {
        private final String key;
        private final int defaultValue;
        private final int min;
        private final int max;
        private final List<String> comments;
        private final String translationKey;

        public IntValue(String key, int defaultValue, int min, int max, String comment, String translationKey)
        {
            this(key, defaultValue, min, max, Collections.singletonList(comment), translationKey);
        }

        public IntValue(String key, int defaultValue, int min, int max, List<String> comments, String translationKey)
        {
            this.key = key;
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
            this.comments = comments;
            this.translationKey = translationKey;
        }

        @Override
        public String key()
        {
            return key;
        }

        public int defaultValue()
        {
            return defaultValue;
        }

        public int min()
        {
            return min;
        }

        public int max()
        {
            return max;
        }

        @Override
        public List<String> comments()
        {
            return comments;
        }

        @Override
        public String translationKey()
        {
            return translationKey;
        }

        public int normalize(Object value)
        {
            if (!(value instanceof Number)) {
                return defaultValue;
            }

            Number number = (Number)value;
            long parsed = number.longValue();
            if (parsed < min || parsed > max) {
                return defaultValue;
            }
            return (int)parsed;
        }
    }

    public static final class ListValue implements ConfigValue
    {
        private final String key;
        private final List<String> defaultValue;
        private final List<String> comments;
        private final String translationKey;

        public ListValue(String key, List<String> defaultValue, List<String> comments, String translationKey)
        {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments;
            this.translationKey = translationKey;
        }

        @Override
        public String key()
        {
            return key;
        }

        public List<String> defaultValue()
        {
            return defaultValue;
        }

        @Override
        public List<String> comments()
        {
            return comments;
        }

        @Override
        public String translationKey()
        {
            return translationKey;
        }

        public List<String> normalize(Object value, Predicate<Object> validator)
        {
            if (!(value instanceof List<?>)) {
                return defaultValue;
            }

            List<?> list = (List<?>)value;
            List<String> normalized = new ArrayList<>();
            for (Object item : list) {
                if (validator.test(item)) {
                    normalized.add((String)item);
                }
            }
            return normalized;
        }
    }
}
