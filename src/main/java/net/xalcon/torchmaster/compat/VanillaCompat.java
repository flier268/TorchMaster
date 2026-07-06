package net.xalcon.torchmaster.compat;

import net.minecraft.entity.EntityType;
//? if >=1.16.5
import net.minecraft.entity.SpawnGroup;
//? if <1.16.5
//import net.minecraft.entity.EntityCategory;
//? if >=1.19.3
import net.minecraft.registry.Registries;
//? if <1.19.3
//import net.minecraft.util.registry.Registry;
import net.xalcon.torchmaster.EntityFilterList;
import net.xalcon.torchmaster.minecraft.adapter.MinecraftAdapterViews;

public class VanillaCompat
{
	public static void registerTorchEntities(EntityFilterList registry)
	{
		//? if >=1.19.3 {
		Registries.ENTITY_TYPE.stream()
//?} else {
		/*Registry.ENTITY_TYPE.stream()
		*///?}
			.filter(entityType -> entityType != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(entityType ->
			//? if >=1.16.5
				!entityType.getSpawnGroup().isPeaceful()
			//? if <1.16.5
				//!entityType.getCategory().isPeaceful()
			)
			.forEach(entityType -> registry.registerEntity(MinecraftAdapterViews.entityTypeKey(EntityType.getId(entityType))));

	}

	public static void registerDreadLampEntities(EntityFilterList registry)
	{
		//? if >=1.19.3 {
		Registries.ENTITY_TYPE.stream()
//?} else {
		/*Registry.ENTITY_TYPE.stream()
		*///?}
			.filter(entityType -> entityType != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(entityType -> {
				//? if >=1.16.5 {
				SpawnGroup cat = entityType.getSpawnGroup();
				return cat != SpawnGroup.MISC && cat.isPeaceful();
				//?} else {
				/*EntityCategory cat = entityType.getCategory();
				return cat != EntityCategory.MISC && cat.isPeaceful();
				*///?}
			})
			.forEach(entityType -> registry.registerEntity(MinecraftAdapterViews.entityTypeKey(EntityType.getId(entityType))));
	}
}
