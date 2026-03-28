package robryo49.rase.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.item.ModMaterials;

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
	
	public static final TagKey<Block> NEEDS_TIER_5_TOOLS = createTag("needs_tier_5_tool");
	public static final TagKey<Block> INCORRECT_FOR_TIER_5_TOOLS = createTag("incorrect_for_tier_5_tool");
	
	public static final TagKey<Block> NEEDS_TIER_4_TOOLS = createTag("needs_tier_4_tool");
	public static final TagKey<Block> INCORRECT_FOR_TIER_4_TOOLS = createTagWithParents("incorrect_for_tier_4_tool", List.of(NEEDS_TIER_5_TOOLS, INCORRECT_FOR_TIER_5_TOOLS));
	
	public static final TagKey<Block> NEEDS_TIER_3_TOOLS = createTag("needs_tier_3_tool");
	public static final TagKey<Block> INCORRECT_FOR_TIER_3_TOOLS = createTagWithParents("incorrect_for_tier_3_tool", List.of(NEEDS_TIER_4_TOOLS, INCORRECT_FOR_TIER_4_TOOLS));
	
	public static final TagKey<Block> NEEDS_TIER_2_TOOLS = createTag("needs_tier_2_tool");
	public static final TagKey<Block> INCORRECT_FOR_TIER_2_TOOLS = createTagWithParents("incorrect_for_tier_2_tool", List.of(NEEDS_TIER_3_TOOLS, INCORRECT_FOR_TIER_3_TOOLS));
	
	public static final TagKey<Block> NEEDS_TIER_1_TOOLS = createTag("needs_tier_1_tool");
	public static final TagKey<Block> INCORRECT_FOR_TIER_1_TOOLS = createTagWithParents("incorrect_for_tier_1_tool", List.of(NEEDS_TIER_2_TOOLS, INCORRECT_FOR_TIER_2_TOOLS));
	
	public static final TagKey<Block> NEEDS_TIER_0_TOOLS = createTag("needs_tier_0_tool");
	public static final TagKey<Block> INCORRECT_FOR_TIER_0_TOOLS = createTagWithParents("incorrect_for_tier_0_tool", List.of(NEEDS_TIER_1_TOOLS, INCORRECT_FOR_TIER_1_TOOLS));
	
	
	public static final TagKey<Block> FORGES = createTag("forges");
	public static final TagKey<Block> ANVILS = createTag("anvils");
	
	public static final TagKey<Block> PRIMITIVE_FORGE_CORE = createTag("primitive_forge_core");
	public static final TagKey<Block> PRIMITIVE_FORGE_SHELL = createTag("primitive_forge_shell", List.of(Blocks.COBBLESTONE));
	
	public static final TagKey<Block> BASIC_FORGE_CORE = createTag("basic_forge_core");
	public static final TagKey<Block> BASIC_FORGE_SHELL = createTag("basic_forge_shell", List.of(Blocks.BRICKS));
	
	public static final TagKey<Block> REFINED_FORGE_CORE = createTag("refined_forge_core");
	public static final TagKey<Block> REFINED_FORGE_SHELL = createTag("refined_forge_shell", List.of(Blocks.STONE_BRICKS));
	
	public static final TagKey<Block> ADVANCED_FORGE_CORE = createTag("advanced_forge_core");
	public static final TagKey<Block> ADVANCED_FORGE_SHELL = createTag("advanced_forge_shell", List.of(Blocks.POLISHED_BLACKSTONE_BRICKS));
	
	public static final TagKey<Block> ETHEREAL_FORGE_CORE = createTag("ethereal_forge_core");
	public static final TagKey<Block> ETHEREAL_FORGE_SHELL = createTag("ethereal_forge_shell", List.of(Blocks.OBSIDIAN));
	
	
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
	
	
	
	public static TagKey<Block> getIncorrectForTier(int tier) {
		return switch (tier) {
			case 0 -> INCORRECT_FOR_TIER_0_TOOLS;
			case 1 -> INCORRECT_FOR_TIER_1_TOOLS;
			case 2 -> INCORRECT_FOR_TIER_2_TOOLS;
			case 3 -> INCORRECT_FOR_TIER_3_TOOLS;
			case 4 -> INCORRECT_FOR_TIER_4_TOOLS;
			case 5 -> INCORRECT_FOR_TIER_5_TOOLS;
			default -> throw new IllegalStateException("Unexpected tier value: " + tier);
		};
	}
	
	public static TagKey<Block> getNeedsTier(int tier) {
		return switch (tier) {
			case 0 -> NEEDS_TIER_0_TOOLS;
			case 1 -> NEEDS_TIER_1_TOOLS;
			case 2 -> NEEDS_TIER_2_TOOLS;
			case 3 -> NEEDS_TIER_3_TOOLS;
			case 4 -> NEEDS_TIER_4_TOOLS;
			case 5 -> NEEDS_TIER_5_TOOLS;
			default -> throw new IllegalStateException("Unexpected tier value: " + tier);
		};
	}
	
	
	
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
	
	
	private static void addToTag(TagKey<Block> tagKey, List<Block> blocks) {
		blocks.forEach(TAGS.computeIfAbsent(tagKey, k -> new ArrayList<>())::add);
	}
	
	private static void addMaterialBlocksToTag(ModMaterials material, List<Block> blocks) {
		addToTag(getNeedsTier(material.getInferiorTier()), blocks);
	}
	
	public static void registerBlockTags() {
		Rase.LOGGER.info("Registering Block Tags for " + Rase.MOD_ID);
		
		TAGS.put(BlockTags.SWORD_EFFICIENT, List.of(Blocks.TALL_GRASS, Blocks.SHORT_GRASS, Blocks.SUGAR_CANE));
		
		
		addMaterialBlocksToTag(ModMaterials.NETHERITE, List.of(
				Blocks.ANCIENT_DEBRIS,
				Blocks.NETHERITE_BLOCK
		));
		
		addMaterialBlocksToTag(ModMaterials.EMERALD, List.of(
				Blocks.EMERALD_ORE,
				Blocks.EMERALD_BLOCK,
				Blocks.DEEPSLATE_EMERALD_ORE
		));
		
		addMaterialBlocksToTag(ModMaterials.LAPIS, List.of(
				Blocks.LAPIS_ORE,
				Blocks.LAPIS_BLOCK,
				Blocks.DEEPSLATE_LAPIS_ORE
		));
		
		addMaterialBlocksToTag(ModMaterials.DIAMOND, List.of(
				Blocks.DIAMOND_ORE,
				Blocks.DIAMOND_BLOCK,
				Blocks.DEEPSLATE_DIAMOND_ORE
		));
		
		addMaterialBlocksToTag(ModMaterials.GOLD, List.of(
				Blocks.GOLD_ORE,
				Blocks.GOLD_BLOCK,
				Blocks.RAW_GOLD_BLOCK,
				Blocks.DEEPSLATE_GOLD_ORE,
				Blocks.NETHER_GOLD_ORE
		));
		
		addMaterialBlocksToTag(ModMaterials.IRON, List.of(
				Blocks.IRON_ORE,
				Blocks.IRON_BLOCK,
				Blocks.DEEPSLATE_IRON_ORE,
				Blocks.RAW_IRON_BLOCK,
				Blocks.IRON_BARS,
				Blocks.IRON_DOOR,
				Blocks.IRON_TRAPDOOR
		));
		
		addMaterialBlocksToTag(ModMaterials.COPPER, List.of(
				Blocks.COPPER_BLOCK,
				Blocks.RAW_COPPER_BLOCK,
				Blocks.COPPER_ORE,
				Blocks.DEEPSLATE_COPPER_ORE,
				Blocks.CUT_COPPER_SLAB,
				Blocks.CUT_COPPER_STAIRS,
				Blocks.CUT_COPPER,
				Blocks.WEATHERED_COPPER,
				Blocks.WEATHERED_CUT_COPPER_SLAB,
				Blocks.WEATHERED_CUT_COPPER_STAIRS,
				Blocks.WEATHERED_CUT_COPPER,
				Blocks.OXIDIZED_COPPER,
				Blocks.OXIDIZED_CUT_COPPER_SLAB,
				Blocks.OXIDIZED_CUT_COPPER_STAIRS,
				Blocks.OXIDIZED_CUT_COPPER,
				Blocks.EXPOSED_COPPER,
				Blocks.EXPOSED_CUT_COPPER_SLAB,
				Blocks.EXPOSED_CUT_COPPER_STAIRS,
				Blocks.EXPOSED_CUT_COPPER,
				Blocks.WAXED_COPPER_BLOCK,
				Blocks.WAXED_CUT_COPPER_SLAB,
				Blocks.WAXED_CUT_COPPER_STAIRS,
				Blocks.WAXED_CUT_COPPER,
				Blocks.WAXED_WEATHERED_COPPER,
				Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
				Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS,
				Blocks.WAXED_WEATHERED_CUT_COPPER,
				Blocks.WAXED_EXPOSED_COPPER,
				Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
				Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS,
				Blocks.WAXED_EXPOSED_CUT_COPPER,
				Blocks.WAXED_OXIDIZED_COPPER,
				Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
				Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
				Blocks.WAXED_OXIDIZED_CUT_COPPER,
				Blocks.LIGHTNING_ROD,
				Blocks.CRAFTER,
				Blocks.CHISELED_COPPER,
				Blocks.EXPOSED_CHISELED_COPPER,
				Blocks.WEATHERED_CHISELED_COPPER,
				Blocks.OXIDIZED_CHISELED_COPPER,
				Blocks.WAXED_CHISELED_COPPER,
				Blocks.WAXED_EXPOSED_CHISELED_COPPER,
				Blocks.WAXED_WEATHERED_CHISELED_COPPER,
				Blocks.WAXED_OXIDIZED_CHISELED_COPPER,
				Blocks.COPPER_GRATE,
				Blocks.EXPOSED_COPPER_GRATE,
				Blocks.WEATHERED_COPPER_GRATE,
				Blocks.OXIDIZED_COPPER_GRATE,
				Blocks.WAXED_COPPER_GRATE,
				Blocks.WAXED_EXPOSED_COPPER_GRATE,
				Blocks.WAXED_WEATHERED_COPPER_GRATE,
				Blocks.WAXED_OXIDIZED_COPPER_GRATE,
				Blocks.COPPER_BULB,
				Blocks.EXPOSED_COPPER_BULB,
				Blocks.WEATHERED_COPPER_BULB,
				Blocks.OXIDIZED_COPPER_BULB,
				Blocks.WAXED_COPPER_BULB,
				Blocks.WAXED_EXPOSED_COPPER_BULB,
				Blocks.WAXED_WEATHERED_COPPER_BULB,
				Blocks.WAXED_OXIDIZED_COPPER_BULB,
				Blocks.COPPER_TRAPDOOR,
				Blocks.EXPOSED_COPPER_TRAPDOOR,
				Blocks.WEATHERED_COPPER_TRAPDOOR,
				Blocks.OXIDIZED_COPPER_TRAPDOOR,
				Blocks.WAXED_COPPER_TRAPDOOR,
				Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
				Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR,
				Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR,
				Blocks.COPPER_DOOR,
				Blocks.EXPOSED_COPPER_DOOR,
				Blocks.WEATHERED_COPPER_DOOR,
				Blocks.OXIDIZED_COPPER_DOOR,
				Blocks.WAXED_COPPER_DOOR,
				Blocks.WAXED_EXPOSED_COPPER_DOOR,
				Blocks.WAXED_WEATHERED_COPPER_DOOR,
				Blocks.WAXED_OXIDIZED_COPPER_DOOR
		));
	}
}