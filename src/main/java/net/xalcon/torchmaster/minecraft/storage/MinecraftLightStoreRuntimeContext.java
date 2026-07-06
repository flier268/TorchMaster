package net.xalcon.torchmaster.minecraft.storage;

import net.xalcon.torchmaster.EntityFilterList;
import net.xalcon.torchmaster.TorchmasterEntityFilters;
import net.xalcon.torchmaster.domain.EntityFilter;
import net.xalcon.torchmaster.domain.LightStoreRuntimeContext;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftConfigView;
import net.xalcon.torchmaster.platform.Services;

final class MinecraftLightStoreRuntimeContext
{
    private MinecraftLightStoreRuntimeContext()
    {
    }

    static LightStoreRuntimeContext create()
    {
        return new LightStoreRuntimeContext(
                new MinecraftConfigView(Services.PLATFORM.getConfig()),
                entityFilter(TorchmasterEntityFilters.megaTorch()),
                entityFilter(TorchmasterEntityFilters.dreadLamp()));
    }

    private static EntityFilter entityFilter(EntityFilterList filterList)
    {
        return MinecraftAdapterViews.entityFilter(filterList);
    }
}
