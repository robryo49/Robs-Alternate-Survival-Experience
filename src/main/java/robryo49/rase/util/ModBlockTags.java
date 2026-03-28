package robryo49.rase.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlockTags {
	
	// --- Collections ---
	
	public static final List<TagKey<Block>> ALL = new ArrayList<>();
	public static final List<TagKey<Block>> TRANSLATABLE = ALL;
	
	public static final Map<TagKey<Block>, List<TagKey<Block>>> PARENT_TAGS = new HashMap<>();
	public static final Map<TagKey<Block>, List<Block>> TAGS = new HashMap<>();
	
	
	// --- Block Tag Registrations
	
	public static final TagKey<Block> NEEDS_BRONZE_TOOL = createTag("needs_bronze_tool");
	public static final TagKey<Block> INCORRECT_FOR_BRONZE_TOOL = createTag("incorrect_for_bronze_tool");
	
	
	public static final TagKey<Block> FORGES = createTag("forges");
	public static final TagKey<Block> ANVILS = createTag("anvils");
	
	public static final TagKey<Block> PRIMITIVE_FORGE_CORE = createTag("primitive_forge_core", List.of(Blocks.IRON_BLOCK));
	public static final TagKey<Block> PRIMITIVE_FORGE_SHELL = createTag("primitive_forge_shell", List.of(Blocks.BRICKS));
	
	public static final TagKey<Block> ADVANCED_FORGE_CORE = createTag("advanced_forge_core", List.of(Blocks.DIAMOND_BLOCK));
	public static final TagKey<Block> ADVANCED_FORGE_SHELL = createTag("advanced_forge_shell", List.of(Blocks.STONE_BRICKS));
	
	
	public static final TagKey<Block> STONE_ORES = createTag("stone_ores", List.of(
			Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.DIAMOND_ORE, Blocks.GOLD_ORE, Blocks.EMERALD_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE
	));
	public static final TagKey<Block> DEEPSLATE_ORES = createTag("deepslate_ores", List.of(
			Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_GOLD_ORE,
			Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE
	));
	public static final TagKey<Block> NETHER_ORES = createTag("nether_ores", List.of(
			Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE
	));
	
	public static final TagKey<Block> ORES = createTagWithParents("ores", List.of(STONE_ORES, DEEPSLATE_ORES, NETHER_ORES));
	
	// --- Core Tag Registration Logic ---
	
	
	private static TagKey<Block> createTag(String name) {
		return createTag(name, List.of(), List.of());
	}
	
	private static TagKey<Block> createTag(String name, List<Block> blocks) {
		return createTag(name, List.of(), blocks);
	}
	
	private static TagKey<Block> createTagWithParents(String name, List<TagKey<Block>> parentTagKeys) {
		return createTag(name, parentTagKeys, List.of());
	}
	
	private static TagKey<Block> createTag(String name, List<TagKey<Block>> parentTagKeys, List<Block> blocks) {
		TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, Rase.getIdentifier(name));
		
		parentTagKeys.forEach(parent -> PARENT_TAGS.computeIfAbsent(tagKey, k -> new ArrayList<>()).add(parent));
		if (!blocks.isEmpty()) TAGS.put(tagKey, blocks);
		
		ALL.add(tagKey);
		
		return tagKey;
	}
	
	public static void registerBlockTags() {
		Rase.LOGGER.info("Registering Block Tags for " + Rase.MOD_ID);
		
		TAGS.put(BlockTags.SWORD_EFFICIENT, List.of(Blocks.TALL_GRASS, Blocks.SHORT_GRASS, Blocks.SUGAR_CANE));
	}
}