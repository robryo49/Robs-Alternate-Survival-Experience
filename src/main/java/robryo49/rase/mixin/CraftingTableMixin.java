package robryo49.rase.mixin;

import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import robryo49.rase.recipe.custom.WorkbenchRecipe;

import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public class CraftingTableMixin {
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Redirect(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeManager;getFirstMatch(" +
                            "Lnet/minecraft/recipe/RecipeType;" +
                            "Lnet/minecraft/recipe/input/RecipeInput;" +
                            "Lnet/minecraft/world/World;" +
                            "Lnet/minecraft/recipe/RecipeEntry;)" +
                            "Ljava/util/Optional;"
            )
    )
    private static Optional<RecipeEntry<CraftingRecipe>> rase$filterWorkbenchRecipes(
            RecipeManager manager,
            RecipeType type,
            RecipeInput input,
            World world,
            RecipeEntry cached) {
        
        return ((Optional<RecipeEntry<CraftingRecipe>>) manager.getFirstMatch(type, input, world, cached))
                .filter(entry -> !(entry.value() instanceof WorkbenchRecipe));
    }
}