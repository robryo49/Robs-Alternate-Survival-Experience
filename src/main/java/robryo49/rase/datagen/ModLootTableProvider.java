package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
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
	
	
	
	
	public void addDrop(List<Block> blocks) {
		for (Block block: blocks) addDrop(block);
	}
	public void addDrop(Block block, Item item, int minCount, int maxCount) {
		addDrop(
				block,
				dropsWithSilkTouch(
						block,
						applyExplosionDecay(
								block,
								ItemEntry.builder(item)
										.apply(SetCountLootFunction.builder(
												UniformLootNumberProvider.create(minCount, maxCount)
										))
						)
				)
		);
	}
	
	public void addDrop(ModItems.OreMaterialSet materialSet, ModBlocks.OreBlockSet blockSet) {
		addDrop(blockSet.ORE(), oreDrops(blockSet.ORE(), materialSet.RAW()));
		addDrop(blockSet.DEEPSLATE_ORE(), oreDrops(blockSet.DEEPSLATE_ORE(), materialSet.RAW()));
		addDrop(blockSet.BLOCK());
		addDrop(blockSet.RAW_BLOCK());
	}
	public void addDrop(ModItems.OreMaterialSet materialSet, ModBlocks.NetherOreBlockSet blockSet) {
		addDrop(blockSet.NETHER_ORE(), oreDrops(blockSet.NETHER_ORE(), materialSet.RAW()));
		addDrop(blockSet.BLOCK());
		addDrop(blockSet.RAW_BLOCK());
	}
	public void addDrop(ModItems.AlloyMaterialSet materialSet, ModBlocks.AlloyBlockSet blockSet) {
		addDrop(blockSet.BLOCK());
	}
	public void addDrop(ModItems.CrystalMaterialSet materialSet, ModBlocks.CrystalBlockSet blockSet) {
		addDrop(blockSet.ORE(), oreDrops(blockSet.ORE(), materialSet.CRYSTAL()));
		addDrop(blockSet.DEEPSLATE_ORE(), oreDrops(blockSet.DEEPSLATE_ORE(), materialSet.CRYSTAL()));
		addDrop(blockSet.BLOCK());
	}
	public void addDrop(ModItems.CrystalMaterialSet materialSet, ModBlocks.NetherCrystalBlockSet blockSet) {
		addDrop(blockSet.NETHER_ORE(), oreDrops(blockSet.NETHER_ORE(), materialSet.CRYSTAL()));
		addDrop(blockSet.BLOCK());
	}
	
	public void addDrop(ModBlocks.SmithingAnvilBlockSet anvil) {
		addDrop(anvil.NORMAL());
		addDrop(anvil.CHIPPED());
		addDrop(anvil.DAMAGED());
	}
	
	
	public void generateMaterialBlockDrops() {
		addDrop(ModItems.TIN, ModBlocks.TIN);
		addDrop(ModItems.ZINC, ModBlocks.ZINC);
		addDrop(ModItems.MAGNETITE, ModBlocks.MAGNETITE);
		addDrop(ModItems.BRONZE, ModBlocks.BRONZE);
		addDrop(ModItems.SILVER, ModBlocks.SILVER);
		addDrop(ModItems.LEAD, ModBlocks.LEAD);
		addDrop(ModItems.STEEL, ModBlocks.STEEL);
		addDrop(ModItems.TITANIUM, ModBlocks.TITANIUM);
		addDrop(ModItems.PLATINUM, ModBlocks.PLATINUM);
		addDrop(ModItems.TUNGSTEN, ModBlocks.TUNGSTEN);
		addDrop(ModItems.PALLADIUM, ModBlocks.PALLADIUM);
		addDrop(ModItems.COBALT, ModBlocks.COBALT);
		addDrop(ModItems.SCANDIUM, ModBlocks.SCANDIUM);
		addDrop(ModItems.MYTHRIL, ModBlocks.MYTHRIL);
		addDrop(ModItems.RHEXIS, ModBlocks.RHEXIS);
	}
	public void generateAnvilDrops() {
		addDrop(ModBlocks.STONE_ANVIL);
		addDrop(ModBlocks.LEAD_ANVIL);
		addDrop(ModBlocks.TITANIUM_ANVIL);
		addDrop(ModBlocks.TUNGSTEN_ANVIL);
	}
	public void generateBlockDrops() {
		addDrop(List.of(
				ModBlocks.PRIMITIVE_FORGE,
				ModBlocks.ADVANCED_FORGE
		));
	}
	
	
	@Override
	public void generate() {
		generateMaterialBlockDrops();
		generateAnvilDrops();
		generateBlockDrops();
	}
}
