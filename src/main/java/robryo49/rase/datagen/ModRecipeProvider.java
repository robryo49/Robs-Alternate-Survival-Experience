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
import robryo49.rase.datagen.builders.AnvilSmithingRecipeJsonBuilder;
import robryo49.rase.datagen.builders.ForgeSmeltingRecipeJsonBuilder;
import robryo49.rase.datagen.builders.ToolingRecipeJsonBuilder;
import robryo49.rase.item.ModItems;
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
	
	// --- Cooking Station Helpers ---
	
	public static <T extends AbstractCookingRecipe> void offerCooking(RecipeExporter exporter, String cooker, RecipeSerializer<T> serializer,
	                                                                  AbstractCookingRecipe.RecipeFactory<T> factory, RecipeCategory category,
	                                                                  int time, ItemConvertible input, ItemConvertible output, float exp) {
		CookingRecipeJsonBuilder.create(Ingredient.ofItems(input), category, output, exp, time, serializer, factory)
				.criterion(hasItem(input), conditionsFromItem(input))
				.offerTo(exporter, Rase.getIdentifier(getItemPath(output) + "_from_" + cooker));
	}
	
	public static void offerAllCooking(RecipeExporter exporter, RecipeCategory category, ItemConvertible input, ItemConvertible output, float exp, boolean furnace, boolean smoker, boolean campfire, boolean blasting) {
		if (furnace) offerCooking(exporter, "smelting", RecipeSerializer.SMELTING, SmeltingRecipe::new, category, 200, input, output, exp);
		if (smoker) offerCooking(exporter, "smoking", RecipeSerializer.SMOKING, SmokingRecipe::new, category, 100, input, output, exp);
		if (campfire) offerCooking(exporter, "campfire", RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new, category, 600, input, output, exp);
		if (blasting) offerCooking(exporter, "blasting", RecipeSerializer.BLASTING, BlastingRecipe::new, category, 100, input, output, exp);
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
		offerShaped(exporter, RecipeCategory.MISC, anvil, 1, Map.of('#', Ingredient.ofItems(block), '-', Ingredient.ofItems(ingot)), List.of("###", " - ", "---"), ingot);
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
		String materialName = toolSet.materialName();
		
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
		offerShaped(exporter, RecipeCategory.TOOLS, armorSet.HELMET(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("###", "# #"), sourceItem);
		offerShaped(exporter, RecipeCategory.TOOLS, armorSet.CHESTPLATE(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("# #", "###", "###"), sourceItem);
		offerShaped(exporter, RecipeCategory.TOOLS, armorSet.LEGGINGS(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("###", "# #", "# #"), sourceItem);
		offerShaped(exporter, RecipeCategory.TOOLS, armorSet.BOOTS(), 1, Map.of('#', Ingredient.ofItems(sourceItem)), List.of("# #", "# #"), sourceItem);
	}
	
	
	public static void offerMoldSetRecipes(RecipeExporter exporter, ModItems.MoldSet moldSet) {
		if (moldSet.FROM_SMELTING() instanceof ModItems.MoldSet unsmelted) {
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.BASE(), moldSet.BASE(), 0.35f, true, false, true, false);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.INGOT(), moldSet.INGOT(), 0.35f, true, false, true, false);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.AXE(), moldSet.AXE(), 0.35f, true, false, true, false);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.PICKAXE(), moldSet.PICKAXE(), 0.35f, true, false, true, false);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.SWORD(), moldSet.SWORD(), 0.35f, true, false, true, false);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.SHOVEL(), moldSet.SHOVEL(), 0.35f, true, false, true, false);
			offerAllCooking(exporter, RecipeCategory.TOOLS, unsmelted.HOE(), moldSet.HOE(), 0.35f, true, false, true, false);
		} else if (moldSet.INGREDIENT() != null){
			offerShaped(exporter, RecipeCategory.TOOLS, moldSet.BASE(), 4, Map.of('#', Ingredient.ofItems(moldSet.INGREDIENT())), List.of("###", "# #", "###"), moldSet.INGREDIENT());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.INGOT(), 1, List.of(Ingredient.ofItems(Items.BRICK), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.AXE(), 1, List.of(Ingredient.fromTag(ModItemTags.AXE_HEADS), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.PICKAXE(), 1, List.of(Ingredient.fromTag(ModItemTags.PICKAXE_HEADS), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.SWORD(), 1, List.of(Ingredient.fromTag(ModItemTags.SWORD_BLADES), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.SHOVEL(), 1, List.of(Ingredient.fromTag(ModItemTags.SHOVEL_HEADS), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
			offerShapeless(exporter, RecipeCategory.TOOLS, moldSet.HOE(), 1, List.of(Ingredient.fromTag(ModItemTags.HOE_HEADS), Ingredient.ofItems(moldSet.BASE())), moldSet.BASE());
		}
	}
	
	public static void offerHideSetRecipes(RecipeExporter exporter, ModItems.HideSet hideSet) {
		offerTooling(exporter, hideSet.entity().getUntranslatedName() + "_hide_cutting", Ingredient.fromTag(ItemTags.SWORDS), hideSet.HIDE(), Items.LEATHER, 1);
	}
	
	public static void offerPebbleSetRecipes(RecipeExporter exporter, ModItems.PebbleSet pebbleSet) {
		offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, pebbleSet.RECONSTRUCTED_BLOCK(), pebbleSet.PEBBLE());
	}
	
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, ModItems.OreMaterialSet materialSet, ModBlocks.OreBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, tier, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, ModItems.OreMaterialSet materialSet, ModBlocks.OreBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		
		String materialName = materialSet.name();
		Item ingot = materialSet.INGOT(), nugget = materialSet.NUGGET(), raw = materialSet.RAW();
		Block ore = blockSet.ORE(), deepslateOre = blockSet.DEEPSLATE_ORE(), block = blockSet.BLOCK(), rawBlock = blockSet.RAW_BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		offerForging(exporter, materialName + "_ingot_from_raw_ore", List.of(Ingredient.ofItems(raw)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		offerForging(exporter, materialName + "_ingot_from_ore", List.of(Ingredient.ofItems(ore)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		offerForging(exporter, materialName + "_ingot_from_deepslate_ore", List.of(Ingredient.ofItems(deepslateOre)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		
		offerIngotCompacting(exporter, ingot, block, nugget);
		offerRawCompacting(exporter, raw, rawBlock);
		
		if (toolSet != null) offerToolSetRecipes(exporter, ingot, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, ingot, armorSet);
		
	}
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, ModItems.OreMaterialSet materialSet, ModBlocks.NetherOreBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, tier, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, ModItems.OreMaterialSet materialSet, ModBlocks.NetherOreBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		
		String materialName = materialSet.name();
		Item ingot = materialSet.INGOT(), nugget = materialSet.NUGGET(), raw = materialSet.RAW();
		Block ore = blockSet.NETHER_ORE(), block = blockSet.BLOCK(), rawBlock = blockSet.RAW_BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		offerForging(exporter, materialName + "_ingot_from_raw_ore", List.of(Ingredient.ofItems(raw)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		offerForging(exporter, materialName + "_ingot_from_ore", List.of(Ingredient.ofItems(ore)),
				ingot, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		
		offerIngotCompacting(exporter, ingot, block, nugget);
		offerRawCompacting(exporter, raw, rawBlock);
		
		if (toolSet != null) offerToolSetRecipes(exporter, ingot, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, ingot, armorSet);
		
	}
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, List<Ingredient> ingredients, ModItems.AlloyMaterialSet materialSet, ModBlocks.AlloyBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, tier, ingredients, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, List<Ingredient> ingredients, ModItems.AlloyMaterialSet materialSet, ModBlocks.AlloyBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		
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
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, ModItems.CrystalMaterialSet materialSet, ModBlocks.CrystalBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, tier, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, ModItems.CrystalMaterialSet materialSet, ModBlocks.CrystalBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		
		String materialName = materialSet.name();
		Item crystal = materialSet.CRYSTAL();
		Block ore = blockSet.ORE(), deepslateOre = blockSet.DEEPSLATE_ORE(), block = blockSet.BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		offerCrystalCompacting(exporter, crystal, block);
		
		if (toolSet != null) offerToolSetRecipes(exporter, crystal, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, crystal, armorSet);
	}
	
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, ModItems.CrystalMaterialSet materialSet, ModBlocks.NetherCrystalBlockSet blockSet) {
		offerMaterialSetRecipes(exporter, tier, materialSet, blockSet, null, null);
	}
	public static void offerMaterialSetRecipes(RecipeExporter exporter, int tier, ModItems.CrystalMaterialSet materialSet, ModBlocks.NetherCrystalBlockSet blockSet,
	                                           @Nullable ModItems.ArmorSet armorSet, @Nullable ModItems.ToolSet toolSet) {
		
		String materialName = materialSet.name();
		Item crystal = materialSet.CRYSTAL();
		Block ore = blockSet.NETHER_ORE(), block = blockSet.BLOCK();
		
		int smeltingTime = 200 * tier, coolingTime = 200 * tier;
		float xp = 1.2f * tier;
		
		offerForging(exporter, materialName + "_ingot_from_ore", List.of(Ingredient.ofItems(ore)),
				crystal, ModItemTags.INGOT_MOLDS, smeltingTime, coolingTime, tier, xp);
		
		offerCrystalCompacting(exporter, crystal, block);
		
		if (toolSet != null) offerToolSetRecipes(exporter, crystal, toolSet, smeltingTime, coolingTime, tier, xp);
		if (armorSet != null) offerArmorSetRecipes(exporter, crystal, armorSet);
	}
	
	// --- Main Generation ---
	
	private void generateMoldSetsRecipes(RecipeExporter exporter) {
		ModItems.MOLD_SETS.forEach(moldSet -> offerMoldSetRecipes(exporter, moldSet));
	}
	
	private void generateHideSetsRecipes(RecipeExporter exporter) {
		ModItems.HIDE_SETS.forEach((hideSet -> offerHideSetRecipes(exporter, hideSet)));
	}
	
	private void generatePebbleSetsRecipes(RecipeExporter exporter) {
		ModItems.PEBBLE_SETS.forEach(pebbleSet -> offerPebbleSetRecipes(exporter, pebbleSet));
	}
	
	private void generateMaterialRecipes(RecipeExporter exporter) {
		
		offerMaterialSetRecipes(exporter, 1, ModItems.TIN, ModBlocks.TIN);
		offerMaterialSetRecipes(exporter, 1, ModItems.ZINC, ModBlocks.ZINC);
		offerMaterialSetRecipes(exporter, 1, ModItems.MAGNETITE, ModBlocks.MAGNETITE);
		
		offerMaterialSetRecipes(exporter, 1,
				List.of(Ingredient.ofItems(Items.COPPER_INGOT), Ingredient.ofItems(Items.COPPER_INGOT),
						Ingredient.ofItems(Items.COPPER_INGOT), Ingredient.ofItems(ModItems.TIN.INGOT())),
				ModItems.BRONZE, ModBlocks.BRONZE, ModItems.BRONZE_ARMOR_SET, ModItems.BRONZE_TOOL_SET);
		
		offerMaterialSetRecipes(exporter, 1, ModItems.SILVER, ModBlocks.SILVER, ModItems.SILVER_ARMOR_SET, ModItems.SILVER_TOOL_SET);
		offerMaterialSetRecipes(exporter, 1, ModItems.LEAD, ModBlocks.LEAD);
		
		offerMaterialSetRecipes(exporter, 1,
				List.of(Ingredient.ofItems(Items.IRON_INGOT), Ingredient.ofItems(Items.IRON_INGOT), Ingredient.ofItems(Items.COAL)),
				ModItems.STEEL, ModBlocks.STEEL, ModItems.STEEL_ARMOR_SET, ModItems.STEEL_TOOL_SET);
		
		offerMaterialSetRecipes(exporter, 1, ModItems.TITANIUM, ModBlocks.TITANIUM, ModItems.TITANIUM_ARMOR_SET, ModItems.TITANIUM_TOOL_SET);
		offerMaterialSetRecipes(exporter, 1, ModItems.PLATINUM, ModBlocks.PLATINUM);
		offerMaterialSetRecipes(exporter, 1, ModItems.TUNGSTEN, ModBlocks.TUNGSTEN);
		offerMaterialSetRecipes(exporter, 1, ModItems.PALLADIUM, ModBlocks.PALLADIUM);
		offerMaterialSetRecipes(exporter, 1, ModItems.COBALT, ModBlocks.COBALT, ModItems.COBALT_ARMOR_SET, ModItems.COBALT_TOOL_SET);
		
		offerMaterialSetRecipes(exporter, 1,
				List.of(Ingredient.ofItems(ModItems.PALLADIUM.INGOT()), Ingredient.ofItems(ModItems.PLATINUM.INGOT())),
				ModItems.SCANDIUM, ModBlocks.SCANDIUM, ModItems.SCANDIUM_ARMOR_SET, ModItems.SCANDIUM_TOOL_SET);
		
		offerMaterialSetRecipes(exporter, 1, ModItems.MYTHRIL, ModBlocks.MYTHRIL, ModItems.MYTHRIL_ARMOR_SET, ModItems.MYTHRIL_TOOL_SET);
		offerMaterialSetRecipes(exporter, 1, ModItems.RHEXIS, ModBlocks.RHEXIS);
		
		
	}
	
	private void generateToolRecipes(RecipeExporter exporter) {
		offerStartingToolSetsRecipes(exporter);
		offerShapeless(exporter, RecipeCategory.TOOLS, ModItems.FIRE_STARTER, 1, List.of(Ingredient.ofItems(Items.STICK), Ingredient.ofItems(Items.STICK)), Items.STICK);
		offerShapeless(exporter, RecipeCategory.MISC, ModItems.TWINE, 1, List.of(Ingredient.ofItems(ModItems.PLANT_FIBER), Ingredient.ofItems(ModItems.PLANT_FIBER), Ingredient.ofItems(ModItems.PLANT_FIBER)), ModItems.PLANT_FIBER);
	}
	
	private void generateItemRecipes(RecipeExporter exporter) {
		offerShapeless(exporter, RecipeCategory.MISC, ModItems.HANDLE, 1, List.of(Ingredient.ofItems(Items.STICK), Ingredient.ofItems(ModItems.LEATHER_STRAP)), Items.LEATHER);
		offerShapeless(exporter, RecipeCategory.MISC, Items.STRING, 4, List.of(Ingredient.fromTag(ItemTags.WOOL)), Items.WHITE_WOOL);
		
		offerTooling(exporter, "leather_cutting", Ingredient.fromTag(ItemTags.SWORDS), Items.LEATHER, ModItems.LEATHER_STRAP, 2);
		offerTooling(exporter, "flint_cutting", Ingredient.fromTag(ItemTags.SWORDS), Items.FLINT, ModItems.SHARP_FLINT, 1);
		
		replaceShaped(exporter, "crafting_table", RecipeCategory.MISC, Items.CRAFTING_TABLE, 1,
				Map.of('#', Ingredient.fromTag(ItemTags.PLANKS), 'f', Ingredient.ofItems(ModItems.FLINT_SHARD), 's', Ingredient.ofItems(ModItems.SHARP_FLINT)),
				List.of("fs", "##"), Items.FLINT);
	}
	
	private void generateAnvilRecipes(RecipeExporter exporter) {
		offerAnvilRecipe(exporter, ModItems.STONE_PEBBLES.PEBBLE(), Blocks.STONE, ModBlocks.STONE_ANVIL.NORMAL());
		offerAnvilRecipe(exporter, ModItems.LEAD.INGOT(), ModBlocks.LEAD.BLOCK(), ModBlocks.LEAD_ANVIL.NORMAL());
		offerAnvilRecipe(exporter, ModItems.TITANIUM.INGOT(), ModBlocks.TITANIUM.BLOCK(), ModBlocks.TITANIUM_ANVIL.NORMAL());
		offerAnvilRecipe(exporter, ModItems.TUNGSTEN.INGOT(), ModBlocks.TUNGSTEN.BLOCK(), ModBlocks.TUNGSTEN_ANVIL.NORMAL());
	}
	
	private void generateFoodRecipes(RecipeExporter exporter) {
		ModItems.MEAT_SETS.forEach(meat -> offerAllCooking(exporter, RecipeCategory.FOOD, meat.RAW(), meat.COOKED(), 0.35f, true, true, true, false));
	}
	
	@Override
	public void generate(RecipeExporter exporter) {
		generateMaterialRecipes(exporter);
		generateToolRecipes(exporter);
		generateAnvilRecipes(exporter);
		
		generateItemRecipes(exporter);
		
		generateFoodRecipes(exporter);
		generateHideSetsRecipes(exporter);
		
		generateMoldSetsRecipes(exporter);
		
		generatePebbleSetsRecipes(exporter);
	}
}