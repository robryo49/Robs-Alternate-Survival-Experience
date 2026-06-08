package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.datagen.builders.*;
import robryo49.rase.item.ModItems;
import robryo49.rase.item.ModMaterials;
import robryo49.rase.recipe.custom.WorkbenchRecipe;
import robryo49.rase.util.ModItemTags;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModRecipeProvider extends FabricRecipeProvider {
	
	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	// --- Static Utility Helpers ---
	
	public static Item[] getItemsFromIngredient(Ingredient input) {
		return Arrays.stream(input.getMatchingStacks())
				.map(ItemStack::getItem)
				.distinct()
				.toArray(Item[]::new);
	}
	
	// --- Standard Crafting Helpers ---
	
	public static void offerShaped(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, int count,
	                               Map<Character, Ingredient> inputs, List<String> pattern, ItemConvertible criterionItem) {
		ShapedRecipeJsonBuilder builder = ShapedRecipeJsonBuilder.create(category, output, count);
		pattern.forEach(builder::pattern);
		inputs.forEach(builder::input);
		builder.criterion(hasItem(criterionItem), conditionsFromItem(criterionItem)).offerTo(exporter);
	}
	public static void offerShaped(RecipeExporter exporter, String source, RecipeCategory category, ItemConvertible output, int count,
	                               Map<Character, Ingredient> inputs, List<String> pattern, ItemConvertible criterionItem) {
		ShapedRecipeJsonBuilder builder = ShapedRecipeJsonBuilder.create(category, output, count);
		pattern.forEach(builder::pattern);
		inputs.forEach(builder::input);
		builder.criterion(hasItem(criterionItem), conditionsFromItem(criterionItem)).offerTo(exporter, getRecipeName(output) + "_from_" + source);
	}
	
	public static void offerShapeless(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, int count,
	                                  List<Ingredient> inputs, ItemConvertible criterionItem) {
		ShapelessRecipeJsonBuilder builder = ShapelessRecipeJsonBuilder.create(category, output, count);
		inputs.forEach(builder::input);
		builder.criterion("has_ingredient", InventoryChangedCriterion.Conditions.items(criterionItem)).offerTo(exporter);
	}
	public static void offerShapeless(RecipeExporter exporter, String source, RecipeCategory category, ItemConvertible output, int count,
	                                  List<Ingredient> inputs, ItemConvertible criterionItem) {
		ShapelessRecipeJsonBuilder builder = ShapelessRecipeJsonBuilder.create(category, output, count);
		inputs.forEach(builder::input);
		builder.criterion("has_ingredient", InventoryChangedCriterion.Conditions.items(criterionItem)).offerTo(exporter, getRecipeName(output) + "_from_" + source);
	}
	
	public void replaceShaped(RecipeExporter exporter, String vanillaPath, RecipeCategory category, ItemConvertible output, int count,
	                          Map<Character, Ingredient> inputs, List<String> pattern, ItemConvertible criterionItem) {
		Identifier recipeId = Identifier.of("minecraft", vanillaPath);
		RawShapedRecipe raw = RawShapedRecipe.create(inputs, pattern);
		
		AdvancementEntry advancementEntry = exporter.getAdvancementBuilder()
				.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId))
				.criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
				.criterion("has_item", conditionsFromItem(criterionItem))
				.build(recipeId.withPrefixedPath("recipes/"));
		
		ShapedRecipe shapedRecipe = new ShapedRecipe("", CraftingRecipeCategory.MISC, raw, new ItemStack(output.asItem(), count), true);
		exporter.accept(recipeId, shapedRecipe, advancementEntry);
	}
	
	public void replaceShapeless(RecipeExporter exporter, String vanillaPath, RecipeCategory category, ItemConvertible output, int count,
	                             List<Ingredient> inputs, ItemConvertible criterionItem) {
		Identifier recipeId = Identifier.of("minecraft", vanillaPath);
		DefaultedList<Ingredient> ingredientList = DefaultedList.of();
		ingredientList.addAll(inputs);
		
		AdvancementEntry advancementEntry = exporter.getAdvancementBuilder()
				.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId))
				.criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
				.criterion("has_item", conditionsFromItem(criterionItem))
				.build(recipeId.withPrefixedPath("recipes/"));
		
		ShapelessRecipe shapelessRecipe = new ShapelessRecipe("", CraftingRecipeCategory.MISC, new ItemStack(output.asItem(), count), ingredientList);
		exporter.accept(recipeId, shapelessRecipe, advancementEntry);
	}
	
	public static void offerWorkbench(RecipeExporter exporter, RecipeCategory category,
	                                  ItemConvertible output, int count,
	                                  Map<Character, Ingredient> inputs, List<String> pattern,
	                                  ItemConvertible criterionItem) {
		// Build a ShapedRecipe internally, then wrap it in WorkbenchRecipe
		Identifier recipeId = Rase.getIdentifier(getRecipeName(output) + "_workbench");
		
		RawShapedRecipe raw = RawShapedRecipe.create(inputs, pattern);
		ShapedRecipe shaped = new ShapedRecipe("", CraftingRecipeCategory.MISC,
				raw, new ItemStack(output.asItem(), count), false);
		WorkbenchRecipe recipe = new WorkbenchRecipe(shaped);
		
		AdvancementEntry advancement = exporter.getAdvancementBuilder()
				.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId))
				.criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
				.criterion("has_item", conditionsFromItem(criterionItem))
				.build(recipeId.withPrefixedPath("recipes/"));
		
		exporter.accept(recipeId, recipe, advancement);
	}
	
	// --- Cooking Station Helpers ---
	
	public static <T extends AbstractCookingRecipe> void offerCooking(RecipeExporter exporter, String cooker, RecipeSerializer<T> serializer,
	                                                                  AbstractCookingRecipe.RecipeFactory<T> factory, RecipeCategory category,
	                                                                  int time, ItemConvertible input, ItemConvertible output, float exp) {
		CookingRecipeJsonBuilder.create(Ingredient.ofItems(input), category, output, exp, time, serializer, factory)
				.criterion(hasItem(input), conditionsFromItem(input))
				.offerTo(exporter, Rase.getIdentifier(getItemPath(output) + "_from_" + cooker));
	}
	
	public static void offerFurnaceCooking(RecipeExporter exporter, RecipeCategory category, ItemConvertible input, ItemConvertible output, float exp) {
		offerCooking(exporter, "smelting", RecipeSerializer.SMELTING, SmeltingRecipe::new, category, 200, input, output, exp);
	}
	public static void offerSmokerCooking(RecipeExporter exporter, RecipeCategory category, ItemConvertible input, ItemConvertible output, float exp) {
		offerCooking(exporter, "smoking", RecipeSerializer.SMOKING, SmokingRecipe::new, category, 100, input, output, exp);
	}
	public static void offerCampfireCooking(RecipeExporter exporter, RecipeCategory category, ItemConvertible input, ItemConvertible output, float exp) {
		offerCooking(exporter, "campfire", RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new, category, 600, input, output, exp);
	}
	
	public static void offerAllCooking(RecipeExporter exporter, RecipeCategory category, ItemConvertible input, ItemConvertible output, float exp, boolean furnace, boolean smoker, boolean campfire) {
		if (furnace) offerCooking(exporter, "smelting", RecipeSerializer.SMELTING, SmeltingRecipe::new, category, 200, input, output, exp);
		if (smoker) offerCooking(exporter, "smoking", RecipeSerializer.SMOKING, SmokingRecipe::new, category, 100, input, output, exp);
		if (campfire) offerCooking(exporter, "campfire", RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new, category, 600, input, output, exp);
	}
	
	// --- Compacting Recipes ---
	
	public static void offerCompacting(RecipeExporter exporter, String source, RecipeCategory category, ItemConvertible input, ItemConvertible output) {
		offerShapeless(exporter, source, category, output, 1, List.of(
				Ingredient.ofItems(input),
				Ingredient.ofItems(input),
				Ingredient.ofItems(input),
				Ingredient.ofItems(input),
				Ingredient.ofItems(input),
				Ingredient.ofItems(input),
				Ingredient.ofItems(input),
				Ingredient.ofItems(input),
				Ingredient.ofItems(input)
		), input);
	}
	
	public static void offerDecompacting(RecipeExporter exporter, String source, RecipeCategory category, ItemConvertible input, ItemConvertible output) {
		offerShapeless(exporter, source, category, output, 9, List.of(Ingredient.ofItems(input)), input);
	}
	
	public static void offerReversibleCompacting(RecipeExporter exporter, String source, String compactedSource, RecipeCategory sourceCategory, RecipeCategory compactedCategory,
	                                             ItemConvertible input, ItemConvertible output) {
		offerCompacting(exporter, compactedSource, compactedCategory, input, output);
		offerDecompacting(exporter, source, sourceCategory, output, input);
	}
	
	public static void offerIngotCompacting(RecipeExporter exporter, Item ingot, Block block, Item nugget) {
		offerReversibleCompacting(exporter, "block", "ingot", RecipeCategory.MISC, RecipeCategory.BUILDING_BLOCKS, ingot, block);
		offerReversibleCompacting(exporter, "ingot", "nugget", RecipeCategory.MISC, RecipeCategory.MISC, nugget, ingot);
	}
	
	public static void offerCrystalCompacting(RecipeExporter exporter, Item ingot, Block block) {
		offerReversibleCompacting(exporter, "block", "crystal", RecipeCategory.MISC, RecipeCategory.BUILDING_BLOCKS, ingot, block);
	}
	
	public static void offerRawCompacting(RecipeExporter exporter, Item raw, Block raw_block) {
		offerReversibleCompacting(exporter, "raw_block", "raw", RecipeCategory.MISC, RecipeCategory.BUILDING_BLOCKS, raw, raw_block);
	}
	
	// --- Other Helpers ---
	
	public static void offerAnvilRecipe(RecipeExporter exporter, Item ingot, Block block, Block anvil) {
		if (anvil == ModBlocks.STONE_ANVIL.NORMAL()) {
			offerShaped(exporter, RecipeCategory.MISC, anvil, 1, Map.of('#', Ingredient.ofItems(block), '-', Ingredient.ofItems(ingot)), List.of("###", " - ", "---"), ingot);
		} else {
			offerWorkbench(exporter, RecipeCategory.MISC, anvil, 1, Map.of('#', Ingredient.ofItems(block), '-', Ingredient.ofItems(ingot)), List.of("###", " - ", "---"), ingot);
		}
	}
	
	// --- Custom Machine Helpers ---
	
	public static void offerTooling(RecipeExporter exporter, String name, Ingredient tool, ItemConvertible inputItem, ItemConvertible result, int count) {
		ToolingRecipeJsonBuilder.create(CraftingRecipeCategory.EQUIPMENT, tool, Ingredient.ofItems(inputItem), result.asItem(), count)
				.criterion(hasItem(inputItem), conditionsFromItem(inputItem))
				.offerTo(exporter, name);
	}
	
	public static void offerForging(RecipeExporter exporter, String name, List<Ingredient> inputs, ItemConvertible result, TagKey<Item> mold, int smeltTime, int coolTime, int tier, float exp) {
		ForgeSmeltingRecipeJsonBuilder builder = ForgeSmeltingRecipeJsonBuilder.create(result.asItem());
		inputs.forEach(builder::input);
		builder.mold(mold).smeltingTime(smeltTime).coolingTime(coolTime).minTier(tier).experience(exp)
				.offerTo(exporter, name + "_forging");
	}
	
	public static void offerAnvilSmithing(RecipeExporter exporter, String name, Ingredient base, Ingredient addition, ItemConvertible result, int cost, int tier) {
		AnvilSmithingRecipeJsonBuilder.create(base, addition, result.asItem())
				.levelCost(cost).tier(tier)
				.offerTo(exporter, name);
	}
	
	public static void offerDrying(RecipeExporter exporter, String name, Ingredient input, ItemConvertible output, int dryingTime) {
		DryingRecipeJsonBuilder.create(CraftingRecipeCategory.MISC, input, output, dryingTime)
				.criterion(hasItem(ModBlocks.DRYING_RACK.asItem()), conditionsFromItem(ModBlocks.DRYING_RACK.asItem()))
				.offerTo(exporter, name);
	}
	
	public static void offerCrushing(RecipeExporter exporter, String name,
	                                 Ingredient input, ItemConvertible output, int outputCount,
	                                 int crushingTime) {
		CrushingRecipeJsonBuilder.create(input, output, outputCount, crushingTime)
				.criterion(hasItem(output), conditionsFromItem(output))
				.offerTo(exporter, name);
	}
	
	// --- Complex Set Helpers ---
	
	public static void offerStartingToolSetsRecipes(RecipeExporter exporter) {
		// Flint Tools (Primitive)
		offerShaped(exporter, RecipeCategory.TOOLS, ModItems.FLINT_KNIFE, 1,
				Map.of('#', Ingredient.ofItems(ModItems.FLINT_SHARD), '/', Ingredient.ofItems(Items.STICK)),
				List.of("#", "/"), Items.FLINT);
		
		offerShaped(exporter, RecipeCategory.TOOLS, ModItems.FLINT_HATCHET, 1,
				Map.of('#', Ingredient.ofItems(Items.FLINT), '/', Ingredient.ofItems(Items.STICK), 's', Ingredient.fromTag(ModItemTags.STRINGS)),
				List.of("#s", " /"), Items.FLINT);
		
		offerShaped(exporter, RecipeCategory.TOOLS, ModItems.FLINT_PICK, 1,
				Map.of('#', Ingredient.ofItems(ModItems.SHARP_FLINT), '/', Ingredient.ofItems(Items.STICK), 's', Ingredient.fromTag(ModItemTags.STRINGS)),
				List.of("#s", "/#"), Items.FLINT);
		
		// Stone Tool Heads
		offerShaped(exporter, RecipeCategory.TOOLS, ModItems.STONE_TOOL_SET.AXE_HEAD(), 1, Map.of('#', Ingredient.ofItems(ModItems.STONE_PEBBLES.PEBBLE())), List.of("##", "# "), ModItems.STONE_PEBBLES.PEBBLE());
		offerShaped(exporter, RecipeCategory.TOOLS, ModItems.STONE_TOOL_SET.PICKAXE_HEAD(), 1, Map.of('#', Ingredient.ofItems(ModItems.STONE_PEBBLES.PEBBLE())), List.of("##", " #"), ModItems.STONE_PEBBLES.PEBBLE());
		offerShaped(exporter, RecipeCategory.TOOLS, ModItems.STONE_TOOL_SET.SWORD_BLADE(), 1, Map.of('#', Ingredient.ofItems(ModItems.STONE_PEBBLES.PEBBLE())), List.of("#", "#"), ModItems.STONE_PEBBLES.PEBBLE());
		offerShaped(exporter, RecipeCategory.TOOLS, ModItems.STONE_TOOL_SET.SHOVEL_HEAD(), 1, Map.of('#', Ingredient.ofItems(ModItems.STONE_PEBBLES.PEBBLE())), List.of("#"), ModItems.STONE_PEBBLES.PEBBLE());
		offerShaped(exporter, RecipeCategory.TOOLS, ModItems.STONE_TOOL_SET.HOE_HEAD(), 1, Map.of('#', Ingredient.ofItems(ModItems.STONE_PEBBLES.PEBBLE())), List.of("##"), ModItems.STONE_PEBBLES.PEBBLE());
		
		// Assembly
		Map<Item, Item> toolAssembly = Map.of(
				ModItems.STONE_TOOL_SET.AXE(), ModItems.STONE_TOOL_SET.AXE_HEAD(),
				ModItems.STONE_TOOL_SET.PICKAXE(), ModItems.STONE_TOOL_SET.PICKAXE_HEAD(),
				ModItems.STONE_TOOL_SET.SWORD(), ModItems.STONE_TOOL_SET.SWORD_BLADE(),
				ModItems.STONE_TOOL_SET.SHOVEL(), ModItems.STONE_TOOL_SET.SHOVEL_HEAD(),
				ModItems.STONE_TOOL_SET.HOE(), ModItems.STONE_TOOL_SET.HOE_HEAD()
		);
		
		toolAssembly.forEach((tool, head) -> offerShaped(exporter, RecipeCategory.TOOLS, tool, 1,
				Map.of('#', Ingredient.ofItems(head), 's', Ingredient.fromTag(ModItemTags.STRINGS), '/', Ingredient.ofItems(Items.STICK)),
				List.of("#", "s", "/"), ModItems.STONE_PEBBLES.PEBBLE()));
	}
	
	public static void offerToolSetRecipes(RecipeExporter exporter, Item ingot, ModItems.ToolSet toolSet, int smeltTime, int coolTime, int tier, float xp) {
		Map<ItemConvertible, TagKey<Item>> parts = Map.of(
				toolSet.AXE_HEAD(), ModItemTags.AXE_HEAD_MOLDS,
				toolSet.PICKAXE_HEAD(), ModItemTags.PICKAXE_HEAD_MOLDS,
				toolSet.SWORD_BLADE(), ModItemTags.SWORD_BLADE_MOLDS,
				toolSet.SHOVEL_HEAD(), ModItemTags.SHOVEL_HEAD_MOLDS,
				toolSet.HOE_HEAD(), ModItemTags.HOE_HEAD_MOLDS
		);
		String materialName = toolSet.name();
		
		parts.forEach((head, mold) -> {
			int count = (head == toolSet.SHOVEL_HEAD()) ? 1 : (head == toolSet.AXE_HEAD() || head == toolSet.PICKAXE_HEAD() ? 3 : 2);
			List<Ingredient> inputs = Stream.generate(() -> Ingredient.ofItems(ingot)).limit(count).toList();
			offerForging(exporter, materialName + "_" + getItemPath(head), inputs, head, mold, smeltTime, coolTime, tier, xp);
		});
		
		offerAnvilSmithing(exporter, materialName + "_axe_smithing", Ingredient.ofItems(ModItems.HANDLE), Ingredient.ofItems(toolSet.AXE_HEAD()), toolSet.AXE(), 0, tier);
		offerAnvilSmithing(exporter, materialName + "_pickaxe_smithing", Ingredient.ofItems(ModItems.HANDLE), Ingredient.ofItems(toolSet.PICKAXE_HEAD()), toolSet.PICKAXE(), 0, tier);
		offerAnvilSmithing(exporter, materialName + "_sword_smithing", Ingredient.ofItems(ModItems.HANDLE), Ingredient.ofItems(toolSet.SWORD_BLADE()), toolSet.SWORD(), 0, tier);
		offerAnvilSmithing(exporter, materialName + "_shovel_smithing", Ingredient.ofItems(ModItems.HANDLE), Ingredient.ofItems(toolSet.SHOVEL_HEAD()), toolSet.SHOVEL(), 0, tier);
		offerAnvilSmithing(exporter, materialName + "_hoe_smithing", Ingredient.ofItems(ModItems.HANDLE), Ingredient.ofItems(toolSet.HOE_HEAD()), toolSet.HOE(), 0, tier);
	}
	
	public static void offerArmorSetRecipes(RecipeExporter exporter, Item sourceItem, ModItems.ArmorSet armorSet) {
		if (armorSet == ModItems.BRONZE_ARMOR_SET) {
			offerShaped(exporter, RecipeCategory.TOOLS, armorSet.HELMET(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("###", "# #"), sourceItem);
			offerShaped(exporter, RecipeCategory.TOOLS, armorSet.CHESTPLATE(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("# #", "###", "###"), sourceItem);
			offerShaped(exporter, RecipeCategory.TOOLS, armorSet.LEGGINGS(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("###", "# #", "# #"), sourceItem);
			offerShaped(exporter, RecipeCategory.TOOLS, armorSet.BOOTS(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("# #", "# #"), sourceItem);
		} else {
			offerWorkbench(exporter, RecipeCategory.TOOLS, armorSet.HELMET(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("###", "# #"), sourceItem);
			offerWorkbench(exporter, RecipeCategory.TOOLS, armorSet.CHESTPLATE(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("# #", "###", "###"), sourceItem);
			offerWorkbench(exporter, RecipeCategory.TOOLS, armorSet.LEGGINGS(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("###", "# #", "# #"), sourceItem);
			offerWorkbench(exporter, RecipeCategory.TOOLS, armorSet.BOOTS(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("# #", "# #"), sourceItem);
			
		}
	}
	
	
	public static void offerMoldSetRecipes(RecipeExporter exporter, ModItems.MoldSet moldSet) {
		if (moldSet.FROM_SMELTING() instanceof ModItems.MoldSet unsmelted) {
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.BASE(), moldSet.BASE(), 0.35f, true, false, true);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.INGOT(), moldSet.INGOT(), 0.35f, true, false, true);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.AXE(), moldSet.AXE(), 0.35f, true, false, true);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.PICKAXE(), moldSet.PICKAXE(), 0.35f, true, false, true);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.SWORD(), moldSet.SWORD(), 0.35f, true, false, true);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.SHOVEL(), moldSet.SHOVEL(), 0.35f, true, false, true);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.HOE(), moldSet.HOE(), 0.35f, true, false, true);
		} else if (moldSet.INGREDIENT() != null){
			
			if (moldSet == ModItems.WET_CLAY_MOLD_SET) {
				offerShaped(exporter, RecipeCategory.TOOLS, moldSet.BASE(), 4, Map.of('#', Ingredient.ofItems(moldSet.INGREDIENT())), List.of("###", "# #", "###"), moldSet.INGREDIENT());
			} else {
				offerWorkbench(exporter, RecipeCategory.TOOLS, moldSet.BASE(), 4, Map.of('#', Ingredient.ofItems(moldSet.INGREDIENT())), List.of("###", "# #", "###"), moldSet.INGREDIENT());
			}
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.INGOT(), 1, List.of(Ingredient.ofItems(Items.BRICK), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.AXE(), 1, List.of(Ingredient.fromTag(ModItemTags.AXE_HEADS), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.PICKAXE(), 1, List.of(Ingredient.fromTag(ModItemTags.PICKAXE_HEADS), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.SWORD(), 1, List.of(Ingredient.fromTag(ModItemTags.SWORD_BLADES), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.SHOVEL(), 1, List.of(Ingredient.fromTag(ModItemTags.SHOVEL_HEADS), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.HOE(), 1, List.of(Ingredient.fromTag(ModItemTags.HOE_HEADS), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
		}
	}
	
	public static void offerHideSetRecipes(RecipeExporter exporter, ModItems.HideSet hideSet) {
		offerTooling(exporter, hideSet.entity().getUntranslatedName() + "_hide_cutting", Ingredient.fromTag(ItemTags.SWORDS), hideSet.HIDE(), ModItems.TANNED_HIDE, 1);
	}
	
	public static void offerPebbleSetRecipes(RecipeExporter exporter, ModItems.PebbleSet pebbleSet) {
		offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, pebbleSet.RECONSTRUCTED_BLOCK(), pebbleSet.PEBBLE());
	}
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, ModItems.OreMaterialSet materialSet, ModBlocks.OreBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, ModItems.OreMaterialSet materialSet, ModBlocks.OreBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		int tier = materialSet.tier();
		
		String materialName = materialSet.name();
		Item ingot = materialSet.INGOT(), nugget = materialSet.NUGGET(), raw = materialSet.RAW(), powder = materialSet.POWDER();
		Block ore = blockSet.ORE(), deepslateOre = blockSet.DEEPSLATE_ORE(), block = blockSet.BLOCK(), rawBlock = blockSet.RAW_BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		
		offerForging(exporter, materialName + "_ingot_forging", List.of(Ingredient.ofItems(raw)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		offerForging(exporter, materialName + "_ingot_forging_from_powder", List.of(Ingredient.ofItems(powder), Ingredient.ofItems(powder)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		
		offerIngotCompacting(exporter, ingot, block, nugget);
		offerRawCompacting(exporter, raw, rawBlock);
		
		offerCrushing(exporter, materialName + "_powder", Ingredient.ofItems(raw), powder, 3, 100 + 100*tier);
		
		if (toolSet != null) offerToolSetRecipes(exporter, ingot, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, ingot, armorSet);
	}
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, ModItems.OreMaterialSet materialSet, ModBlocks.NetherOreBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, ModItems.OreMaterialSet materialSet, ModBlocks.NetherOreBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		int tier = materialSet.tier();
		
		String materialName = materialSet.name();
		Item ingot = materialSet.INGOT(), nugget = materialSet.NUGGET(), raw = materialSet.RAW(), powder = materialSet.POWDER();
		Block ore = blockSet.NETHER_ORE(), block = blockSet.BLOCK(), rawBlock = blockSet.RAW_BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		offerForging(exporter, materialName + "_ingot_forging", List.of(Ingredient.ofItems(raw)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		offerForging(exporter, materialName + "_ingot_forging_from_powder", List.of(Ingredient.ofItems(powder), Ingredient.ofItems(powder)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		
		offerIngotCompacting(exporter, ingot, block, nugget);
		offerRawCompacting(exporter, raw, rawBlock);
		
		offerCrushing(exporter, materialName + "_powder", Ingredient.ofItems(raw), powder, 3, 100 + 100*tier);
		
		if (toolSet != null) offerToolSetRecipes(exporter, ingot, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, ingot, armorSet);
	}
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, List<Ingredient> ingredients, ModItems.AlloyMaterialSet materialSet, ModBlocks.AlloyBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, ingredients, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, List<Ingredient> ingredients, ModItems.AlloyMaterialSet materialSet, ModBlocks.AlloyBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		int tier = materialSet.tier();
		
		String materialName = materialSet.name();
		Item ingot = materialSet.INGOT(), nugget = materialSet.NUGGET();
		Block block = blockSet.BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		offerForging(exporter, materialName + "_ingot_forging", ingredients,
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		
		offerIngotCompacting(exporter, ingot, block, nugget);
		
		if (toolSet != null) offerToolSetRecipes(exporter, ingot, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, ingot, armorSet);
	}
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, ModItems.CrystalMaterialSet materialSet, ModBlocks.CrystalBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, ModItems.CrystalMaterialSet materialSet, ModBlocks.CrystalBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		int tier = materialSet.tier();
		
		String materialName = materialSet.name();
		Item crystal = materialSet.CRYSTAL();
		Block ore = blockSet.ORE(), deepslateOre = blockSet.DEEPSLATE_ORE(), block = blockSet.BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		offerCrystalCompacting(exporter, crystal, block);
		
		if (toolSet != null) offerToolSetRecipes(exporter, crystal, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, crystal, armorSet);
	}
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, ModItems.CrystalMaterialSet materialSet, ModBlocks.NetherCrystalBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, ModItems.CrystalMaterialSet materialSet, ModBlocks.NetherCrystalBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		int tier = materialSet.tier();
		
		String materialName = materialSet.name();
		Item crystal = materialSet.CRYSTAL();
		Block ore = blockSet.NETHER_ORE(), block = blockSet.BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		offerCrystalCompacting(exporter, crystal, block);
		
		if (toolSet != null) offerToolSetRecipes(exporter, crystal, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, crystal, armorSet);
	}
	
	// --- Main Generation ---
	
	private void generateMoldSetsRecipes(RecipeExporter exporter) {
		ModItems.MOLD_SETS.forEach(moldSet -> offerMoldSetRecipes(exporter, moldSet));
	}
	
	
	private void generatePebbleSetsRecipes(RecipeExporter exporter) {
		ModItems.PEBBLE_SETS.forEach(pebbleSet -> offerPebbleSetRecipes(exporter, pebbleSet));
	}
	
	
	private void generateVanillaMaterialRecipes(RecipeExporter exporter) {
		
		Map.of(
				ModMaterials.IRON, List.of(Items.RAW_IRON, ModItems.IRON_POWDER, Items.IRON_INGOT),
				ModMaterials.COPPER, List.of(Items.RAW_COPPER, ModItems.COPPER_POWDER, Items.COPPER_INGOT),
				ModMaterials.GOLD, List.of(Items.RAW_GOLD, ModItems.GOLD_POWDER, Items.GOLD_INGOT)
		).forEach((material, items) -> {
			
			String materialName = material.getId();
			Item ingot = items.getLast(), raw = items.getFirst(), powder = items.get(1);
			
			int tier = material.getTier();
			int smeltingTime = 200 * tier, coolingTime = 200 * tier;
			float xp = 1.2f * tier;
			
			offerForging(exporter, material.getId() + "_ingot_forging", List.of(Ingredient.ofItems(raw)),
					material.getMaterialItem(), ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
			offerForging(exporter, materialName + "_ingot_forging_from_powder", List.of(Ingredient.ofItems(powder), Ingredient.ofItems(powder)),
					ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
			
			offerCrushing(exporter, materialName + "_powder", Ingredient.ofItems(raw), powder, 3, 100 + 100*tier);
		});
	}
	
	
	private void generateMaterialRecipes(RecipeExporter exporter) {
		
		offerMaterialSetRecipes(exporter, ModItems.TIN, ModBlocks.TIN);
		offerMaterialSetRecipes(exporter, ModItems.MAGNETITE, ModBlocks.MAGNETITE);
		
		offerMaterialSetRecipes(exporter,
				List.of(Ingredient.ofItems(Items.COPPER_INGOT), Ingredient.ofItems(Items.COPPER_INGOT),
						Ingredient.ofItems(Items.COPPER_INGOT), Ingredient.ofItems(ModItems.TIN.INGOT())),
				ModItems.BRONZE, ModBlocks.BRONZE, ModItems.BRONZE_ARMOR_SET, ModItems.BRONZE_TOOL_SET);
		
		offerMaterialSetRecipes(exporter, ModItems.SILVER, ModBlocks.SILVER, ModItems.SILVER_ARMOR_SET, ModItems.SILVER_TOOL_SET);
		offerMaterialSetRecipes(exporter, ModItems.LEAD, ModBlocks.LEAD);
		
		offerMaterialSetRecipes(exporter,
				List.of(Ingredient.ofItems(Items.IRON_INGOT), Ingredient.ofItems(Items.IRON_INGOT), Ingredient.ofItems(ModItems.COAL_COKE)),
				ModItems.STEEL, ModBlocks.STEEL, ModItems.STEEL_ARMOR_SET, ModItems.STEEL_TOOL_SET);
		
		offerMaterialSetRecipes(exporter, ModItems.TITANIUM, ModBlocks.TITANIUM, ModItems.TITANIUM_ARMOR_SET, ModItems.TITANIUM_TOOL_SET);
		offerMaterialSetRecipes(exporter, ModItems.PLATINUM, ModBlocks.PLATINUM);
		offerMaterialSetRecipes(exporter, ModItems.TUNGSTEN, ModBlocks.TUNGSTEN);
		offerMaterialSetRecipes(exporter, ModItems.PALLADIUM, ModBlocks.PALLADIUM);
		offerMaterialSetRecipes(exporter, ModItems.COBALT, ModBlocks.COBALT, ModItems.COBALT_ARMOR_SET, ModItems.COBALT_TOOL_SET);
		
		offerMaterialSetRecipes(exporter,
				List.of(Ingredient.ofItems(ModItems.PALLADIUM.INGOT()), Ingredient.ofItems(ModItems.PLATINUM.INGOT())),
				ModItems.SCANDIUM, ModBlocks.SCANDIUM, ModItems.SCANDIUM_ARMOR_SET, ModItems.SCANDIUM_TOOL_SET);
		
		offerMaterialSetRecipes(exporter, ModItems.MITHRIL, ModBlocks.MITHRIL, ModItems.MITHRIL_ARMOR_SET, ModItems.MITHRIL_TOOL_SET);
		offerMaterialSetRecipes(exporter, ModItems.RHEXIS, ModBlocks.RHEXIS);
	}
	
	
	private void generateToolRecipes(RecipeExporter exporter) {
		offerStartingToolSetsRecipes(exporter);
	}
	
	
	private void generateToolingRecipes(RecipeExporter exporter) {
		ModItems.HIDE_SETS.forEach((hideSet -> offerHideSetRecipes(exporter, hideSet)));
		
		offerTooling(exporter, "leather_cutting", Ingredient.fromTag(ItemTags.SWORDS), Items.LEATHER, ModItems.LEATHER_STRAP, 2);
		offerTooling(exporter, "flint_cutting", Ingredient.fromTag(ItemTags.SWORDS), Items.FLINT, ModItems.SHARP_FLINT, 1);
	}
	
	
	private void generateAnvilRecipes(RecipeExporter exporter) {
		offerAnvilRecipe(exporter, ModItems.STONE_PEBBLES.PEBBLE(), Blocks.STONE, ModBlocks.STONE_ANVIL.NORMAL());
		offerAnvilRecipe(exporter, ModItems.LEAD.INGOT(), ModBlocks.LEAD.BLOCK(), ModBlocks.LEAD_ANVIL.NORMAL());
		offerAnvilRecipe(exporter, ModItems.TITANIUM.INGOT(), ModBlocks.TITANIUM.BLOCK(), ModBlocks.TITANIUM_ANVIL.NORMAL());
		offerAnvilRecipe(exporter, ModItems.TUNGSTEN.INGOT(), ModBlocks.TUNGSTEN.BLOCK(), ModBlocks.TUNGSTEN_ANVIL.NORMAL());
	}
	
	
	private void generateForgeRecipes(RecipeExporter exporter) {
		
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PRIMITIVE_FORGE, 1,
				Map.of('#', Ingredient.ofItems(Items.COBBLESTONE), '-', Ingredient.ofItems(ModItems.STONE_PEBBLES.PEBBLE()), 'o', Ingredient.ofItems(Items.CLAY_BALL)),
				List.of("o-o", "- -", "###"), ModItems.STONE_PEBBLES.PEBBLE());
		
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BASIC_FORGE, 1,
				Map.of('#', Ingredient.ofItems(Items.BRICKS), '-', Ingredient.ofItems(Items.BRICK), 'o', Ingredient.ofItems(Items.CLAY_BALL)),
				List.of("o-o", "- -", "###"), Items.BRICK);
		
		offerWorkbench(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.REFINED_FORGE, 1,
				Map.of('#', Ingredient.ofItems(Items.SMOOTH_STONE), '-', Ingredient.ofItems(Items.STONE), 'i', Ingredient.ofItems(Items.IRON_INGOT)),
				List.of("---", "i -", "###"), Items.STONE);
		
		offerWorkbench(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ADVANCED_FORGE, 1,
				Map.of('#', Ingredient.ofItems(ModBlocks.STEEL.BLOCK()), '-', Ingredient.ofItems(Items.NETHER_BRICKS),
						'i', Ingredient.ofItems(ModItems.STEEL.INGOT())),
				List.of("---", "i -", "###"), Items.NETHER_BRICK);
		
		offerWorkbench(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ETHEREAL_FORGE, 1,
				Map.of('#', Ingredient.ofItems(ModBlocks.STEEL.BLOCK()), '-', Ingredient.ofItems(ModBlocks.OBSIDIAN_BRICKS),
						'i', Ingredient.ofItems(ModItems.STEEL.INGOT())),
				List.of("---", "i -", "###"), Items.OBSIDIAN);
		
	}
	
	
	private void generateCookingRecipes(RecipeExporter exporter) {
		ModItems.MEAT_SETS.forEach(meat -> offerAllCooking(exporter, RecipeCategory.FOOD, meat.RAW(), meat.COOKED(), 0.35f, true, true, true));
		offerFurnaceCooking(exporter, RecipeCategory.MISC, Items.COAL, ModItems.COAL_COKE, 1);
		offerAllCooking(exporter, RecipeCategory.FOOD, ModItems.DOUGH, Items.BREAD, 1, true, true, true);
	}
	
	private void generateDryingRecipes(RecipeExporter exporter) {
		offerDrying(exporter, "leather_drying", Ingredient.ofItems(ModItems.TANNED_HIDE), Items.LEATHER, 60*3*20);
		offerDrying(exporter, "dry_cane_drying", Ingredient.ofItems(Items.SUGAR_CANE), ModItems.DRY_CANE, 60*2*20);
		offerDrying(exporter, "brick_drying", Ingredient.ofItems(ModItems.CLAY_BRICK), Items.BRICK, 60*3*20);
	}
	
	private void generateShapedRecipes(RecipeExporter exporter) {
		offerShaped(exporter, RecipeCategory.MISC, ModItems.CLAY_BRICK, 1, Map.of('#', Ingredient.ofItems(Items.CLAY_BALL)), List.of("##"), Items.CLAY_BALL);
		
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.OBSIDIAN_BRICKS, 4,
				Map.of('#', Ingredient.ofItems(Items.OBSIDIAN)), List.of("##", "##"), Items.OBSIDIAN);
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRYING_OBSIDIAN_BRICKS, 4,
				Map.of('#', Ingredient.ofItems(Items.CRYING_OBSIDIAN)), List.of("##", "##"), Items.CRYING_OBSIDIAN);
		
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DRYING_RACK, 1, Map.of(
				'#', Ingredient.ofItems(Items.STICK), 's', Ingredient.ofItems(ModItems.STRING_MESH)), List.of("#s#", "# #", "# #"), Items.STRING);
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BASKET, 1, Map.of(
				'#', Ingredient.ofItems(ModItems.WICKER_MESH)), List.of(" # ", "# #", " # "), Items.SUGAR_CANE);
		
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WORKBENCH, 1, Map.of(
				'#', Ingredient.fromTag(ItemTags.PLANKS), 'I', Ingredient.ofItems(Items.IRON_INGOT),
				'S', Ingredient.ofItems(Items.SMOOTH_STONE), 'D', Ingredient.ofItems(Items.DEEPSLATE_TILE_SLAB)), List.of("SDS", "I#I", "###"), Items.IRON_INGOT);
		
		generateForgeRecipes(exporter);
		generateToolRecipes(exporter);
		generateAnvilRecipes(exporter);
		generateMoldSetsRecipes(exporter);
		generatePebbleSetsRecipes(exporter);
		
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PRIMITIVE_FORGE_CORE, 1,
				Map.of('#', Ingredient.ofItems(Blocks.COBBLESTONE), 'o', Ingredient.ofItems(Blocks.COAL_BLOCK), 'b', Ingredient.ofItems(ModItems.STONE_PEBBLES.PEBBLE())),
				List.of("#b#", "bob", "#b#"),  Items.COBBLESTONE);
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BASIC_FORGE_CORE, 1,
				Map.of('#', Ingredient.ofItems(Blocks.BRICKS), 'o', Ingredient.ofItems(Blocks.COAL_BLOCK), 'b', Ingredient.ofItems(Blocks.COPPER_GRATE)),
				List.of("#b#", "bob", "#b#"),  Items.COPPER_INGOT);
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.REFINED_FORGE_CORE, 1,
				Map.of('#', Ingredient.ofItems(Blocks.STONE_BRICKS), 'o', Ingredient.ofItems(Blocks.COAL_BLOCK), 'b', Ingredient.ofItems(Blocks.IRON_BARS)),
				List.of("#b#", "bob", "#b#"),  Items.IRON_INGOT);
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ADVANCED_FORGE_CORE, 1,
				Map.of('#', Ingredient.ofItems(Blocks.NETHER_BRICKS), 'o', Ingredient.ofItems(Blocks.COAL_BLOCK), 'b', Ingredient.ofItems(Blocks.IRON_BARS)),
				List.of("#b#", "bob", "#b#"),  Items.NETHER_BRICKS);
		offerShaped(exporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ETHEREAL_FORGE_CORE, 1,
				Map.of('#', Ingredient.ofItems(ModBlocks.OBSIDIAN_BRICKS), 'o', Ingredient.ofItems(Blocks.COAL_BLOCK), 'b', Ingredient.ofItems(Blocks.IRON_BARS)),
				List.of("#b#", "bob", "#b#"),  ModBlocks.OBSIDIAN_BRICKS);
		
		offerWorkbench(exporter, RecipeCategory.REDSTONE, ModItems.COPPER_COIL, 1,
				Map.of('i', Ingredient.ofItems(Items.IRON_INGOT), 'c', Ingredient.ofItems(Items.COPPER_INGOT)),
				List.of("cic", "cic"), Items.COPPER_INGOT);
		offerWorkbench(exporter, RecipeCategory.REDSTONE, ModBlocks.MOTOR, 1,
				Map.of('#', Ingredient.ofItems(Blocks.COBBLESTONE), 'c', Ingredient.ofItems(ModItems.COPPER_COIL), 'm', Ingredient.ofItems(ModBlocks.MAGNETITE.BLOCK())),
				List.of("c#c", "#m#", "c#c"), Items.COBBLESTONE);
	}
	
	private void generateShapelessRecipes(RecipeExporter exporter) {
		offerShapeless(exporter, RecipeCategory.MISC, ModItems.HANDLE, 1, List.of(Ingredient.ofItems(Items.STICK), Ingredient.ofItems(ModItems.LEATHER_STRAP)), Items.LEATHER);
		
		offerShapeless(exporter, RecipeCategory.MISC, ModItems.LONG_STRING, 1,
				List.of(Ingredient.ofItems(Items.STRING), Ingredient.ofItems(Items.STRING), Ingredient.ofItems(Items.STRING)), Items.STRING);
		
		offerShapeless(exporter, RecipeCategory.MISC, ModItems.KNITTING_KIT, 1,
				List.of(Ingredient.ofItems(ModItems.LONG_STRING), Ingredient.ofItems(ModItems.LONG_STRING),
						Ingredient.ofItems(Items.STICK), Ingredient.ofItems(Items.STICK)), Items.STICK);
		
		offerShapeless(exporter, RecipeCategory.MISC, ModItems.DRY_CANE_BUNDLE, 1,
				List.of(Ingredient.ofItems(ModItems.DRY_CANE), Ingredient.ofItems(ModItems.DRY_CANE),
						Ingredient.ofItems(ModItems.DRY_CANE)), ModItems.DRY_CANE);
		
		offerShapeless(exporter, RecipeCategory.TOOLS, ModItems.FIRE_STARTER, 1, List.of(Ingredient.ofItems(Items.STICK), Ingredient.ofItems(Items.STICK)), Items.STICK);
		offerShapeless(exporter, RecipeCategory.MISC, ModItems.TWINE, 1, List.of(Ingredient.ofItems(ModItems.PLANT_FIBER), Ingredient.ofItems(ModItems.PLANT_FIBER), Ingredient.ofItems(ModItems.PLANT_FIBER)), ModItems.PLANT_FIBER);
		
		offerShapeless(exporter, RecipeCategory.MISC, Items.STICK, 1, List.of(Ingredient.fromTag(ItemTags.SAPLINGS)), Items.STICK);
		
		offerShapeless(exporter, RecipeCategory.FOOD, ModItems.DOUGH, 1, List.of(Ingredient.ofItems(ModItems.FLOUR), Ingredient.ofItems(ModItems.FLOUR)), ModItems.FLOUR);
	}
	
	private void replaceVanillaRecipes(RecipeExporter exporter) {
		replaceShaped(exporter, "furnace", RecipeCategory.BUILDING_BLOCKS, Items.FURNACE, 1,
				Map.of('#', Ingredient.ofItems(Items.BRICKS)), List.of("###", "# #", "###"), Items.BRICK);
		replaceShaped(exporter, "campfire", RecipeCategory.BUILDING_BLOCKS, Items.CAMPFIRE, 1,
				Map.of('#', Ingredient.fromTag(ItemTags.LOGS), '/', Ingredient.ofItems(Items.STICK)), List.of(" / ", "/#/", "###"), Items.STICK);
		replaceShaped(exporter, "crafting_table", RecipeCategory.MISC, Items.CRAFTING_TABLE, 1,
				Map.of('#', Ingredient.fromTag(ItemTags.PLANKS), 'f', Ingredient.ofItems(ModItems.FLINT_SHARD), 's', Ingredient.ofItems(ModItems.SHARP_FLINT)),
				List.of("fs", "##"), Items.FLINT);
		
		replaceShaped(exporter, "chest", RecipeCategory.MISC, Items.CHEST, 1,
				Map.of('#', Ingredient.fromTag(ItemTags.PLANKS), 'i', Ingredient.ofItems(Items.IRON_INGOT)),
				List.of("##i", "# #", "###"), Items.IRON_INGOT);
		replaceShaped(exporter, "barrel", RecipeCategory.MISC, Items.BARREL, 1,
				Map.of('#', Ingredient.fromTag(ItemTags.PLANKS), 'i', Ingredient.ofItems(Items.IRON_INGOT), 's', Ingredient.fromTag(ItemTags.WOODEN_SLABS)),
				List.of("#s#", "i i", "#s#"), Items.IRON_INGOT);
		
		replaceShaped(exporter, "rail", RecipeCategory.BUILDING_BLOCKS, Items.RAIL, 8,
				Map.of('/', Ingredient.ofItems(Items.STICK), '#', Ingredient.ofItems(Items.IRON_INGOT)), List.of("/ /", "/#/", "# #"), Items.IRON_INGOT);
		replaceShaped(exporter, "iron_bars", RecipeCategory.BUILDING_BLOCKS, Items.IRON_BARS, 8,
				Map.of('#', Ingredient.ofItems(Items.IRON_INGOT)), List.of("###", "###"), Items.IRON_INGOT);
		replaceShaped(exporter, "glass_pane", RecipeCategory.BUILDING_BLOCKS, Items.GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.GLASS)), List.of("###", "###"), Items.GLASS);
		
		replaceShaped(exporter, "lime_stained_glass_pane",                  RecipeCategory.BUILDING_BLOCKS, Items.LIME_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.LIME_STAINED_GLASS)),                  List.of("###", "###"), Items.LIME_STAINED_GLASS);
		replaceShaped(exporter, "pink_stained_glass_pane",                  RecipeCategory.BUILDING_BLOCKS, Items.PINK_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.PINK_STAINED_GLASS)),                  List.of("###", "###"), Items.PINK_STAINED_GLASS);
		replaceShaped(exporter, "green_stained_glass_pane",                 RecipeCategory.BUILDING_BLOCKS, Items.GREEN_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.GREEN_STAINED_GLASS)),                 List.of("###", "###"), Items.GREEN_STAINED_GLASS);
		replaceShaped(exporter, "light_gray_stained_glass_pane",            RecipeCategory.BUILDING_BLOCKS, Items.LIGHT_GRAY_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.LIGHT_GRAY_STAINED_GLASS)),            List.of("###", "###"), Items.LIGHT_GRAY_STAINED_GLASS);
		replaceShaped(exporter, "cyan_stained_glass_pane",                  RecipeCategory.BUILDING_BLOCKS, Items.CYAN_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.CYAN_STAINED_GLASS)),                  List.of("###", "###"), Items.CYAN_STAINED_GLASS);
		replaceShaped(exporter, "red_stained_glass_pane",                   RecipeCategory.BUILDING_BLOCKS, Items.RED_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.RED_STAINED_GLASS)),                   List.of("###", "###"), Items.RED_STAINED_GLASS);
		replaceShaped(exporter, "gray_stained_glass_pane",                  RecipeCategory.BUILDING_BLOCKS, Items.GRAY_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.GRAY_STAINED_GLASS)),                  List.of("###", "###"), Items.GRAY_STAINED_GLASS);
		replaceShaped(exporter, "brown_stained_glass_pane",                 RecipeCategory.BUILDING_BLOCKS, Items.BROWN_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.BROWN_STAINED_GLASS)),                 List.of("###", "###"), Items.BROWN_STAINED_GLASS);
		replaceShaped(exporter, "purple_stained_glass_pane",                RecipeCategory.BUILDING_BLOCKS, Items.PURPLE_STAINED_GLASS_PANE, 8, 
				Map.of('#', Ingredient.ofItems(Items.PURPLE_STAINED_GLASS)),                List.of("###", "###"), Items.PURPLE_STAINED_GLASS);
		replaceShaped(exporter, "magenta_stained_glass_pane",               RecipeCategory.BUILDING_BLOCKS, Items.MAGENTA_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.MAGENTA_STAINED_GLASS)),               List.of("###", "###"), Items.MAGENTA_STAINED_GLASS);
		replaceShaped(exporter, "black_stained_glass_pane",                 RecipeCategory.BUILDING_BLOCKS, Items.BLACK_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.BLACK_STAINED_GLASS)),                 List.of("###", "###"), Items.BLACK_STAINED_GLASS);
		replaceShaped(exporter, "white_stained_glass_pane",                 RecipeCategory.BUILDING_BLOCKS, Items.WHITE_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.WHITE_STAINED_GLASS)),                 List.of("###", "###"), Items.WHITE_STAINED_GLASS);
		replaceShaped(exporter, "light_blue_stained_glass_pane",            RecipeCategory.BUILDING_BLOCKS, Items.LIGHT_BLUE_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.LIGHT_BLUE_STAINED_GLASS)),            List.of("###", "###"), Items.LIGHT_BLUE_STAINED_GLASS);
		replaceShaped(exporter, "yellow_stained_glass_pane",                RecipeCategory.BUILDING_BLOCKS, Items.YELLOW_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.YELLOW_STAINED_GLASS)),                List.of("###", "###"), Items.YELLOW_STAINED_GLASS);
		replaceShaped(exporter, "orange_stained_glass_pane",                RecipeCategory.BUILDING_BLOCKS, Items.ORANGE_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.ORANGE_STAINED_GLASS)),                List.of("###", "###"), Items.ORANGE_STAINED_GLASS);
		replaceShaped(exporter, "blue_stained_glass_pane",                  RecipeCategory.BUILDING_BLOCKS, Items.BLUE_STAINED_GLASS_PANE, 8,
				Map.of('#', Ingredient.ofItems(Items.BLUE_STAINED_GLASS)),                  List.of("###", "###"), Items.BLUE_STAINED_GLASS);
		
	}
	
	private void generateCrushingRecipes(RecipeExporter exporter) {
		offerCrushing(exporter, "flour", Ingredient.ofItems(Items.WHEAT), ModItems.FLOUR, 1, 200);
	}
	
	@Override
	public void generate(RecipeExporter exporter) {
		replaceVanillaRecipes(exporter);
		
		generateVanillaMaterialRecipes(exporter);
		generateMaterialRecipes(exporter);
		
		generateShapedRecipes(exporter);
		generateShapelessRecipes(exporter);
		
		generateDryingRecipes(exporter);
		generateCookingRecipes(exporter);
		generateToolingRecipes(exporter);
		generateCrushingRecipes(exporter);
	}
}