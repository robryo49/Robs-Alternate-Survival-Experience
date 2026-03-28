package robryo49.rase.item.custom;

import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class UsableItem extends Item {
	final ItemStack output;
	final SoundEvent useSound;
	
	public UsableItem(Settings settings, int durability, ItemStack output, SoundEvent useSound) {
		super(settings.maxDamage(durability));
		this.output = output;
		this.useSound = useSound;
	}
	
	public UsableItem(Settings settings, int durability, Item output, SoundEvent useSound) {
		super(settings.maxDamage(durability));
		this.output = new ItemStack(output);
		this.useSound = useSound;
	}
	
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}
	
	public SoundEvent getDrinkSound() {
		return useSound;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return 128;
	}
	
	@Override
	public boolean allowComponentsUpdateAnimation(PlayerEntity user, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return oldStack.getItem() != newStack.getItem();
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.setCurrentHand(hand);
		return TypedActionResult.consume(user.getStackInHand(hand));
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTime) {
		if (!world.isClient && user instanceof PlayerEntity player) {
			if (remainingUseTime % 5 == 0) {
				int currentDamage = stack.getDamage();
				
				stack.damage(1, (ServerWorld) world, (ServerPlayerEntity) player, (item) ->
						player.setStackInHand(user.getActiveHand(), output.copy()));
				player.addExhaustion(0.2f);
			}
		}
	}
	
	public int getUseTime() {
		return getDefaultStack().getMaxDamage();
	}
	
	public ItemStack getOutput() {
		return output.copy();
	}
}
