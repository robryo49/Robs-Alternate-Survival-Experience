package robryo49.rase.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class BlockStateMixin {
	@Inject(method = "getHardness", at = @At("RETURN"), cancellable = true)
	private void modifyHardness(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		if (cir.getReturnValue() == 0.0f) {
			cir.setReturnValue(0.2f);
		}
	}
}
