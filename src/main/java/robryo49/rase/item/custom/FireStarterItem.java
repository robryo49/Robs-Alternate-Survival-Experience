package robryo49.rase.item.custom;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireStarterItem extends Item {
	float fireProbability;
	
	public FireStarterItem(Settings settings, int durability, float fireProbability) {
		super(settings.maxDamage(durability));
		this.fireProbability = fireProbability;
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);
		
		boolean isLitTarget = CampfireBlock.canBeLit(state) || CandleBlock.canBeLit(state) || CandleCakeBlock.canBeLit(state);
		BlockPos firePos = pos.offset(context.getSide());
		boolean canPlaceFire = AbstractFireBlock.canPlaceAt(world, firePos, context.getHorizontalPlayerFacing());
		
		if (isLitTarget || canPlaceFire) {
			if (context.getPlayer() != null) {
				context.getPlayer().setCurrentHand(context.getHand());
			}
			return ActionResult.CONSUME;
		}
		
		return ActionResult.FAIL;
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (remainingUseTicks % 4 != 0) return;
		
		var hit = user.raycast(5.0, 0.0f, false);
		if (hit.getType() != HitResult.Type.BLOCK) return;
		
		BlockHitResult blockHit = (BlockHitResult) hit;
		BlockPos pos = blockHit.getBlockPos();
		Vec3d hitPos = blockHit.getPos();
		BlockState state = world.getBlockState(pos);
		
		if (world.isClient) {
			world.addParticle(ParticleTypes.SMOKE, hitPos.x, hitPos.y, hitPos.z, 0, 0.02, 0);
		}
		
		world.playSound(null, pos, SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC, SoundCategory.BLOCKS,
				0.5f, world.getRandom().nextFloat() * 0.4f + 1.5f);
		
		if (!world.isClient) {
			stack.damage(1, user, LivingEntity.getSlotForHand(user.getActiveHand()));
			if (user instanceof PlayerEntity p) p.addExhaustion(0.1f);
			
			if (world.random.nextFloat() <= fireProbability) {
				boolean success = false;
				BlockPos targetPos = pos;
				
				if (CampfireBlock.canBeLit(state) || CandleBlock.canBeLit(state) || CandleCakeBlock.canBeLit(state)) {
					if (!state.get(Properties.LIT)) {
						world.setBlockState(pos, state.with(Properties.LIT, true));
						success = true;
					}
				} else {
					targetPos = pos.offset(blockHit.getSide());
					if (world.isAir(targetPos) && AbstractFireBlock.canPlaceAt(world, targetPos, user.getHorizontalFacing())) {
						world.setBlockState(targetPos, AbstractFireBlock.getState(world, targetPos));
						success = true;
					}
				}
				
				if (success) {
					world.playSound(null, targetPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
					user.stopUsingItem();
				}
			}
		}
		
		
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 72000;
	}
}
