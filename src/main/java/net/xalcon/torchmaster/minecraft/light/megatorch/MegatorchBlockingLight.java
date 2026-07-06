package net.xalcon.torchmaster.minecraft.light.megatorch;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterContent;
import net.xalcon.torchmaster.domain.LightDefinition;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.domain.LightKind;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;
import net.xalcon.torchmaster.port.LightAccessEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MegatorchBlockingLight implements MinecraftBlockingLight
{
    private BlockPos pos;
    private final boolean diamondBase;
    private final LightSettings settings;
    private final Optional<UUID> ownerUuid;
    private final List<LightAccessEntry> allowedPlayers;
    private final boolean rangeVisible;

    public MegatorchBlockingLight(BlockPos pos)
    {
        this(pos, false, LightSettings.unconfigured());
    }

    public MegatorchBlockingLight(BlockPos pos, boolean diamondBase)
    {
        this(pos, diamondBase, LightSettings.unconfigured());
    }

    public MegatorchBlockingLight(BlockPos pos, boolean diamondBase, LightSettings settings)
    {
        this(pos, diamondBase, settings, Optional.empty(), Collections.emptyList());
    }

    public MegatorchBlockingLight(BlockPos pos, boolean diamondBase, LightSettings settings, Optional<UUID> ownerUuid)
    {
        this(pos, diamondBase, settings, ownerUuid, Collections.emptyList());
    }

    public MegatorchBlockingLight(BlockPos pos, boolean diamondBase, LightSettings settings, Optional<UUID> ownerUuid,
            List<LightAccessEntry> allowedPlayers)
    {
        this(pos, diamondBase, settings, ownerUuid, allowedPlayers, false);
    }

    public MegatorchBlockingLight(BlockPos pos, boolean diamondBase, LightSettings settings, Optional<UUID> ownerUuid,
            List<LightAccessEntry> allowedPlayers, boolean rangeVisible)
    {
        this.pos = pos;
        this.diamondBase = diamondBase;
        this.settings = settings;
        this.ownerUuid = ownerUuid == null ? Optional.empty() : ownerUuid;
        this.allowedPlayers = Collections.unmodifiableList(new ArrayList<>(allowedPlayers == null ? Collections.emptyList() : allowedPlayers));
        this.rangeVisible = rangeVisible;
    }

    @Override
    public String getLightSerializerType()
    {
        return MegatorchSerializer.SERIALIZER_KEY;
    }

    @Override
    public LightKind kind()
    {
        return LightKind.MEGA_TORCH;
    }

    @Override
    public BlockPosView position()
    {
        return MinecraftAdapterViews.blockPos(pos);
    }

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     *
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    public boolean cleanupCheck(World level)
    {
        //? if >=1.21.11 {
        /*return level.isInBuildLimit(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockMegaTorch.get();
        *///?} else {
        return level.canSetBlock(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockMegaTorch.get();
        //?}
    }

    @Override
    public String displayName()
    {
        return LightDefinition.MEGA_TORCH.displayName();
    }

    @Override
    public boolean blocksNaturalSpawnsOnly()
    {
        return diamondBase;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public boolean hasDiamondBase()
    {
        return diamondBase;
    }

    @Override
    public LightSettings settings()
    {
        return settings;
    }

    @Override
    public MegatorchBlockingLight withSettings(LightSettings settings)
    {
        return new MegatorchBlockingLight(pos, diamondBase, settings, ownerUuid, allowedPlayers, rangeVisible);
    }

    @Override
    public Optional<UUID> ownerUuid()
    {
        return ownerUuid;
    }

    @Override
    public MegatorchBlockingLight withOwner(Optional<UUID> ownerUuid)
    {
        return new MegatorchBlockingLight(pos, diamondBase, settings, ownerUuid, allowedPlayers, rangeVisible);
    }

    @Override
    public List<LightAccessEntry> allowedPlayers()
    {
        return allowedPlayers;
    }

    @Override
    public MegatorchBlockingLight withAllowedPlayers(List<LightAccessEntry> allowedPlayers)
    {
        return new MegatorchBlockingLight(pos, diamondBase, settings, ownerUuid, allowedPlayers, rangeVisible);
    }

    @Override
    public boolean rangeVisible()
    {
        return rangeVisible;
    }

    @Override
    public MegatorchBlockingLight withRangeVisible(boolean rangeVisible)
    {
        return new MegatorchBlockingLight(pos, diamondBase, settings, ownerUuid, allowedPlayers, rangeVisible);
    }
}
