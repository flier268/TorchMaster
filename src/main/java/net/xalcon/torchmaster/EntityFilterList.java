package net.xalcon.torchmaster;

import net.xalcon.torchmaster.domain.EntityFilterOverride;
import net.xalcon.torchmaster.domain.EntityFilterOverrideRules;
import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftEntityIds;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class EntityFilterList
{
	private final String filterListId;
	private final Set<EntityTypeKey> list = new HashSet<>();

    public EntityFilterList(String identifier)
    {
        this.filterListId = identifier;
    }

    public boolean containsEntity(EntityTypeKey entityName)
	{
		return this.list.contains(entityName);
	}

	public void registerEntity(EntityTypeKey entityName)
	{
		this.list.add(entityName);
	}

	public static boolean IsValidFilterString(Object object)
	{
		return EntityFilterOverrideRules.isValidFilterString(object);
	}

	public void applyListOverrides(List<String> overrides)
	{
		applyListOverrides(overrides, MinecraftEntityIds::entityExists);
	}

	public void applyListOverrides(List<String> overrides, Predicate<EntityTypeKey> entityExists)
	{
		for(String override: overrides)
		{
			EntityFilterOverride parsed = EntityFilterOverrideRules.parse(override, entityExists);
			switch (parsed.action()) {
				case ADD:
					if(!this.containsEntity(parsed.entityType()))
					{
						this.registerEntity(parsed.entityType());
						TorchmasterRuntime.LOG.info("[{}] Added '{}' to the block list", filterListId, parsed.entityType());
					}
					break;
				case REMOVE:
					if(this.list.removeIf(registeredEntity -> registeredEntity.equals(parsed.entityType())))
					{
						TorchmasterRuntime.LOG.info("[{}] Removed '{}' from the block list", filterListId, parsed.entityType());
					}
					break;
				case UNKNOWN_ENTITY:
					TorchmasterRuntime.LOG.warn("[{}] The entity '{}' does not exist, skipping", filterListId, parsed.entityType());
					break;
				case INVALID_ENTITY_ID:
					TorchmasterRuntime.LOG.warn("[{}] Invalid entity id '{}'", filterListId, parsed.entityId());
					break;
				case INVALID_FORMAT:
				default:
				TorchmasterRuntime.LOG.warn("[{}] Invalid filter definition '{}'", filterListId, override);
					break;
			}
		}
	}

	public Set<EntityTypeKey> getEntities()
	{
		return Collections.unmodifiableSet(list);
	}

	public void clear()
	{
		list.clear();
	}
}
