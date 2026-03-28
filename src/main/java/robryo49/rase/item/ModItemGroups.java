package robryo49.rase.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;

import java.util.List;

public class ModItemGroups {
	
	// --- Collections ---
	
	public static final List<ItemGroup> ALL = List.of();
	public static final List<ItemGroup> TRANSLATABLE = ALL;
	
	// --- Group Registrations ---
	
	
	
	// --- Core Registration Logic ---
	
	public static ItemGroup registerItemGroup(String id, Item displayItem, List<Item> items) {
		return Registry.register(Registries.ITEM_GROUP,
				Identifier.of(Rase.MOD_ID, id),
				FabricItemGroup.builder()
						.icon(() -> new ItemStack(displayItem))
						.displayName(Text.translatable("itemgroup.rase." + id))
						.entries((displayContext, entries) -> {for (Item item: items) entries.add(item);})
						.build());
	}
	
	public static ItemGroup registerItemGroup(String id, Block displayBlock, List<Block> blocks) {
		return Registry.register(Registries.ITEM_GROUP,
				Identifier.of(Rase.MOD_ID, id),
				FabricItemGroup.builder()
						.icon(() -> new ItemStack(displayBlock))
						.displayName(Text.translatable("itemgroup.rase." + id))
						.entries((displayContext, entries) -> {for (Block block: blocks) entries.add(block);})
						.build());
	}
	
	public static void registerItemGroups() {
		Rase.LOGGER.info("Registering Item Groups for " + Rase.MOD_ID);
		
	}
}