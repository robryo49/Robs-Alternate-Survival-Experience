package robryo49.rase.block.entity.custom;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.block.custom.forge.ForgeBlock;
import robryo49.rase.block.custom.forge.ForgeTiers;
import robryo49.rase.block.entity.ImplementedInventory;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.item.custom.MoldItem;
import robryo49.rase.recipe.custom.ForgeRecipe;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.screen.custom.ForgeScreenHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForgeBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
	
	public static final int INPUT_SLOT_0 = 0, INPUT_SLOT_1 = 1, INPUT_SLOT_2 = 2, INPUT_SLOT_3 = 3;
	public static final int FUEL_SLOT = 4, OUTPUT_SLOT = 5;
	
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
	private int cookTime = 0, cookTimeTotal = 200, burnTime = 0, fuelTime = 0;
	private final ForgeTiers forgeTier;
	private boolean active = true;
	private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap<>();
	private Identifier currentRecipeId = null;
	
	protected final PropertyDelegate propertyDelegate;
	
	
	public ForgeBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FORGE_BLOCK_ENTITY, pos, state);
		this.forgeTier = ((ForgeBlock) state.getBlock()).getTier();
		this.propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> cookTime;
					case 1 -> cookTimeTotal;
					case 2 -> burnTime;
					case 3 -> fuelTime;
					default -> 0;
				};
			}
			
			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> cookTime = value;
					case 1 -> cookTimeTotal = value;
					case 2 -> burnTime = value;
					case 3 -> fuelTime = value;
				}
			}
			
			@Override
			public int size() { return 4; }
		};
	}
	
	
	@Override
	public DefaultedList<ItemStack> getItems() { return inventory; }
	
	@Override
	public Text getDisplayName() { return forgeTier.getDisplayName(); }
	
	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new ForgeScreenHandler(syncId, playerInventory, this, propertyDelegate);
	}
	
	@Override
	public BlockPos getScreenOpeningData(ServerPlayerEntity player) { return pos; }
	
	
	public void tick(World world, BlockPos pos, BlockState state) {
		boolean wasBurning = isBurning();
		boolean dirty = false;
		
		if (world.getTime() % 20 == 0) active = checkForgeStructure(world, pos, state);
		if (isBurning()) if (isActive()) burnTime--; else burnTime = Math.max(0, burnTime-10);
		
		if (isActive() && canSmelt()) {
			if (!isBurning() && tryConsumeFuel()) dirty = true;
			
			if (isBurning()) {
				Optional<RecipeEntry<ForgeRecipe>> recipe = getCurrentRecipe();
				Identifier newRecipeId = recipe.map(RecipeEntry::id).orElse(null);
				
				if (!java.util.Objects.equals(currentRecipeId, newRecipeId)) {
					currentRecipeId = newRecipeId;
					cookTime = 0;
					cookTimeTotal = recipe.map(r -> forgeTier.getSmeltingTime(r.value().cookTime())).orElse(200);
				}
				
				if (cookTime == 0) cookTimeTotal = getCurrentCookTime();
				cookTime++;
				
				if (cookTime >= cookTimeTotal) {
					craftCurrentRecipe(world);
					currentRecipeId = null;
					cookTime = 0;
					dirty = true;
				}
			} else decreaseCookTime();
		} else {
			currentRecipeId = null;
			decreaseCookTime();
		}
		
		if (wasBurning != isBurning()) {
			world.setBlockState(pos, state.with(ForgeBlock.LIT, isBurning()), Block.NOTIFY_ALL);
			dirty = true;
		}
		
		if (dirty) markDirty(world, pos, state);
	}
	
	private boolean checkForgeStructure(World world, BlockPos pos, BlockState state) {
		
		Direction facing = state.get(ForgeBlock.FACING);
		BlockPos forgeCorePos = pos.offset(facing.getOpposite());
		
		if (!forgeTier.isCore(world.getBlockState(forgeCorePos))) return false;
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					BlockPos checkPos = forgeCorePos.add(x, y, z);
					if (!forgeTier.isShell(world.getBlockState(checkPos)) && !checkPos.equals(pos) && !checkPos.equals(forgeCorePos)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void decreaseCookTime() {
		cookTime = Math.max(cookTime - 2, 0);
	}
	
	private boolean tryConsumeFuel() {
		ItemStack fuelStack = inventory.get(FUEL_SLOT);
		if (!isFuel(fuelStack)) return false;
		
		fuelTime = getFuelTime(fuelStack);
		burnTime = fuelTime;
		
		ItemStack remainder = fuelStack.getItem().getRecipeRemainder() != null
				? new ItemStack(fuelStack.getItem().getRecipeRemainder())
				: ItemStack.EMPTY;
		
		fuelStack.decrement(1);
		if (fuelStack.isEmpty()) inventory.set(FUEL_SLOT, remainder);
		
		return isBurning();
	}
	
	private int getCurrentCookTime() {
		return getCurrentRecipe().map(r -> forgeTier.getSmeltingTime(r.value().cookTime()))
				.orElse(cookTimeTotal);
	}
	
	private void craftCurrentRecipe(World world) {
		getCurrentRecipe().ifPresent(recipe -> {
			List<Ingredient> remaining = new ArrayList<>(recipe.value().ingredients());
			
			int slot = INPUT_SLOT_0;
			
			int attempts = 0;
			int maxAttempts = remaining.size() * (INPUT_SLOT_3 + 1);
			
			while (!remaining.isEmpty() && attempts++ < maxAttempts) {
				ItemStack stack = getStack(slot);
				
				for (int i = 0; i < remaining.size(); i++) {
					if (remaining.get(i).test(stack)) {
						removeStack(slot, 1);
						remaining.remove(i);
						break;
					}
				}
				
				slot = (slot + 1) % (INPUT_SLOT_3 + 1);
			}
			
			ItemStack result = recipe.value().result().copy();
			ItemStack mold = getStack(OUTPUT_SLOT);
			
			if (mold.getItem() instanceof MoldItem moldItem) {
				moldItem.setStoredItem(mold, result);
				MoldItem.startCooling(mold, recipe.value().coolTime(), world);
			}
			setLastRecipe(recipe);
		});
	}
	
	private void setLastRecipe(RecipeEntry<?> recipe) {
		if (recipe != null) recipesUsed.addTo(recipe.id(), 1);
	}
	
	public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
		if (!(world instanceof ServerWorld serverWorld)) return;
		
		List<RecipeEntry<?>> recipes = getRecipesUsedAndDropExperience(serverWorld, player.getPos());
		player.unlockRecipes(recipes);
		recipes.forEach(r -> player.onRecipeCrafted(r, inventory));
		recipesUsed.clear();
	}
	
	public List<RecipeEntry<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
		List<RecipeEntry<?>> list = new ArrayList<>();
		recipesUsed.object2IntEntrySet().forEach(entry ->
				world.getRecipeManager().get(entry.getKey()).ifPresent(recipe -> {
					list.add(recipe);
					float xp = recipe.value() instanceof ForgeRecipe fr ? fr.experience()
							: recipe.value() instanceof SmeltingRecipe sr ? sr.getExperience()
							  : 0f;
					dropExperience(world, pos, entry.getIntValue(), xp);
				})
		);
		return list;
	}
	
	private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
		int totalXp = MathHelper.floor(multiplier * experience);
		if (Math.random() < MathHelper.fractionalPart(multiplier * experience)) totalXp++;
		ExperienceOrbEntity.spawn(world, pos, totalXp);
	}
	
	// --- Recipe Lookup ---
	private Optional<RecipeEntry<ForgeRecipe>> getCurrentRecipe() {
		if (getWorld() == null) return Optional.empty();
		return getWorld().getRecipeManager().getFirstMatch(
						ModRecipes.FORGE_RECIPE_TYPE,
						new ForgeRecipe.Input(getStack(INPUT_SLOT_0), getStack(INPUT_SLOT_1),
								getStack(INPUT_SLOT_2), getStack(INPUT_SLOT_3), getStack(OUTPUT_SLOT)),
						getWorld())
				.filter(r -> forgeTier.getTier() >= r.value().tier());
	}
	
	private boolean canSmelt() {
		return getCurrentRecipe().map(r -> canInsertIntoOutputSlot(r.value().result())).orElse(false);
	}
	
	private boolean canInsertIntoOutputSlot(ItemStack output) {
		ItemStack outStack = getStack(OUTPUT_SLOT);
		if (outStack.getItem() instanceof MoldItem moldItem) return moldItem.canStoreItem(outStack, output);
		return false;
	}
	
	
	private boolean isBurning() { return burnTime > 0; }
	private boolean isFuel(ItemStack stack) { return AbstractFurnaceBlockEntity.canUseAsFuel(stack); }
	private int getFuelTime(ItemStack stack) {
		return stack.isEmpty() ? 0 : forgeTier.getFuelTime(AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(stack.getItem(), 0));
	}
	
	
	private boolean isActive() { return active; }
	
	public ForgeTiers getForgeTier() {
		return forgeTier;
	}
	
	
	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		Inventories.writeNbt(nbt, inventory, registryLookup);
		nbt.putInt("forge.cookTime", cookTime);
		nbt.putInt("forge.cookTimeTotal", cookTimeTotal);
		nbt.putInt("forge.burnTime", burnTime);
		nbt.putInt("forge.fuelTime", fuelTime);
		nbt.putInt("forge.active", active ? 1 : 0);
	}
	
	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		Inventories.readNbt(nbt, inventory, registryLookup);
		cookTime = nbt.getInt("forge.cookTime");
		cookTimeTotal = nbt.getInt("forge.cookTimeTotal");
		burnTime = nbt.getInt("forge.burnTime");
		fuelTime = nbt.getInt("forge.fuelTime");
		active = nbt.getInt("forge.active") == 1;
	}
	
	
	@Override
	public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return createNbt(registryLookup);
	}
}