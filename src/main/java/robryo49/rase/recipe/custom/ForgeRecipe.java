package robryo49.rase.recipe.custom;


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
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import robryo49.rase.item.custom.MoldItem;
import robryo49.rase.recipe.ModRecipes;

import java.util.ArrayList;
import java.util.List;



public record ForgeRecipe(
		List<Ingredient> ingredients,
		ItemStack result,
		TagKey<Item> moldTagKey,
		int cookTime,
		int coolTime,
		int tier,
		float experience
) implements Recipe<ForgeRecipe.Input> {
	
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
			buf.writeInt(recipe.tier());
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
	public boolean matches(Input input, World world) {
		if (world.isClient) return false;
		if (!input.mold().isIn(moldTagKey)) return false;
		
		if ((input.mold().getItem() instanceof MoldItem moldItem)) if (moldItem.getTier() < tier) return false;
		
		List<Ingredient> unmatched = new ArrayList<>(ingredients);
		
		List<ItemStack> available = new ArrayList<>();
		for (int slot = 0; slot < input.getSize(); slot++) {
			ItemStack stack = input.getStackInSlot(slot);
			if (!stack.isEmpty()) {
				for (int i = 0; i < stack.getCount(); i++) {
					available.add(stack.copyWithCount(1));
				}
			}
		}
		
		for (Ingredient ingredient : unmatched) {
			boolean found = false;
			for (int i = 0; i < available.size(); i++) {
				if (ingredient.test(available.get(i))) {
					available.remove(i);
					found = true;
					break;
				}
			}
			if (!found) return false;
		}
		
		return true;
	}
	
	@Override
	public ItemStack craft(Input input, RegistryWrapper.WrapperLookup lookup) {
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
		return ModRecipes.FORGE_RECIPE_SERIALIZER;
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
						Codec.INT.fieldOf("tier").forGetter(ForgeRecipe::tier),
						Codec.FLOAT.fieldOf("experience").forGetter(ForgeRecipe::experience)
				).apply(instance, ForgeRecipe::new)
		);
		
		public static final PacketCodec<RegistryByteBuf, ForgeRecipe> PACKET_CODEC = new PacketCodec<>() {
			@Override
			public ForgeRecipe decode(RegistryByteBuf buf) {
				List<Ingredient> ingredients = Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()).decode(buf);
				ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
				
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
				buf.writeInt(recipe.tier());
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
	
	public record Input(
			ItemStack slot1, ItemStack slot2,
			ItemStack slot3, ItemStack slot4,
			ItemStack mold
	) implements RecipeInput {
		
		@Override
		public ItemStack getStackInSlot(int slot) {
			return switch (slot) {
				case 0 -> slot1;
				case 1 -> slot2;
				case 2 -> slot3;
				case 3 -> slot4;
				default -> ItemStack.EMPTY;
			};
		}
		
		@Override
		public int getSize() {
			return 4;
		}
	}
}