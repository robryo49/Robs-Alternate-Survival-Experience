package robryo49.rase.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlocks {
	
	// --- Collections ---
	
	public static final List<Block> ALL = new ArrayList<>();
	public static final List<Block> TRANSLATABLE = ALL;
	
	public static final Map<Model, List<Block>> MODELS = new HashMap<>();
	public static final Map<RegistryKey<ItemGroup>, List<Block>> GROUPS = new HashMap<>();
	public static final Map<TagKey<Block>, List<Block>> TAGS = new HashMap<>();
	
	// --- Block Registrations ---
	
	
	
	// --- Ore Specific Registration Methods ---
	
	public record OreBlockSet(Block ORE, Block DEEPSLATE_ORE, Block BLOCK, Block RAW_BLOCK) {}
	public record NetherOreBlockSet(Block NETHER_ORE, Block BLOCK, Block RAW_BLOCK) {}
	
	private static Block registerMaterialBlock(String name, float strength, TagKey<Block> toolTier) {
		return registerBlock(name + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.METAL)),
				List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL);
	}
	
	private static OreBlockSet registerOreBlockSet(String name, float strength, TagKey<Block> toolTier) {
		return new OreBlockSet(
				registerBlock(name + "_ore", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.STONE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock("deepslate_" + name + "_ore", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.DEEPSLATE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock(name + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.METAL)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock("raw_" + name + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.STONE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL)
		);
	}
	
	private static NetherOreBlockSet registerNetherOreBlockSet(String name, float strength, TagKey<Block> toolTier) {
		return new NetherOreBlockSet(
				registerBlock("nether_" + name + "_ore", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.NETHER_ORE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock(name + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.METAL)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock("raw_" + name + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.STONE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL)
		);
	}
	
	
	// --- Core Registration Logic ---
	
	private static Block registerBlock(String name, Block block, Model model) {
		registerBlockItem(name, block);
		Block registeredBlock = Registry.register(Registries.BLOCK, Identifier.of(Rase.MOD_ID, name), block);
		ALL.add(registeredBlock);
		MODELS.computeIfAbsent(model, k -> new ArrayList<>()).add(registeredBlock);
		return registeredBlock;
	}
	
	private static Block registerBlock(String name, Block block, List<TagKey<Block>> tags, Model model) {
		Block registeredBlock = registerBlock(name, block, model);
		for (TagKey<Block> tag : tags) TAGS.computeIfAbsent(tag, k -> new ArrayList<>()).add(registeredBlock);
		return registeredBlock;
	}
	
	private static void registerBlockItem(String name, Block block) {
		Registry.register(Registries.ITEM, Identifier.of(Rase.MOD_ID, name), new BlockItem(block, new Item.Settings()));
	}
	
	// --- Item Group Logic ---
	
	private static void addToItemGroup(RegistryKey<ItemGroup> groupRegistryKey, List<Block> blocks) {
		ItemGroupEvents.modifyEntriesEvent(groupRegistryKey).register(entries -> blocks.forEach(entries::add));
	}
	
	public static void registerModBlocks() {
		Rase.LOGGER.info("Registering Mod Blocks for " + Rase.MOD_ID);
		GROUPS.forEach(ModBlocks::addToItemGroup);
	}
}