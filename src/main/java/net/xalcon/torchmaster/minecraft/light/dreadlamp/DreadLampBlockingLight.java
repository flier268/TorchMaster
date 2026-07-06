package net.xalcon.torchmaster.minecraft.light.dreadlamp;

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

public class DreadLampBlockingLight implements MinecraftBlockingLight
{
    private final BlockPos pos;
    private final LightSettings settings;
    private final Optional<UUID> ownerUuid;
    private final List<LightAccessEntry> allowedPlayers;
    private final boolean rangeVisible;

    public DreadLampBlockingLight(BlockPos pos)
    {
        this(pos, LightSettings.unconfigured());
    }

    public DreadLampBlockingLight(BlockPos pos, LightSettings settings)
    {
        this(pos, settings, Optional.empty(), Collections.emptyList());
    }

    public DreadLampBlockingLight(BlockPos pos, LightSettings settings, Optional<UUID> ownerUuid)
    {
        this(pos, settings, ownerUuid, Collections.emptyList());
    }

    public DreadLampBlockingLight(BlockPos pos, LightSettings settings, Optional<UUID> ownerUuid, List<LightAccessEntry> allowedPlayers)
    {
        this(pos, settings, ownerUuid, allowedPlayers, false);
    }

    public DreadLampBlockingLight(BlockPos pos, LightSettings settings, Optional<UUID> ownerUuid, List<LightAccessEntry> allowedPlayers,
            boolean rangeVisible)
    {
        this.pos = pos;
        this.settings = settings;
        this.ownerUuid = ownerUuid == null ? Optional.empty() : ownerUuid;
        this.allowedPlayers = Collections.unmodifiableList(new ArrayList<>(allowedPlayers == null ? Collections.emptyList() : allowedPlayers));
        this.rangeVisible = rangeVisible;
    }

    @Override
    public String getLightSerializerType()
    {
        return DreadLampSerializer.SERIALIZER_KEY;
    }

    @Override
    public LightKind kind()
    {
        return LightKind.DREAD_LAMP;
    }

    @Override
    public BlockPosView position()
    {
        return MinecraftAdapterViews.blockPos(pos);
    }

    public boolean cleanupCheck(World level)
    {
        //? if >=1.21.11 {
        /*return level.isInBuildLimit(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockDreadLamp.get();
        *///?} else {
        return level.canSetBlock(this.pos) && level.getBlockState(pos).getBlock() != TorchmasterContent.blockDreadLamp.get();
        //?}
    }

    @Override
    public String displayName()
    {
        return LightDefinition.DREAD_LAMP.displayName();
    }

    public BlockPos getPos()
    {
        return pos;
    }

    @Override
    public LightSettings settings()
    {
        return settings;
    }

    @Override
    public DreadLampBlockingLight withSettings(LightSettings settings)
    {
        return new DreadLampBlockingLight(pos, settings, ownerUuid, allowedPlayers, rangeVisible);
    }

    @Override
    public Optional<UUID> ownerUuid()
    {
        return ownerUuid;
    }

    @Override
    public DreadLampBlockingLight withOwner(Optional<UUID> ownerUuid)
    {
        return new DreadLampBlockingLight(pos, settings, ownerUuid, allowedPlayers, rangeVisible);
    }

    @Override
    public List<LightAccessEntry> allowedPlayers()
    {
        return allowedPlayers;
    }

    @Override
    public DreadLampBlockingLight withAllowedPlayers(List<LightAccessEntry> allowedPlayers)
    {
        return new DreadLampBlockingLight(pos, settings, ownerUuid, allowedPlayers, rangeVisible);
    }

    @Override
    public boolean rangeVisible()
    {
        return rangeVisible;
    }

    @Override
    public DreadLampBlockingLight withRangeVisible(boolean rangeVisible)
    {
        return new DreadLampBlockingLight(pos, settings, ownerUuid, allowedPlayers, rangeVisible);
    }
}
