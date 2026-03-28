package robryo49.rase.recipe.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import robryo49.rase.recipe.ModRecipes;

public record DryingRecipe(
        Ingredient input,
        ItemStack result,
        int dryingTime
) implements Recipe<SingleStackRecipeInput> {

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return this.input.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) { return true; }
    
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(1);
        list.add(input);
        return list;
    }
    
    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup lookup) { return result; }

    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.DRYING_RECIPE_SERIALIZER; }

    @Override
    public RecipeType<?> getType() { return ModRecipes.DRYING_RECIPE_TYPE; }

    public static class Serializer implements RecipeSerializer<DryingRecipe> {

        public static final MapCodec<DryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(DryingRecipe::input),
                        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(DryingRecipe::result),
                        Codec.INT.fieldOf("drying_time").forGetter(DryingRecipe::dryingTime)
                ).apply(instance, DryingRecipe::new)
        );
        
        public static final PacketCodec<RegistryByteBuf, DryingRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, DryingRecipe::input,
                ItemStack.PACKET_CODEC, DryingRecipe::result,
                PacketCodecs.INTEGER, DryingRecipe::dryingTime,
                DryingRecipe::new
        );

        @Override
        public MapCodec<DryingRecipe> codec() { return CODEC; }

        @Override
        public PacketCodec<RegistryByteBuf, DryingRecipe> packetCodec() { return PACKET_CODEC; }
    }
}