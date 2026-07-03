package net.xalcon.torchmaster.logic.entityblocking;

//? if >=1.21.5 {
/*import com.mojang.serialization.Codec;
*///?}
//? if >=1.21
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
//? if >=1.21.2 {
/*import net.minecraft.world.entity.EntitySpawnReason;
*///?} else {
import net.minecraft.world.entity.MobSpawnType;
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
//? if >=1.21.5 {
/*import net.minecraft.world.level.saveddata.SavedDataType;
*///?}
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.Torchmaster;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FilteredLightManager extends SavedData implements IBlockingLightManager
{
    private final Map<String, IEntityBlockingLight> lights = new HashMap<>();

    public FilteredLightManager()
    {
        //? if <1.17 {
        /*super("torchmaster_lights");
        *///?}
    }

    @Override
    //? if >=1.21.2 {
    /*public boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, EntitySpawnReason spawnType)
    *///?} else {
    public boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, MobSpawnType spawnType)
    //?}
    {
        for(IEntityBlockingLight light: lights.values())
        {
            if(light.shouldBlockEntityType(entityType, level, pos, spawnType))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldBlockVillageZombieRaid(Vec3 pos)
    {
        for(IEntityBlockingLight light: lights.values())
        {
            if(light.shouldBlockVillageZombieRaid(pos))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerLight(String lightKey, IEntityBlockingLight light)
    {
        lights.put(lightKey, light);
        setDirty();
    }

    @Override
    public void unregisterLight(String lightKey)
    {
        lights.remove(lightKey);
        setDirty();
    }

    @Override
    public Optional<IEntityBlockingLight> getLight(String lightKey)
    {
        IEntityBlockingLight light = lights.get(lightKey);
        if(light == null) return Optional.empty();
        return Optional.of(light);
    }

    @Override
    public void onGlobalTick(Level level)
    {
        // TODO: Rate limit this
        for(IEntityBlockingLight light: lights.values())
        {
            light.cleanupCheck(level);
        }
    }

    @Override
    public TorchInfo[] getEntries()
    {
        // TODO: implement for command?
        return new TorchInfo[0];
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
        for (Map.Entry<String, IEntityBlockingLight> pair : lights.entrySet())
        {
            String lightKey = pair.getKey();
            IEntityBlockingLight light = pair.getValue();
            String serializerType = light.getLightSerializerType();
            Optional<ILightSerializer> serializer = LightSerializerRegistry.getLightSerializer(serializerType);
            if (serializer.isPresent()) {
                CompoundTag tag = serializer.get().serializeLight(light);
                tag.putString("_type", serializerType);
                tag.putString("_key", lightKey);
                list.add(tag);
            } else {
                Torchmaster.LOG.error("Unable to save light {}, data is lost", light.getPos());
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
                    mgr.lights.put(lightKey, light.get());
                } else {
                    Torchmaster.LOG.error("Unable to load light data from nbt for {} - {}, deserialization failed, data is lost", lightKey, serializerType);
                }
            } else {
                Torchmaster.LOG.error("Unable to load light data from nbt for {} - {}. Serializer not found, data is lost", lightKey, serializerType);
            }
        }
    }
}
