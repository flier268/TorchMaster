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
import net.xalcon.torchmaster.port.WorldView;

public class SavedLightStore extends PersistentState implements LightStoreBridge
{
    private final LightStoreRuntime runtime;

    public SavedLightStore()
    {
        this(new LightStoreRuntime());
    }

    SavedLightStore(LightStoreRuntime runtime)
    {
        //? if <1.17 {
        /*super("torchmaster_lights");
        *///?}
        this.runtime = runtime;
    }

    @Override
    public boolean shouldBlockEntityType(EntityTypeKey entityType, Vec3View pos, SpawnReason spawnType)
    {
        return runtime.shouldBlockEntity(MinecraftLightStoreRuntimeContext.create(), entityType, pos);
    }

    @Override
    public boolean shouldBlockVillageZombieRaid(Vec3View pos)
    {
        return runtime.shouldBlockVillageSiege(MinecraftLightStoreRuntimeContext.create(), pos);
    }

    @Override
    public void registerLight(String lightKey, LightEntry light)
    {
        runtime.registerLight(lightKey, light);
        markDirty();
    }

    @Override
    public void unregisterLight(String lightKey)
    {
        runtime.unregisterLight(lightKey);
        markDirty();
    }

    @Override
    public void onGlobalTick(WorldView world)
    {
        // TODO: Rate limit cleanup once cleanup has a Minecraft-free world port.
    }

    @Override
    public LightInfo[] getEntries()
    {
        return runtime.entries();
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
