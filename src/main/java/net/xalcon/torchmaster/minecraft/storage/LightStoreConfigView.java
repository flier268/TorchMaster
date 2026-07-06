package net.xalcon.torchmaster.minecraft.storage;

import net.xalcon.torchmaster.EntityFilterList;
import net.xalcon.torchmaster.TorchmasterEntityFilters;
import net.xalcon.torchmaster.domain.EntityFilter;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftConfigView;
import net.xalcon.torchmaster.port.ConfigView;
import net.xalcon.torchmaster.platform.Services;

final class LightStoreConfigView
{
    private LightStoreConfigView()
    {
    }

    static ConfigView config()
    {
        return new MinecraftConfigView(Services.PLATFORM.getConfig());
    }

    static EntityFilter megaTorchFilter()
    {
        return entityFilter(TorchmasterEntityFilters.megaTorch());
    }

    static EntityFilter dreadLampFilter()
    {
        return entityFilter(TorchmasterEntityFilters.dreadLamp());
    }

    private static EntityFilter entityFilter(EntityFilterList filterList)
    {
        return MinecraftAdapterViews.entityFilter(filterList);
    }
}
