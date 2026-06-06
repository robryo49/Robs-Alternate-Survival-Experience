package robryo49.rase.block.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.recipe.custom.DryingRecipe;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntity {

    public static final int SLOT_COUNT = 4;

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(SLOT_COUNT, ItemStack.EMPTY);
    private final int[] dryingTimes = new int[SLOT_COUNT];
    private final int[] dryingTotalTimes = new int[SLOT_COUNT];

    private final RecipeManager.MatchGetter<SingleStackRecipeInput, DryingRecipe> matchGetter =
            RecipeManager.createCachedMatchGetter(ModRecipes.DRYING_RECIPE_TYPE);

    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_RACK_BLOCK_ENTITY, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, DryingRackBlockEntity rack) {
        boolean isDrying = isDrying(state);
        boolean dirty = false;

        for (int i = 0; i < SLOT_COUNT; i++) {
            ItemStack stack = rack.items.get(i);
            if (stack.isEmpty()) continue;

            if (!isDrying) {
                if (rack.dryingTimes[i] > 0) {
                    rack.dryingTimes[i] = MathHelper.clamp(rack.dryingTimes[i] - 2, 0, rack.dryingTotalTimes[i]);
                    dirty = true;
                }
                continue;
            }

            rack.dryingTimes[i]++;
            dirty = true;

            if (rack.dryingTimes[i] >= rack.dryingTotalTimes[i]) {
                SingleStackRecipeInput input = new SingleStackRecipeInput(stack);
                ItemStack result = rack.matchGetter
                        .getFirstMatch(input, world)
                        .map(entry -> entry.value().craft(input, world.getRegistryManager()))
                        .orElse(ItemStack.EMPTY);

                if (!result.isEmpty()) {
                    ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, result);
                }
                rack.items.set(i, ItemStack.EMPTY);
                rack.dryingTimes[i] = 0;
                rack.dryingTotalTimes[i] = 0;
                world.updateListeners(pos, state, state, 3);
            }
        }

        if (dirty) markDirty(world, pos, state);
    }

    private static boolean isDrying(BlockState state) {
        return state.get(robryo49.rase.block.custom.DryingRackBlock.DRY);
    }

    public boolean addItem(ItemStack stack, World world) {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items.get(i).isEmpty()) continue;

            Optional<DryingRecipe> recipe = matchGetter
                    .getFirstMatch(new SingleStackRecipeInput(stack), world)
                    .map(RecipeEntry::value);

            if (recipe.isEmpty()) return false;

            items.set(i, stack.copyWithCount(1));
            stack.decrement(1);
            dryingTimes[i] = 0;
            dryingTotalTimes[i] = recipe.get().dryingTime();
            markDirty(world, getPos(), world.getBlockState(getPos()));
            world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
            return true;
        }
        return false;
    }

    public DefaultedList<ItemStack> getItems() { return items; }
    public int getDryingTime(int slot) { return dryingTimes[slot]; }
    public int getDryingTotalTime(int slot) { return dryingTotalTimes[slot]; }

    public void dropAll(World world, BlockPos pos) {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) ItemScatterer.spawn(world, pos, items);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(nbt, lookup);
        Inventories.writeNbt(nbt, items, true, lookup);
        nbt.putIntArray("DryingTimes", dryingTimes);
        nbt.putIntArray("DryingTotalTimes", dryingTotalTimes);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        items.clear();
        Inventories.readNbt(nbt, items, lookup);
        if (nbt.contains("DryingTimes", 11)) {
            int[] saved = nbt.getIntArray("DryingTimes");
            System.arraycopy(saved, 0, dryingTimes, 0, Math.min(saved.length, SLOT_COUNT));
        }
        if (nbt.contains("DryingTotalTimes", 11)) {
            int[] saved = nbt.getIntArray("DryingTotalTimes");
            System.arraycopy(saved, 0, dryingTotalTimes, 0, Math.min(saved.length, SLOT_COUNT));
        }
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup lookup) {
        NbtCompound nbt = new NbtCompound();
        Inventories.writeNbt(nbt, items, true, lookup);
        return nbt;
    }
}