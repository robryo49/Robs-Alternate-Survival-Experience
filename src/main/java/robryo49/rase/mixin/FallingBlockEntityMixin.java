package robryo49.rase.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.block.custom.forge.SmithingAnvilBlock;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {
	
	@Shadow private BlockState block;
	@Shadow private boolean destroyedOnLanding;
	
	@Inject(
			method = "handleFallDamage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/AnvilBlock;getLandingState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;"
			),
			cancellable = true
	)
	private void interceptAnvilDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (!(this.block.getBlock() instanceof SmithingAnvilBlock anvil)) return;
		
		ModBlocks.SmithingAnvilBlockSet set = anvil.getAnvilBlockSet();
		if (set == null) return;
		
		if (this.block.isOf(set.NORMAL())) {
			this.block = set.CHIPPED().getDefaultState()
					.with(SmithingAnvilBlock.FACING, this.block.get(SmithingAnvilBlock.FACING));
		} else if (this.block.isOf(set.CHIPPED())) {
			this.block = set.DAMAGED().getDefaultState()
					.with(SmithingAnvilBlock.FACING, this.block.get(SmithingAnvilBlock.FACING));
		} else {
			this.destroyedOnLanding = true;
		}
		
		cir.setReturnValue(false);
	}
}