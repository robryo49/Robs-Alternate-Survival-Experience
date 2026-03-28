package robryo49.rase.recipe;

import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.recipe.custom.AnvilSmithingRecipe;
import robryo49.rase.recipe.custom.ForgeRecipe;
import robryo49.rase.recipe.custom.ToolingRecipe;

public class ModRecipes {
	
	public static final RecipeSerializer<ToolingRecipe> TOOLING_RECIPE_SERIALIZER = Registry.register(
			Registries.RECIPE_SERIALIZER, Rase.getIdentifier("tooling"), new ToolingRecipe.Serializer()
	);
	
	
	public static RecipeSerializer<ForgeRecipe> FORGE_RECIPE_SERIALIZER = Registry.register(
			Registries.RECIPE_SERIALIZER, Rase.getIdentifier("forging"), new ForgeRecipe.Serializer()
	);
	
	public static RecipeType<ForgeRecipe> FORGE_RECIPE_TYPE = Registry.register(
			Registries.RECIPE_TYPE, Rase.getIdentifier("forging"),
			new RecipeType<ForgeRecipe>() {
				@Override
				public String toString() {
					return "forging";
				}
			}
	);
	
	
	public static RecipeSerializer<AnvilSmithingRecipe> ANVIL_SMITHING_SERIALIZER = Registry.register(
			Registries.RECIPE_SERIALIZER, Rase.getIdentifier("anvil_smithing"), new AnvilSmithingRecipe.Serializer()
	);
	
	public static RecipeType<AnvilSmithingRecipe> ANVIL_SMITHING_RECIPE_TYPE = Registry.register(
			Registries.RECIPE_TYPE, Rase.getIdentifier("anvil_smithing"),
			new RecipeType<AnvilSmithingRecipe>() {
				@Override
				public String toString() {
					return "anvil_smithing";
				}
			}
	);
	
	
	public static void registerRecipes() {
		Rase.LOGGER.info("Registering Custom Recipes for " + Rase.MOD_ID);
	}
}