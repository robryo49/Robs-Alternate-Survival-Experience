package robryo49.rase.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.tag.TagKey;

public record ForgeRecipeInput(
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