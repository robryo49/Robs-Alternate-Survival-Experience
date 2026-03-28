package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.item.ModItems;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModModelProvider extends FabricModelProvider {
	
	private static final Map<Model, BiConsumer<BlockStateModelGenerator, Block>> MODEL_GENERATORS = Map.of(
			Models.CUBE_ALL, BlockStateModelGenerator::registerSimpleCubeAll
	);
	
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}
	
	public void registerBlockStateModels(BlockStateModelGenerator blockStateModelGenerator, List<Block> blocks, Model model) {
		BiConsumer<BlockStateModelGenerator, Block> generator = MODEL_GENERATORS.getOrDefault(model, BlockStateModelGenerator::registerSimpleCubeAll);
		blocks.forEach(block -> generator.accept(blockStateModelGenerator, block));
	}
	
	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		ModBlocks.MODELS.forEach((Model model, List<Block> blocks) -> registerBlockStateModels(blockStateModelGenerator, blocks, model));
	}
	
	
	public void registerItemModels(ItemModelGenerator itemModelGenerator, Model model, List<Item> items) {
		items.forEach(item -> {
			if (item instanceof ArmorItem) itemModelGenerator.registerArmor((ArmorItem) item);
			else itemModelGenerator.register(item, model);
		});
	}
	
	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		ModItems.MODELS.forEach((Model model, List<Item> items) -> registerItemModels(itemModelGenerator, model, items));
	}
}
