package robryo49.rase.datagen;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.recipe.ForgeRecipe;
import robryo49.rase.util.ModItemTags;

import java.util.ArrayList;
import java.util.List;

public class ForgeRecipeJsonBuilder {
	
	private final List<Ingredient> ingredients = new ArrayList<>();
	private ItemStack result = ItemStack.EMPTY.copy();
	private TagKey<Item> moldTagKey = ModItemTags.MOLDS;
	private int smeltingTime = 200;
	private int coolingTime = 200;
	private int minTier = 1;
	private float experience = 0.0f;
	
	public static ForgeRecipeJsonBuilder create() {
		return new ForgeRecipeJsonBuilder();
	}
	
	public ForgeRecipeJsonBuilder ingredient(Item item) {
		if (ingredients.size() < 4) {
			ingredients.add(Ingredient.ofItems(item));
		}
		return this;
	}
	
	public ForgeRecipeJsonBuilder ingredient(Ingredient ingredient) {
		if (ingredients.size() < 4) {
			ingredients.add(ingredient);
		}
		return this;
	}
	
	public ForgeRecipeJsonBuilder result(Item item) {
		this.result = new ItemStack(item);
		return this;
	}
	
	public ForgeRecipeJsonBuilder mold(TagKey<Item> moldTagKey) {
		this.moldTagKey = moldTagKey;
		return this;
	}
	
	public ForgeRecipeJsonBuilder smeltingTime(int ticks) {
		this.smeltingTime = ticks;
		return this;
	}
	
	public ForgeRecipeJsonBuilder coolingTime(int ticks) {
		this.coolingTime = ticks;
		return this;
	}
	
	public ForgeRecipeJsonBuilder minTier(int tier) {
		this.minTier = tier;
		return this;
	}
	
	public ForgeRecipeJsonBuilder experience(float experience) {
		this.experience = experience;
		return this;
	}
	
	public void offerTo(RecipeExporter exporter, String name) {
		ForgeRecipe recipe = new ForgeRecipe(
				new ArrayList<>(ingredients),  // defensive copy
				result,
				moldTagKey,
				smeltingTime,
				coolingTime,
				minTier,
				experience
		);
		exporter.accept(
				Identifier.of(Rase.MOD_ID, name),
				recipe,
				null
		);
	}
}