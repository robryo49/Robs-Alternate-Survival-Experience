package robryo49.rase.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import robryo49.rase.compat.emi.RaseEmiPlugin;
import robryo49.rase.recipe.custom.WorkbenchRecipe;

import java.util.List;

public class WorkbenchEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public WorkbenchEmiRecipe(WorkbenchRecipe recipe, Identifier id) {
        this.id = id;
        this.inputs = recipe.getIngredients().stream()
                .map(EmiIngredient::of)
                .collect(java.util.stream.Collectors.toList());
        this.outputs = List.of(EmiStack.of(recipe.getResult(null)));
    }

    @Override public EmiRecipeCategory getCategory() { return RaseEmiPlugin.WORKBENCH_CATEGORY; }
    @Override public Identifier getId() { return id; }
    @Override public List<EmiIngredient> getInputs() { return inputs; }
    @Override public List<EmiStack> getOutputs() { return outputs; }
    @Override public int getDisplayWidth() { return 100; }
    @Override public int getDisplayHeight() { return 54; }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        // 3x3 grid — ingredients come in row-major order from ShapedRecipe
        for (int i = 0; i < 9; i++) {
            int col = i % 3;
            int row = i / 3;
            EmiIngredient ingredient = i < inputs.size() ? inputs.get(i) : EmiStack.EMPTY;
            widgets.addSlot(ingredient, col * 18, row * 18);
        }
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);
        widgets.addSlot(outputs.getFirst(), 82, 18).recipeContext(this);
    }
}