package net.xalcon.torchmaster.minecraft.storage;

import net.xalcon.torchmaster.minecraft.light.dreadlamp.DreadLampSerializer;
import net.xalcon.torchmaster.minecraft.light.megatorch.MegatorchSerializer;

import java.util.HashMap;
import java.util.Optional;

public class LightSerializerRegistry
{
    private static HashMap<String, LightNbtSerializer> registry = new HashMap<>();

    static
    {
        registerLightSerializer(MegatorchSerializer.INSTANCE);
        registerLightSerializer(DreadLampSerializer.INSTANCE);
    }

    public static void registerLightSerializer(LightNbtSerializer serializer)
    {
        String lightSerializerKey = serializer.getSerializerKey();

        if(lightSerializerKey == null)
            throw new RuntimeException("SerializerKey is null! " + serializer.getClass().getCanonicalName());

        if(registry.containsKey(lightSerializerKey))
            throw new RuntimeException("lightSerializer '" + lightSerializerKey + "' already exists");

        registry.put(lightSerializerKey, serializer);
    }

    public static Optional<LightNbtSerializer> getLightSerializer(String lightSerializerKey)
    {
        LightNbtSerializer serializer = registry.get(lightSerializerKey);
        if(serializer == null) return Optional.empty();
        return Optional.of(registry.get(lightSerializerKey));
    }
}
