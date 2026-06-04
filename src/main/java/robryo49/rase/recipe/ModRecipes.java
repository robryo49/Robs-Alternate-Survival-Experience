package robryo49.rase.recipe;

import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import robryo49.rase.Rase;
import robryo49.rase.recipe.custom.*;

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
	
	
	public static RecipeSerializer<DryingRecipe> DRYING_RECIPE_SERIALIZER = Registry.register(
			Registries.RECIPE_SERIALIZER, Rase.getIdentifier("drying"), new DryingRecipe.Serializer()
	);
	
	public static RecipeType<DryingRecipe> DRYING_RECIPE_TYPE = Registry.register(
			Registries.RECIPE_TYPE, Rase.getIdentifier("drying"),
			new RecipeType<DryingRecipe>() {
				@Override public String toString() { return "drying"; }
			}
	);
	
	
	public static RecipeSerializer<CrushingRecipe> CRUSHING_RECIPE_SERIALIZER = Registry.register(
			Registries.RECIPE_SERIALIZER, Rase.getIdentifier("crushing"), new CrushingRecipe.Serializer()
	);
	
	public static RecipeType<CrushingRecipe> CRUSHING_RECIPE_TYPE = Registry.register(
			Registries.RECIPE_TYPE, Rase.getIdentifier("crushing"),
			new RecipeType<CrushingRecipe>() {
				@Override public String toString() { return "crushing"; }
			}
	);
	
	
	public static RecipeSerializer<WorkbenchRecipe> WORKBENCH_RECIPE_SERIALIZER = Registry.register(
			Registries.RECIPE_SERIALIZER, Rase.getIdentifier("workbench"), new WorkbenchRecipe.Serializer()
	);
	
	public static RecipeType<WorkbenchRecipe> WORKBENCH_RECIPE_TYPE = Registry.register(
			Registries.RECIPE_TYPE, Rase.getIdentifier("workbench"),
			new RecipeType<WorkbenchRecipe>() {
				@Override
				public String toString() { return "workbench"; }
			}
	);
	
	
	public static void registerModRecipes() {
		Rase.LOGGER.info("Registering Custom Recipes for " + Rase.MOD_ID);
	}
}