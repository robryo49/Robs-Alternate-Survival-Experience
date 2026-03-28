package robryo49.rase.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.recipe.custom.ForgeRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForgeEmiRecipe implements EmiRecipe {
	
	private final Identifier recipeId;
	private final List<EmiIngredient> inputs;
	private final List<EmiStack> outputs;
	private final float experience;
	private final int tier;
	
	public ForgeEmiRecipe(ForgeRecipe recipe, Identifier recipeId) {
		this.recipeId = recipeId;
		
		List<EmiIngredient> ingredientSlots = new ArrayList<>();
		List<net.minecraft.recipe.Ingredient> ingredients = recipe.ingredients();
		for (int i = 0; i < 4; i++) {
			if (i < ingredients.size()) {
				ingredientSlots.add(EmiIngredient.of(ingredients.get(i)));
			} else {
				ingredientSlots.add(EmiStack.EMPTY);
			}
		}
		
		ingredientSlots.add(EmiIngredient.of(recipe.moldTagKey()));
		
		this.inputs = ingredientSlots;
		this.outputs = List.of(EmiStack.of(recipe.result()));
		this.experience = recipe.experience();
		this.tier = recipe.tier();
	}
	
	@Override
	public int getDisplayWidth() { return 110; }
	
	@Override
	public int getDisplayHeight() { return 50; }
	
	@Override
	public void addWidgets(WidgetHolder widgets) {
		
		for (int i = 0; i < 4; i++) {
			int col = i % 2;
			int row = i / 2;
			widgets.addSlot(inputs.get(i), 2 + col * 18, 6 + row * 18);
		}
		
		widgets.addSlot(inputs.get(4), 42, 15);
		
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 15);
		
		widgets.addSlot(outputs.getFirst(), 87, 15).recipeContext(this);
		
		widgets.addText(
				net.minecraft.text.Text.literal("Tier: " + tier),
				42, 38,
				0x444444,
				false
		);
	}
	
	@Override
	public EmiRecipeCategory getCategory() {
		return RaseEmiPlugin.FORGE_CATEGORY;
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
	
	public int getTier() {
		return tier;
	}
}