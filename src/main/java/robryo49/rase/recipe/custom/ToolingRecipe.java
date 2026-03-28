package robryo49.rase.recipe.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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


public class ToolingRecipe implements CraftingRecipe {
	private final CraftingRecipeCategory category;
	private final Ingredient tool;
	private final Ingredient input;
	private final ItemStack result;
	
	public ToolingRecipe(CraftingRecipeCategory category, Ingredient tool, Ingredient input, ItemStack result) {
		this.category = category;
		this.tool = tool;
		this.input = input;
		this.result = result;
	}
	
	@Override
	public boolean matches(CraftingRecipeInput inventory, World world) {
		boolean hasInput = false;
		boolean hasTool = false;
		
		for (int i = 0; i < inventory.getSize(); ++i) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack.isEmpty()) continue;
			
			if (tool.test(stack) && !hasTool) {
				hasTool = true;
			} else if (input.test(stack) && !hasInput) {
				hasInput = true;
			} else {
				return false;
			}
		}
		return hasInput && hasTool;
	}
	
	@Override
	public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup wrapperLookup) {
		return this.result.copy();
	}
	
	@Override
	public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput inventory) {
		DefaultedList<ItemStack> remainders = DefaultedList.ofSize(inventory.getSize(), ItemStack.EMPTY);
		
		for (int i = 0; i < remainders.size(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			
			if (tool.test(stack) && stack.isDamageable()) {
				ItemStack remainder = stack.copyWithCount(1);
				remainder.setDamage(remainder.getDamage() + 1);
				
				if (remainder.getDamage() < remainder.getMaxDamage()) {
					remainders.set(i, remainder);
				}
			} else if (stack.getItem().hasRecipeRemainder()) {
				remainders.set(i, new ItemStack(stack.getItem().getRecipeRemainder()));
			}
		}
		
		return remainders;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
		return this.result;
	}
	
	public Ingredient tool() { return tool; }
	public Ingredient input() { return input; }
	public ItemStack result() { return result; }
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.TOOLING_RECIPE_SERIALIZER;
	}
	
	public static class Serializer implements RecipeSerializer<ToolingRecipe> {
		
		public static final MapCodec<ToolingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
				instance.group(
						CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(ToolingRecipe::getCategory),
						Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("tool").forGetter(ToolingRecipe::tool),
						Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(ToolingRecipe::input),
						ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(ToolingRecipe::result)
				).apply(instance, ToolingRecipe::new)
		);
		
		public static final PacketCodec<RegistryByteBuf, ToolingRecipe> PACKET_CODEC = PacketCodec.tuple(
				CraftingRecipeCategory.PACKET_CODEC, ToolingRecipe::getCategory,
				Ingredient.PACKET_CODEC, ToolingRecipe::tool,
				Ingredient.PACKET_CODEC, ToolingRecipe::input,
				ItemStack.PACKET_CODEC, ToolingRecipe::result,
				ToolingRecipe::new
		);
		
		@Override
		public MapCodec<ToolingRecipe> codec() { return CODEC; }
		
		@Override
		public PacketCodec<RegistryByteBuf, ToolingRecipe> packetCodec() { return PACKET_CODEC; }
	}
	
	@Override
	public CraftingRecipeCategory getCategory() {
		return this.category;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> ingredients = DefaultedList.of();
		ingredients.add(input);
		ingredients.add(tool);
		return ingredients;
	}
}