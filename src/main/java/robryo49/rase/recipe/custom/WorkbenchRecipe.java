package robryo49.rase.recipe.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import robryo49.rase.recipe.ModRecipes;


public class WorkbenchRecipe implements CraftingRecipe {

    private final ShapedRecipe inner;

    public WorkbenchRecipe(ShapedRecipe inner) {
        this.inner = inner;
    }

    // Delegate everything to the wrapped ShapedRecipe

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return inner.matches(input, world);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return inner.craft(input, lookup);
    }

    @Override
    public boolean fits(int width, int height) {
        return inner.fits(width, height);
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup lookup) {
        return inner.getResult(lookup);
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return inner.getIngredients();
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
        return inner.getRemainder(input);
    }
    
    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public String getGroup() {
        return inner.getGroup();
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return inner.getCategory();
    }
    
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WORKBENCH_RECIPE_TYPE;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WORKBENCH_RECIPE_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<WorkbenchRecipe> {

        public static final MapCodec<WorkbenchRecipe> CODEC =
                ShapedRecipe.Serializer.CODEC.xmap(WorkbenchRecipe::new, r -> r.inner);

        public static final PacketCodec<RegistryByteBuf, WorkbenchRecipe> PACKET_CODEC =
                ShapedRecipe.Serializer.PACKET_CODEC.xmap(WorkbenchRecipe::new, r -> r.inner);

        @Override
        public MapCodec<WorkbenchRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, WorkbenchRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}