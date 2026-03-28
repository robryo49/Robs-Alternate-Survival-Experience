package robryo49.rase.util;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItemTags {
	
	// --- Collections ---
	
	
	public static final List<TagKey<Item>> ALL = new ArrayList<>();
	public static final List<TagKey<Item>> TRANSLATABLE = ALL;
	
	public static final Map<TagKey<Item>, List<TagKey<Item>>> PARENT_TAGS = new HashMap<>();
	public static final Map<TagKey<Item>, List<Item>> TAGS = new HashMap<>();
	
	
	// --- Item Tag Registrations ---
	
	public static final TagKey<Item> INGOTS = createTag("ingots", List.of(Items.IRON_INGOT, Items.GOLD_INGOT, Items.COPPER_INGOT, Items.NETHERITE_INGOT));
	public static final TagKey<Item> STRINGS = createTag("strings", List.of(Items.STRING));
	
	public static final TagKey<Item> HIDES = createTag("hides", List.of(Items.RABBIT_HIDE));
	
	public static final TagKey<Item> PRIMITIVE_TOOLS = createTag("primitive_tools");
	
	public static final TagKey<Item> PICKAXE_HEADS = createTag("pickaxe_heads");
	public static final TagKey<Item> AXE_HEADS = createTag("axe_heads");
	public static final TagKey<Item> SWORD_BLADES = createTag("sword_blades");
	public static final TagKey<Item> SHOVEL_HEADS = createTag("shovel_heads");
	public static final TagKey<Item> HOE_HEADS = createTag("hoe_heads");
	
	public static final TagKey<Item> INGOT_MOLDS = createTag("ingot_molds");
	public static final TagKey<Item> PICKAXE_HEAD_MOLDS = createTag("pickaxe_head_molds");
	public static final TagKey<Item> AXE_HEAD_MOLDS = createTag("axe_head_molds");
	public static final TagKey<Item> SWORD_BLADE_MOLDS = createTag("sword_blade_molds");
	public static final TagKey<Item> SHOVEL_HEAD_MOLDS = createTag("shovel_head_molds");
	public static final TagKey<Item> HOE_HEAD_MOLDS = createTag("hoe_head_molds");
	
	public static final TagKey<Item> MOLDS = createTagWithParent("ingots_molds",
			List.of(INGOT_MOLDS, PICKAXE_HEAD_MOLDS, AXE_HEAD_MOLDS, SWORD_BLADE_MOLDS, SHOVEL_HEAD_MOLDS, HOE_HEAD_MOLDS)
	);
	
	// --- Core Registration Logic ---
	
	
	private static TagKey<Item> createTag(String name) {
		return createTag(name, List.of(), List.of());
	}
	
	private static TagKey<Item> createTag(String name, List<Item> items) {
		return createTag(name, List.of(), items);
	}
	
	private static TagKey<Item> createTagWithParent(String name, List<TagKey<Item>> parentTagKeys) {
		return createTag(name, parentTagKeys, List.of());
	}
	
	private static TagKey<Item> createTag(String name, List<TagKey<Item>> parentTagKeys, List<Item> items) {
		TagKey<Item> tagKey = TagKey.of(RegistryKeys.ITEM, Rase.getIdentifier(name));
		
		parentTagKeys.forEach(parent -> PARENT_TAGS.computeIfAbsent(tagKey, k -> new ArrayList<>()).add(parent));
		if (!items.isEmpty()) TAGS.put(tagKey, items);
		
		ALL.add(tagKey);
		
		return tagKey;
	}
	
	public static void registerItemTags() {
		Rase.LOGGER.info("Registering Item Tags for " + Rase.MOD_ID);
	}
}