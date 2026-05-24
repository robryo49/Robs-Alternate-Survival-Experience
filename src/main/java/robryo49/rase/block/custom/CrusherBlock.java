package robryo49.rase.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.block.entity.custom.CrusherBlockEntity;

public class CrusherBlock extends BlockWithEntity {
	
	public static final MapCodec<CrusherBlock> CODEC = createCodec(CrusherBlock::new);
	
	public static final BooleanProperty CRUSHING = BooleanProperty.of("crushing");
	public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
	
	
	public CrusherBlock(Settings settings) {
		super(settings);
		setDefaultState(stateManager.getDefaultState()
				.with(CRUSHING, false)
				.with(FACING, Direction.NORTH));
	}
	
	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(CRUSHING, FACING);
	}
	
	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState()
				.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
				.with(CRUSHING, false);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CrusherBlockEntity(pos, state);
	}
	
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos,
	                             PlayerEntity player, BlockHitResult hit) {
		BlockEntity be = world.getBlockEntity(pos);
		if (!(be instanceof CrusherBlockEntity crusher)) return ActionResult.PASS;
		
		ItemStack held = player.getMainHandStack();
		
		if (!held.isEmpty()) {
			if (crusher.insertItem(held, world)) {
				if (!player.isCreative()) held.decrement(1);
				player.setStackInHand(player.getActiveHand(), held);
				return ActionResult.SUCCESS;
			}
			return ActionResult.FAIL;
		}
		
		ItemStack extracted = crusher.extractItem();
		if (!extracted.isEmpty()) {
			if (!player.getInventory().insertStack(extracted)) {
				world.spawnEntity(new ItemEntity(world,
						player.getX(), player.getY(), player.getZ(), extracted));
			}
			return ActionResult.SUCCESS;
		}
		
		return ActionResult.PASS;
	}
	
	@Override
	protected void onStateReplaced(BlockState state, World world, BlockPos pos,
	                               BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof CrusherBlockEntity crusher) {
				crusher.dropContents(world, pos);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
			World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) return null;
		return validateTicker(type, ModBlockEntities.CRUSHER_BLOCK_ENTITY,
				(w, pos, s, be) -> be.tick(w, pos, s));
	}
}