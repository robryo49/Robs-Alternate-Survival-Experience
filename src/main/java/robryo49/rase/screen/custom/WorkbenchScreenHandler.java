package robryo49.rase.screen.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.recipe.custom.WorkbenchRecipe;
import robryo49.rase.screen.ModScreenHandlers;

import java.util.Optional;

public class WorkbenchScreenHandler extends AbstractRecipeScreenHandler<CraftingRecipeInput, WorkbenchRecipe> {
	
	private final RecipeInputInventory input;
	private final CraftingResultInventory result = new CraftingResultInventory();
	private final PlayerEntity player;
	private boolean filling = false;
	
	public WorkbenchScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(ModScreenHandlers.WORKBENCH_SCREEN_HANDLER, syncId);
		this.player = playerInventory.player;
		this.input = new net.minecraft.inventory.CraftingInventory(this, 3, 3);
		
		this.addSlot(new CraftingResultSlot(player, this.input, this.result, 0, 124, 35));
		
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				this.addSlot(new Slot(this.input, col + row * 3, 30 + col * 18, 17 + row * 18));
			}
		}
		
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
	
	public void onInputSlotFillStart() {
		this.filling = true;
	}
	
	public void onInputSlotFillFinish(RecipeEntry recipe) {
		this.filling = false;
		updateResult(this, player.getWorld(), this.player, this.input, this.result, recipe);
	}
	
	@Override
	public void onContentChanged(Inventory inventory) {
		if (!this.filling) {
			updateResult(this, player.getWorld(), this.player, this.input, this.result, null);
		}
	}
	
	private static void updateResult(WorkbenchScreenHandler handler, World world, PlayerEntity player,
	                                 RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory,
	                                 @Nullable RecipeEntry<CraftingRecipe> recipe) {
		if (world.isClient) return;
		
		CraftingRecipeInput input = craftingInventory.createRecipeInput();
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
		ItemStack result = ItemStack.EMPTY;
		
		Optional<RecipeEntry<WorkbenchRecipe>> workbenchMatch = world.getServer()
				.getRecipeManager()
				.getFirstMatch(ModRecipes.WORKBENCH_RECIPE_TYPE, input, world);
		
		if (workbenchMatch.isPresent()) {
			RecipeEntry<WorkbenchRecipe> entry = workbenchMatch.get();
			if (resultInventory.shouldCraftRecipe(world, serverPlayer, entry)) {
				ItemStack crafted = entry.value().craft(input, world.getRegistryManager());
				if (crafted.isItemEnabled(world.getEnabledFeatures())) {
					result = crafted;
				}
			}
		} else {
			Optional<RecipeEntry<CraftingRecipe>> vanillaMatch = world.getServer()
					.getRecipeManager()
					.getFirstMatch(RecipeType.CRAFTING, input, world, recipe);
			
			if (vanillaMatch.isPresent()) {
				RecipeEntry<CraftingRecipe> entry = vanillaMatch.get();
				if (resultInventory.shouldCraftRecipe(world, serverPlayer, entry)) {
					ItemStack crafted = entry.value().craft(input, world.getRegistryManager());
					if (crafted.isItemEnabled(world.getEnabledFeatures())) {
						result = crafted;
					}
				}
			}
		}
		
		resultInventory.setStack(0, result);
		handler.setPreviousTrackedSlot(0, result);
		serverPlayer.networkHandler.sendPacket(
				new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, result)
		);
	}
	
	@Override
	public void populateRecipeFinder(RecipeMatcher finder) {
		this.input.provideRecipeInputs(finder);
	}
	
	@Override
	public void clearCraftingSlots() {
		this.input.clear();
		this.result.clear();
	}
	
	@Override
	public boolean matches(RecipeEntry<WorkbenchRecipe> recipe) {
		return recipe.value() instanceof CraftingRecipe cr
				&& cr.matches(this.input.createRecipeInput(), this.player.getWorld());
	}
	
	@Override
	public int getCraftingResultSlotIndex() { return 0; }
	
	@Override
	public int getCraftingWidth() { return 3; }
	
	@Override
	public int getCraftingHeight() { return 3; }
	
	@Override
	public int getCraftingSlotCount() { return 10; }
	
	@Override
	public RecipeBookCategory getCategory() { return RecipeBookCategory.CRAFTING; }
	
	@Override
	public boolean canInsertIntoSlot(int index) { return index != 0; }
	
	@Override
	public boolean canUse(PlayerEntity player) { return true; }
	
	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		this.dropInventory(player, this.input);
	}
	
	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		ItemStack result = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (!slot.hasStack()) return result;
		
		ItemStack stack = slot.getStack();
		result = stack.copy();
		
		if (index == 0) {
			if (!this.insertItem(stack, 10, 46, true)) return ItemStack.EMPTY;
			slot.onQuickTransfer(stack, result);
		} else if (index >= 10) {
			if (!this.insertItem(stack, 1, 10, false)) {
				if (index < 37) {
					if (!this.insertItem(stack, 37, 46, false)) return ItemStack.EMPTY;
				} else {
					if (!this.insertItem(stack, 10, 37, false)) return ItemStack.EMPTY;
				}
			}
		} else {
			if (!this.insertItem(stack, 10, 46, false)) return ItemStack.EMPTY;
		}
		
		if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY);
		else slot.markDirty();
		
		if (stack.getCount() == result.getCount()) return ItemStack.EMPTY;
		slot.onTakeItem(player, stack);
		return result;
	}
}