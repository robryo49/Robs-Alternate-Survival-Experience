package robryo49.rase.screen.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import robryo49.rase.screen.ModScreenHandlers;

public class BasketScreenHandler extends ScreenHandler {
    
    private static final int BASKET_SLOTS = 9;
    private static final int SLOT_X = 62;  // centers a 3×3 in the 176px wide GUI
    private static final int SLOT_Y = 17;
    
    private final Inventory inventory;
    
    public BasketScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.BASKET_SCREEN_HANDLER, syncId);
        checkSize(inventory, BASKET_SLOTS);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new Slot(inventory, col + row * 3,
                        SLOT_X + col * 18, SLOT_Y + row * 18));
            }
        }
        
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }
    
    public BasketScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new net.minecraft.inventory.SimpleInventory(BASKET_SLOTS));
    }
    
    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasStack()) return result;
        
        ItemStack stack = slot.getStack();
        result = stack.copy();
        
        if (index < BASKET_SLOTS) {
            if (!insertItem(stack, BASKET_SLOTS, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            if (!insertItem(stack, 0, BASKET_SLOTS, false)) return ItemStack.EMPTY;
        }
        
        if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY);
        else slot.markDirty();
        
        if (stack.getCount() == result.getCount()) return ItemStack.EMPTY;
        slot.onTakeItem(player, stack);
        
        return result;
    }
    
    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }
    
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, 84 + row * 18));
    }
    
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++)
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }
}