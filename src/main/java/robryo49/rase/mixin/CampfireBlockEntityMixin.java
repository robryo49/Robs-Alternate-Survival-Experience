package robryo49.rase.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robryo49.rase.block.CampfireFuelAccessor;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin extends BlockEntity implements CampfireFuelAccessor {
	
	@Unique private int burnTime = 0;
	
	public CampfireBlockEntityMixin(BlockPos pos, BlockState state) {
		super(BlockEntityType.CAMPFIRE, pos, state);
	}
	
	@Inject(method = "litServerTick", at = @At("HEAD"))
	private static void tickBurnTime(World world, BlockPos pos, BlockState state, CampfireBlockEntity entity, CallbackInfo ci) {
		CampfireBlockEntityMixin self = (CampfireBlockEntityMixin) (Object) entity;
		
		if (state.get(CampfireBlock.LIT) && self != null) {
			
			if (world.isRaining() && world.isSkyVisible(pos) && world.getBiome(pos).value().getPrecipitation(pos) == Biome.Precipitation.RAIN) {
				extinguishCampfire(world, pos, state);
				return;
			}
			
			if (self.burnTime > 0) {
				self.burnTime--;
			} else {
				extinguishCampfire(world, pos, state);
			}
		}
	}
	
	@Unique
	private static void extinguishCampfire(World world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state.with(CampfireBlock.LIT, false));
		world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 1.0f);
	}
	
	@Inject(method = "writeNbt", at = @At("TAIL"))
	protected void saveBurnTime(NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
		nbt.putInt("BurnTime", this.burnTime);
	}
	
	@Inject(method = "readNbt", at = @At("TAIL"))
	protected void loadBurnTime(NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
		this.burnTime = nbt.getInt("BurnTime");
	}
	
	@Override
	public void rase$addBurnTime(int amount) {
		this.burnTime += amount;
	}
	
	@Override
	public int rase$getBurnTime() {
		return burnTime;
	}
}