package net.xalcon.torchmaster.logic.entityblocking;

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
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.adapter.EntityTypeKey;
import net.xalcon.torchmaster.adapter.LightInfo;
import net.xalcon.torchmaster.adapter.SpawnReason;
import net.xalcon.torchmaster.adapter.Vec3View;
import net.xalcon.torchmaster.adapter.WorldView;
import net.xalcon.torchmaster.core.LightEntry;
import net.xalcon.torchmaster.core.LightRegistry;
import net.xalcon.torchmaster.minecraft.MinecraftAdapterViews;
import net.xalcon.torchmaster.minecraft.MinecraftConfigView;
import net.xalcon.torchmaster.platform.Services;

import java.util.Map;
import java.util.Optional;

public class FilteredLightManager extends SavedData implements IBlockingLightManager
{
    private final LightRegistry lights = new LightRegistry();

    public FilteredLightManager()
    {
        //? if <1.17 {
        /*super("torchmaster_lights");
        *///?}
    }

    @Override
    public boolean shouldBlockEntityType(EntityTypeKey entityType, Vec3View pos, SpawnReason spawnType)
    {
        return lights.blocksEntity(
                MinecraftAdapterViews.entityFilter(Torchmaster.MegaTorchFilterRegistry),
                MinecraftAdapterViews.entityFilter(Torchmaster.DreadLampFilterRegistry),
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
    public void registerLight(String lightKey, IEntityBlockingLight light)
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
    public Optional<IEntityBlockingLight> getLight(String lightKey)
    {
        return lights.get(lightKey)
                .filter(IEntityBlockingLight.class::isInstance)
                .map(IEntityBlockingLight.class::cast);
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
    /*private static final Codec<FilteredLightManager> CODEC = CompoundTag.CODEC.xmap(
            FilteredLightManager::load,
            manager -> {
                CompoundTag tag = new CompoundTag();
                manager.saveInto(tag);
                return tag;
            });

    public static SavedDataType<FilteredLightManager> type(String id)
    {
        return new SavedDataType<>(id, FilteredLightManager::new, CODEC, null);
    }
    *///?} elif >=1.21 {
    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider)
    {
        CompoundTag tag = new CompoundTag();
        saveInto(tag);
        return tag;
    }

    private static FilteredLightManager load(CompoundTag tag, HolderLookup.Provider provider)
    {
        return load(tag);
    }

    public static final SavedData.Factory<FilteredLightManager> Factory = new Factory<>(FilteredLightManager::new, FilteredLightManager::load, null);
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
    public static FilteredLightManager load(CompoundTag tag)
    {
        FilteredLightManager mgr = new FilteredLightManager();
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
            if (!(entry instanceof IEntityBlockingLight)) {
                Torchmaster.LOG.error("Unable to save light {}, data is lost", entry.position());
                continue;
            }
            IEntityBlockingLight light = (IEntityBlockingLight)entry;
            String serializerType = light.getLightSerializerType();
            Optional<ILightSerializer> serializer = LightSerializerRegistry.getLightSerializer(serializerType);
            if (serializer.isPresent()) {
                CompoundTag tag = serializer.get().serializeLight(light);
                tag.putString("_type", serializerType);
                tag.putString("_key", lightKey);
                list.add(tag);
            } else {
                Torchmaster.LOG.error("Unable to save light {}, data is lost", light.position());
            }
        }
        compoundTag.put("lights", list);
    }

    private static void loadInto(FilteredLightManager mgr, CompoundTag tag)
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
            Optional<ILightSerializer> serializer = LightSerializerRegistry.getLightSerializer(serializerType);
            if (serializer.isPresent()) {
                Optional<IEntityBlockingLight> light = serializer.get().deserializeLight(lightNbt);
                if (light.isPresent()) {
                    mgr.lights.register(lightKey, light.get());
                } else {
                    Torchmaster.LOG.error("Unable to load light data from nbt for {} - {}, deserialization failed, data is lost", lightKey, serializerType);
                }
            } else {
                Torchmaster.LOG.error("Unable to load light data from nbt for {} - {}. Serializer not found, data is lost", lightKey, serializerType);
            }
        }
    }
}
