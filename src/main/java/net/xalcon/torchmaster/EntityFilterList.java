package net.xalcon.torchmaster;

//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class EntityFilterList
{
	//? if >=1.21.11 {
	/*private final Identifier filterListId;
	private final Set<Identifier> list = new HashSet<>();
	*///?} else {
	private final ResourceLocation filterListId;
	private final Set<ResourceLocation> list = new HashSet<>();
	//?}

	//? if >=1.21.11 {
	/*public EntityFilterList(Identifier identifier)
	*///?} else {
    public EntityFilterList(ResourceLocation identifier)
	//?}
    {
        this.filterListId = identifier;
    }

	//? if >=1.21.11 {
	/*public boolean containsEntity(Identifier entityName)
	*///?} else {
    public boolean containsEntity(ResourceLocation entityName)
	//?}
	{
		return this.list.contains(entityName);
	}

	//? if >=1.21.11 {
	/*public void registerEntity(Identifier entityName)
	*///?} else {
	public void registerEntity(ResourceLocation entityName)
	//?}
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
		for(String override: overrides)
		{
			// minimum len is prefix + valid resource location, i.e. +a:b
			if(override.length() < 4)
			{
				Torchmaster.LOG.warn("[{}] Invalid filter definition '{}'", filterListId, override);
				continue;
			}

			char prefix = override.charAt(0);
			//? if >=1.21.11 {
			/*Identifier rl = Identifier.parse(override.substring(1));
			*///?} elif >=1.21 {
			ResourceLocation rl = ResourceLocation.parse(override.substring(1));
	//?} else {
			/*ResourceLocation rl = new ResourceLocation(override.substring(1));
			*///?}

			switch (prefix)
			{
				case '+':
					if(!this.containsEntity(rl))
					{
							//? if >=1.19.3 {
							if(!BuiltInRegistries.ENTITY_TYPE.containsKey(rl))
//?} else {
							/*if(!Registry.ENTITY_TYPE.containsKey(rl))
							*///?}
						{
							Torchmaster.LOG.warn("[{}] The entity '{}' does not exist, skipping", filterListId, rl);
							continue;
						}
						this.registerEntity(rl);
						Torchmaster.LOG.info("[{}] Added '{}' to the block list", filterListId, rl);
					}
					break;
				case '-':
					if(this.list.removeIf(rrl -> rrl.equals(rl)))
					{
						Torchmaster.LOG.info("[{}] Removed '{}' from the block list", filterListId, rl);
					}
					break;
				default:
					Torchmaster.LOG.warn("[{}] Invalid block list prefix: '{}', only + and - are valid prefixes", filterListId, prefix);
					break;
			}
		}
	}

	//? if >=1.21.11 {
	/*public Identifier[] getEntities()
	{
		return this.list.toArray(new Identifier[0]);
	}
	*///?} else {
	public ResourceLocation[] getEntities()
	{
		return this.list.toArray(new ResourceLocation[0]);
	}
	//?}

	public void clear()
	{
		list.clear();
	}
}
