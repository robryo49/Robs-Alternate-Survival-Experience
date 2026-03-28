package robryo49.rase.util;

import net.minecraft.block.Block;
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
	
	
	// --- Tag Registrations
	
	
	
	// --- Core Tag Registration Logic ---
	
	private static TagKey<Block> createTag(String name) {
		return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Rase.MOD_ID, name));
	}
	
	private static TagKey<Block> createTag(String name, TagKey<Block> parentTagKey) {
		TagKey<Block> tag = createTag(name);
		PARENT_TAGS.computeIfAbsent(tag, k -> new ArrayList<>()).add(parentTagKey);
		return tag;
	}
	
	private static TagKey<Block> createTag(String name, List<TagKey<Block>> parentTagKeys) {
		TagKey<Block> tag = createTag(name);
		for (TagKey<Block> parentTagKey : parentTagKeys) PARENT_TAGS.computeIfAbsent(tag, k -> new ArrayList<>()).add(parentTagKey);
		return tag;
	}
}