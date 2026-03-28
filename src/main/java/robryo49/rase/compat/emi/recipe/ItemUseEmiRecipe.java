package robryo49.rase.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robryo49.rase.compat.emi.RaseEmiPlugin;
import robryo49.rase.item.custom.UsableItem;

import java.util.List;

public class ItemUseEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiIngredient input;
    private final EmiStack output;
    private final int useTime;

    public ItemUseEmiRecipe(Item usableItem, Identifier id) {
        this.id = id;
        this.input = EmiIngredient.of(Ingredient.ofItems(usableItem));
        this.output = EmiStack.of(((UsableItem) usableItem).getOutput());
        this.useTime = ((UsableItem) usableItem).getUseTime();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return RaseEmiPlugin.ITEM_USE_CATEGORY;
    }

    @Override
    public Identifier getId() { return id; }

    @Override
    public List<EmiIngredient> getInputs() { return List.of(input); }

    @Override
    public List<EmiStack> getOutputs() { return List.of(output); }

    @Override
    public int getDisplayWidth() { return 76; }

    @Override
    public int getDisplayHeight() { return 18; }

    @Override
    public void addWidgets(dev.emi.emi.api.widget.WidgetHolder widgets) {
        widgets.addSlot(input, 0, 0);
        widgets.addFillingArrow(24, 1, 50 * useTime*5).tooltip((mx, my) ->
                List.of(TooltipComponent.of(Text.translatable("emi.cooking.time", useTime*5 / 20f).asOrderedText())));
        widgets.addSlot(output, 58, 0).recipeContext(this);
    }
}