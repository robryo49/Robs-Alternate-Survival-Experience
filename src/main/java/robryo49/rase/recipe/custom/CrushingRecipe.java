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

public record CrushingRecipe(
        Ingredient input,
        ItemStack result,
        int crushingTime
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
    public RecipeSerializer<?> getSerializer() { return ModRecipes.CRUSHING_RECIPE_SERIALIZER; }

    @Override
    public RecipeType<?> getType() { return ModRecipes.CRUSHING_RECIPE_TYPE; }

    public static class Serializer implements RecipeSerializer<CrushingRecipe> {

        public static final MapCodec<CrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(CrushingRecipe::input),
                        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(CrushingRecipe::result),
                        Codec.INT.fieldOf("crushing_time").forGetter(CrushingRecipe::crushingTime)
                ).apply(instance, CrushingRecipe::new)
        );

        public static final PacketCodec<RegistryByteBuf, CrushingRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, CrushingRecipe::input,
                ItemStack.PACKET_CODEC, CrushingRecipe::result,
                PacketCodecs.INTEGER, CrushingRecipe::crushingTime,
                CrushingRecipe::new
        );

        @Override
        public MapCodec<CrushingRecipe> codec() { return CODEC; }

        @Override
        public PacketCodec<RegistryByteBuf, CrushingRecipe> packetCodec() { return PACKET_CODEC; }
    }
}