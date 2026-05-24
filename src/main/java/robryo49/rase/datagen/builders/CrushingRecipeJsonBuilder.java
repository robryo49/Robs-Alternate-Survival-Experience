package robryo49.rase.datagen.builders;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.recipe.custom.CrushingRecipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class CrushingRecipeJsonBuilder {

    private final CraftingRecipeCategory category;
    private final Ingredient input;
    private final Item output;
    private final int outputCount;
    private final int crushingTime;
    private final Map<String, AdvancementCriterion<?>> advancementBuilder = new LinkedHashMap<>();

    public CrushingRecipeJsonBuilder(CraftingRecipeCategory category, Ingredient input,
                                      ItemConvertible output, int outputCount, int crushingTime) {
        this.category = category;
        this.input = input;
        this.output = output.asItem();
        this.outputCount = outputCount;
        this.crushingTime = crushingTime;
    }

    public static CrushingRecipeJsonBuilder create(Ingredient input, ItemConvertible output, int outputCount, int crushingTime) {
        return new CrushingRecipeJsonBuilder(CraftingRecipeCategory.MISC, input, output, outputCount, crushingTime);
    }


    public CrushingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.advancementBuilder.put(name, criterion);
        return this;
    }

    public void offerTo(RecipeExporter exporter, String name) {
        Identifier recipeId = Rase.getIdentifier(name);
        this.validate(recipeId);

        Advancement.Builder builder = exporter.getAdvancementBuilder()
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);

        this.advancementBuilder.forEach(builder::criterion);

        CrushingRecipe recipe = new CrushingRecipe(input, new ItemStack(output, outputCount), crushingTime);

        exporter.accept(recipeId, recipe,
                builder.build(recipeId.withPrefixedPath("recipes/" + this.category.name().toLowerCase() + "/")));
    }

    private void validate(Identifier recipeId) {
        if (this.advancementBuilder.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }
}