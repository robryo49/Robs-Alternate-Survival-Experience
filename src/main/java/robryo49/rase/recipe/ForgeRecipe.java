package robryo49.rase.recipe;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;



public record ForgeRecipe(
		List<Ingredient> ingredients,
		ItemStack result,
		TagKey<Item> moldTagKey,
		int cookTime,
		int coolTime,
		int minTier,
		float experience
) implements Recipe<ForgeRecipeInput> {
	
	public static final PacketCodec<RegistryByteBuf, ForgeRecipe> PACKET_CODEC = new PacketCodec<>() {
		@Override
		public ForgeRecipe decode(RegistryByteBuf buf) {
			return new ForgeRecipe(
					Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()).decode(buf),
					ItemStack.PACKET_CODEC.decode(buf),
					TagKey.of(RegistryKeys.ITEM, Identifier.PACKET_CODEC.decode(buf)),
					buf.readInt(),
					buf.readInt(),
					buf.readInt(),
					buf.readFloat()
			);
		}
		
		@Override
		public void encode(RegistryByteBuf buf, ForgeRecipe recipe) {
			Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()).encode(buf, recipe.ingredients());
			ItemStack.PACKET_CODEC.encode(buf, recipe.result());
			Identifier.PACKET_CODEC.encode(buf, recipe.moldTagKey().id());
			buf.writeInt(recipe.cookTime());
			buf.writeInt(recipe.coolTime());
			buf.writeInt(recipe.minTier());
			buf.writeFloat(recipe.experience());
		}
	};
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> list = DefaultedList.of();
		list.addAll(ingredients);
		return list;
	}
	
	@Override
	public boolean matches(ForgeRecipeInput input, World world) {
		if (world.isClient) return false;
		
		if (!input.mold().isIn(moldTagKey)) return false;
		
		List<Ingredient> remaining = new ArrayList<>(ingredients);
		
		for (int slot = 0; slot < input.getSize(); slot++) {
			ItemStack stack = input.getStackInSlot(slot);
			if (stack.isEmpty()) continue;
			
			boolean matched = false;
			for (int i = 0; i < remaining.size(); i++) {
				if (remaining.get(i).test(stack)) {
					remaining.remove(i);
					matched = true;
					break;
				}
			}
			if (!matched) return false;
		}
		
		return remaining.isEmpty();
	}
	
	@Override
	public ItemStack craft(ForgeRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
		return result.copy();
	}
	
	@Override
	public boolean fits(int width, int height) {
		return width <= 4 && height <= 1;
	}
	
	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
		return result;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.FORGE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return ModRecipes.FORGE_RECIPE_TYPE;
	}
	
	public static class Serializer implements RecipeSerializer<ForgeRecipe> {
		
		public static final MapCodec<ForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
				instance.group(
						Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").forGetter(ForgeRecipe::ingredients),
						ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(ForgeRecipe::result),
						TagKey.codec(RegistryKeys.ITEM).fieldOf("mold_tag").forGetter(ForgeRecipe::moldTagKey),
						Codec.INT.fieldOf("cook_time").forGetter(ForgeRecipe::cookTime),
						Codec.INT.fieldOf("cool_time").forGetter(ForgeRecipe::coolTime),
						Codec.INT.fieldOf("min_tier").forGetter(ForgeRecipe::minTier),
						Codec.FLOAT.fieldOf("experience").forGetter(ForgeRecipe::experience)
				).apply(instance, ForgeRecipe::new)
		);
		
		// REFACTORED PART: Manual PacketCodec implementation
		public static final PacketCodec<RegistryByteBuf, ForgeRecipe> PACKET_CODEC = new PacketCodec<>() {
			@Override
			public ForgeRecipe decode(RegistryByteBuf buf) {
				// Read them in the EXACT same order they are defined in your record
				List<Ingredient> ingredients = Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()).decode(buf);
				ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
				
				// Convert the Identifier back to a TagKey
				Identifier moldTagId = Identifier.PACKET_CODEC.decode(buf);
				TagKey<Item> moldTagKey = TagKey.of(RegistryKeys.ITEM, moldTagId);
				
				int cookTime = buf.readInt();
				int coolTime = buf.readInt();
				int minTier = buf.readInt();
				float experience = buf.readFloat();
				
				return new ForgeRecipe(ingredients, result, moldTagKey, cookTime, coolTime, minTier, experience);
			}
			
			@Override
			public void encode(RegistryByteBuf buf, ForgeRecipe recipe) {
				Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()).encode(buf, recipe.ingredients());
				ItemStack.PACKET_CODEC.encode(buf, recipe.result());
				
				Identifier.PACKET_CODEC.encode(buf, recipe.moldTagKey().id());
				
				buf.writeInt(recipe.cookTime());
				buf.writeInt(recipe.coolTime());
				buf.writeInt(recipe.minTier());
				buf.writeFloat(recipe.experience());
			}
		};
	
		
		@Override
		public MapCodec<ForgeRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public PacketCodec<RegistryByteBuf, ForgeRecipe> packetCodec() {
			return PACKET_CODEC;
		}
	}
	
}