package robryo49.rase.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.compat.emi.RaseEmiPlugin;
import robryo49.rase.recipe.custom.CrushingRecipe;

import java.util.List;

public class CrushingEmiRecipe implements EmiRecipe {

    private final Identifier id;
    private final EmiIngredient input;
    private final EmiStack output;
    private final int crushingTime;

    public CrushingEmiRecipe(CrushingRecipe recipe, Identifier id) {
        this.id = id;
        this.input = EmiIngredient.of(recipe.input());
        this.output = EmiStack.of(recipe.result());
        this.crushingTime = recipe.crushingTime();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return RaseEmiPlugin.CRUSHING_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() { return id; }

    @Override
    public List<EmiIngredient> getInputs() { return List.of(input); }

    @Override
    public List<EmiStack> getOutputs() { return List.of(output); }

    @Override
    public int getDisplayWidth() { return 100; }

    @Override
    public int getDisplayHeight() { return 36; }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input, 4, 9);

        widgets.addFillingArrow(28, 9, crushingTime * 50).tooltip((mx, my) ->
                List.of(TooltipComponent.of(
                        Text.translatable("emi.cooking.time", crushingTime / 20f).asOrderedText())));

        widgets.addSlot(output, 76, 9).recipeContext(this);
    }
}