package net.xalcon.torchmaster.minecraft.light.dreadlamp;

//? if >=1.16.5
import net.minecraft.nbt.NbtCompound;
//? if <1.16.5
//import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.storage.LightNbtSerializer;

import java.util.Optional;

public class DreadLampSerializer implements LightNbtSerializer
{
    public static final String SERIALIZER_KEY = "dreadlamp";
    public static final DreadLampSerializer INSTANCE = new DreadLampSerializer();

    private DreadLampSerializer() { }

    @Override
    //? if >=1.16.5
    public NbtCompound serializeLight(MinecraftBlockingLight light)
    //? if <1.16.5
    //public CompoundTag serializeLight(MinecraftBlockingLight light)
    {
        if(light == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(light instanceof DreadLampBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + light.getClass().getCanonicalName() + "', expected '"+ DreadLampBlockingLight.class.getCanonicalName() +"'");
        DreadLampBlockingLight dreadLampLight = (DreadLampBlockingLight)light;

        //? if >=1.16.5
        NbtCompound nbt = new NbtCompound();
        //? if <1.16.5
        //CompoundTag nbt = new CompoundTag();
        //? if >=1.21.11 {
        /*nbt.put("pos", BlockPos.CODEC, dreadLampLight.getPos());
*///?} else {
        nbt.put("pos", NbtHelper.fromBlockPos(dreadLampLight.getPos()));
        //?}

        return nbt;
    }

    @Override
    //? if >=1.16.5
    public Optional<MinecraftBlockingLight> deserializeLight(NbtCompound nbt)
    //? if <1.16.5
    //public Optional<MinecraftBlockingLight> deserializeLight(CompoundTag nbt)
    {
        //? if >=1.21.11 {
        /*Optional<BlockPos> pos = nbt.get("pos", BlockPos.CODEC);
        *///?} elif >=1.20.6 {
        Optional<BlockPos> pos = NbtHelper.toBlockPos(nbt, "pos");
	//?} else {
        /*Optional<BlockPos> pos = Optional.of(NbtHelper.toBlockPos(nbt.getCompound("pos")));
        *///?}
        return pos.map(DreadLampBlockingLight::new);
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
