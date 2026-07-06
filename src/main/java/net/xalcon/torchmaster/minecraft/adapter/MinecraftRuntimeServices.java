package net.xalcon.torchmaster.minecraft.adapter;

import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.platform.Services;
import net.xalcon.torchmaster.port.ConfigView;

public interface MinecraftRuntimeServices
{
    MinecraftRuntimeServices DEFAULT = new MinecraftRuntimeServices()
    {
        @Override
        public ConfigView config()
        {
            return new MinecraftConfigView(Services.PLATFORM.getConfig());
        }

        @Override
        public void debug(String message, Object... args)
        {
            TorchmasterRuntime.LOG.debug(message, args);
        }
    };

    ConfigView config();

    void debug(String message, Object... args);
}
