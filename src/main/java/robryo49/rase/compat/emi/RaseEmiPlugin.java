package robryo49.rase.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.compat.emi.handlers.AnvilSmithingEmiRecipeHandler;
import robryo49.rase.compat.emi.handlers.ForgeEmiRecipeHandler;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.recipe.custom.AnvilSmithingRecipe;
import robryo49.rase.recipe.custom.ForgeRecipe;

import net.minecraft.item.Items;
import robryo49.rase.screen.ModScreenHandlers;


public class RaseEmiPlugin implements EmiPlugin {
	
	private static final EmiStack FORGE_ICON =
			EmiStack.of(ModBlocks.PRIMITIVE_FORGE);
	private static final EmiStack ANVIL_ICON =
			EmiStack.of(Items.ANVIL);
	
	public static final EmiRecipeCategory FORGE_CATEGORY = new EmiRecipeCategory(
			Rase.getIdentifier("forge"), FORGE_ICON
	);
	public static final EmiRecipeCategory ANVIL_SMITHING_CATEGORY = new EmiRecipeCategory(
			Rase.getIdentifier("anvil_smithing"), ANVIL_ICON
	);
	
	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(FORGE_CATEGORY);
		registry.addCategory(ANVIL_SMITHING_CATEGORY);
		
		
		registry.addWorkstation(FORGE_CATEGORY, EmiStack.of(ModBlocks.PRIMITIVE_FORGE));
		registry.addWorkstation(FORGE_CATEGORY, EmiStack.of(ModBlocks.ADVANCED_FORGE));
		
		registry.addWorkstation(ANVIL_SMITHING_CATEGORY, EmiStack.of(ModBlocks.STONE_ANVIL.NORMAL()));
		
		for (RecipeEntry<ForgeRecipe> entry :
				registry.getRecipeManager().listAllOfType(ModRecipes.FORGE_RECIPE_TYPE)) {
			registry.addRecipe(new ForgeEmiRecipe(entry.value(), entry.id()));
		}
		
		for (RecipeEntry<AnvilSmithingRecipe> entry :
				registry.getRecipeManager().listAllOfType(ModRecipes.ANVIL_SMITHING_RECIPE_TYPE)) {
			registry.addRecipe(new AnvilSmithingEMIRecipe(entry.value(), entry.id()));
		}
		
		
		registry.addRecipeHandler(ScreenHandlerType.ANVIL, new AnvilSmithingEmiRecipeHandler());
		registry.addRecipeHandler(ModScreenHandlers.FORGE_SCREEN_HANDLER, new ForgeEmiRecipeHandler());
		
		

	}
}