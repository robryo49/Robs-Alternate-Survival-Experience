package robryo49.rase.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.block.entity.custom.MiningDrillBlockEntity;

public class MiningDrillBlock extends BlockWithEntity {
	
	public static final MapCodec<MiningDrillBlock> CODEC = createCodec(MiningDrillBlock::new);
	
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
	
	public MiningDrillBlock(Settings settings) {
		super(settings);
		setDefaultState(stateManager.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(ACTIVE, false));
	}
	
	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, ACTIVE);
	}
	
	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}
	
	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos,
	                              Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if (world.isClient) return;
		boolean powered = world.isReceivingRedstonePower(pos);
		if (powered != state.get(ACTIVE)) {
			world.setBlockState(pos, state.with(ACTIVE, powered), Block.NOTIFY_ALL);
		}
	}
	
	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MiningDrillBlockEntity(pos, state);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }
	
	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
			World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) return null;
		return validateTicker(type, ModBlockEntities.MINING_DRILL_BLOCK_ENTITY,
				(w, pos, s, be) -> be.tick(w, pos, s));
	}
}