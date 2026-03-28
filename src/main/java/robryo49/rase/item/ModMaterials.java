package robryo49.rase.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import robryo49.rase.block.ModBlocks;

import java.util.function.Supplier;

public enum ModMaterials {
	FLINT(0, "flint",           () -> ModItems.FLINT_SHARD, () -> Blocks.GRAVEL),
	STONE(0, "stone",           () -> ModItems.STONE_PEBBLES.PEBBLE(), () -> Blocks.COBBLESTONE),
	
	COPPER(1, "copper",         () -> Items.COPPER_INGOT, () -> Blocks.COPPER_BLOCK),
	TIN(1, "tin",               () -> ModItems.TIN.INGOT(), () -> ModBlocks.TIN.BLOCK()),
	BRONZE(1, "bronze",         () -> ModItems.BRONZE.INGOT(), () -> ModBlocks.BRONZE.BLOCK()),
	ZINC(1, "zinc",             () -> ModItems.ZINC.INGOT(), () -> ModBlocks.ZINC.BLOCK()),
	MAGNETITE(1, "magnetite",   () -> ModItems.MAGNETITE.INGOT(), () -> ModBlocks.MAGNETITE.BLOCK()),
	LEAD(1, "lead",             () -> ModItems.LEAD.INGOT(), () -> ModBlocks.LEAD.BLOCK()),
	
	IRON(2, "iron",             () -> Items.IRON_INGOT, () -> Blocks.IRON_BLOCK ),
	SILVER(2, "silver",         () -> ModItems.SILVER.INGOT(), () -> ModBlocks.SILVER.BLOCK()),
	STEEL(2, "steel",           () -> ModItems.STEEL.INGOT(), () -> ModBlocks.STEEL.BLOCK()),
	TITANIUM(2, "titanium",     () -> ModItems.TITANIUM.INGOT(), () -> ModBlocks.TITANIUM.BLOCK()),
	
	LAPIS(3, "lapis",           () -> Items.LAPIS_LAZULI, () -> Blocks.LAPIS_BLOCK),
	GOLD(3, "gold",             () -> Items.GOLD_INGOT, () -> Blocks.GOLD_BLOCK),
	PLATINUM(3, "platinum",     () -> ModItems.PLATINUM.INGOT(), () -> ModBlocks.PLATINUM.BLOCK()),
	TUNGSTEN(3, "tungsten",     () -> ModItems.TUNGSTEN.INGOT(), () -> ModBlocks.TUNGSTEN.BLOCK()),
	
	DIAMOND(4, "diamond",       () -> Items.DIAMOND, () -> Blocks.DIAMOND_BLOCK),
	EMERALD(4, "emerald",       () -> Items.EMERALD, () -> Blocks.EMERALD_BLOCK),
	PALLADIUM(4, "palladium",   () -> ModItems.PALLADIUM.INGOT(), () -> ModBlocks.PALLADIUM.BLOCK()),
	COBALT(4, "cobalt",         () -> ModItems.COBALT.INGOT(), () -> ModBlocks.COBALT.BLOCK()),
	
	NETHERITE(5, "netherite",   () -> Items.NETHERITE_INGOT, () -> Blocks.NETHERITE_BLOCK),
	SCANDIUM(5, "scandium",     () -> ModItems.SCANDIUM.INGOT(), () -> ModBlocks.SCANDIUM.BLOCK()),
	MYTHRIL(5, "mythril",       () -> ModItems.MYTHRIL.INGOT(), () -> ModBlocks.MYTHRIL.BLOCK()),
	RHEXIS(5, "rhexis",         () -> ModItems.RHEXIS.CRYSTAL(), () -> ModBlocks.RHEXIS.BLOCK())
	;
	
	private final int tier;
	private final int miningTier;
	private final String id;
	
	private final Supplier<Item> materialItem;
	private final Supplier<Block> materialBlock;
	
	ModMaterials(final int tier, final String id, Supplier<Item> materialItem, Supplier<Block> materialBlock) {
		this.tier = tier;
		this.miningTier = tier;
		this.id = id;
		this.materialItem = materialItem;
		this.materialBlock = materialBlock;
	}
	
	ModMaterials(final int tier, final int miningTier, final String id, Supplier<Item> materialItem, Supplier<Block> materialBlock) {
		this.tier = tier;
		this.miningTier = miningTier;
		this.id = id;
		this.materialItem = materialItem;
		this.materialBlock = materialBlock;
	}
	
	public int getTier() {
		return tier;
	}
	
	public int getInferiorTier() {
		return tier - 1;
	}
	
	public int getSuperiorTier() {
		return tier + 1;
	}
	
	public String getId() {
		return id;
	}
	
	
	public Item getMaterialItem() {
		return materialItem.get();
	}
	
	public Block getMaterialBlock() {
		return materialBlock.get();
	}
	
	public Supplier<Item> getMaterialItemSupplier() {
		return materialItem;
	}
	
	public Supplier<Block> getMaterialBlockSupplier() {
		return materialBlock;
	}
}
