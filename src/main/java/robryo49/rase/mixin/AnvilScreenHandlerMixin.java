package robryo49.rase.mixin;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robryo49.rase.block.custom.forge.SmithingAnvilBlock;
import robryo49.rase.recipe.custom.AnvilSmithingRecipe;
import robryo49.rase.recipe.ModRecipes;

import java.util.Optional;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
	
	@Shadow
	@Final
	private Property levelCost;
	
	public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}
	
	@Unique
	private Optional<AnvilSmithingRecipe> rase$getRecipe() {
		ItemStack left = this.input.getStack(0);
		ItemStack right = this.input.getStack(1);
		
		if (left.isEmpty() || right.isEmpty()) {
			return Optional.empty();
		}
		
		Optional<Integer> anvilTier = this.context.get((world, pos) -> {
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof SmithingAnvilBlock smithingAnvil) {
				return smithingAnvil.getMaterial().getTier();
			}
			return -1;
		});
		
		if (anvilTier.isEmpty() || anvilTier.get() < 0) {
			return Optional.empty();
		}
		
		int tier = anvilTier.get();
		
		return this.player.getWorld().getRecipeManager().getFirstMatch(
						ModRecipes.ANVIL_SMITHING_RECIPE_TYPE,
						new AnvilSmithingRecipe.Input(left, right),
						this.player.getWorld()
				)
				.map(net.minecraft.recipe.RecipeEntry::value)
				.filter(recipe -> tier >= recipe.tier());
	}
	
	@Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
	private void rase$updateResult(CallbackInfo ci) {
		Optional<AnvilSmithingRecipe> match = rase$getRecipe();
		
		if (match.isEmpty()) {
			return;
		}
		
		AnvilSmithingRecipe recipe = match.get();
		
		ItemStack result = recipe.craft(
				new AnvilSmithingRecipe.Input(
						this.input.getStack(0),
						this.input.getStack(1)
				),
				this.player.getWorld().getRegistryManager()
		);
		
		this.output.setStack(0, result);
		this.levelCost.set(this.player.getAbilities().creativeMode ? 0 : recipe.levelCost());
		
		this.sendContentUpdates();
		ci.cancel();
	}
	
	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	private void rase$canTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
		if (rase$getRecipe().isPresent()) {
			cir.setReturnValue(player.getAbilities().creativeMode || player.experienceLevel >= this.levelCost.get());
		}
	}
	
	@Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
	private void rase$onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		Optional<AnvilSmithingRecipe> match = rase$getRecipe();
		
		if (match.isEmpty()) {
			return;
		}
		
		if (!player.getAbilities().creativeMode) {
			player.addExperienceLevels(-this.levelCost.get());
		}
		
		this.input.getStack(0).decrement(1);
		this.input.getStack(1).decrement(1);
		
		this.input.markDirty();
		this.updateResult();
		this.sendContentUpdates();
		
		this.context.run((world, pos) -> {
			BlockState blockState = world.getBlockState(pos);
			
			if (!player.isInCreativeMode() && blockState.isIn(BlockTags.ANVIL)) {
				float damageChance = blockState.getBlock() instanceof SmithingAnvilBlock smithingAnvil
						? 3.0F / smithingAnvil.getMaterial().getDurability()
						: 0.12F;
				
				if (player.getRandom().nextFloat() < damageChance) {
					BlockState damagedState = blockState.getBlock() instanceof SmithingAnvilBlock smithingAnvil
							? smithingAnvil.getDamagedVersion(blockState)
							: AnvilBlock.getLandingState(blockState);
					
					if (damagedState == null) {
						world.removeBlock(pos, false);
						world.syncWorldEvent(WorldEvents.ANVIL_DESTROYED, pos, 0);
					} else {
						world.setBlockState(pos, damagedState, Block.NOTIFY_LISTENERS);
						world.syncWorldEvent(WorldEvents.ANVIL_USED, pos, 0);
					}
				} else {
					world.syncWorldEvent(WorldEvents.ANVIL_USED, pos, 0);
				}
			}
		});
		
		ci.cancel();
	}
}

