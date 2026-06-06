package robryo49.rase.block.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import robryo49.rase.block.custom.BasketBlock;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.screen.custom.BasketScreenHandler;

public class BasketBlockEntity extends LootableContainerBlockEntity {
	
	public static final int SIZE = 9;
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
	
	private final ViewerCountManager stateManager = new ViewerCountManager() {
		@Override
		protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
			world.playSound(null,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_DOWN, SoundCategory.BLOCKS,
					0.5f, world.random.nextFloat() * 0.1f + 0.9f);
			world.setBlockState(pos, state.with(BasketBlock.OPEN, true), 3);
		}
		
		@Override
		protected void onContainerClose(World world, BlockPos pos, BlockState state) {
			world.playSound(null,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					SoundEvents.BLOCK_BIG_DRIPLEAF_TILT_UP, SoundCategory.BLOCKS,
					0.5f, world.random.nextFloat() * 0.1f + 0.9f);
			world.setBlockState(pos, state.with(BasketBlock.OPEN, false), 3);
		}
		
		@Override
		protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state,
		                                   int oldViewerCount, int newViewerCount) {
		}
		
		@Override
		protected boolean isPlayerViewing(PlayerEntity player) {
			if (player.currentScreenHandler instanceof BasketScreenHandler) {
				return ((BasketScreenHandler) player.currentScreenHandler).getInventory() == BasketBlockEntity.this;
			}
			return false;
		}
	};
	
	public BasketBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.BASKET_BLOCK_ENTITY_TYPE, pos, state);
	}
	
	@Override
	public int size() { return SIZE; }
	
	@Override
	protected DefaultedList<ItemStack> getHeldStacks() { return inventory; }
	
	@Override
	protected void setHeldStacks(DefaultedList<ItemStack> list) { this.inventory = list; }
	
	@Override
	protected Text getContainerName() { return Text.translatable("block.rase.basket"); }
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new BasketScreenHandler(syncId, playerInventory, this);
	}
	
	@Override
	public void onOpen(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
	
	@Override
	public void onClose(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
	
	public void tick() {
		if (!this.removed) {
			this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
		}
	}
}