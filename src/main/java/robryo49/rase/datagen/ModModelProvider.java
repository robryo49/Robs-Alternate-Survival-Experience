package robryo49.rase.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.block.custom.forge.ForgeBlock;
import robryo49.rase.item.ModItems;
import robryo49.rase.item.custom.MoldItem;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModModelProvider extends FabricModelProvider {
	
	private static final Map<Model, BiConsumer<BlockStateModelGenerator, Block>> MODEL_GENERATORS = Map.of(
			Models.CUBE_ALL, BlockStateModelGenerator::registerSimpleCubeAll,
			
			Models.ORIENTABLE, (blockStateModelGenerator, block) -> {
				if (block instanceof ForgeBlock) blockStateModelGenerator.registerCooker(block, TexturedModel.ORIENTABLE);
				else blockStateModelGenerator.registerNorthDefaultHorizontalRotated(block, TexturedModel.ORIENTABLE);
			}
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
		ModBlocks.MODELS.forEach((ModBlocks.Models model, List<Block> blocks) -> model.generate(blockStateModelGenerator, blocks));
	}
	
	
	public void registerItemModel(ItemModelGenerator itemModelGenerator, Model model, Item item) {
		if (item instanceof ArmorItem armorItem) {
			registerArmorItemModel(itemModelGenerator, armorItem);
		} else if (item instanceof MoldItem moldItem) {
			registerMoldItemModel(itemModelGenerator, moldItem);
		} else {
			itemModelGenerator.register(item, model);
		}
	}
	
	public void registerArmorItemModel(ItemModelGenerator itemModelGenerator, ArmorItem item) {
		itemModelGenerator.registerArmor(item);
	}
	
	private void registerMoldItemModel(ItemModelGenerator itemModelGenerator, MoldItem moldItem) {
		
		Identifier filledModelId = ModelIds.getItemSubModelId(moldItem, "_filled");
		itemModelGenerator.writer.accept(filledModelId, () -> {
			JsonObject json = new JsonObject();
			json.addProperty("parent", "minecraft:item/generated");
			
			JsonObject textures = new JsonObject();
			textures.addProperty("layer0", Rase.MOD_ID + ":item/mold_base_" + moldItem.getMaterialName());
			textures.addProperty("layer1",  Rase.MOD_ID + ":item/mold_pattern_" + moldItem.getPatternName());
			textures.addProperty("layer2",  Rase.MOD_ID + ":item/mold_pattern_" + moldItem.getPatternName() + "_filled");
			json.add("textures", textures);
			
			return json;
		});
		
		itemModelGenerator.writer.accept(ModelIds.getItemModelId(moldItem), () -> {
			JsonObject json = new JsonObject();
			json.addProperty("parent", "minecraft:item/generated");
			
			JsonObject textures = new JsonObject();
			textures.addProperty("layer0", Rase.MOD_ID + ":item/mold_base_" + moldItem.getMaterialName());
			textures.addProperty("layer1",  Rase.MOD_ID + ":item/mold_pattern_" + moldItem.getPatternName());
			json.add("textures", textures);
			
			JsonArray overrides = new JsonArray();
			JsonObject overrideObj = new JsonObject();
			
			JsonObject predicate = new JsonObject();
			predicate.addProperty(Identifier.of(Rase.MOD_ID, "filled").toString(), 1.0f);
			
			overrideObj.add("predicate", predicate);
			overrideObj.addProperty("model", filledModelId.toString());
			
			overrides.add(overrideObj);
			json.add("overrides", overrides);
			
			return json;
		});
	}
	
	
	public void registerItemModels(ItemModelGenerator itemModelGenerator, Model model, List<Item> items) {
		items.forEach(item -> registerItemModel(itemModelGenerator, model, item));
	}
	
	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		ModItems.MODELS.forEach((Model model, List<Item> items) -> registerItemModels(itemModelGenerator, model, items));
	}
}
