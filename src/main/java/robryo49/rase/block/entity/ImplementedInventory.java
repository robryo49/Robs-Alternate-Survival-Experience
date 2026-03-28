package robryo49.rase.block.entity; // Change this to your actual package

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * A simple interface for testable SidedInventory implementations.
 */
@FunctionalInterface
public interface ImplementedInventory extends SidedInventory {
	
	// Gets the item list from the block entity
	DefaultedList<ItemStack> getItems();
	
	// Creates an inventory from the item list
	static ImplementedInventory of(DefaultedList<ItemStack> items) {
		return () -> items;
	}
	
	@Override
	default int[] getAvailableSlots(Direction side) {
		int[] result = new int[getItems().size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = i;
		}
		return result;
	}
	
	@Override
	default boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
		return true;
	}
	
	@Override
	default boolean canExtract(int slot, ItemStack stack, Direction side) {
		return true;
	}
	
	@Override
	default int size() {
		return getItems().size();
	}
	
	@Override
	default boolean isEmpty() {
		for (int i = 0; i < size(); i++) {
			ItemStack stack = getStack(i);
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	default ItemStack getStack(int slot) {
		return getItems().get(slot);
	}
	
	@Override
	default ItemStack removeStack(int slot, int count) {
		ItemStack result = Inventories.splitStack(getItems(), slot, count);
		if (!result.isEmpty()) {
			markDirty();
		}
		return result;
	}
	
	@Override
	default ItemStack removeStack(int slot) {
		return Inventories.removeStack(getItems(), slot);
	}
	
	@Override
	default void setStack(int slot, ItemStack stack) {
		getItems().set(slot, stack);
		if (stack.getCount() > getMaxCountPerStack()) {
			stack.setCount(getMaxCountPerStack());
		}
	}
	
	@Override
	default void clear() {
		getItems().clear();
	}
	
	@Override
	default void markDirty() {
		// Overridden by BlockEntity
	}
	
	@Override
	default boolean canPlayerUse(net.minecraft.entity.player.PlayerEntity player) {
		return true;
	}
}