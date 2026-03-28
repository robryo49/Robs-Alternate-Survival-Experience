package robryo49.rase.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.recipe.custom.AnvilSmithingRecipe;

import java.util.List;

public class AnvilSmithingEMIRecipe implements EmiRecipe {
	
	private final Identifier recipeId;
	private final List<EmiIngredient> inputs;
	private final List<EmiStack> outputs;
	
	public AnvilSmithingEMIRecipe(AnvilSmithingRecipe recipe, Identifier recipeId) {
		this.recipeId = recipeId;
		
		this.inputs = List.of(
				EmiIngredient.of(recipe.base()),
				EmiIngredient.of(recipe.addition())
		);
		this.outputs = List.of(EmiStack.of(recipe.result()));
	}
	
	@Override
	public EmiRecipeCategory getCategory() {
		return RaseEmiPlugin.ANVIL_SMITHING_CATEGORY;
	}
	
	@Override
	public @Nullable Identifier getId() {
		return recipeId;
	}
	
	@Override
	public List<EmiIngredient> getInputs() {
		return inputs;
	}
	
	@Override
	public List<EmiStack> getOutputs() {
		return outputs;
	}
	
	@Override
	public int getDisplayWidth() {
		return 100;
	}
	
	@Override
	public int getDisplayHeight() {
		return 36;
	}
	
	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addSlot(inputs.get(0), 4, 9);
		widgets.addSlot(inputs.get(1), 24, 9);
		
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 50, 9);
		
		widgets.addSlot(outputs.getFirst(), 78, 9).recipeContext(this);
	}
}