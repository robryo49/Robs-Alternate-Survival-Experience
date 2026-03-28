package robryo49.rase.loot_table;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.*;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.EnchantmentsPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.predicate.item.ItemSubPredicateTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import robryo49.rase.item.ModItems;

import java.util.List;

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
	
	public static void modifyLeavesDrops(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
		
		List<Block> LEAVES = List.of(
				Blocks.OAK_LEAVES,
				Blocks.ACACIA_LEAVES,
				Blocks.AZALEA_LEAVES,
				Blocks.BIRCH_LEAVES,
				Blocks.CHERRY_LEAVES,
				Blocks.DARK_OAK_LEAVES,
				Blocks.FLOWERING_AZALEA_LEAVES,
				Blocks.JUNGLE_LEAVES,
				Blocks.MANGROVE_LEAVES,
				Blocks.SPRUCE_LEAVES
		);
		
		for (Block block : LEAVES) {
			if (source.isBuiltin() && block.getLootTableKey().equals(key)) {
				tableBuilder.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0f))
						.conditionally(InvertedLootCondition.builder(
								AnyOfLootCondition.builder(
										MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(Items.SHEARS)),
										MatchToolLootCondition.builder(ItemPredicate.Builder.create()
												.subPredicate(ItemSubPredicateTypes.ENCHANTMENTS,
														EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(
																registries.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH),
																NumberRange.IntRange.atLeast(1)
														)))
												)
										)
								)
						))
						.with(ItemEntry.builder(Items.STICK)
								.conditionally(RandomChanceLootCondition.builder(0.1f)) // 20% extra chance
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)))
								.apply(net.minecraft.loot.function.ExplosionDecayLootFunction.builder()) // Performance safety
						)
				);
			}
		}
	}
	
	public static void modifyPlantFiberDrops(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
		// List of blocks that should receive the extra plant fiber pool
		List<Block> FIBER_PLANTS = List.of(
				Blocks.SHORT_GRASS,
				Blocks.FERN,
				Blocks.TALL_GRASS,
				Blocks.LARGE_FERN
		);
		
		
		for (Block block : FIBER_PLANTS) {
			if (source.isBuiltin() && block.getLootTableKey().equals(key)) {
				tableBuilder.pool(
						LootPool.builder()
								.with(ItemEntry.builder(ModItems.PLANT_FIBER)
										.conditionally(MatchToolLootCondition.builder(
												ItemPredicate.Builder.create().tag(ItemTags.SWORDS)
										))
										.conditionally(SurvivesExplosionLootCondition.builder())
										.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2)))
										.apply(ApplyBonusLootFunction.uniformBonusCount(
												registries.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
														.getOrThrow(Enchantments.FORTUNE), 2
										))
								)
								.build()
				);
			}
		}
	}
	
	private static LootTable replaceGravelDrops(RegistryKey<LootTable> key, LootTable original, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
		if (source.isBuiltin() && Blocks.GRAVEL.getLootTableKey().equals(key)) {
			return LootTable.builder()
					.pool(LootPool.builder()
							.rolls(ConstantLootNumberProvider.create(1.0f))
							.with(AlternativeEntry.builder(
									ItemEntry.builder(Items.FLINT)
											.conditionally(RandomChanceLootCondition.builder(0.15f)),
									ItemEntry.builder(ModItems.FLINT_SHARD)
											.conditionally(RandomChanceLootCondition.builder(0.1f)),
									ItemEntry.builder(Blocks.GRAVEL)
							))
							.apply(net.minecraft.loot.function.ExplosionDecayLootFunction.builder())
					).build();
		}
		return null;
	}
	
	private static LootTable replacePebbleDrops(RegistryKey<LootTable> key, LootTable original, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
		for (ModItems.PebbleSet pebbleSet : ModItems.PEBBLE_SETS) {
			if (pebbleSet.SOURCE_BLOCK().getLootTableKey().equals(key)) {
				return LootTable.builder().pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0f))
						.with(AlternativeEntry.builder(
								ItemEntry.builder(pebbleSet.SOURCE_BLOCK())
										.conditionally(MatchToolLootCondition.builder(
												ItemPredicate.Builder.create().subPredicate(ItemSubPredicateTypes.ENCHANTMENTS,
														EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(
																registries.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH),
																NumberRange.IntRange.atLeast(1))))))),
								ItemEntry.builder(pebbleSet.PEBBLE())
										.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f)))
										.apply(net.minecraft.loot.function.ExplosionDecayLootFunction.builder())
						))
				).build();
			}
		}
		return null;
	}
	
	public static void modifyLootTables() {
		LootTableEvents.MODIFY.register(ModLootTableModifiers::modifyMobMeatDrops);
		LootTableEvents.MODIFY.register(ModLootTableModifiers::modifyMobHideDrops);
		LootTableEvents.MODIFY.register(ModLootTableModifiers::modifyLeavesDrops);
		LootTableEvents.MODIFY.register(ModLootTableModifiers::modifyPlantFiberDrops);
		LootTableEvents.REPLACE.register(ModLootTableModifiers::replaceGravelDrops);
		LootTableEvents.REPLACE.register(ModLootTableModifiers::replacePebbleDrops);
	}
}
