package net.xalcon.torchmaster.logic;

//? if >=1.21.5 {
/*import com.mojang.serialization.Codec;
*///?}
import net.minecraft.core.BlockPos;
//? if >=1.21
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
//? if >=1.21.2 {
/*import net.minecraft.world.entity.EntitySpawnReason;
*///?} else {
import net.minecraft.world.entity.MobSpawnType;
//?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
//? if >=1.21.5 {
/*import net.minecraft.world.level.saveddata.SavedDataType;
*///?}
import net.xalcon.torchmaster.Torchmaster;

import java.util.HashMap;
import java.util.Map;

public class EntityBlockingManager extends SavedData
{
    private final Map<String, EntityBlocker> entityBlockers = new HashMap<>();

    public EntityBlockingManager()
    {
        //? if <1.17 {
        /*super("torchmaster_entity_blockers");
        *///?}
    }

    public void registerBlocker(EntityBlocker blocker)
    {
        entityBlockers.putIfAbsent(blocker.getIdentifier(), blocker);
        setDirty();
    }

    public void unregisterBlocker(EntityBlocker blocker)
    {
        entityBlockers.remove(blocker.getIdentifier());
        setDirty();
    }

    //? if >=1.21.2 {
    /*public boolean shouldBlockEntitySpawn(Entity entity, Level level, EntitySpawnReason spawnType)
    *///?} else {
    public boolean shouldBlockEntitySpawn(Entity entity, Level level, MobSpawnType spawnType)
    //?}
    {
        for(EntityBlocker blocker: entityBlockers.values())
        {
            if(blocker.shouldBlockEntitySpawn(entity, level, spawnType))
            {
                return true;
            }
        }
        return false;
    }

    public boolean shouldBlockVillageSiege(Level level, BlockPos pos)
    {
        for(EntityBlocker blocker: entityBlockers.values())
        {
            if(blocker.shouldBlockVillageSiege(level, pos))
            {
                return true;
            }
        }
        return false;
    }

    public boolean shouldBlockVillageRaid(Level level, BlockPos pos)
    {
        for(EntityBlocker blocker: entityBlockers.values())
        {
            if(blocker.shouldBlockVillageRaid(level, pos))
            {
                return true;
            }
        }
        return false;
    }

    public CompoundTag save()
    {
        ListTag list = new ListTag();
        for (EntityBlocker blocker : entityBlockers.values())
        {
            CompoundTag tag = EntityBlockerSerializerRegistry.Serialize(blocker);
            if(tag == null)
            {
                Torchmaster.LOG.error("Unable to save entity blocker {}, data is lost", blocker);
                continue;
            }
            list.add(tag);
        }
        CompoundTag tag = new CompoundTag();
        tag.put("blockers", list);
        return tag;
    }

    public void loadFrom(CompoundTag tag)
    {
        entityBlockers.clear();

        //? if >=1.21.5 {
        /*ListTag list = tag.getListOrEmpty("blockers");
*///?} elif >=1.17 {
        ListTag list = tag.getList("blockers", Tag.TAG_COMPOUND);
	//?} else {
        /*ListTag list = tag.getList("blockers", 10);
        *///?}
        for(int i = 0; i < list.size(); i++)
        {
            EntityBlocker blocker = EntityBlockerSerializerRegistry.Deserialize(
                    //? if >=1.21.5 {
                    /*list.getCompoundOrEmpty(i)
                    *///?} else {
                    list.getCompound(i)
                    //?}
            );
            if(blocker == null)
            {
                Torchmaster.LOG.error("Unable to load entity blocker from nbt, data is lost");
                continue;
            }

            entityBlockers.put(blocker.getIdentifier(), blocker);
        }

        setDirty();
    }

    //? if >=1.21.5 {
    /*private static final Codec<EntityBlockingManager> CODEC = CompoundTag.CODEC.xmap(
            EntityBlockingManager::load,
            manager -> {
                CompoundTag tag = new CompoundTag();
                tag.put("registry", manager.save());
                return tag;
            });

    public static SavedDataType<EntityBlockingManager> type(String id)
    {
        return new SavedDataType<>(id, EntityBlockingManager::new, CODEC, null);
    }

    public static EntityBlockingManager load(CompoundTag tag)
    {
        CompoundTag registryTag = tag.getCompoundOrEmpty("registry");
        EntityBlockingManager mgr = new EntityBlockingManager();
        mgr.loadFrom(registryTag);
        return mgr;
    }
    *///?} elif >=1.21 {
    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider)
    {
        compoundTag.put("registry", save());
        return compoundTag;
    }

    public static EntityBlockingManager load(CompoundTag tag, HolderLookup.Provider provider)
    {
        CompoundTag registryTag = tag.getCompound("registry");
        EntityBlockingManager mgr = new EntityBlockingManager();
        mgr.loadFrom(registryTag);
        return mgr;
    }

    public static final SavedData.Factory<EntityBlockingManager> Factory = new Factory<>(EntityBlockingManager::new, EntityBlockingManager::load, null);
//?} elif >=1.17 {
    /*@Override
    public CompoundTag save(CompoundTag compoundTag)
    {
        compoundTag.put("registry", save());
        return compoundTag;
    }

    public static EntityBlockingManager load(CompoundTag tag)
    {
        CompoundTag registryTag = tag.getCompound("registry");
        EntityBlockingManager mgr = new EntityBlockingManager();
    mgr.loadFrom(registryTag);
        return mgr;
    }
    *///?} else {
    /*@Override
    public CompoundTag save(CompoundTag compoundTag)
    {
        compoundTag.put("registry", save());
        return compoundTag;
    }

    @Override
    public void load(CompoundTag tag)
    {
        loadFrom(tag.getCompound("registry"));
    }
    *///?}
}
