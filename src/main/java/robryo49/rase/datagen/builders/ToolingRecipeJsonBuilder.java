package robryo49.rase.datagen.builders;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.recipe.custom.ToolingRecipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToolingRecipeJsonBuilder {
	private final CraftingRecipeCategory category;
	private final Ingredient tool;
	private final Ingredient input;
	private final Item output;
	private final int count;
	private final Map<String, AdvancementCriterion<?>> advancementBuilder = new LinkedHashMap<>();
	
	public ToolingRecipeJsonBuilder(CraftingRecipeCategory category, Ingredient tool, Ingredient input, ItemConvertible output, int count) {
		this.category = category;
		this.tool = tool;
		this.input = input;
		this.output = output.asItem();
		this.count = count;
	}
	
	public static ToolingRecipeJsonBuilder create(CraftingRecipeCategory category, Ingredient tool, Ingredient input, ItemConvertible output, int count) {
		return new ToolingRecipeJsonBuilder(category, tool, input, output, count);
	}
	
	public ToolingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
		this.advancementBuilder.put(name, criterion);
		return this;
	}
	
	public void offerTo(RecipeExporter exporter, String name) {
		Identifier recipeId = Rase.getIdentifier(name);
		this.validate(recipeId);
		
		Advancement.Builder builder = exporter.getAdvancementBuilder()
				.rewards(AdvancementRewards.Builder.recipe(recipeId))
				.criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
		
		this.advancementBuilder.forEach(builder::criterion);
		
		ToolingRecipe recipe = new ToolingRecipe(category, tool, input, new ItemStack(output, count));
		
		exporter.accept(recipeId, recipe, builder.build(recipeId.withPrefixedPath("recipes/" + this.category.name().toLowerCase() + "/")));
	}
	
	private void validate(Identifier recipeId) {
		if (this.advancementBuilder.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		}
	}
}
