package robryo49.rase.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItemTags {
	
	public static final Map<TagKey<Item>, List<TagKey<Item>>> TAGS = new HashMap<>();
	
	
	
	
	private static TagKey<Item> createTag(String name) {
		return TagKey.of(RegistryKeys.ITEM, Identifier.of(Rase.MOD_ID, name));
	}
	
	private static TagKey<Item> createTag(String name, TagKey<Item> parentTag) {
		TagKey<Item> tag = createTag(name);
		TAGS.computeIfAbsent(parentTag, k -> new ArrayList<>()).add(tag);
		return tag;
	}
	
	private static TagKey<Item> createTag(String name, List<TagKey<Item>> parentTags) {
		TagKey<Item> tag = createTag(name);
		for (TagKey<Item> tagKey : parentTags) TAGS.computeIfAbsent(tagKey, k -> new ArrayList<>()).add(tag);
		return tag;
	}
}