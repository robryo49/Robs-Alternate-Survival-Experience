package robryo49.rase.loot_table;

import net.fabricmc.fabric.api.loot.v3.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.AlternativeLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import robryo49.rase.item.ModItems;

import java.util.Set;

public class ModLootTableModifiers {
	
	public static void modifyMobMeatDrop(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries,
	                                      EntityType<?> entity, Item drop) {
		if (entity.getLootTableId().equals(key)) {
			LootPool.Builder poolBuilder = LootPool.builder()
					.rolls(ConstantLootNumberProvider.create(1))
					.with(ItemEntry.builder(drop)
							.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 3.0f)))
							.apply(FurnaceSmeltLootFunction.builder()
									.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS,
											EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true)))))
							.apply(EnchantedCountIncreaseLootFunction.builder(registries, UniformLootNumberProvider.create(0, 1))));
			
			tableBuilder.pool(poolBuilder);
		}
	}
	
	public static void modifyMobMeatDrops(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
		ModItems.MEAT_SETS.forEach((meatSet) -> modifyMobMeatDrop(
				key, tableBuilder, source, registries, meatSet.entity(), meatSet.RAW()
		));
	}
	
	public static void modifyMobHideDrop(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries,
	                                     EntityType<?> entity, Item drop) {
		if (entity.getLootTableId().equals(key)) {
			LootPool.Builder poolBuilder = LootPool.builder()
					.rolls(ConstantLootNumberProvider.create(1))
					.with(ItemEntry.builder(drop)
							.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 2.0f)))
							.apply(EnchantedCountIncreaseLootFunction.builder(registries, UniformLootNumberProvider.create(0, 1))));
			
			tableBuilder.pool(poolBuilder);
		}
		
		if (entity == EntityType.LLAMA) modifyMobHideDrop(key, tableBuilder, source, registries, EntityType.TRADER_LLAMA, drop);
	}
	
	public static void modifyMobHideDrops(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
		ModItems.HIDE_SETS.forEach((hideSet) -> modifyMobHideDrop(
				key, tableBuilder, source, registries, hideSet.entity(), hideSet.HIDE()
		));
	}
	
	
	public static void modifyLootTables() {
		LootTableEvents.MODIFY.register(ModLootTableModifiers::modifyMobMeatDrops);
		LootTableEvents.MODIFY.register(ModLootTableModifiers::modifyMobHideDrops);
	}
}
