package robryo49.rase.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

public class ModRecipes {
	
	public static RecipeSerializer<ForgeRecipe> FORGE_SERIALIZER = Registry.register(
			Registries.RECIPE_SERIALIZER, Identifier.of(Rase.MOD_ID, "forge"), new ForgeRecipe.Serializer()
	);
	
	public static RecipeType<ForgeRecipe> FORGE_RECIPE_TYPE = Registry.register(
			Registries.RECIPE_TYPE, Identifier.of(Rase.MOD_ID, "forge"),
			new RecipeType<ForgeRecipe>() {
				@Override
				public String toString() {
					return "forge";
				}
			}
	);
	
	public static void registerRecipes() {
		Rase.LOGGER.info("Registering Custom Recipes for " + Rase.MOD_ID);
	}
}