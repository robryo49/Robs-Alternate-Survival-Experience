package robryo49.rase.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class SwordDurabilityMixin {
	
	@Inject(method = "postMine", at = @At("HEAD"), cancellable = true)
	private void customSwordDamage(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof SwordItem) {
			if (state.isIn(BlockTags.SWORD_EFFICIENT)) {
				if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
					stack.damage(1, miner, EquipmentSlot.MAINHAND);
				}
				cir.setReturnValue(true);
			}
		}
	}
}