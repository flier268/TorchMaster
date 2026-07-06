package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.config.ITorchmasterConfig;

final class TorchmasterLightScreenModel
{
    static final String TITLE_KEY = "screen.torchmaster.light.title";
    static final String RANGE_KEY = "screen.torchmaster.light.range";
    private static final String MEGA_TORCH_BLOCK_KEY = "block.torchmaster.megatorch";
    private static final String DREAD_LAMP_BLOCK_KEY = "block.torchmaster.dreadlamp";
    private static final String SHOW_RANGE_KEY = "screen.torchmaster.light.showRange";
    private static final String HIDE_RANGE_KEY = "screen.torchmaster.light.hideRange";

    private final LightType lightType;
    private final int radius;

    TorchmasterLightScreenModel(LightType lightType, ITorchmasterConfig config)
    {
        this.lightType = lightType;
        this.radius = lightType == LightType.MegaTorch ? config.getMegaTorchRadius() : config.getDreadLampRadius();
    }

    LightType lightType()
    {
        return lightType;
    }

    int radius()
    {
        return radius;
    }

    String blockKey()
    {
        return lightType == LightType.MegaTorch ? MEGA_TORCH_BLOCK_KEY : DREAD_LAMP_BLOCK_KEY;
    }

    String visibilityButtonKey(boolean visible)
    {
        return visible ? HIDE_RANGE_KEY : SHOW_RANGE_KEY;
    }
}
