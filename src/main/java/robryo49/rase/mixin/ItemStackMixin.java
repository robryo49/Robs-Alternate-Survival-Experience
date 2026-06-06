package robryo49.rase.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    
    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void overrideStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        Item item = stack.getItem();
        
        int baseMax = item.getComponents().getOrDefault(DataComponentTypes.MAX_STACK_SIZE, 64);
        
        if (baseMax > 1) {
            if (item instanceof BlockItem) {
                cir.setReturnValue(8);
            } else {
                cir.setReturnValue(16);
            }
        }
    }
}