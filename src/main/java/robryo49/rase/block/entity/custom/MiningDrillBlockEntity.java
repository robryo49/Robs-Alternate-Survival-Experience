package robryo49.rase.block.entity.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.block.custom.MiningDrillBlock;
import robryo49.rase.block.entity.ModBlockEntities;

import java.util.List;

public class MiningDrillBlockEntity extends BlockEntity {
	
	private static final float MINING_SPEED = 4f;
	private static final int SOUND_INTERVAL_TICKS = 4;
	
	private float miningProgress = 0f;
	private BlockState lastTargetState = null;
	private int tickCounter = 0;
	
	public MiningDrillBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MINING_DRILL_BLOCK_ENTITY, pos, state);
	}
	
	public void tick(World world, BlockPos pos, BlockState state) {
		if (world.isClient) return;
		
		if (!state.get(MiningDrillBlock.ACTIVE)) {
			if (miningProgress > 0f) {
				miningProgress = Math.max(0f, miningProgress - 0.05f);
				markDirty();
			}
			tickCounter = 0;
			return;
		}
		
		Direction facing = state.get(MiningDrillBlock.FACING);
		BlockPos targetPos = pos.offset(facing);
		BlockState targetState = world.getBlockState(targetPos);
		
		if (targetState.isAir() || targetState.getHardness(world, targetPos) < 0) {
			resetProgress(world, pos);
			return;
		}
		
		if (lastTargetState == null || !lastTargetState.isOf(targetState.getBlock())) {
			resetProgress(world, pos);
			lastTargetState = targetState;
		}
		
		float hardness = targetState.getHardness(world, targetPos);
		miningProgress += MINING_SPEED / (hardness * 20f);
		tickCounter++;
		markDirty();
		
		if (tickCounter % SOUND_INTERVAL_TICKS == 0) {
			var sound = targetState.getSoundGroup();
			world.playSound(null, targetPos,
					sound.getHitSound(), SoundCategory.BLOCKS,
					sound.getVolume() * 0.6f, sound.getPitch() * 0.9f);
		}
		
		int stage = Math.min((int)(miningProgress * 10f), 9);
		((ServerWorld) world).setBlockBreakingInfo(pos.hashCode(), targetPos, stage);
		
		if (miningProgress >= 1f) {
			mine(world, pos, targetPos, targetState, facing);
		}
	}
	
	private void mine(World world, BlockPos drillPos, BlockPos targetPos,
	                  BlockState targetState, Direction facing) {
		if (!(world instanceof ServerWorld serverWorld)) return;
		
		var sound = targetState.getSoundGroup();
		world.playSound(null, targetPos, sound.getBreakSound(),
				SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch());
		
		LootContextParameterSet.Builder lootBuilder = new LootContextParameterSet.Builder(serverWorld)
				.add(LootContextParameters.ORIGIN, Vec3d.ofCenter(targetPos))
				.add(LootContextParameters.TOOL, ItemStack.EMPTY)
				.addOptional(LootContextParameters.BLOCK_ENTITY, world.getBlockEntity(targetPos))
				.add(LootContextParameters.BLOCK_STATE, targetState);
		
		LootTable lootTable = serverWorld.getServer().getReloadableRegistries()
				.getLootTable(targetState.getBlock().getLootTableKey());
		
		List<ItemStack> drops = lootTable.generateLoot(lootBuilder.build(LootContextTypes.BLOCK));
		
		Vec3d dropPos = Vec3d.ofCenter(targetPos);
		for (ItemStack drop : drops) {
			ItemEntity entity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, drop);
			Vec3d vel = Vec3d.of(facing.getVector()).multiply(0.1);
			entity.setVelocity(vel.x, 0.1, vel.z);
			world.spawnEntity(entity);
		}
		
		world.breakBlock(targetPos, false);
		resetProgress(world, drillPos);
	}
	
	private void resetProgress(World world, BlockPos pos) {
		miningProgress = 0f;
		lastTargetState = null;
		tickCounter = 0;
		if (world instanceof ServerWorld sw) {
			Direction facing = getCachedState().get(MiningDrillBlock.FACING);
			sw.setBlockBreakingInfo(pos.hashCode(), pos.offset(facing), -1);
		}
	}
	
	public float getMiningProgress() { return miningProgress; }
	
	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
		super.writeNbt(nbt, lookup);
		nbt.putFloat("MiningProgress", miningProgress);
	}
	
	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
		super.readNbt(nbt, lookup);
		miningProgress = nbt.getFloat("MiningProgress");
	}
	
	@Override
	public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup lookup) {
		return createNbt(lookup);
	}
}