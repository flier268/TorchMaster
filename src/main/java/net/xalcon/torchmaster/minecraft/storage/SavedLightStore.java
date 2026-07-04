package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.21.5 {
/*import com.mojang.serialization.Codec;
*///?}
//? if >=1.21
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
//? if >=1.21.5 {
/*import net.minecraft.world.level.saveddata.SavedDataType;
*///?}
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;
import net.xalcon.torchmaster.port.WorldView;
import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.domain.LightRegistry;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftConfigView;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.platform.Services;

import java.util.Map;
import java.util.Optional;

public class SavedLightStore extends SavedData implements LightStoreBridge
{
    private final LightRegistry lights = new LightRegistry();

    public SavedLightStore()
    {
        //? if <1.17 {
        /*super("torchmaster_lights");
        *///?}
    }

    @Override
    public boolean shouldBlockEntityType(EntityTypeKey entityType, Vec3View pos, SpawnReason spawnType)
    {
        return lights.blocksEntity(
                MinecraftAdapterViews.entityFilter(TorchmasterRuntime.MegaTorchFilterRegistry),
                MinecraftAdapterViews.entityFilter(TorchmasterRuntime.DreadLampFilterRegistry),
                new MinecraftConfigView(Services.PLATFORM.getConfig()),
                entityType,
                pos);
    }

    @Override
    public boolean shouldBlockVillageZombieRaid(Vec3View pos)
    {
        return lights.blocksVillageSiege(new MinecraftConfigView(Services.PLATFORM.getConfig()), pos);
    }

    @Override
    public void registerLight(String lightKey, MinecraftBlockingLight light)
    {
        lights.register(lightKey, light);
        setDirty();
    }

    @Override
    public void unregisterLight(String lightKey)
    {
        lights.unregister(lightKey);
        setDirty();
    }

    @Override
    public Optional<MinecraftBlockingLight> getLight(String lightKey)
    {
        return lights.get(lightKey)
                .filter(MinecraftBlockingLight.class::isInstance)
                .map(MinecraftBlockingLight.class::cast);
    }

    @Override
    public void onGlobalTick(WorldView world)
    {
        // TODO: Rate limit cleanup once cleanup has a Minecraft-free world port.
    }

    @Override
    public LightInfo[] getEntries()
    {
        return lights.entries().stream()
                .map(light -> new LightInfo(light.displayName(), light.position()))
                .toArray(LightInfo[]::new);
    }

    //? if >=1.21.5 {
    /*private static final Codec<SavedLightStore> CODEC = CompoundTag.CODEC.xmap(
            SavedLightStore::load,
            manager -> {
                CompoundTag tag = new CompoundTag();
                manager.saveInto(tag);
                return tag;
            });

    public static SavedDataType<SavedLightStore> type(String id)
    {
        return new SavedDataType<>(id, SavedLightStore::new, CODEC, null);
    }
    *///?} elif >=1.21 {
    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider)
    {
        CompoundTag tag = new CompoundTag();
        saveInto(tag);
        return tag;
    }

    private static SavedLightStore load(CompoundTag tag, HolderLookup.Provider provider)
    {
        return load(tag);
    }

    public static final SavedData.Factory<SavedLightStore> Factory = new Factory<>(SavedLightStore::new, SavedLightStore::load, null);
//?} else {
    /*@Override
    public CompoundTag save(CompoundTag compoundTag)
    {
        saveInto(compoundTag);
        return compoundTag;
    }
    *///?}

    //? if <1.17 {
    /*@Override
    public void load(CompoundTag tag)
    {
        loadInto(this, tag);
    }
    *///?}

    //? if >=1.17 {
    public static SavedLightStore load(CompoundTag tag)
    {
        SavedLightStore mgr = new SavedLightStore();
        loadInto(mgr, tag);
        return mgr;
    }
//?}

    private void saveInto(CompoundTag compoundTag)
    {
        ListTag list = new ListTag();
        for (Map.Entry<String, LightEntry> pair : lights.keyedEntries())
        {
            String lightKey = pair.getKey();
            LightEntry entry = pair.getValue();
            if (!(entry instanceof MinecraftBlockingLight)) {
                TorchmasterRuntime.LOG.error("Unable to save light {}, data is lost", entry.position());
                continue;
            }
            MinecraftBlockingLight light = (MinecraftBlockingLight)entry;
            String serializerType = light.getLightSerializerType();
            Optional<LightNbtSerializer> serializer = LightSerializerRegistry.getLightSerializer(serializerType);
            if (serializer.isPresent()) {
                CompoundTag tag = serializer.get().serializeLight(light);
                tag.putString("_type", serializerType);
                tag.putString("_key", lightKey);
                list.add(tag);
            } else {
                TorchmasterRuntime.LOG.error("Unable to save light {}, data is lost", light.position());
            }
        }
        compoundTag.put("lights", list);
    }

    private static void loadInto(SavedLightStore mgr, CompoundTag tag)
    {
        //? if >=1.21.5 {
        /*ListTag lightsList = tag.getListOrEmpty("lights");
*///?} elif >=1.17 {
        ListTag lightsList = tag.getList("lights", Tag.TAG_COMPOUND);
	//?} else {
        /*ListTag lightsList = tag.getList("lights", 10);
        *///?}
        mgr.lights.clear();
        for(int i = 0; i < lightsList.size(); i++)
        {
            //? if >=1.21.5 {
            /*CompoundTag lightNbt = lightsList.getCompoundOrEmpty(i);
            String lightKey = lightNbt.getStringOr("_key", "");
            String serializerType = lightNbt.getStringOr("_type", "");
            *///?} else {
            CompoundTag lightNbt = lightsList.getCompound(i);
            String lightKey = lightNbt.getString("_key");
            String serializerType = lightNbt.getString("_type");
            //?}
            Optional<LightNbtSerializer> serializer = LightSerializerRegistry.getLightSerializer(serializerType);
            if (serializer.isPresent()) {
                Optional<MinecraftBlockingLight> light = serializer.get().deserializeLight(lightNbt);
                if (light.isPresent()) {
                    mgr.lights.register(lightKey, light.get());
                } else {
                    TorchmasterRuntime.LOG.error("Unable to load light data from nbt for {} - {}, deserialization failed, data is lost", lightKey, serializerType);
                }
            } else {
                TorchmasterRuntime.LOG.error("Unable to load light data from nbt for {} - {}. Serializer not found, data is lost", lightKey, serializerType);
            }
        }
    }
}
