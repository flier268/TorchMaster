package net.xalcon.torchmaster;

import net.xalcon.torchmaster.config.ITorchmasterConfig;

public final class TorchmasterEntityFilters
{
    private static final EntityFilterList MEGA_TORCH_FILTER = new EntityFilterList(Constants.MOD_ID + ":entity_filter/mega_torch");
    private static final EntityFilterList DREAD_LAMP_FILTER = new EntityFilterList(Constants.MOD_ID + ":entity_filter/dread_lamp");

    private TorchmasterEntityFilters()
    {
    }

    public static EntityFilterList megaTorch()
    {
        return MEGA_TORCH_FILTER;
    }

    public static EntityFilterList dreadLamp()
    {
        return DREAD_LAMP_FILTER;
    }

    public static void reload(ITorchmasterConfig config)
    {
        TorchmasterEntityFilterRuntime.reload(MEGA_TORCH_FILTER, DREAD_LAMP_FILTER, config);
    }
}
