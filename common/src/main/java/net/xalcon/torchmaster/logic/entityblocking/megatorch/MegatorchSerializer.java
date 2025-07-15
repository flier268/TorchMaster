package net.xalcon.torchmaster.logic.entityblocking.megatorch;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.logic.entityblocking.ILightSerializer;

import java.util.Arrays;
import java.util.Optional;

public class MegatorchSerializer implements ILightSerializer
{
    public static final String SERIALIZER_KEY = "megatorch";

    public static final MegatorchSerializer INSTANCE = new MegatorchSerializer();

    private MegatorchSerializer() { }

    @Override
    public CompoundTag serializeLight(IEntityBlockingLight light)
    {
        if(light == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(light instanceof MegatorchEntityBlockingLight megatorchLight))
            throw new IllegalArgumentException("Unable to serialize '" + light.getClass().getCanonicalName() + "', expected '"+MegatorchEntityBlockingLight.class.getCanonicalName()+"'");

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
            return Optional.of(new MegatorchEntityBlockingLight(blockPos));
        }
        return Optional.empty();
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
