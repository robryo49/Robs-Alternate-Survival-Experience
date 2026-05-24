package robryo49.rase.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class MotorBlock extends Block {
    
    public static final MapCodec<MotorBlock> CODEC = createCodec(MotorBlock::new);
    
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    
    public MotorBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(ACTIVE, false));
    }
    
    @Override
    protected MapCodec<? extends Block> getCodec() { return CODEC; }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }
    
    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        // FACING points from motor toward the crusher — same direction player is looking
        return getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing())
                .with(ACTIVE, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }
    
    // React to redstone changes
    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos,
                                  Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) return;
        boolean powered = world.isReceivingRedstonePower(pos);
        if (powered != state.get(ACTIVE)) {
            world.setBlockState(pos, state.with(ACTIVE, powered), 2);
        }
    }
    
    // Also react when first placed next to existing redstone
    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction,
                                                   BlockState neighborState, WorldAccess world,
                                                   BlockPos pos, BlockPos neighborPos) {
        return state;
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}