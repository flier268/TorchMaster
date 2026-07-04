package net.xalcon.torchmaster.minecraft.light.dreadlamp;

import net.minecraft.nbt.CompoundTag;
//? if <1.21.5
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.BlockPos;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.storage.LightNbtSerializer;

import java.util.Optional;

public class DreadLampSerializer implements LightNbtSerializer
{
    public static final String SERIALIZER_KEY = "dreadlamp";
    public static final DreadLampSerializer INSTANCE = new DreadLampSerializer();

    private DreadLampSerializer() { }

    @Override
    public CompoundTag serializeLight(MinecraftBlockingLight light)
    {
        if(light == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(light instanceof DreadLampBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + light.getClass().getCanonicalName() + "', expected '"+ DreadLampBlockingLight.class.getCanonicalName() +"'");
        DreadLampBlockingLight dreadLampLight = (DreadLampBlockingLight)light;

        CompoundTag nbt = new CompoundTag();
        //? if >=1.21.5 {
        /*nbt.store("pos", BlockPos.CODEC, dreadLampLight.getPos());
        *///?} else {
        nbt.put("pos", NbtUtils.writeBlockPos(dreadLampLight.getPos()));
        //?}

        return nbt;
    }

    @Override
    public Optional<MinecraftBlockingLight> deserializeLight(CompoundTag nbt)
    {
        //? if >=1.21.5 {
        /*Optional<BlockPos> pos = nbt.read("pos", BlockPos.CODEC);
*///?} elif >=1.21 {
        Optional<BlockPos> pos = NbtUtils.readBlockPos(nbt, "pos");
	//?} else {
        /*Optional<BlockPos> pos = Optional.of(NbtUtils.readBlockPos(nbt.getCompound("pos")));
        *///?}
        return pos.map(DreadLampBlockingLight::new);
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
