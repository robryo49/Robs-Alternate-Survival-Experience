package robryo49.rase.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlockTags {
	
	// --- Collections ---
	
	
	public static final Map<TagKey<Block>, List<TagKey<Block>>> PARENT_TAGS = new HashMap<>();
	public static final Map<TagKey<Block>, List<Block>> TAGS = new HashMap<>();
	
	
	// --- Block Tag Registrations
	
	public static final TagKey<Block> NEEDS_BRONZE_TOOL = createTag("needs_bronze_tool");
	public static final TagKey<Block> INCORRECT_FOR_BRONZE_TOOL = createTag("incorrect_for_bronze_tool");
	
	
	public static final TagKey<Block> PRIMITIVE_FORGE_CORE = createTag("primitive_forge_core", List.of(Blocks.IRON_BLOCK));
	public static final TagKey<Block> PRIMITIVE_FORGE_SHELL = createTag("primitive_forge_shell", List.of(Blocks.BRICKS));
	
	public static final TagKey<Block> ADVANCED_FORGE_CORE = createTag("advanced_forge_core", List.of(Blocks.DIAMOND_BLOCK));
	public static final TagKey<Block> ADVANCED_FORGE_SHELL = createTag("advanced_forge_shell", List.of(Blocks.STONE_BRICKS));
	
	
	// --- Core Tag Registration Logic ---
	
	
	private static TagKey<Block> createTag(String name) {
		return createTag(name, List.of(), List.of());
	}
	
	private static TagKey<Block> createTag(String name, List<Block> blocks) {
		return createTag(name, List.of(), blocks);
	}
	
	private static TagKey<Block> createTagWithParent(String name, List<TagKey<Block>> parentTagKeys) {
		return createTag(name, parentTagKeys, List.of());
	}
	
	private static TagKey<Block> createTag(String name, List<TagKey<Block>> parentTagKeys, List<Block> blocks) {
		TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, Identifier.of(Rase.MOD_ID, name));
		
		parentTagKeys.forEach(parent -> PARENT_TAGS.computeIfAbsent(tagKey, k -> new ArrayList<>()).add(parent));
		if (!blocks.isEmpty()) TAGS.put(tagKey, blocks);
		
		return tagKey;
	}
	
	public static void registerBlockTags() {
		Rase.LOGGER.info("Registering Block Tags for " + Rase.MOD_ID);
	}
}