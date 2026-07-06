package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.config.ITorchmasterConfig;

import java.util.function.Supplier;

final class TorchmasterConfigRuntimeAccess
{
    static final TorchmasterConfigScreenController.ConfigRuntime DEFAULT = from(TorchmasterRuntime::getConfig, TorchmasterRuntime::onWorldLoaded);

    private TorchmasterConfigRuntimeAccess()
    {
    }

    static TorchmasterConfigScreenController.ConfigRuntime from(Supplier<ITorchmasterConfig> config, Runnable reload)
    {
        return new TorchmasterConfigScreenController.ConfigRuntime()
        {
            @Override
            public ITorchmasterConfig config()
            {
                return config.get();
            }

            @Override
            public void reload()
            {
                reload.run();
            }

            @Override
            public void refreshRangeDisplay(ITorchmasterConfig config)
            {
                TorchmasterLightRangeDisplay.refreshRadii(config);
            }
        };
    }
}
