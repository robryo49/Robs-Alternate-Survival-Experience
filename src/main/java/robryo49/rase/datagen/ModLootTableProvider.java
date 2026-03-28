package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
	public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}
	
	
	
	public void addDrops(List<Block> blocks) {
		for (Block block: blocks) addDrop(block);
	}
	
	public void addPebbleDrop(Block block, Item pebble) {
		addDrop(
				block,
				dropsWithSilkTouch(
						block,
						applyExplosionDecay(
								block,
								ItemEntry.builder(pebble)
										.apply(SetCountLootFunction.builder(
												UniformLootNumberProvider.create(2, 4)
										))
						)
				)
		);
	}
	
	public void addPebbleDrop(ModItems.PebbleSet pebbleSet) {
		addPebbleDrop(pebbleSet.SOURCE_BLOCK(), pebbleSet.PEBBLE());
	}
	
	public void addPebbleDrops(List<ModItems.PebbleSet> pebbles) {
		pebbles.forEach(this::addPebbleDrop);
	}
	
	
	
	private LootPool plantFiberPool() {
		return LootPool.builder()
				.with(ItemEntry.builder(ModItems.PLANT_FIBER)
						.conditionally(MatchToolLootCondition.builder(
								ItemPredicate.Builder.create().tag(ItemTags.SWORDS)
						))
						.conditionally(SurvivesExplosionLootCondition.builder())
						.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2)))
						.apply(ApplyBonusLootFunction.uniformBonusCount(
								registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
										.getOrThrow(Enchantments.FORTUNE), 2
						))
				)
				.build();
	}
	
	public void addShortGrassPlantFiberDrop() {
		addDrop(Blocks.SHORT_GRASS, LootTable.builder()
				.pool(LootPool.builder()
						.with(AlternativeEntry.builder(
								ItemEntry.builder(Blocks.SHORT_GRASS)
										.conditionally(MatchToolLootCondition.builder(
												ItemPredicate.Builder.create().items(Items.SHEARS)
										)),
								ItemEntry.builder(Items.WHEAT_SEEDS)
										.conditionally(RandomChanceLootCondition.builder(0.125f))
										.apply(ApplyBonusLootFunction.uniformBonusCount(
												registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
														.getOrThrow(Enchantments.FORTUNE), 2
										))
										.apply(ExplosionDecayLootFunction.builder())
						))
				)
				.pool(plantFiberPool())
		);
	}
	
	public void addFernPlantFiberDrop() {
		addDrop(Blocks.FERN, LootTable.builder()
				.pool(LootPool.builder()
						.with(AlternativeEntry.builder(
								ItemEntry.builder(Blocks.FERN)
										.conditionally(MatchToolLootCondition.builder(
												ItemPredicate.Builder.create().items(Items.SHEARS)
										)),
								ItemEntry.builder(Items.WHEAT_SEEDS)
										.conditionally(RandomChanceLootCondition.builder(0.125f))
										.apply(ApplyBonusLootFunction.uniformBonusCount(
												registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
														.getOrThrow(Enchantments.FORTUNE), 2
										))
										.apply(ExplosionDecayLootFunction.builder())
						))
				)
				.pool(plantFiberPool())
		);
	}
	
	public void addTallGrassPlantFiberDrop() {
		addDrop(Blocks.TALL_GRASS, LootTable.builder()
				.pool(LootPool.builder()
						.conditionally(BlockStatePropertyLootCondition.builder(Blocks.TALL_GRASS)
								.properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER))
						)
						.conditionally(LocationCheckLootCondition.builder(
								LocationPredicate.Builder.create().block(
										BlockPredicate.Builder.create()
												.blocks(Blocks.TALL_GRASS)
												.state(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.UPPER))
								), new BlockPos(0, 1, 0)
						))
						.with(AlternativeEntry.builder(
								ItemEntry.builder(Blocks.SHORT_GRASS)
										.conditionally(MatchToolLootCondition.builder(
												ItemPredicate.Builder.create().items(Items.SHEARS)
										))
										.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2))),
								ItemEntry.builder(Items.WHEAT_SEEDS)
										.conditionally(SurvivesExplosionLootCondition.builder())
										.conditionally(RandomChanceLootCondition.builder(0.125f))
						))
				)
				.pool(LootPool.builder()
						.conditionally(BlockStatePropertyLootCondition.builder(Blocks.TALL_GRASS)
								.properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.UPPER))
						)
						.conditionally(LocationCheckLootCondition.builder(
								LocationPredicate.Builder.create().block(
										BlockPredicate.Builder.create()
												.blocks(Blocks.TALL_GRASS)
												.state(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER))
								), new BlockPos(0, -1, 0)
						))
						.with(AlternativeEntry.builder(
								ItemEntry.builder(Blocks.SHORT_GRASS)
										.conditionally(MatchToolLootCondition.builder(
												ItemPredicate.Builder.create().items(Items.SHEARS)
										))
										.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2))),
								ItemEntry.builder(Items.WHEAT_SEEDS)
										.conditionally(SurvivesExplosionLootCondition.builder())
										.conditionally(RandomChanceLootCondition.builder(0.125f))
						))
				)
				.pool(plantFiberPool())
		);
	}
	
	public void addPlantFiberDrops() {
		addFernPlantFiberDrop();
		addShortGrassPlantFiberDrop();
		addTallGrassPlantFiberDrop();
	}
	
	public void addGravelFlintDrop() {
		addDrop(Blocks.GRAVEL, LootTable.builder()
				.pool(LootPool.builder()
						.with(AlternativeEntry.builder(
								ItemEntry.builder(Items.FLINT)
										.conditionally(RandomChanceLootCondition.builder(0.2f))
										.apply(ExplosionDecayLootFunction.builder()),
								ItemEntry.builder(Blocks.GRAVEL)
										.apply(ExplosionDecayLootFunction.builder())
						))
				)
		);
	}
	
	@Override
	public void generate() {
		addPebbleDrops(ModItems.PEBBLE_SETS);
		addPlantFiberDrops();
		addGravelFlintDrop();
		
		
		addPebbleDrop(ModBlocks.CHIPPED_STONE, ModItems.STONE_PEBBLES.PEBBLE());
	}
}
