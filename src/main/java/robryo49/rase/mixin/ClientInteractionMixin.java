package robryo49.rase.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robryo49.rase.util.ModItemTags;

import static robryo49.rase.event.ModEvents.getReplacingBlock;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientInteractionMixin {
	
	@Shadow @Final private MinecraftClient client;
	
	@Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
	private void rase$preventClientBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (client.player != null && !client.player.isCreative()) {
			ItemStack stack = client.player.getMainHandStack();
			
			if (stack.isIn(ModItemTags.PRIMITIVE_TOOLS)) {
				BlockState state = client.world.getBlockState(pos);
				
				if (getReplacingBlock(state.getBlock()) != null) {
					cir.setReturnValue(false);
				}
			}
		}
	}
}