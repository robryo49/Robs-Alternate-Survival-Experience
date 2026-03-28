package robryo49.rase.datagen.builders;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.recipe.custom.ForgeRecipe;
import robryo49.rase.util.ModItemTags;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ForgeSmeltingRecipeJsonBuilder {
	
	private final List<Ingredient> inputs = new ArrayList<>();
	private ItemStack result = ItemStack.EMPTY;
	private TagKey<Item> moldTagKey = ModItemTags.MOLDS;
	private int smeltingTime = 200;
	private int coolingTime = 200;
	private int minTier = 1;
	private float experience = 0.0f;
	
	private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
	
	public static ForgeSmeltingRecipeJsonBuilder create(Item result) {
		return new ForgeSmeltingRecipeJsonBuilder().result(result);
	}
	
	public ForgeSmeltingRecipeJsonBuilder input(Item item) {
		if (inputs.size() < 4) {
			inputs.add(Ingredient.ofItems(item));
		}
		return this;
	}
	
	public ForgeSmeltingRecipeJsonBuilder input(Ingredient ingredient) {
		if (inputs.size() < 4) {
			inputs.add(ingredient);
		}
		return this;
	}
	
	public ForgeSmeltingRecipeJsonBuilder result(Item item) {
		this.result = new ItemStack(item);
		return this;
	}
	
	public ForgeSmeltingRecipeJsonBuilder mold(TagKey<Item> moldTagKey) {
		this.moldTagKey = moldTagKey;
		return this;
	}
	
	public ForgeSmeltingRecipeJsonBuilder smeltingTime(int ticks) {
		this.smeltingTime = ticks;
		return this;
	}
	
	public ForgeSmeltingRecipeJsonBuilder coolingTime(int ticks) {
		this.coolingTime = ticks;
		return this;
	}
	
	public ForgeSmeltingRecipeJsonBuilder minTier(int tier) {
		this.minTier = tier;
		return this;
	}
	
	public ForgeSmeltingRecipeJsonBuilder experience(float experience) {
		this.experience = experience;
		return this;
	}
	
	public ForgeSmeltingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}
	
	public void offerTo(RecipeExporter exporter, String name) {
		Identifier identifier = Rase.getIdentifier(name);
		
		Advancement.Builder advancementBuilder = exporter.getAdvancementBuilder()
				.criterion("has_the_recipe", InventoryChangedCriterion.Conditions.items(inputs.getFirst().getMatchingStacks()[0].getItem()));
		
		this.criteria.forEach(advancementBuilder::criterion);
		
		ForgeRecipe recipe = new ForgeRecipe(
				new ArrayList<>(inputs),
				result,
				moldTagKey,
				smeltingTime,
				coolingTime,
				minTier,
				experience
		);
		
		exporter.accept(
				identifier,
				recipe,
				advancementBuilder.build(identifier.withPrefixedPath("recipes/forge_smelting/"))
		);
	}
}