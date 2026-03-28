package robryo49.rase.screen.custom;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import robryo49.rase.block.custom.forge.ForgeTiers;
import robryo49.rase.block.entity.custom.ForgeBlockEntity;
import robryo49.rase.item.custom.MoldItem;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.screen.ModScreenHandlers;
import robryo49.rase.util.ModItemTags;

import java.util.Objects;

public class ForgeScreenHandler extends ScreenHandler {
	
	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;
	public final ForgeBlockEntity blockEntity;
	
	private static final int INPUT_X = 20, INPUT_Y = 17;
	private static final int FUEL_X = 47, FUEL_Y = 53;
	private static final int OUTPUT_X = 131, OUTPUT_Y = 35;
	
	public ForgeScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
		this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos), new ArrayPropertyDelegate(4));
	}
	
	public ForgeScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate propertyDelegate) {
		super(ModScreenHandlers.FORGE_SCREEN_HANDLER, syncId);
		this.inventory = (Inventory) blockEntity;
		this.blockEntity = (ForgeBlockEntity) blockEntity;
		this.propertyDelegate = propertyDelegate;
		
		for (int i = 0; i < 4; i++) addSlot(new Slot(inventory, ForgeBlockEntity.INPUT_SLOT_0 + i, INPUT_X + i * 18, INPUT_Y) {
			@Override public boolean canInsert(ItemStack stack) {return isValidIngredient(stack); }
		});
		
		addSlot(new Slot(inventory, ForgeBlockEntity.FUEL_SLOT, FUEL_X, FUEL_Y) {
			@Override public boolean canInsert(ItemStack stack) {return AbstractFurnaceBlockEntity.canUseAsFuel(stack); }
		});
		
		addSlot(new Slot(inventory, ForgeBlockEntity.OUTPUT_SLOT, OUTPUT_X, OUTPUT_Y) {
			@Override
			public boolean canInsert(ItemStack stack) {
				if (stack.getItem() instanceof MoldItem moldItem) {
					return moldItem.getTier() >= ((ForgeBlockEntity) blockEntity).getForgeTier().getTier();
				} else return false;
			}
			
			@Override
			public void onTakeItem(PlayerEntity player, ItemStack stack) {
				super.onTakeItem(player, stack);
				if (!player.getWorld().isClient && player instanceof ServerPlayerEntity serverPlayer) {
					((ForgeBlockEntity) blockEntity).dropExperienceForRecipesUsed(serverPlayer);
				}
			}
		});
		
		addPlayerInventory(playerInventory);
		addPlayerHotbar(playerInventory);
		addProperties(propertyDelegate);
	}
	
	public boolean isCooking() { return getCookTime() > 0; }
	public boolean isBurning() { return getBurnTime() > 0; }
	
	public int getScaledCookTime() {
		return Math.ceilDiv(getCookTime() * 24, Math.max(getCookTimeTotal(), 1));
	}
	
	public int getScaledBurnTime() {
		return Math.ceilDiv(getBurnTime() * 14, Math.max(getFuelTime(), 1));
	}
	
	private boolean isValidIngredient(ItemStack stack) {
		return Objects.requireNonNull(blockEntity.getWorld()).getRecipeManager()
				.listAllOfType(ModRecipes.FORGE_RECIPE_TYPE)
				.stream()
				.anyMatch(entry -> entry.value().ingredients().stream()
						.anyMatch(ingredient -> ingredient.test(stack)));
	}
	
	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		ItemStack result = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		
		if (!slot.hasStack()) return result;
		
		ItemStack original = slot.getStack();
		result = original.copy();
		
		if (index == ForgeBlockEntity.OUTPUT_SLOT) {
			if (!insertItem(original, 6, 42, true)) return ItemStack.EMPTY;
			slot.onQuickTransfer(original, result);
		}
		else if (index < 6) {
			if (!insertItem(original, 6, 42, false)) return ItemStack.EMPTY;
		}
		else {
			if (original.isIn(ModItemTags.MOLDS)) {
				if (!insertItem(original, ForgeBlockEntity.OUTPUT_SLOT, ForgeBlockEntity.OUTPUT_SLOT + 1, false)
						&& !insertItem(original, ForgeBlockEntity.INPUT_SLOT_0, ForgeBlockEntity.INPUT_SLOT_3 + 1, false)) {
					if (index < 33 ? !insertItem(original, 33, 42, false) : !insertItem(original, 6, 33, false))
						return ItemStack.EMPTY;
				}
			} else if (AbstractFurnaceBlockEntity.canUseAsFuel(original)) {
				if (!insertItem(original, ForgeBlockEntity.FUEL_SLOT, ForgeBlockEntity.FUEL_SLOT + 1, false)
						&& !insertItem(original, ForgeBlockEntity.INPUT_SLOT_0, ForgeBlockEntity.INPUT_SLOT_3 + 1, false)) {
					if (index < 33 ? !insertItem(original, 33, 42, false) : !insertItem(original, 6, 33, false))
						return ItemStack.EMPTY;
				}
			} else {
				if (!isValidIngredient(original)) {
					if (index < 33 ? !insertItem(original, 33, 42, false) : !insertItem(original, 6, 33, false))
						return ItemStack.EMPTY;
				} else if (!insertItem(original, ForgeBlockEntity.INPUT_SLOT_0, ForgeBlockEntity.INPUT_SLOT_3 + 1, false)) {
					if (index < 33 ? !insertItem(original, 33, 42, false) : !insertItem(original, 6, 33, false))
						return ItemStack.EMPTY;
				}
			}
		}
		
		if (original.isEmpty()) slot.setStack(ItemStack.EMPTY);
		else slot.markDirty();
		
		if (original.getCount() == result.getCount()) return ItemStack.EMPTY;
		slot.onTakeItem(player, original);
		
		return result;
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return inventory.canPlayerUse(player);
	}
	
	private void addPlayerInventory(PlayerInventory playerInventory) {
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 9; col++)
				addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
	}
	
	private void addPlayerHotbar(PlayerInventory playerInventory) {
		for (int i = 0; i < 9; i++)
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
	}
	
	public int getCookTime() { return propertyDelegate.get(0); }
	public int getCookTimeTotal() { return propertyDelegate.get(1); }
	public int getBurnTime() { return propertyDelegate.get(2); }
	public int getFuelTime() { return propertyDelegate.get(3); }
	
	public ForgeTiers getForgeTier() {
		return blockEntity.getForgeTier();
	}
}