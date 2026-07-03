package net.xalcon.torchmaster.compat;

//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.xalcon.torchmaster.EntityFilterList;
import net.xalcon.torchmaster.minecraft.MinecraftAdapterViews;

public class VanillaCompat
{
	public static void registerTorchEntities(EntityFilterList registry)
	{
		//? if >=1.19.3 {
		BuiltInRegistries.ENTITY_TYPE.stream()
//?} else {
		/*Registry.ENTITY_TYPE.stream()
		*///?}
			.filter(entityType -> entityType != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(entityType -> !entityType.getCategory().isFriendly())
			.forEach(entityType -> registry.registerEntity(MinecraftAdapterViews.entityTypeKey(EntityType.getKey(entityType))));

	}

	public static void registerDreadLampEntities(EntityFilterList registry)
	{
		//? if >=1.19.3 {
		BuiltInRegistries.ENTITY_TYPE.stream()
//?} else {
		/*Registry.ENTITY_TYPE.stream()
		*///?}
			.filter(entityType -> entityType != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(entityType -> {
				MobCategory cat = entityType.getCategory();
				return cat != MobCategory.MISC && cat.isFriendly();
			})
			.forEach(entityType -> registry.registerEntity(MinecraftAdapterViews.entityTypeKey(EntityType.getKey(entityType))));
	}
}
