package robryo49.rase.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MotorBlock extends Block {

    public static final MapCodec<MotorBlock> CODEC = createCodec(MotorBlock::new);

    public static final DirectionProperty FACING = Properties.FACING;
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
        return getDefaultState()
                .with(FACING, ctx.getPlayerLookDirection().getOpposite())
                .with(ACTIVE, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
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

    public static boolean isActiveMotorFacing(World world, BlockPos motorPos, Direction outputDir) {
        BlockState state = world.getBlockState(motorPos);
        if (!(state.getBlock() instanceof MotorBlock)) return false;
        return state.get(ACTIVE) && state.get(FACING) == outputDir;
    }
}