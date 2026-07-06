package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.ConfigView;

public final class LightStoreRuntimeContext
{
    private final ConfigView config;
    private final EntityFilter megaTorchFilter;
    private final EntityFilter dreadLampFilter;

    public LightStoreRuntimeContext(ConfigView config, EntityFilter megaTorchFilter, EntityFilter dreadLampFilter)
    {
        this.config = config;
        this.megaTorchFilter = megaTorchFilter;
        this.dreadLampFilter = dreadLampFilter;
    }

    public ConfigView config()
    {
        return config;
    }

    public EntityFilter megaTorchFilter()
    {
        return megaTorchFilter;
    }

    public EntityFilter dreadLampFilter()
    {
        return dreadLampFilter;
    }
}
