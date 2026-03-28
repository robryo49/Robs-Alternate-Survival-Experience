package robryo49.rase.item.custom;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import robryo49.rase.block.CampfireFuelAccessor;

public class FireStarterItem extends Item {
	private final float fireProbability;
	private final SoundEvent useSound;
	
	public FireStarterItem(Settings settings, int durability, float fireProbability, SoundEvent useSound) {
		super(settings.maxDamage(durability));
		this.fireProbability = fireProbability;
		this.useSound = useSound;
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);
		
		if (canIgnite(context)) {
			if (context.getPlayer() != null) {
				context.getPlayer().setCurrentHand(context.getHand());
			}
			return ActionResult.CONSUME;
		}
		return ActionResult.FAIL;
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (remainingUseTicks % 5 != 0) return;
		
		user.swingHand(user.getActiveHand());
		HitResult hit = user.raycast(5.0, 0.0f, false);
		
		if (!(hit instanceof BlockHitResult blockHit)) return;
		
		BlockPos pos = blockHit.getBlockPos();
		BlockState state = world.getBlockState(pos);
		world.playSound(null, pos, useSound, SoundCategory.BLOCKS, 0.4f, 1.5f);
		
		user.swingHand(user.getActiveHand());
		if (world.isClient) {
			world.addParticle(ParticleTypes.SMOKE, blockHit.getPos().x, blockHit.getPos().y, blockHit.getPos().z, 0, 0.05, 0);
			return;
		}
		
		stack.damage(1, user, LivingEntity.getSlotForHand(user.getActiveHand()));
		if (user instanceof PlayerEntity p) {
			p.addExhaustion(0.2f);
		}
		
		if (world.random.nextFloat() <= fireProbability && tryIgnite(world, pos, state, blockHit, user)) {
			user.stopUsingItem();
			world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 0.4f, 1.5f);
		}
	}
	
	private boolean canLitCampfire(World world, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof CampfireBlock) {
			if (world.getBlockEntity(pos) instanceof CampfireFuelAccessor accessor) {
				return CampfireBlock.canBeLit(state) && accessor.rase$getBurnTime() > 0;
			}
		}
		return CampfireBlock.canBeLit(state);
	}
	
	private boolean canLit(World world, BlockPos pos, BlockState state) {
		return canLitCampfire(world, pos, state) || CandleBlock.canBeLit(state) || CandleCakeBlock.canBeLit(state);
	}
	
	private boolean canIgnite(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);
		
		return canLit(world, pos, state) || AbstractFireBlock.canPlaceAt(world, pos.offset(context.getSide()), context.getHorizontalPlayerFacing());
	}
	
	private boolean tryIgnite(World world, BlockPos pos, BlockState state, BlockHitResult hit, LivingEntity user) {
		if (canLit(world, pos, state)) {
			if (!state.get(Properties.LIT)) {
				world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL);
				world.emitGameEvent(user, GameEvent.BLOCK_CHANGE, pos);
				return true;
			}
			return false;
		}
		
		BlockPos firePos = pos.offset(hit.getSide());
		if (AbstractFireBlock.canPlaceAt(world, firePos, user.getHorizontalFacing())) {
			world.setBlockState(firePos, AbstractFireBlock.getState(world, firePos), Block.NOTIFY_ALL);
			world.emitGameEvent(user, GameEvent.BLOCK_PLACE, firePos);
			return true;
		}
		
		return false;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.NONE;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 72000;
	}
}