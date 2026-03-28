package robryo49.rase.recipe.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import robryo49.rase.recipe.ModRecipes;

public record AnvilSmithingRecipe(
		Ingredient base,
		Ingredient addition,
		ItemStack result,
		int levelCost,
		int tier
) implements Recipe<AnvilSmithingRecipe.Input> {
	
	@Override
	public boolean matches(Input input, World world) {
		return base.test(input.getStackInSlot(0)) && addition.test(input.getStackInSlot(1)) || base.test(input.getStackInSlot(1)) && addition.test(input.getStackInSlot(0));
	}
	
	@Override
	public ItemStack craft(Input input, RegistryWrapper.WrapperLookup lookup) {
		return result.copy();
	}
	
	@Override
	public boolean fits(int width, int height) { return true; }
	
	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup lookup) { return result; }
	
	@Override
	public RecipeSerializer<?> getSerializer() { return ModRecipes.ANVIL_SMITHING_SERIALIZER; }
	
	@Override
	public RecipeType<?> getType() { return ModRecipes.ANVIL_SMITHING_RECIPE_TYPE; }
	
	
	public record Input(
			ItemStack base,
			ItemStack addition
	) implements RecipeInput {
		
		@Override public ItemStack getStackInSlot(int slot) {
			return slot == 0 ? base : addition;
		}
		
		@Override public int getSize() {
			return 2;
		}
	}
	
	public static class Serializer implements RecipeSerializer<AnvilSmithingRecipe> {
		
		public static final MapCodec<AnvilSmithingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
				instance.group(
						Ingredient.ALLOW_EMPTY_CODEC.fieldOf("base").forGetter(AnvilSmithingRecipe::base),
						Ingredient.ALLOW_EMPTY_CODEC.fieldOf("addition").forGetter(AnvilSmithingRecipe::addition),
						ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(AnvilSmithingRecipe::result),
						Codec.INT.fieldOf("level_cost").forGetter(AnvilSmithingRecipe::levelCost),
						Codec.INT.fieldOf("tier").forGetter(AnvilSmithingRecipe::tier)
				).apply(instance, AnvilSmithingRecipe::new)
		);
		
		public static final PacketCodec<RegistryByteBuf, AnvilSmithingRecipe> PACKET_CODEC = new PacketCodec<>() {
			@Override
			public AnvilSmithingRecipe decode(RegistryByteBuf buf) {
				Ingredient base = Ingredient.PACKET_CODEC.decode(buf);
				Ingredient addition = Ingredient.PACKET_CODEC.decode(buf);
				ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
				int levelCost = buf.readInt();
				int tier = buf.readInt();
				return new AnvilSmithingRecipe(base, addition, result, levelCost, tier);
			}
			
			@Override
			public void encode(RegistryByteBuf buf, AnvilSmithingRecipe recipe) {
				Ingredient.PACKET_CODEC.encode(buf, recipe.base);
				Ingredient.PACKET_CODEC.encode(buf, recipe.addition);
				ItemStack.PACKET_CODEC.encode(buf, recipe.result);
				buf.writeInt(recipe.levelCost);
				buf.writeInt(recipe.tier);
			}
		};
		
		@Override
		public MapCodec<AnvilSmithingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public PacketCodec<RegistryByteBuf, AnvilSmithingRecipe> packetCodec() {
			return PACKET_CODEC;
		}
	}
}