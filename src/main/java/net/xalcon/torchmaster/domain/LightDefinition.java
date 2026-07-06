package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;

public final class LightDefinition
{
    public static final LightDefinition MEGA_TORCH = new LightDefinition(
            LightKind.MEGA_TORCH,
            "MT",
            "Mega Torch",
            "block.torchmaster.megatorch");

    public static final LightDefinition DREAD_LAMP = new LightDefinition(
            LightKind.DREAD_LAMP,
            "DL",
            "Dread Lamp",
            "block.torchmaster.dreadlamp");

    private final LightKind kind;
    private final String keyPrefix;
    private final String displayName;
    private final String blockTranslationKey;

    private LightDefinition(LightKind kind, String keyPrefix, String displayName, String blockTranslationKey)
    {
        this.kind = kind;
        this.keyPrefix = keyPrefix;
        this.displayName = displayName;
        this.blockTranslationKey = blockTranslationKey;
    }

    public static LightDefinition forKind(LightKind kind)
    {
        switch (kind) {
            case MEGA_TORCH:
                return MEGA_TORCH;
            case DREAD_LAMP:
                return DREAD_LAMP;
            default:
                throw new IllegalArgumentException("Unsupported light kind: " + kind);
        }
    }

    public LightKind kind()
    {
        return kind;
    }

    public String key(BlockPosView pos)
    {
        return keyPrefix + "_" + pos.x() + "_" + pos.y() + "_" + pos.z();
    }

    public String displayName()
    {
        return displayName;
    }

    public String blockTranslationKey()
    {
        return blockTranslationKey;
    }
}
