package net.xalcon.torchmaster.logic.entityblocking.dreadlamp;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.logic.entityblocking.ILightSerializer;
import net.xalcon.torchmaster.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;

import java.util.Optional;

public class DreadLampSerializer implements ILightSerializer
{
    public static final String SERIALIZER_KEY = "dreadlamp";
    public static final DreadLampSerializer INSTANCE = new DreadLampSerializer();

    private DreadLampSerializer() { }

    @Override
    public CompoundTag serializeLight(IEntityBlockingLight light)
    {
        if(light == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(light instanceof DreadLampEntityBlockingLight dreadLampEntityBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + light.getClass().getCanonicalName() + "', expected '"+DreadLampEntityBlockingLight.class.getCanonicalName()+"'");

        var nbt = new CompoundTag();

        var pos = light.getPos();
        nbt.putIntArray("pos", new int[] { pos.getX(), pos.getY(), pos.getZ() });

        return nbt;
    }

    @Override
    public Optional<IEntityBlockingLight> deserializeLight(CompoundTag nbt)
    {
        var posTag = nbt.getIntArray("pos");
        if(posTag.isPresent() && posTag.get().length == 3)
        {
            var posArray = posTag.get();
            var blockPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
            return Optional.of(new DreadLampEntityBlockingLight(blockPos));
        }
        return Optional.empty();
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
