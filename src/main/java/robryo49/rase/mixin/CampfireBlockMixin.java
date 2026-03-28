package robryo49.rase.mixin;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robryo49.rase.block.CampfireFuelAccessor;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {
	
	@Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
	private void getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
		BlockState originalState = cir.getReturnValue();
		
		if (originalState != null && originalState.contains(CampfireBlock.LIT)) {
			cir.setReturnValue(originalState.with(CampfireBlock.LIT, false));
		}
	}
	
	@Inject(method = "onUseWithItem", at = @At("HEAD"), cancellable = true)
	private void handleFuelAndIgnition(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
		BlockEntity be = world.getBlockEntity(pos);
		if (!(be instanceof CampfireBlockEntity campfire)) return;
		
		CampfireFuelAccessor accessor = (CampfireFuelAccessor) campfire;
		Integer fuelValue = FuelRegistry.INSTANCE.get(stack.getItem());
		
		if (fuelValue != null && fuelValue > 0) {
			accessor.rase$addBurnTime(fuelValue);
			if (!player.getAbilities().creativeMode) {
				if (stack.getItem() instanceof BucketItem bucketItem && stack.isOf(Items.LAVA_BUCKET)) {
					player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				} else {
					stack.decrement(1);
				}
			}
			cir.setReturnValue(ItemActionResult.SUCCESS);
		}
	}
}