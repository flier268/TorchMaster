package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.config.ITorchmasterConfig;

final class TorchmasterLightScreenModel
{
    static final String TITLE_KEY = "screen.torchmaster.light.title";

    private final LightType lightType;
    private final int radius;

    TorchmasterLightScreenModel(LightType lightType, ITorchmasterConfig config)
    {
        this.lightType = lightType;
        this.radius = radius(lightType, config);
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
        return lightType.blockTranslationKey();
    }

    static int radius(LightType lightType, ITorchmasterConfig config)
    {
        switch (lightType.kind()) {
            case MEGA_TORCH:
                return config.getMegaTorchRadius();
            case DREAD_LAMP:
                return config.getDreadLampRadius();
            default:
                return 0;
        }
    }
}
