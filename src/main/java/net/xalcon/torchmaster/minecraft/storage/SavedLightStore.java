package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.21.11
//import com.mojang.serialization.Codec;
//? if >=1.21.11
//import net.minecraft.datafixer.DataFixTypes;
//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
*///?}
//? if >=1.20.6
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
//? if >=1.21.11
//import net.minecraft.world.PersistentStateType;
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

public class SavedLightStore extends PersistentState implements LightStoreBridge
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
        markDirty();
    }

    @Override
    public void unregisterLight(String lightKey)
    {
        lights.unregister(lightKey);
        markDirty();
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

    //? if >=1.21.11 {
    /*private static final Codec<SavedLightStore> CODEC = NbtCompound.CODEC.xmap(
            SavedLightStore::load,
            manager -> {
                NbtCompound tag = new NbtCompound();
                manager.saveInto(tag);
                return tag;
            });

    public static PersistentStateType<SavedLightStore> type(String id)
    {
        return new PersistentStateType<>(id, SavedLightStore::new, CODEC, DataFixTypes.SAVED_DATA_MAP_DATA);
    }
*///?} elif >=1.20.6 {
    @Override
    public NbtCompound writeNbt(NbtCompound compoundTag, RegistryWrapper.WrapperLookup provider)
    {
        NbtCompound tag = new NbtCompound();
        saveInto(tag);
        return tag;
    }

    private static SavedLightStore load(NbtCompound tag, RegistryWrapper.WrapperLookup provider)
    {
        return load(tag);
    }

    public static final PersistentState.Type<SavedLightStore> Factory = new Type<>(SavedLightStore::new, SavedLightStore::load, null);
//?} elif >=1.16.5 {
    /*@Override
    public NbtCompound writeNbt(NbtCompound compoundTag)
    {
        saveInto(compoundTag);
        return compoundTag;
    }
    *///?} else {
    /*@Override
    public CompoundTag toTag(CompoundTag compoundTag)
    {
        saveInto(compoundTag);
        return compoundTag;
    }
    *///?}

    //? if >=1.16.5 && <1.17 {
    /*@Override
    public void fromTag(NbtCompound tag)
    {
        loadInto(this, tag);
    }
    *///?} else if <1.16.5 {
    /*@Override
    public void fromTag(CompoundTag tag)
    {
        loadInto(this, tag);
    }
    *///?}

    //? if >=1.16.5 {
    public static SavedLightStore load(NbtCompound tag)
    {
        SavedLightStore mgr = new SavedLightStore();
        loadInto(mgr, tag);
        return mgr;
    }
//?}

    //? if >=1.16.5
    private void saveInto(NbtCompound compoundTag)
    //? if <1.16.5
    //private void saveInto(CompoundTag compoundTag)
    {
        //? if >=1.16.5
        NbtList list = new NbtList();
        //? if <1.16.5
        //ListTag list = new ListTag();
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
                //? if >=1.16.5
                NbtCompound tag = serializer.get().serializeLight(light);
                //? if <1.16.5
                //CompoundTag tag = serializer.get().serializeLight(light);
                tag.putString("_type", serializerType);
                tag.putString("_key", lightKey);
                list.add(tag);
            } else {
                TorchmasterRuntime.LOG.error("Unable to save light {}, data is lost", light.position());
            }
        }
        compoundTag.put("lights", list);
    }

    //? if >=1.16.5
    private static void loadInto(SavedLightStore mgr, NbtCompound tag)
    //? if <1.16.5
    //private static void loadInto(SavedLightStore mgr, CompoundTag tag)
    {
        //? if >=1.21.11 {
        /*NbtList lightsList = tag.getListOrEmpty("lights");
*///?} elif >=1.16.5 {
        NbtList lightsList = tag.getList("lights", 10);
	//?} else {
        /*ListTag lightsList = tag.getList("lights", 10);
        *///?}
        mgr.lights.clear();
        for(int i = 0; i < lightsList.size(); i++)
        {
            //? if >=1.21.11 {
            /*NbtCompound lightNbt = lightsList.getCompoundOrEmpty(i);
            String lightKey = lightNbt.getString("_key", "");
            String serializerType = lightNbt.getString("_type", "");
            *///?} else {
            //? if >=1.16.5
            NbtCompound lightNbt = lightsList.getCompound(i);
            //? if <1.16.5
            //CompoundTag lightNbt = lightsList.getCompound(i);
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
