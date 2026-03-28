package robryo49.rase.item.custom;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import robryo49.rase.Rase;
import robryo49.rase.block.custom.forge.ForgeTiers;

import java.util.List;

public class MoldItem extends Item {
	
	public static final ComponentType<ItemStack> STORED_ITEM_COMPONENT = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			Identifier.of(Rase.MOD_ID, "stored_item"),
			ComponentType.<ItemStack>builder()
					.codec(ItemStack.CODEC)
					.packetCodec(ItemStack.PACKET_CODEC)
					.build()
	);
	
	public static final ComponentType<Integer> COOLING_TIME = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			Identifier.of(Rase.MOD_ID, "cooling_time"),
			ComponentType.<Integer>builder()
					.codec(Codec.INT)
					.packetCodec(PacketCodecs.INTEGER)
					.build()
	);
	
	public static final ComponentType<Integer> COOLING_START_TIME = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			Identifier.of(Rase.MOD_ID, "cooling_start_time"),
			ComponentType.<Integer>builder()
					.codec(Codec.INT)
					.packetCodec(PacketCodecs.INTEGER)
					.build()
	);
	
	private final MoldMaterials.MoldMaterial material;
	private final TagKey<Item> acceptedItems;
	private final String patternName;
	
	public MoldItem(Settings settings, MoldMaterials.MoldMaterial material, TagKey<Item> acceptedItems, String patternName) {
		super(settings.maxCount(1).maxDamage(material.durability()));
		this.material = material;
		this.acceptedItems = acceptedItems;
		this.patternName = patternName;
	}
	
	public MoldMaterials.MoldMaterial getMaterial() {
		return material;
	}
	
	public String getMaterialName() {
		return material.name();
	}
	
	public ForgeTiers.ForgeTier getForgeTier() {
		return material.forgeTier();
	}
	
	public String getPatternName() {
		return patternName;
	}
	
	public TagKey<Item> getAcceptedItems() {
		return acceptedItems;
	}
	
	public boolean acceptsItem(ItemStack item) {
		return item.isIn(acceptedItems);
	}
	
	public static ItemStack getStoredItem(ItemStack mold) {
		ItemStack stored = mold.get(MoldItem.STORED_ITEM_COMPONENT);
		return stored == null ? ItemStack.EMPTY : stored;
	}
	
	public void setStoredItem(ItemStack mold, ItemStack stack) {
		if (stack.isEmpty()) {
			mold.remove(MoldItem.STORED_ITEM_COMPONENT);
		} else {
			mold.set(MoldItem.STORED_ITEM_COMPONENT, stack.copyWithCount(1));
		}
	}
	
	public void removeStoredItem(ItemStack mold) {
		setStoredItem(mold, ItemStack.EMPTY);
	}
	
	public ItemStack takeStoredItem(ItemStack mold) {
		ItemStack itemStack = getStoredItem(mold);
		removeStoredItem(mold);
		return itemStack;
	}
	
	public boolean hasStoredItem(ItemStack mold) {
		return !getStoredItem(mold).isEmpty();
	}
	
	public boolean canStoreItem(ItemStack mold, ItemStack item) {
		return item.isIn(acceptedItems) && !hasStoredItem(mold);
	}
	
	public static int getCoolingTime(ItemStack mold) {
		return mold.getOrDefault(COOLING_TIME, 0);
	}
	
	public static void setCoolingTime(ItemStack mold, int time) {
		if (time <= 0) {
			mold.remove(COOLING_TIME);
		} else {
			mold.set(COOLING_TIME, time);
		}
	}
	
	public static int getCoolingStartTime(ItemStack mold) {
		return mold.getOrDefault(COOLING_START_TIME, 0);
	}
	
	public static void setCoolingStartTime(ItemStack mold, int time) {
		if (time <= 0) {
			mold.remove(COOLING_START_TIME);
		} else {
			mold.set(COOLING_START_TIME, time);
		}
	}
	
	public static void startCooling(ItemStack mold, int time, World world) {
		setCoolingTime(mold, time);
		setCoolingStartTime(mold, Math.round(world.getTime()));
	}
	
	public boolean isCool(ItemStack mold, World world) {
		return world.getTime() >= getCoolingTime(mold) + getCoolingStartTime(mold);
	}
	
	@Override
	public int getMaxCount() {
		return super.getMaxCount();
	}
	
	@Override
	public boolean onClicked(ItemStack mold, ItemStack itemStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType == ClickType.RIGHT) {
			ItemStack cursorItemStack = cursorStackReference.get();
			if (cursorItemStack.isEmpty()) {
				if (hasStoredItem(mold) && isCool(mold, player.getWorld())) {
					cursorStackReference.set(takeStoredItem(mold));
					setCoolingTime(mold, 0);
					
					if (!player.isCreative() && player instanceof ServerPlayerEntity serverPlayer) {
						mold.damage(1, (ServerWorld) player.getWorld(), serverPlayer, (item) ->
								player.sendEquipmentBreakStatus(itemStack.getItem(), EquipmentSlot.MAINHAND));
					}
					
					return true;
				}
			} else {
				if (canStoreItem(mold, cursorItemStack)) {
					setStoredItem(mold, cursorItemStack);
					cursorItemStack.decrement(1);
					cursorStackReference.set(cursorItemStack);
					
					startCooling(mold, 100, player.getWorld());
					
					return true;
				}
			}
		}
		return super.onClicked(mold, itemStack, slot, clickType, player, cursorStackReference);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (!hasStoredItem(stack)) return;
		
		// Show stored item
		tooltip.add(Text.translatable("item.rase.mold.tooltip.held_item")
				.append(": ").append(getStoredItem(stack).getName()));
		
		// Get the client world for the time check
		net.minecraft.client.world.ClientWorld world = net.minecraft.client.MinecraftClient.getInstance().world;
		if (world != null) {
			long ticksLeft = getCoolingTime(stack) + getCoolingStartTime(stack) - world.getTime();
			
			if (ticksLeft > 0) {
				float seconds = ticksLeft / 20.0f;
				tooltip.add(Text.translatable("item.rase.mold.tooltip.cooling_time")
						.append(": " + String.format("%.1fs", seconds))
						.formatted(net.minecraft.util.Formatting.RED));
			} else {
				tooltip.add(Text.translatable("item.rase.mold.tooltip.cooled")
						.formatted(net.minecraft.util.Formatting.GREEN));
			}
		}
	}
}
