package robryo49.rase.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerType;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.compat.emi.handlers.AnvilSmithingEmiRecipeHandler;
import robryo49.rase.compat.emi.handlers.ForgeEmiRecipeHandler;
import robryo49.rase.compat.emi.recipe.*;
import robryo49.rase.item.ModItems;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.recipe.custom.AnvilSmithingRecipe;
import robryo49.rase.recipe.custom.DryingRecipe;
import robryo49.rase.recipe.custom.ForgeRecipe;

import net.minecraft.item.Items;
import robryo49.rase.screen.ModScreenHandlers;

import java.util.Map;


public class RaseEmiPlugin implements EmiPlugin {
	
	
	public static final EmiRecipeCategory FORGE_CATEGORY = new EmiRecipeCategory(
			Rase.getIdentifier("forge"), EmiStack.of(ModBlocks.PRIMITIVE_FORGE)
	);
	public static final EmiRecipeCategory ANVIL_SMITHING_CATEGORY = new EmiRecipeCategory(
			Rase.getIdentifier("anvil_smithing"), EmiStack.of(Items.ANVIL)
	);
	
	public static final EmiRecipeCategory FORGE_STRUCTURE_CATEGORY = new EmiRecipeCategory(
			Rase.getIdentifier("forge_structure"),
			EmiStack.of(ModBlocks.PRIMITIVE_FORGE)
	);
	
	public static final EmiRecipeCategory DRYING_CATEGORY = new EmiRecipeCategory(
			Rase.getIdentifier("drying"),
			EmiStack.of(ModBlocks.DRYING_RACK)
	);
	
	public static final EmiRecipeCategory ITEM_USE_CATEGORY = new EmiRecipeCategory(
			Rase.getIdentifier("item_use"),
			EmiStack.of(Blocks.CHEST)
	);
	
	private static EmiIngredient damagedTool(EmiIngredient tool) {
		for (EmiStack stack : tool.getEmiStacks()) {
			ItemStack is = stack.getItemStack().copy();
			is.setDamage(1);
			stack.setRemainder(EmiStack.of(is));
		}
		return tool;
	}
	
	private static void addLogCutting(EmiRegistry registry, TagKey<Block> tagKey, Block planks) {
		registry.addRecipe(EmiWorldInteractionRecipe.builder()
				.id(Rase.getIdentifier("/world/log_cutting/" + tagKey.id().getPath()))
				.leftInput(EmiIngredient.of(tagKey))
				.rightInput(damagedTool(EmiIngredient.of(ItemTags.AXES)), true)
				.output(EmiStack.of(planks, 2))
				.build());
	}
	
	@Override
	public void register(EmiRegistry registry) {
		
		registry.addCategory(FORGE_CATEGORY);
		registry.addCategory(ANVIL_SMITHING_CATEGORY);
		registry.addCategory(FORGE_STRUCTURE_CATEGORY);
		registry.addCategory(DRYING_CATEGORY);
		registry.addCategory(ITEM_USE_CATEGORY);
		
		
		registry.addWorkstation(FORGE_CATEGORY, EmiStack.of(ModBlocks.PRIMITIVE_FORGE));
		registry.addWorkstation(FORGE_CATEGORY, EmiStack.of(ModBlocks.BASIC_FORGE));
		registry.addWorkstation(FORGE_CATEGORY, EmiStack.of(ModBlocks.REFINED_FORGE));
		registry.addWorkstation(FORGE_CATEGORY, EmiStack.of(ModBlocks.ADVANCED_FORGE));
		registry.addWorkstation(FORGE_CATEGORY, EmiStack.of(ModBlocks.ETHEREAL_FORGE));
		registry.addWorkstation(ANVIL_SMITHING_CATEGORY, EmiStack.of(ModBlocks.STONE_ANVIL.NORMAL()));
		registry.addWorkstation(ANVIL_SMITHING_CATEGORY, EmiStack.of(ModBlocks.LEAD_ANVIL.NORMAL()));
		registry.addWorkstation(ANVIL_SMITHING_CATEGORY, EmiStack.of(ModBlocks.TITANIUM_ANVIL.NORMAL()));
		registry.addWorkstation(ANVIL_SMITHING_CATEGORY, EmiStack.of(ModBlocks.TUNGSTEN_ANVIL.NORMAL()));
		registry.addWorkstation(DRYING_CATEGORY, EmiStack.of(ModBlocks.DRYING_RACK));
		
		registry.addRecipeHandler(ScreenHandlerType.ANVIL, new AnvilSmithingEmiRecipeHandler());
		registry.addRecipeHandler(ModScreenHandlers.FORGE_SCREEN_HANDLER, new ForgeEmiRecipeHandler());
		
		
		
		for (RecipeEntry<ForgeRecipe> entry :
				registry.getRecipeManager().listAllOfType(ModRecipes.FORGE_RECIPE_TYPE)) {
			registry.addRecipe(new ForgeEmiRecipe(entry.value(), entry.id()));
		}
		
		for (RecipeEntry<AnvilSmithingRecipe> entry :
				registry.getRecipeManager().listAllOfType(ModRecipes.ANVIL_SMITHING_RECIPE_TYPE)) {
			registry.addRecipe(new AnvilSmithingEMIRecipe(entry.value(), entry.id()));
		}
		
		for (RecipeEntry<DryingRecipe> recipe :
				registry.getRecipeManager().listAllOfType(ModRecipes.DRYING_RECIPE_TYPE)) {
			registry.addRecipe(new DryingEmiRecipe(recipe.value(), recipe.id()));
		}
		
		registry.addRecipe(new ForgeStructureEmiRecipe(ModBlocks.PRIMITIVE_FORGE));
		registry.addRecipe(new ForgeStructureEmiRecipe(ModBlocks.BASIC_FORGE));
		registry.addRecipe(new ForgeStructureEmiRecipe(ModBlocks.REFINED_FORGE));
		registry.addRecipe(new ForgeStructureEmiRecipe(ModBlocks.ADVANCED_FORGE));
		registry.addRecipe(new ForgeStructureEmiRecipe(ModBlocks.ETHEREAL_FORGE));
		
		
		registry.addRecipe(new ItemUseEmiRecipe(ModItems.KNITTING_KIT, Rase.getIdentifier("/item_use/string_mesh")));
		
		Map.of(
				BlockTags.OAK_LOGS, Blocks.OAK_PLANKS,
				BlockTags.SPRUCE_LOGS, Blocks.SPRUCE_PLANKS,
				BlockTags.BIRCH_LOGS, Blocks.BIRCH_PLANKS,
				BlockTags.JUNGLE_LOGS, Blocks.JUNGLE_PLANKS,
				BlockTags.ACACIA_LOGS, Blocks.ACACIA_PLANKS,
				BlockTags.DARK_OAK_LOGS, Blocks.DARK_OAK_PLANKS,
				BlockTags.MANGROVE_LOGS, Blocks.MANGROVE_PLANKS,
				BlockTags.CHERRY_LOGS, Blocks.CHERRY_PLANKS
		).forEach((tagKey, block) -> addLogCutting(registry, tagKey, block));
		
		registry.addRecipe(EmiWorldInteractionRecipe.builder()
				.id(Rase.getIdentifier("/world/plank_cutting"))
				.leftInput(EmiIngredient.of(ItemTags.PLANKS))
				.rightInput(damagedTool(EmiIngredient.of(ItemTags.AXES)), true)
				.output(EmiStack.of(Items.STICK, 2))
				.build());
	}
}