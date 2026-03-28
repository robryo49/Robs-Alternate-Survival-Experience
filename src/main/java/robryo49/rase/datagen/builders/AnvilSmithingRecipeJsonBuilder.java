package robryo49.rase.datagen.builders;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.recipe.custom.AnvilSmithingRecipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnvilSmithingRecipeJsonBuilder {
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;
    private int levelCost = 0;
    private int tier = 0;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    
    private AnvilSmithingRecipeJsonBuilder(Ingredient base, Ingredient addition, ItemStack result) {
        this.base = base;
        this.addition = addition;
        this.result = result;
    }
    
    public static AnvilSmithingRecipeJsonBuilder create(Ingredient base, Ingredient addition, Item result) {
        return new AnvilSmithingRecipeJsonBuilder(base, addition, new ItemStack(result));
    }
    
    public AnvilSmithingRecipeJsonBuilder levelCost(int levelCost) {
        this.levelCost = levelCost;
        return this;
    }
    
    public AnvilSmithingRecipeJsonBuilder tier(int tier) {
        this.tier = tier;
        return this;
    }
    
    public AnvilSmithingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }
    
    public void offerTo(RecipeExporter exporter, String name) {
        Identifier identifier = Rase.getIdentifier(name);
        
        Advancement.Builder advancementBuilder = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", InventoryChangedCriterion.Conditions.items(base.getMatchingStacks()[0].getItem()));
        
        this.criteria.forEach(advancementBuilder::criterion);
        
        AnvilSmithingRecipe recipe = new AnvilSmithingRecipe(base, addition, result, levelCost, tier);
        
        exporter.accept(identifier, recipe, advancementBuilder.build(identifier.withPrefixedPath("recipes/anvil_smithing/")));
    }
}