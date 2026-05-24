package robryo49.rase.mixin;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    
    @Shadow
    private int foodLevel;
    
    @Inject(method = "add", at = @At("RETURN"))
    private void capFoodAt10(int food, float saturationModifier, CallbackInfo ci) {
        if (this.foodLevel > 10) {
            this.foodLevel = 10;
        }
    }
}