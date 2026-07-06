package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
*///?}
import net.xalcon.torchmaster.TorchmasterRuntime;
import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.domain.LightRegistry;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;

import java.util.Map;
import java.util.Optional;

final class SavedLightStoreSerializer {
    private SavedLightStoreSerializer() {
    }

    //? if >=1.16.5
    static void saveInto(NbtCompound compoundTag, LightRegistry lights)
    //? if <1.16.5
    //static void saveInto(CompoundTag compoundTag, LightRegistry lights)
    {
        //? if >=1.16.5
        NbtList list = new NbtList();
        //? if <1.16.5
        //ListTag list = new ListTag();
        for (Map.Entry<String, LightEntry> pair : lights.keyedEntries()) {
            saveLight(list, pair.getKey(), pair.getValue());
        }
        compoundTag.put("lights", list);
    }

    //? if >=1.16.5
    private static void saveLight(NbtList list, String lightKey, LightEntry entry)
    //? if <1.16.5
    //private static void saveLight(ListTag list, String lightKey, LightEntry entry)
    {
        if (!(entry instanceof MinecraftBlockingLight)) {
            TorchmasterRuntime.LOG.error("Unable to save light {}, data is lost", entry.position());
            return;
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

    //? if >=1.16.5
    static void loadInto(LightRegistry lights, NbtCompound tag)
    //? if <1.16.5
    //static void loadInto(LightRegistry lights, CompoundTag tag)
    {
        //? if >=1.21.11 {
        /*NbtList lightsList = tag.getListOrEmpty("lights");
        *///?} elif >=1.16.5 {
        NbtList lightsList = tag.getList("lights", 10);
        //?} else {
        /*ListTag lightsList = tag.getList("lights", 10);
        *///?}
        lights.clear();
        for (int i = 0; i < lightsList.size(); i++) {
            loadLight(lights, lightsList, i);
        }
    }

    //? if >=1.16.5
    private static void loadLight(LightRegistry lights, NbtList lightsList, int index)
    //? if <1.16.5
    //private static void loadLight(LightRegistry lights, ListTag lightsList, int index)
    {
        //? if >=1.21.11 {
        /*NbtCompound lightNbt = lightsList.getCompoundOrEmpty(index);
        String lightKey = lightNbt.getString("_key", "");
        String serializerType = lightNbt.getString("_type", "");
        *///?} else {
        //? if >=1.16.5
        NbtCompound lightNbt = lightsList.getCompound(index);
        //? if <1.16.5
        //CompoundTag lightNbt = lightsList.getCompound(index);
        String lightKey = lightNbt.getString("_key");
        String serializerType = lightNbt.getString("_type");
        //?}
        Optional<LightNbtSerializer> serializer = LightSerializerRegistry.getLightSerializer(serializerType);
        if (serializer.isPresent()) {
            Optional<MinecraftBlockingLight> light = serializer.get().deserializeLight(lightNbt);
            if (light.isPresent()) {
                lights.register(lightKey, light.get());
            } else {
                TorchmasterRuntime.LOG.error("Unable to load light data from nbt for {} - {}, deserialization failed, data is lost", lightKey, serializerType);
            }
        } else {
            TorchmasterRuntime.LOG.error("Unable to load light data from nbt for {} - {}. Serializer not found, data is lost", lightKey, serializerType);
        }
    }
}
