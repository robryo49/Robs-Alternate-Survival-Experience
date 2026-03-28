package robryo49.rase.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robryo49.rase.util.ModItemTags;

@Mixin(net.minecraft.item.Item.class)
public class FoodPoisonMixin {
	
	@Inject(method = "finishUsing", at = @At("HEAD"))
	public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		if (stack.isIn(ModItemTags.RAW_FOOD) && world.getRandom().nextFloat() < 0.5F) {
			user.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*(world.getRandom().nextInt(5) + 5)));
		}
	}
}
