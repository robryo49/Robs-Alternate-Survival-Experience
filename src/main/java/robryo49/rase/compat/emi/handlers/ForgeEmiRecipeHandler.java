package robryo49.rase.compat.emi.handlers;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;
import robryo49.rase.compat.emi.ForgeEmiRecipe;
import robryo49.rase.compat.emi.RaseEmiPlugin;
import robryo49.rase.screen.custom.ForgeScreenHandler;
import robryo49.rase.block.entity.custom.ForgeBlockEntity;

import java.util.List;

public class ForgeEmiRecipeHandler implements StandardRecipeHandler<ForgeScreenHandler> {
	
	@Override
	public List<Slot> getInputSources(ForgeScreenHandler handler) {
		return handler.slots;
	}
	
	@Override
	public List<Slot> getCraftingSlots(ForgeScreenHandler handler) {
		return List.of(
				handler.getSlot(ForgeBlockEntity.INPUT_SLOT_0),
				handler.getSlot(ForgeBlockEntity.INPUT_SLOT_1),
				handler.getSlot(ForgeBlockEntity.INPUT_SLOT_2),
				handler.getSlot(ForgeBlockEntity.INPUT_SLOT_3),
				handler.getSlot(ForgeBlockEntity.OUTPUT_SLOT)
		);
	}
	
	@Override
	public boolean supportsRecipe(EmiRecipe recipe) {
		return recipe.getCategory() == RaseEmiPlugin.FORGE_CATEGORY;
	}
	
	@Override
	public boolean canCraft(EmiRecipe recipe, EmiCraftContext<ForgeScreenHandler> context) {
		if (recipe instanceof ForgeEmiRecipe forgeRecipe) {
			if (forgeRecipe.getTier() > context.getScreenHandler().getForgeTier().getTier()) {
				return false;
			}
		}
		
		return StandardRecipeHandler.super.canCraft(recipe, context);
	}
}