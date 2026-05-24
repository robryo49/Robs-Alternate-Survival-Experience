package robryo49.rase.block.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.block.custom.CrusherBlock;
import robryo49.rase.block.custom.MotorBlock;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.recipe.custom.CrushingRecipe;

public class CrusherBlockEntity extends BlockEntity {
	
	private ItemStack inputItem = ItemStack.EMPTY;
	private ItemStack outputItem = ItemStack.EMPTY;
	
	private int crushingTime = 0;
	private int crushingTimeTotal = 0;
	
	private final RecipeManager.MatchGetter<SingleStackRecipeInput, CrushingRecipe> matchGetter =
			RecipeManager.createCachedMatchGetter(ModRecipes.CRUSHING_RECIPE_TYPE);
	
	public CrusherBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.CRUSHER_BLOCK_ENTITY, pos, state);
	}
	
	public void tick(World world, BlockPos pos, BlockState state) {
		if (world.isClient) return;
		
		boolean hasPower = hasValidMotor(world, pos);
		boolean hasInput = !inputItem.isEmpty();
		boolean hasOutput = !outputItem.isEmpty();
		
		if (hasOutput || !hasInput || !hasPower) {
			if (state.get(CrusherBlock.CRUSHING)) {
				world.setBlockState(pos, state.with(CrusherBlock.CRUSHING, false), 2);
			}
			if (!hasPower || !hasInput) {
				crushingTime = 0;
			}
			return;
		}
		
		if (crushingTimeTotal == 0) {
			RecipeEntry<CrushingRecipe> recipe = matchGetter
					.getFirstMatch(new SingleStackRecipeInput(inputItem), world)
					.orElse(null);
			
			if (recipe == null) {
				if (state.get(CrusherBlock.CRUSHING)) {
					world.setBlockState(pos, state.with(CrusherBlock.CRUSHING, false), 2);
				}
				return;
			}
			
			crushingTimeTotal = recipe.value().crushingTime();
		}
		
		if (!state.get(CrusherBlock.CRUSHING)) {
			world.setBlockState(pos, state.with(CrusherBlock.CRUSHING, true), 2);
		}
		
		crushingTime++;
		
		if (crushingTime >= crushingTimeTotal) {
			RecipeEntry<CrushingRecipe> recipe = matchGetter
					.getFirstMatch(new SingleStackRecipeInput(inputItem), world)
					.orElse(null);
			
			if (recipe != null) {
				outputItem = recipe.value().craft(
						new SingleStackRecipeInput(inputItem),
						world.getRegistryManager());
				inputItem = ItemStack.EMPTY;
			}
			
			crushingTime = 0;
			crushingTimeTotal = 0;
			
			world.setBlockState(pos, state.with(CrusherBlock.CRUSHING, false), 2);
			markDirty(world, pos, state);
			world.updateListeners(pos, state, state, 3);
		}
		
		markDirty(world, pos, state);
	}
	
	private boolean hasValidMotor(World world, BlockPos pos) {
		for (Direction dir : Direction.Type.HORIZONTAL) {
			BlockPos neighborPos = pos.offset(dir);
			BlockState neighbor = world.getBlockState(neighborPos);
			
			if (!(neighbor.getBlock() instanceof MotorBlock)) continue;
			if (!neighbor.get(MotorBlock.ACTIVE)) continue;
			
			Direction motorFacing = neighbor.get(MotorBlock.FACING);
			if (motorFacing == dir.getOpposite()) return true;
		}
		return false;
	}
	
	public boolean insertItem(ItemStack stack, World world) {
		if (!inputItem.isEmpty() || !outputItem.isEmpty()) return false;
		
		RecipeEntry<CrushingRecipe> recipe = matchGetter
				.getFirstMatch(new SingleStackRecipeInput(stack), world)
				.orElse(null);
		
		if (recipe == null) return false;
		
		inputItem = stack.copyWithCount(1);
		crushingTimeTotal = recipe.value().crushingTime();
		crushingTime = 0;
		
		markDirty(world, getPos(), getCachedState());
		world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
		return true;
	}
	
	public ItemStack extractItem() {
		if (!outputItem.isEmpty()) {
			ItemStack result = outputItem.copy();
			outputItem = ItemStack.EMPTY;
			crushingTime = 0;
			crushingTimeTotal = 0;
			markDirty();
			return result;
		}
		if (!inputItem.isEmpty()) {
			ItemStack result = inputItem.copy();
			inputItem = ItemStack.EMPTY;
			crushingTime = 0;
			crushingTimeTotal = 0;
			markDirty();
			return result;
		}
		return ItemStack.EMPTY;
	}
	
	public void dropContents(World world, BlockPos pos) {
		if (!inputItem.isEmpty()) {
			world.spawnEntity(new ItemEntity(world,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, inputItem));
			inputItem = ItemStack.EMPTY;
		}
		if (!outputItem.isEmpty()) {
			world.spawnEntity(new ItemEntity(world,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, outputItem));
			outputItem = ItemStack.EMPTY;
		}
	}
	
	public ItemStack getInputItem() { return inputItem; }
	public ItemStack getOutputItem() { return outputItem; }
	public int getCrushingTime() { return crushingTime; }
	public int getCrushingTimeTotal() { return crushingTimeTotal; }
	
	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
		super.writeNbt(nbt, lookup);
		if (!inputItem.isEmpty()) nbt.put("InputItem", inputItem.encode(lookup));
		if (!outputItem.isEmpty()) nbt.put("OutputItem", outputItem.encode(lookup));
		nbt.putInt("CrushingTime", crushingTime);
		nbt.putInt("CrushingTimeTotal", crushingTimeTotal);
	}
	
	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
		super.readNbt(nbt, lookup);
		inputItem = nbt.contains("InputItem")
				? ItemStack.fromNbtOrEmpty(lookup, nbt.getCompound("InputItem"))
				: ItemStack.EMPTY;
		outputItem = nbt.contains("OutputItem")
				? ItemStack.fromNbtOrEmpty(lookup, nbt.getCompound("OutputItem"))
				: ItemStack.EMPTY;
		crushingTime = nbt.getInt("CrushingTime");
		crushingTimeTotal = nbt.getInt("CrushingTimeTotal");
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