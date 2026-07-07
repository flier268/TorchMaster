package net.xalcon.torchmaster.minecraft.light.megatorch;

//? if >=1.16.5
import net.minecraft.nbt.NbtCompound;
//? if >=1.16.5
import net.minecraft.nbt.NbtList;
//? if <1.16.5
//import net.minecraft.nbt.CompoundTag;
//? if <1.16.5
//import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.storage.LightNbtSerializer;
import net.xalcon.torchmaster.minecraft.storage.PersistedLightEntry;
import net.xalcon.torchmaster.port.LightAccessEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MegatorchSerializer implements LightNbtSerializer
{
    public static final String SERIALIZER_KEY = "megatorch";

    public static final MegatorchSerializer INSTANCE = new MegatorchSerializer();

    private MegatorchSerializer() { }

    @Override
    //? if >=1.16.5
    public NbtCompound serializeLight(PersistedLightEntry light)
    //? if <1.16.5
    //public CompoundTag serializeLight(PersistedLightEntry light)
    {
        if(light == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(light instanceof MegatorchBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + light.getClass().getCanonicalName() + "', expected '"+MegatorchBlockingLight.class.getCanonicalName()+"'");
        MegatorchBlockingLight megatorchLight = (MegatorchBlockingLight)light;

        //? if >=1.16.5
        NbtCompound nbt = new NbtCompound();
        //? if <1.16.5
        //CompoundTag nbt = new CompoundTag();
        //? if >=1.21.11 {
        /*nbt.put("pos", BlockPos.CODEC, megatorchLight.getPos());
	*///?} else {
        nbt.put("pos", NbtHelper.fromBlockPos(megatorchLight.getPos()));
        //?}
        nbt.putBoolean("diamondBase", megatorchLight.hasDiamondBase());
        writeSettings(nbt, megatorchLight.settings());
        writeOwner(nbt, megatorchLight.ownerUuid());
        writeAllowedPlayers(nbt, megatorchLight.allowedPlayers());
        writeRangeVisible(nbt, megatorchLight.rangeVisible());

        return nbt;
    }

    @Override
    //? if >=1.16.5
    public Optional<PersistedLightEntry> deserializeLight(NbtCompound nbt)
    //? if <1.16.5
    //public Optional<PersistedLightEntry> deserializeLight(CompoundTag nbt)
    {
        //? if >=1.21.11 {
        /*Optional<BlockPos> pos = nbt.get("pos", BlockPos.CODEC);
        *///?} elif >=1.20.6 {
        Optional<BlockPos> pos = NbtHelper.toBlockPos(nbt, "pos");
	//?} else {
        /*Optional<BlockPos> pos = Optional.of(NbtHelper.toBlockPos(nbt.getCompound("pos")));
        *///?}
        //? if >=1.21.11 {
        /*boolean diamondBase = nbt.getBoolean("diamondBase").orElse(false);
        *///?} else {
        boolean diamondBase = nbt.getBoolean("diamondBase");
        //?}
        LightSettings settings = readSettings(nbt);
        Optional<UUID> ownerUuid = readOwner(nbt);
        List<LightAccessEntry> allowedPlayers = readAllowedPlayers(nbt);
        boolean rangeVisible = readRangeVisible(nbt);
        return pos.map(blockPos -> new MegatorchBlockingLight(blockPos, diamondBase, settings, ownerUuid, allowedPlayers, rangeVisible));
    }

    //? if >=1.16.5
    private static void writeSettings(NbtCompound nbt, LightSettings settings)
    //? if <1.16.5
    //private static void writeSettings(CompoundTag nbt, LightSettings settings)
    {
        if (!settings.configured()) {
            return;
        }
        nbt.putBoolean("enabled", settings.enabled());
        nbt.putInt("rangeWest", settings.rangeWest());
        nbt.putInt("rangeEast", settings.rangeEast());
        nbt.putInt("rangeDown", settings.rangeDown());
        nbt.putInt("rangeUp", settings.rangeUp());
        nbt.putInt("rangeNorth", settings.rangeNorth());
        nbt.putInt("rangeSouth", settings.rangeSouth());
    }

    //? if >=1.16.5
    private static LightSettings readSettings(NbtCompound nbt)
    //? if <1.16.5
    //private static LightSettings readSettings(CompoundTag nbt)
    {
        if (!nbt.contains("enabled")) {
            return LightSettings.unconfigured();
        }
        if (!nbt.contains("rangeWest") || !nbt.contains("rangeEast") || !nbt.contains("rangeDown") || !nbt.contains("rangeUp")
                || !nbt.contains("rangeNorth") || !nbt.contains("rangeSouth")) {
            return LightSettings.unconfigured();
        }
        //? if >=1.21.11 {
        /*return LightSettings.configured(
                nbt.getBoolean("enabled").orElse(true),
                nbt.getInt("rangeWest").orElse(0),
                nbt.getInt("rangeEast").orElse(0),
                nbt.getInt("rangeDown").orElse(0),
                nbt.getInt("rangeUp").orElse(0),
                nbt.getInt("rangeNorth").orElse(0),
                nbt.getInt("rangeSouth").orElse(0));
        *///?} else {
        return LightSettings.configured(nbt.getBoolean("enabled"), nbt.getInt("rangeWest"), nbt.getInt("rangeEast"),
                nbt.getInt("rangeDown"), nbt.getInt("rangeUp"), nbt.getInt("rangeNorth"), nbt.getInt("rangeSouth"));
        //?}
    }

    //? if >=1.16.5
    private static void writeOwner(NbtCompound nbt, Optional<UUID> ownerUuid)
    //? if <1.16.5
    //private static void writeOwner(CompoundTag nbt, Optional<UUID> ownerUuid)
    {
        ownerUuid.ifPresent(uuid -> nbt.putString("ownerUuid", uuid.toString()));
    }

    //? if >=1.16.5
    private static Optional<UUID> readOwner(NbtCompound nbt)
    //? if <1.16.5
    //private static Optional<UUID> readOwner(CompoundTag nbt)
    {
        if (!nbt.contains("ownerUuid")) {
            return Optional.empty();
        }
        //? if >=1.21.11 {
        /*Optional<String> ownerUuid = nbt.getString("ownerUuid");
        if (!ownerUuid.isPresent()) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(ownerUuid.get()));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
        *///?} else {
        try {
            return Optional.of(UUID.fromString(nbt.getString("ownerUuid")));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
        //?}
    }

    //? if >=1.16.5
    private static void writeAllowedPlayers(NbtCompound nbt, List<LightAccessEntry> allowedPlayers)
    //? if <1.16.5
    //private static void writeAllowedPlayers(CompoundTag nbt, List<LightAccessEntry> allowedPlayers)
    {
        if (allowedPlayers == null || allowedPlayers.isEmpty()) {
            return;
        }
        //? if >=1.16.5
        NbtList list = new NbtList();
        //? if <1.16.5
        //ListTag list = new ListTag();
        for (LightAccessEntry player : allowedPlayers) {
            if (player.uuid() == null) {
                continue;
            }
            //? if >=1.16.5
            NbtCompound entry = new NbtCompound();
            //? if <1.16.5
            //CompoundTag entry = new CompoundTag();
            entry.putString("uuid", player.uuidString());
            entry.putString("name", player.name());
            list.add(entry);
        }
        nbt.put("allowedPlayers", list);
    }

    //? if >=1.16.5
    private static List<LightAccessEntry> readAllowedPlayers(NbtCompound nbt)
    //? if <1.16.5
    //private static List<LightAccessEntry> readAllowedPlayers(CompoundTag nbt)
    {
        List<LightAccessEntry> players = new ArrayList<>();
        if (!nbt.contains("allowedPlayers")) {
            return players;
        }
        //? if >=1.21.11 {
        /*NbtList list = nbt.getListOrEmpty("allowedPlayers");
        *///?} elif >=1.16.5 {
        NbtList list = nbt.getList("allowedPlayers", 10);
        //?} else {
        /*ListTag list = nbt.getList("allowedPlayers", 10);
        *///?}
        for (int i = 0; i < list.size(); i++) {
            //? if >=1.21.11 {
            /*NbtCompound entry = list.getCompoundOrEmpty(i);
            String uuid = entry.getString("uuid", "");
            String name = entry.getString("name", "");
            *///?} else {
            //? if >=1.16.5
            NbtCompound entry = list.getCompound(i);
            //? if <1.16.5
            //CompoundTag entry = list.getCompound(i);
            String uuid = entry.getString("uuid");
            String name = entry.getString("name");
            //?}
            try {
                players.add(new LightAccessEntry(UUID.fromString(uuid), name));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return players;
    }

    //? if >=1.16.5
    private static void writeRangeVisible(NbtCompound nbt, boolean rangeVisible)
    //? if <1.16.5
    //private static void writeRangeVisible(CompoundTag nbt, boolean rangeVisible)
    {
        if (rangeVisible) {
            nbt.putBoolean("rangeVisible", true);
        }
    }

    //? if >=1.16.5
    private static boolean readRangeVisible(NbtCompound nbt)
    //? if <1.16.5
    //private static boolean readRangeVisible(CompoundTag nbt)
    {
        if (!nbt.contains("rangeVisible")) {
            return false;
        }
        //? if >=1.21.11 {
        /*return nbt.getBoolean("rangeVisible").orElse(false);
        *///?} else {
        return nbt.getBoolean("rangeVisible");
        //?}
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
