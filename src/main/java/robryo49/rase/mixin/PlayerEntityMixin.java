package robryo49.rase.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
	private void slowDownMining(BlockState block, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(cir.getReturnValue() / 3.0f);
	}
	
	@Inject(method = "travel", at = @At("HEAD"))
	private void normalizeSprintJump(Vec3d movementInput, CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		
		if (player.isSprinting() && !player.isOnGround()) {
			
			Vec3d velocity = player.getVelocity();
			player.setVelocity(velocity.x * 0.91, velocity.y, velocity.z * 0.91);
		}
	}
}