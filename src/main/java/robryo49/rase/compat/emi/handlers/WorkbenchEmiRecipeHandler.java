package robryo49.rase.compat.emi.handlers;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import robryo49.rase.compat.emi.RaseEmiPlugin;
import robryo49.rase.screen.custom.WorkbenchScreenHandler;

import java.util.List;

public class WorkbenchEmiRecipeHandler implements StandardRecipeHandler<WorkbenchScreenHandler> {

    @Override
    public List<Slot> getInputSources(WorkbenchScreenHandler handler) {
        return handler.slots;
    }

    @Override
    public List<Slot> getCraftingSlots(WorkbenchScreenHandler handler) {
        // Slots 1-9 are the 3x3 grid
        return handler.slots.subList(1, 10);
    }
    
    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == RaseEmiPlugin.WORKBENCH_CATEGORY
                || recipe.getCategory().getId().equals(Identifier.of("emi", "crafting"));
    }
}