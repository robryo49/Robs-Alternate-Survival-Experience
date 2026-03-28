package robryo49.rase.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robryo49.rase.util.ModItemTags;

import static robryo49.rase.event.ModEvents.getReplacingBlock;

@Mixin(ServerPlayerInteractionManager.class)
public class BlockTransformMixin {
	
	@Shadow @Final
	protected ServerPlayerEntity player;
	@Shadow
	protected ServerWorld world;
	
	@Inject(method = "tryBreakBlock", at = @At("HEAD"), cancellable = true)
	private void rase$interceptBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		BlockState state = world.getBlockState(pos);
		ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
		
		if (stack.isIn(ModItemTags.PRIMITIVE_TOOLS) && !player.isCreative()) {
			Block replacementBlock = getReplacingBlock(state.getBlock());
			
			if (replacementBlock != null) {
				world.syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
				
				if (!world.isClient) {
					BlockEntity blockEntity = world.getBlockEntity(pos);
					
					Block.dropStacks(state, world, pos, blockEntity, player, stack);
					stack.damage(1, world, player, item ->
							player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND)
					);
					
					world.setBlockState(pos, replacementBlock.getDefaultState(), Block.NOTIFY_ALL);
					player.networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, world.getBlockState(pos)));
				}
				
				cir.setReturnValue(false);
			}
		}
	}
}
