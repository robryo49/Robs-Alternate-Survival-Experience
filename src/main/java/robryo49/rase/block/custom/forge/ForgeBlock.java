package robryo49.rase.block.custom.forge;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.block.entity.custom.ForgeBlockEntity;

import java.util.List;

public class ForgeBlock extends BlockWithEntity {
	
	public static final MapCodec<ForgeBlock> CODEC = ForgeBlock.createCodec(settings -> new ForgeBlock(settings, ForgeTiers.PRIMITIVE));
	
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty LIT = Properties.LIT;
	
	private final ForgeTiers tier;
	
	public ForgeBlock(Settings settings, ForgeTiers forgeTier) {
		super(settings);
		this.tier = forgeTier;
		this.setDefaultState(this.stateManager.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(LIT, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}
	
	public ForgeTiers getTier() {
		return tier;
	}
	
	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ForgeBlockEntity(pos, state);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() == newState.getBlock()) return;
		
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ForgeBlockEntity forge) {
			ItemScatterer.spawn(world, pos, forge);
			
			if (!world.isClient && world instanceof ServerWorld serverWorld) {
				forge.getRecipesUsedAndDropExperience(serverWorld, Vec3d.ofCenter(pos));
			}
			
			world.updateComparators(pos, this);
		}
		
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory screenHandlerFactory = (ForgeBlockEntity) world.getBlockEntity(pos);
			if (screenHandlerFactory != null) {
				player.openHandledScreen(screenHandlerFactory);
			}
		}
		return ActionResult.SUCCESS;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) return null;
		
		return validateTicker(
				type, ModBlockEntities.FORGE_BLOCK_ENTITY,
				(world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1)
		);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add(Text.empty());
		tooltip.add(
				Text.translatable("item.rase.mold.tooltip.tier")
						.append(": ")
						.formatted(net.minecraft.util.Formatting.GRAY)
						.append(Text.literal(String.valueOf(getTier().getTier())).formatted(net.minecraft.util.Formatting.GOLD)));
		
		super.appendTooltip(stack, context, tooltip, options);
	}
}