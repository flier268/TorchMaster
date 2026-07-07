package net.xalcon.torchmaster.minecraft.storage;

//? if >=1.16.5 {
import net.minecraft.nbt.NbtCompound;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
//? if >=1.20.6
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
import net.xalcon.torchmaster.domain.LightEntry;
import net.xalcon.torchmaster.domain.LightStoreRuntime;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.SpawnReason;
import net.xalcon.torchmaster.port.Vec3View;

import java.util.Optional;

public class SavedLightStore extends PersistentState implements LightStoreBridge
{
    private final LightStoreRuntime runtime;

    public SavedLightStore()
    {
        this(new LightStoreRuntime(), "torchmaster_lights");
    }

    SavedLightStore(LightStoreRuntime runtime)
    {
        this(runtime, "torchmaster_lights");
    }

    SavedLightStore(String storageId)
    {
        this(new LightStoreRuntime(), storageId);
    }

    SavedLightStore(LightStoreRuntime runtime, String storageId)
    {
        //? if <1.17 {
        /*super(storageId);
        *///?}
        this.runtime = runtime;
    }

    @Override
    public boolean shouldBlockEntityType(EntityTypeKey entityType, Vec3View pos, SpawnReason spawnType)
    {
        return runtime.shouldBlockEntity(MinecraftLightStoreRuntimeContext.create(), entityType, pos, spawnType);
    }

    @Override
    public boolean shouldBlockVillageZombieRaid(Vec3View pos)
    {
        return runtime.shouldBlockVillageSiege(MinecraftLightStoreRuntimeContext.create(), pos);
    }

    @Override
    public boolean shouldBlockNaturalSpawnPosition(Vec3View pos)
    {
        return runtime.shouldBlockNaturalSpawnPosition(MinecraftLightStoreRuntimeContext.create(), pos);
    }

    @Override
    public boolean shouldBlockNaturalSpawnChunk(int chunkX, int chunkZ)
    {
        return runtime.shouldBlockNaturalSpawnChunk(MinecraftLightStoreRuntimeContext.create(), chunkX, chunkZ);
    }

    @Override
    public void registerLight(String lightKey, LightEntry light)
    {
        runtime.registerLight(lightKey, light);
        markDirty();
    }

    @Override
    public Optional<LightEntry> getLight(String lightKey)
    {
        return runtime.getLight(lightKey);
    }

    @Override
    public void unregisterLight(String lightKey)
    {
        runtime.unregisterLight(lightKey);
        markDirty();
    }

    @Override
    public LightInfo[] getEntries()
    {
        return runtime.entries();
    }

    @Override
    public LightEntry[] lightEntries()
    {
        return runtime.lightEntries();
    }

    LightStoreRuntime runtime()
    {
        return runtime;
    }

    //? if >=1.20.6 <1.21.11 {
    @Override
    public NbtCompound writeNbt(NbtCompound compoundTag, RegistryWrapper.WrapperLookup provider)
    {
        return SavedLightStoreStateBridge.write(this);
    }
//?} elif >=1.16.5 <1.20.6 {
    /*@Override
    public NbtCompound writeNbt(NbtCompound compoundTag)
    {
        return SavedLightStoreStateBridge.writeIntoExistingTag(this, compoundTag);
    }
    *///?} elif <1.16.5 {
	    /*@Override
	    public CompoundTag toTag(CompoundTag compoundTag)
	    {
	        return SavedLightStoreStateBridge.writeIntoExistingTag(this, compoundTag);
	    }
	    *///?}

    //? if >=1.16.5 && <1.17 {
    /*@Override
    public void fromTag(NbtCompound tag)
    {
        SavedLightStoreStateBridge.readIntoExistingStore(this, tag);
    }
    *///?} else if <1.16.5 {
	    /*@Override
	    public void fromTag(CompoundTag tag)
	    {
	        SavedLightStoreStateBridge.readIntoExistingStore(this, tag);
	    }
	    *///?}
}
