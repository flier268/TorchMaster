package net.xalcon.torchmaster.minecraft.light.megatorch;

import net.minecraft.nbt.CompoundTag;
//? if <1.21.5
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.BlockPos;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;
import net.xalcon.torchmaster.minecraft.storage.LightNbtSerializer;

import java.util.Optional;

public class MegatorchSerializer implements LightNbtSerializer
{
    public static final String SERIALIZER_KEY = "megatorch";

    public static final MegatorchSerializer INSTANCE = new MegatorchSerializer();

    private MegatorchSerializer() { }

    @Override
    public CompoundTag serializeLight(MinecraftBlockingLight light)
    {
        if(light == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(light instanceof MegatorchBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + light.getClass().getCanonicalName() + "', expected '"+MegatorchBlockingLight.class.getCanonicalName()+"'");
        MegatorchBlockingLight megatorchLight = (MegatorchBlockingLight)light;

        CompoundTag nbt = new CompoundTag();
        //? if >=1.21.5 {
        /*nbt.store("pos", BlockPos.CODEC, megatorchLight.getPos());
        *///?} else {
        nbt.put("pos", NbtUtils.writeBlockPos(megatorchLight.getPos()));
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
        return pos.map(MegatorchBlockingLight::new);
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
