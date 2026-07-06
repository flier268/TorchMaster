package net.xalcon.torchmaster;

import net.xalcon.torchmaster.compat.VanillaCompat;
import net.xalcon.torchmaster.config.ITorchmasterConfig;

public final class TorchmasterEntityFilterRuntime
{
    private TorchmasterEntityFilterRuntime()
    {
    }

    public static void reload(EntityFilterList megaTorchFilter, EntityFilterList dreadLampFilter, ITorchmasterConfig config)
    {
        dreadLampFilter.clear();
        megaTorchFilter.clear();

        VanillaCompat.registerDreadLampEntities(dreadLampFilter);
        VanillaCompat.registerTorchEntities(megaTorchFilter);

        dreadLampFilter.applyListOverrides(config.getDreadLampEntityBlockListOverrides());
        megaTorchFilter.applyListOverrides(config.getMegaTorchEntityBlockListOverrides());
    }
}
