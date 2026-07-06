package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
//? if >=1.20.6
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;
import net.xalcon.torchmaster.port.WorldView;
import net.xalcon.torchmaster.domain.LightRegistry;
import net.xalcon.torchmaster.minecraft.light.MinecraftBlockingLight;

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
                LightStoreConfigView.megaTorchFilter(),
                LightStoreConfigView.dreadLampFilter(),
                LightStoreConfigView.config(),
                entityType,
                pos);
    }

    @Override
    public boolean shouldBlockVillageZombieRaid(Vec3View pos)
    {
        return lights.blocksVillageSiege(LightStoreConfigView.config(), pos);
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

    //? if >=1.20.6 <1.21.11 {
    @Override
    public NbtCompound writeNbt(NbtCompound compoundTag, RegistryWrapper.WrapperLookup provider)
    {
        return writeState(new NbtCompound());
    }
//?} elif >=1.16.5 <1.20.6 {
    /*@Override
    public NbtCompound writeNbt(NbtCompound compoundTag)
    {
        return writeState(compoundTag);
    }
    *///?} elif <1.16.5 {
	    /*@Override
	    public CompoundTag toTag(CompoundTag compoundTag)
	    {
	        return SavedLightStoreNbtBridge.save(lights, compoundTag);
	    }
	    *///?}

    //? if >=1.16.5 && <1.17 {
    /*@Override
    public void fromTag(NbtCompound tag)
    {
        readState(tag);
    }
    *///?} else if <1.16.5 {
	    /*@Override
	    public void fromTag(CompoundTag tag)
	    {
	        SavedLightStoreNbtBridge.load(this.lights, tag);
	    }
	    *///?}

    //? if >=1.16.5 {
    NbtCompound writeState(NbtCompound tag)
    {
        return SavedLightStoreNbtBridge.save(lights, tag);
    }

    void readState(NbtCompound tag)
    {
        SavedLightStoreNbtBridge.load(this.lights, tag);
    }
//?}
}
