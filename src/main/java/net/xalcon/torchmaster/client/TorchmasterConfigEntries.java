package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.domain.EntityFilterOverrideRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class TorchmasterConfigEntries
{
    private TorchmasterConfigEntries()
    {
    }

    static List<EntryDefinition> fromConfig(ITorchmasterConfig config)
    {
        List<EntryDefinition> entries = new ArrayList<>();
        entries.add(EntryDefinition.integer("screen.torchmaster.config.feralFlareTickRate",
                config.getFeralFlareTickRate(), "screen.torchmaster.config.unit.ticks"));
        entries.add(EntryDefinition.integer("screen.torchmaster.config.feralFlareLanternLightHardcap",
                config.getFeralFlareLanternLightCountHardcap(), "screen.torchmaster.config.unit.lights"));
        entries.add(EntryDefinition.integer("screen.torchmaster.config.feralFlareRadius",
                config.getFeralFlareRadius(), "screen.torchmaster.config.unit.blocks"));
        entries.add(EntryDefinition.integer("screen.torchmaster.config.feralFlareMinLightLevel",
                config.getFeralFlareMinLightLevel(), "screen.torchmaster.config.unit.lightLevel"));
        entries.add(EntryDefinition.integer("screen.torchmaster.config.dreadLampRadius",
                config.getDreadLampRadius(), "screen.torchmaster.config.unit.blocks"));
        entries.add(EntryDefinition.integer("screen.torchmaster.config.megaTorchRadius",
                config.getMegaTorchRadius(), "screen.torchmaster.config.unit.blocks"));
        entries.add(EntryDefinition.bool("screen.torchmaster.config.aggressiveSpawnChecks", config.getAggressiveSpawnChecks()));
        entries.add(EntryDefinition.bool("screen.torchmaster.config.blockOnlyNaturalSpawns", config.getBlockOnlyNaturalSpawns()));
        entries.add(EntryDefinition.bool("screen.torchmaster.config.blockVillageSieges", config.getBlockVillageSieges()));
        entries.add(EntryDefinition.list("screen.torchmaster.config.megaTorchEntityBlockListOverrides", config.getMegaTorchEntityBlockListOverrides()));
        entries.add(EntryDefinition.list("screen.torchmaster.config.dreadLampEntityBlockListOverrides", config.getDreadLampEntityBlockListOverrides()));
        return Collections.unmodifiableList(entries);
    }

    static Collector collector()
    {
        return new Collector();
    }

    static List<String> parseList(String text)
    {
        List<String> values = new ArrayList<>();
        if (text.trim().isEmpty()) {
            return values;
        }
        for (String value : Arrays.asList(text.split(","))) {
            String trimmed = value.trim();
            if (!trimmed.isEmpty()) {
                values.add(trimmed);
            }
        }
        return values;
    }

    enum EntryType
    {
        INTEGER,
        BOOLEAN,
        LIST
    }

    static final class EntryDefinition
    {
        private final EntryType type;
        private final String translationKey;
        private final String unitKey;
        private final int intValue;
        private final boolean booleanValue;
        private final List<String> listValue;

        private EntryDefinition(EntryType type, String translationKey, String unitKey, int intValue, boolean booleanValue,
                List<String> listValue)
        {
            this.type = type;
            this.translationKey = translationKey;
            this.unitKey = unitKey;
            this.intValue = intValue;
            this.booleanValue = booleanValue;
            this.listValue = listValue;
        }

        static EntryDefinition integer(String translationKey, int value)
        {
            return integer(translationKey, value, "");
        }

        static EntryDefinition integer(String translationKey, int value, String unitKey)
        {
            return new EntryDefinition(EntryType.INTEGER, translationKey, unitKey, value, false, Collections.emptyList());
        }

        static EntryDefinition bool(String translationKey, boolean value)
        {
            return new EntryDefinition(EntryType.BOOLEAN, translationKey, "", 0, value, Collections.emptyList());
        }

        static EntryDefinition list(String translationKey, List<String> value)
        {
            return new EntryDefinition(EntryType.LIST, translationKey, "", 0, false, value);
        }

        EntryType type()
        {
            return type;
        }

        String translationKey()
        {
            return translationKey;
        }

        String unitKey()
        {
            return unitKey;
        }

        int intValue()
        {
            return intValue;
        }

        boolean booleanValue()
        {
            return booleanValue;
        }

        List<String> listValue()
        {
            return listValue;
        }
    }

    static final class Collector
    {
        private final List<Integer> ints = new ArrayList<>();
        private final List<Boolean> booleans = new ArrayList<>();
        private final List<List<String>> lists = new ArrayList<>();

        ReadResult addInt(String text)
        {
            try {
                ints.add(Integer.parseInt(text.trim()));
                return ReadResult.success();
            } catch (NumberFormatException ignored) {
                return ReadResult.failure("screen.torchmaster.config.invalidNumber");
            }
        }

        ReadResult addBoolean(boolean value)
        {
            booleans.add(value);
            return ReadResult.success();
        }

        ReadResult addList(String text)
        {
            List<String> parsed = parseList(text);
            for (String value : parsed) {
                if (!EntityFilterOverrideRules.isValidFilterString(value)) {
                    return ReadResult.failure("screen.torchmaster.config.invalidFilter");
                }
            }
            lists.add(parsed);
            return ReadResult.success();
        }

        TorchmasterConfigDraft toDraft()
        {
            return TorchmasterConfigDraft.fromEntries(ints, booleans, lists);
        }
    }

    static final class ReadResult
    {
        private static final ReadResult SUCCESS = new ReadResult(true, null);

        private final boolean success;
        private final String errorKey;

        private ReadResult(boolean success, String errorKey)
        {
            this.success = success;
            this.errorKey = errorKey;
        }

        static ReadResult success()
        {
            return SUCCESS;
        }

        static ReadResult failure(String errorKey)
        {
            return new ReadResult(false, errorKey);
        }

        boolean isSuccess()
        {
            return success;
        }

        String errorKey()
        {
            return errorKey;
        }
    }
}
