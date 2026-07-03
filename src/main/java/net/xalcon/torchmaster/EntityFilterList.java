package net.xalcon.torchmaster;

import net.xalcon.torchmaster.adapter.EntityTypeKey;
import net.xalcon.torchmaster.minecraft.MinecraftEntityIds;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

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

	private static final Pattern FILTER_PATTERN = Pattern.compile("[+-][a-z0-9_-]+:[a-z0-9_-]+");

	public static boolean IsValidFilterString(Object object)
	{
		if(!(object instanceof String))
			return false;

		String filterString = (String)object;
		return FILTER_PATTERN.matcher(filterString).matches();
	}

	public void applyListOverrides(List<String> overrides)
	{
		applyListOverrides(overrides, MinecraftEntityIds::entityExists);
	}

	public void applyListOverrides(List<String> overrides, Predicate<EntityTypeKey> entityExists)
	{
		for(String override: overrides)
		{
			// minimum len is prefix + valid resource location, i.e. +a:b
			if(override.length() < 4)
			{
				Torchmaster.LOG.warn("[{}] Invalid filter definition '{}'", filterListId, override);
				continue;
			}

			char prefix = override.charAt(0);
			EntityTypeKey entityType;
			try {
				entityType = MinecraftEntityIds.parseEntityTypeKey(override.substring(1));
			} catch (RuntimeException ignored) {
				Torchmaster.LOG.warn("[{}] Invalid entity id '{}'", filterListId, override.substring(1));
				continue;
			}

			switch (prefix)
			{
				case '+':
					if(!this.containsEntity(entityType))
					{
						if(!entityExists.test(entityType))
						{
							Torchmaster.LOG.warn("[{}] The entity '{}' does not exist, skipping", filterListId, entityType);
							continue;
						}
						this.registerEntity(entityType);
						Torchmaster.LOG.info("[{}] Added '{}' to the block list", filterListId, entityType);
					}
					break;
				case '-':
					if(this.list.removeIf(registeredEntity -> registeredEntity.equals(entityType)))
					{
						Torchmaster.LOG.info("[{}] Removed '{}' from the block list", filterListId, entityType);
					}
					break;
				default:
					Torchmaster.LOG.warn("[{}] Invalid block list prefix: '{}', only + and - are valid prefixes", filterListId, prefix);
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
