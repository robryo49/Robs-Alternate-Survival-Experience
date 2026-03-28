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
import robryo49.rase.recipe.custom.DryingRecipe;
import robryo49.rase.recipe.custom.ToolingRecipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class DryingRecipeJsonBuilder {
	private final CraftingRecipeCategory category;
	private final Ingredient input;
	private final Item output;
	private final int dryingTime;
	private final Map<String, AdvancementCriterion<?>> advancementBuilder = new LinkedHashMap<>();
	
	public DryingRecipeJsonBuilder(CraftingRecipeCategory category, Ingredient input, ItemConvertible output, int dryingTime) {
		this.category = category;
		this.input = input;
		this.output = output.asItem();
		this.dryingTime = dryingTime;
	}
	
	public static DryingRecipeJsonBuilder create(CraftingRecipeCategory category, Ingredient input, ItemConvertible output, int dryingTime) {
		return new DryingRecipeJsonBuilder(category, input, output, dryingTime);
	}
	
	public DryingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
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
		
		DryingRecipe recipe = new DryingRecipe(input, new ItemStack(output, 1), dryingTime);
		
		exporter.accept(recipeId, recipe, builder.build(recipeId.withPrefixedPath("recipes/" + this.category.name().toLowerCase() + "/")));
	}
	
	private void validate(Identifier recipeId) {
		if (this.advancementBuilder.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		}
	}
}
