package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.config.TorchmasterTomlConfig;

import java.util.List;

final class TorchmasterConfigDraft
{
    private static final int EXPECTED_INTS = 6;
    private static final int EXPECTED_BOOLEANS = 4;
    private static final int EXPECTED_LISTS = 2;

    private final int feralFlareTickRate;
    private final int feralFlareLanternLightCountHardcap;
    private final int feralFlareRadius;
    private final int feralFlareMinLightLevel;
    private final int dreadLampRadius;
    private final int megaTorchRadius;
    private final boolean aggressiveSpawnChecks;
    private final boolean blockOnlyNaturalSpawns;
    private final boolean blockVillageSieges;
    private final boolean restrictLightSettingsToOwner;
    private final List<String> megaTorchEntityBlockListOverrides;
    private final List<String> dreadLampEntityBlockListOverrides;

    private TorchmasterConfigDraft(
            int feralFlareTickRate,
            int feralFlareLanternLightCountHardcap,
            int feralFlareRadius,
            int feralFlareMinLightLevel,
            int dreadLampRadius,
            int megaTorchRadius,
            boolean aggressiveSpawnChecks,
            boolean blockOnlyNaturalSpawns,
            boolean blockVillageSieges,
            boolean restrictLightSettingsToOwner,
            List<String> megaTorchEntityBlockListOverrides,
            List<String> dreadLampEntityBlockListOverrides)
    {
        this.feralFlareTickRate = feralFlareTickRate;
        this.feralFlareLanternLightCountHardcap = feralFlareLanternLightCountHardcap;
        this.feralFlareRadius = feralFlareRadius;
        this.feralFlareMinLightLevel = feralFlareMinLightLevel;
        this.dreadLampRadius = dreadLampRadius;
        this.megaTorchRadius = megaTorchRadius;
        this.aggressiveSpawnChecks = aggressiveSpawnChecks;
        this.blockOnlyNaturalSpawns = blockOnlyNaturalSpawns;
        this.blockVillageSieges = blockVillageSieges;
        this.restrictLightSettingsToOwner = restrictLightSettingsToOwner;
        this.megaTorchEntityBlockListOverrides = megaTorchEntityBlockListOverrides;
        this.dreadLampEntityBlockListOverrides = dreadLampEntityBlockListOverrides;
    }

    static TorchmasterConfigDraft fromEntries(List<Integer> ints, List<Boolean> booleans, List<List<String>> lists)
    {
        if (ints.size() != EXPECTED_INTS || booleans.size() != EXPECTED_BOOLEANS || lists.size() != EXPECTED_LISTS) {
            throw new IllegalArgumentException("Unexpected Torchmaster config draft entry counts");
        }
        return new TorchmasterConfigDraft(
                ints.get(0),
                ints.get(1),
                ints.get(2),
                ints.get(3),
                ints.get(4),
                ints.get(5),
                booleans.get(0),
                booleans.get(1),
                booleans.get(2),
                booleans.get(3),
                lists.get(0),
                lists.get(1));
    }

    void saveTo(TorchmasterTomlConfig config)
    {
        config.save(
                feralFlareTickRate,
                feralFlareLanternLightCountHardcap,
                feralFlareRadius,
                feralFlareMinLightLevel,
                dreadLampRadius,
                megaTorchRadius,
                aggressiveSpawnChecks,
                blockOnlyNaturalSpawns,
                blockVillageSieges,
                restrictLightSettingsToOwner,
                megaTorchEntityBlockListOverrides,
                dreadLampEntityBlockListOverrides);
    }

    int feralFlareTickRate()
    {
        return feralFlareTickRate;
    }

    boolean aggressiveSpawnChecks()
    {
        return aggressiveSpawnChecks;
    }

    boolean restrictLightSettingsToOwner()
    {
        return restrictLightSettingsToOwner;
    }

    List<String> dreadLampEntityBlockListOverrides()
    {
        return dreadLampEntityBlockListOverrides;
    }
}
