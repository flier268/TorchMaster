package net.xalcon.torchmaster.minecraft.adapter;

import net.xalcon.torchmaster.port.ConfigView;
import net.xalcon.torchmaster.config.ITorchmasterConfig;

public final class MinecraftConfigView implements ConfigView {
    private final ITorchmasterConfig config;

    public MinecraftConfigView(ITorchmasterConfig config) {
        this.config = config;
    }

    @Override
    public int feralFlareRadius() {
        return config.getFeralFlareRadius();
    }

    @Override
    public int dreadLampRadius() {
        return config.getDreadLampRadius();
    }

    @Override
    public int megaTorchRadius() {
        return config.getMegaTorchRadius();
    }

    @Override
    public boolean aggressiveSpawnChecks() {
        return config.getAggressiveSpawnChecks();
    }

    @Override
    public boolean blockOnlyNaturalSpawns() {
        return config.getBlockOnlyNaturalSpawns();
    }

    @Override
    public boolean blockVillageSieges() {
        return config.getBlockVillageSieges();
    }
}
