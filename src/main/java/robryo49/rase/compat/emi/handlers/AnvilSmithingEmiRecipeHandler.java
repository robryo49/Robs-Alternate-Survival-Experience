package robryo49.rase.compat.emi.handlers;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.slot.Slot;
import robryo49.rase.compat.emi.RaseEmiPlugin;
import java.util.List;

public class AnvilSmithingEmiRecipeHandler implements StandardRecipeHandler<AnvilScreenHandler> {
	
	@Override
	public List<Slot> getInputSources(AnvilScreenHandler handler) {
		return handler.slots;
	}
	
	@Override
	public List<Slot> getCraftingSlots(AnvilScreenHandler handler) {
		return List.of(handler.getSlot(0), handler.getSlot(1));
	}
	
	@Override
	public boolean supportsRecipe(EmiRecipe recipe) {
		return recipe.getCategory() == RaseEmiPlugin.ANVIL_SMITHING_CATEGORY;
	}
}