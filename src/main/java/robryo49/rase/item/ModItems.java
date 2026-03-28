package robryo49.rase.item;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

import java.util.*;

import static net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent;



public class ModItems {
	
	// --- Collections ---
	
	public static final List<Item> ALL = new ArrayList<>();
	public static final List<Item> TRANSLATABLE = ALL;
	
	public static final Map<Model, List<Item>> MODELS = new HashMap<>();
	public static final Map<RegistryKey<ItemGroup>, List<Item>> GROUPS = new HashMap<>();
	public static final Map<TagKey<Item>, List<Item>> TAGS = new HashMap<>();
	
	// --- Item Registrations ---
	
	
	
	// --- Material Specific Registration Methods ---
	
	
	public record MaterialSet(Item INGOT, Item NUGGET, Item RAW) {}
	public record AlloyMaterialSet(Item INGOT, Item NUGGET) {}
	
	public static MaterialSet registerMaterialSet(String name) {
		return new MaterialSet(
				registerItem(name + "_ingot", new Item(new Item.Settings())),
				registerItem(name + "_nugget", new Item(new Item.Settings())),
				registerItem("raw_" + name, new Item(new Item.Settings()))
		);
	}
	
	public static AlloyMaterialSet registerAlloyMaterial(String name) {
		return new AlloyMaterialSet(
				registerItem(name + "_ingot", new Item(new Item.Settings())),
				registerItem(name + "_nugget", new Item(new Item.Settings()))
		);
	}
	
	public static Item registerSimpleMaterial(String name) {
		return registerItem(name, new Item(new Item.Settings()));
	}
	
	
	// --- Armor Specific Registration Methods ---
	
	public record ArmorSet(Item HELMET, Item CHESTPLATE, Item LEGGINGS, Item BOOTS) {}
	
	public static ArmorSet registerArmorSet(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return new ArmorSet(
				registerHelmetItem(name + "_helmet", material, multiplier),
				registerChestplateItem(name + "_chestplate", material, multiplier),
				registerLeggingsItem(name + "_leggings", material, multiplier),
				registerBootsItem(name + "_boots", material, multiplier)
		);
	}
	
	public static Item registerHelmetItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return registerItem(name, new ArmorItem(material, ArmorItem.Type.HELMET,
				new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(multiplier))), ItemTags.HEAD_ARMOR);
	}
	
	public static Item registerChestplateItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return registerItem(name, new ArmorItem(material, ArmorItem.Type.CHESTPLATE,
				new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(multiplier))), ItemTags.CHEST_ARMOR, Models.GENERATED);
	}
	
	public static Item registerLeggingsItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return registerItem(name, new ArmorItem(material, ArmorItem.Type.LEGGINGS,
				new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(multiplier))), ItemTags.LEG_ARMOR, Models.GENERATED);
	}
	
	public static Item registerBootsItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return registerItem(name, new ArmorItem(material, ArmorItem.Type.BOOTS,
				new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(multiplier))), ItemTags.FOOT_ARMOR, Models.GENERATED);
	}
	
	// --- Tool Specific Registration Methods ---
	
	public record ToolSet(Item SWORD, Item AXE, Item PICKAXE, Item SHOVEL, Item HOE) {}
	
	public static ToolSet registerToolSet(String name, ToolMaterial material) {
		return new ToolSet(
				registerSwordItem(name + "_sword", material),
				registerAxeItem(name + "_axe", material),
				registerPickaxeItem(name + "_pickaxe", material),
				registerShovelItem(name + "_shovel", material),
				registerHoeItem(name + "_hoe", material)
		);
	}
	
	public static Item registerSwordItem(String name, ToolMaterial material) {
		return registerItem(name, new SwordItem(material, new Item.Settings().attributeModifiers(
				SwordItem.createAttributeModifiers(material, 3, -2.4f))), ItemTags.SWORDS, Models.HANDHELD);
	}
	
	public static Item registerAxeItem(String name, ToolMaterial material) {
		return registerItem(name, new AxeItem(material, new Item.Settings().attributeModifiers(
				AxeItem.createAttributeModifiers(material, 5, -3.0f))), ItemTags.AXES, Models.HANDHELD);
	}
	
	public static Item registerPickaxeItem(String name, ToolMaterial material) {
		return registerItem(name, new PickaxeItem(material, new Item.Settings().attributeModifiers(
				PickaxeItem.createAttributeModifiers(material, 1, -2.8f))), ItemTags.PICKAXES, Models.HANDHELD);
	}
	
	public static Item registerShovelItem(String name, ToolMaterial material) {
		return registerItem(name, new ShovelItem(material, new Item.Settings().attributeModifiers(
				ShovelItem.createAttributeModifiers(material, 1.5f, -3.0f))), ItemTags.SHOVELS, Models.HANDHELD);
	}
	
	public static Item registerHoeItem(String name, ToolMaterial material) {
		return registerItem(name, new HoeItem(material, new Item.Settings().attributeModifiers(
				HoeItem.createAttributeModifiers(material, -3.0f, 0f))), ItemTags.HOES, Models.HANDHELD);
	}
	
	// --- Core Registration Logic ---
	
	public static Item registerItem(String name, Item item) {
		return registerItem(name, item, Models.GENERATED);
	}
	
	public static Item registerItem(String name, Item item, Model model) {
		Item registeredItem = Registry.register(Registries.ITEM, Identifier.of(Rase.MOD_ID, name), item);
		MODELS.computeIfAbsent(model, k -> new ArrayList<>()).add(registeredItem);
		ALL.add(registeredItem);
		return registeredItem;
	}
	
	public static Item registerItem(String name, Item item, TagKey<Item> tag) {
		return registerItem(name, item, tag, Models.GENERATED);
	}
	
	public static Item registerItem(String name, Item item, TagKey<Item> tag, Model model) {
		Item registeredItem = registerItem(name, item, model);
		TAGS.computeIfAbsent(tag, k -> new ArrayList<>()).add(registeredItem);
		return registeredItem;
	}
	
	public static Item registerItem(String name, Item item, List<TagKey<Item>> tags) {
		return registerItem(name, item, tags, Models.GENERATED);
	}
	
	public static Item registerItem(String name, Item item, List<TagKey<Item>> tags, Model model) {
		Item registeredItem = registerItem(name, item, model);
		for (TagKey<Item> tag : tags) TAGS.computeIfAbsent(tag, k -> new ArrayList<>()).add(registeredItem);
		return registeredItem;
	}
	
	// --- Item Group Logic ---
	
	public static void addToItemGroup(RegistryKey<ItemGroup> groupRegistryKey, List<Item> items) {
		modifyEntriesEvent(groupRegistryKey).register(entries -> items.forEach(entries::add));
	}
	
	public static void addToItemGroup(RegistryKey<ItemGroup> groupRegistryKey, Item item) {
		modifyEntriesEvent(groupRegistryKey).register(entries -> entries.add(item));
	}
	
	public static void registerModItems() {
		Rase.LOGGER.info("Registering Mod Items for " + Rase.MOD_ID);
		GROUPS.forEach(ModItems::addToItemGroup);
	}
}