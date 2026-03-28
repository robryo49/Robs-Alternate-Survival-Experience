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
import robryo49.rase.block.custom.forge.ForgeTiers;
import robryo49.rase.item.custom.MoldItem;
import robryo49.rase.item.custom.MoldMaterials;
import robryo49.rase.item.custom.MoldMaterials.MoldMaterial;
import robryo49.rase.util.ModItemTags;

import java.util.*;

import static net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent;



public class ModItems {
	
	// --- Collections ---
	
	
	public static final List<Item> ALL = new ArrayList<>();
	public static final List<Item> TRANSLATABLE = ALL;
	
	public static final Map<Model, List<Item>> MODELS = new HashMap<>();
	public static final Map<RegistryKey<ItemGroup>, List<Item>> GROUPS = new HashMap<>();
	public static final Map<TagKey<Item>, List<Item>> TAGS = new HashMap<>();
	
	public static final List<MoldSet> MOLD_SETS = new ArrayList<>();
	
	public static final List<Item> INGOTS = new ArrayList<>();
	
	
	// --- Item Registrations ---
	
	
	public static final MoldSet CLAY_MOLD_SET = registerMoldSet("clay", MoldMaterials.CLAY);
	public static final MoldSet WET_CLAY_MOLD_SET = registerMoldSet("wet_clay", MoldMaterials.WET_CLAY);
	
	public static final AlloyMaterialSet BRONZE = registerAlloyMaterialSet("bronze");
	public static final ToolSet BRONZE_TOOL_SET = registerToolSet("bronze", ModToolMaterials.BRONZE);
	public static final ArmorSet BRONZE_ARMOR_SET = registerArmorSet("bronze", ModArmorMaterials.BRONZE, 20);
	
	public static final MaterialSet TIN = registerMaterialSet("tin");
	
	
	// --- Mold Specific Registration Methods ---
	
	
	public record MoldSet(MoldItem INGOT, MoldItem PICKAXE, MoldItem AXE, MoldItem SWORD, MoldItem SHOVEL, MoldItem HOE) {}
	
	
	public static MoldSet registerMoldSet(String materialName, MoldMaterial moldMaterial) {
		MoldSet moldset = new MoldSet(
				registerMoldItem(materialName, "ingot", moldMaterial, ModItemTags.INGOTS, ModItemTags.INGOT_MOLDS),
				registerMoldItem(materialName, "pickaxe", moldMaterial, ModItemTags.PICKAXE_HEADS, ModItemTags.PICKAXE_HEAD_MOLDS),
				registerMoldItem(materialName, "axe", moldMaterial, ModItemTags.AXE_HEADS, ModItemTags.AXE_HEAD_MOLDS),
				registerMoldItem(materialName, "sword", moldMaterial, ModItemTags.SWORD_BLADES, ModItemTags.SWORD_BLADE_MOLDS),
				registerMoldItem(materialName, "shovel", moldMaterial, ModItemTags.SHOVEL_HEADS, ModItemTags.SHOVEL_HEAD_MOLDS),
				registerMoldItem(materialName, "hoe", moldMaterial, ModItemTags.HOE_HEADS, ModItemTags.HOE_HEAD_MOLDS)
		);
		MOLD_SETS.add(moldset);
		return moldset;
	}
	
	public static MoldItem registerMoldItem(String materialName, String patternName, MoldMaterial moldMaterial, TagKey<Item> acceptedItems) {
		return ((MoldItem) registerItem(materialName + "_" + patternName + "_mold", new MoldItem(new Item.Settings(), moldMaterial, acceptedItems, patternName)));
	}
	
	public static MoldItem registerMoldItem(String materialName, String patternName, MoldMaterial moldMaterial, TagKey<Item> acceptedItems, TagKey<Item> tagKey) {
		return ((MoldItem) registerItem(materialName + "_" + patternName + "_mold",
				new MoldItem(new Item.Settings(), moldMaterial, acceptedItems, patternName), tagKey));
	}
	
	public static MoldItem registerMoldItem(String materialName, String patternName, MoldMaterial moldMaterial, TagKey<Item> acceptedItems, List<TagKey<Item>> tagKeys) {
		return ((MoldItem) registerItem(materialName + "_" + patternName + "_mold",
				new MoldItem(new Item.Settings(), moldMaterial, acceptedItems, patternName), tagKeys));
	}
	
	
	// --- Material Specific Registration Methods ---
	
	
	public record MaterialSet(Item INGOT, Item NUGGET, Item RAW) {}
	public record AlloyMaterialSet(Item INGOT, Item NUGGET) {}
	
	public static MaterialSet registerMaterialSet(String name) {
		Item ingot = registerItem(name + "_ingot", ModItemTags.INGOTS);
		Item nugget = registerItem(name + "_nugget");
		Item raw = registerItem("raw_" + name);
		
		return new MaterialSet(ingot, nugget, raw);
	}
	
	public static AlloyMaterialSet registerAlloyMaterialSet(String name) {
		Item ingot = registerItem(name + "_ingot", ModItemTags.INGOTS);
		Item nugget = registerItem(name + "_nugget");
		
		return new AlloyMaterialSet(ingot, nugget);
	}
	
	public static Item registerSimpleMaterial(String name) {
		return registerItem(name, new Item(new Item.Settings()));
	}
	
	
	// --- Armor Specific Registration Methods ---
	
	public record ArmorSet(ArmorItem HELMET, ArmorItem CHESTPLATE, ArmorItem LEGGINGS, ArmorItem BOOTS) {}
	
	public static ArmorSet registerArmorSet(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return new ArmorSet(
				registerHelmetItem(name + "_helmet", material, multiplier),
				registerChestplateItem(name + "_chestplate", material, multiplier),
				registerLeggingsItem(name + "_leggings", material, multiplier),
				registerBootsItem(name + "_boots", material, multiplier)
		);
	}
	
	public static ArmorItem registerHelmetItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return (ArmorItem) registerItem(name, new ArmorItem(material, ArmorItem.Type.HELMET,
				new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(multiplier))), ItemTags.HEAD_ARMOR);
	}
	
	public static ArmorItem registerChestplateItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return (ArmorItem) registerItem(name, new ArmorItem(material, ArmorItem.Type.CHESTPLATE,
				new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(multiplier))), ItemTags.CHEST_ARMOR);
	}
	
	public static ArmorItem registerLeggingsItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return (ArmorItem) registerItem(name, new ArmorItem(material, ArmorItem.Type.LEGGINGS,
				new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(multiplier))), ItemTags.LEG_ARMOR);
	}
	
	public static ArmorItem registerBootsItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return (ArmorItem) registerItem(name, new ArmorItem(material, ArmorItem.Type.BOOTS,
				new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(multiplier))), ItemTags.FOOT_ARMOR);
	}
	
	
	// --- Tool Specific Registration Methods ---
	
	
	public record ToolSet(
			SwordItem SWORD, Item SWORD_BLADE,
			AxeItem AXE, Item AXE_HEAD,
			PickaxeItem PICKAXE, Item PICKAXE_HEAD,
			ShovelItem SHOVEL, Item SHOVEL_HEAD,
			HoeItem HOE, Item HOE_HEAD
	) {}
	
	public static ToolSet registerToolSet(String materialName, ToolMaterial material) {
		return new ToolSet(
				registerSwordItem(materialName + "_sword", material),
				registerSwordBladeItem(materialName + "_sword_blade"),
				registerAxeItem(materialName + "_axe", material),
				registerAxeHeadItem(materialName + "_axe_head"),
				registerPickaxeItem(materialName + "_pickaxe", material),
				registerPickaxeHeadItem(materialName + "_pickaxe_head"),
				registerShovelItem(materialName + "_shovel", material),
				registerShovelHeadItem(materialName + "_shovel_head"),
				registerHoeItem(materialName + "_hoe", material),
				registerHoeHeadItem(materialName + "_hoe_head")
		);
	}
	
	public static SwordItem registerSwordItem(String name, ToolMaterial material) {
		return (SwordItem) registerItem(name, new SwordItem(material, new Item.Settings().attributeModifiers(
				SwordItem.createAttributeModifiers(material, 3, -2.4f))), ItemTags.SWORDS, Models.HANDHELD);
	}
	
	public static Item registerSwordBladeItem(String name) {
		return registerItem(name, ModItemTags.SWORD_BLADES);
	}
	
	public static AxeItem registerAxeItem(String name, ToolMaterial material) {
		return (AxeItem) registerItem(name, new AxeItem(material, new Item.Settings().attributeModifiers(
				AxeItem.createAttributeModifiers(material, 5, -3.0f))), ItemTags.AXES, Models.HANDHELD);
	}
	
	public static Item registerAxeHeadItem(String name) {
		return registerItem(name, ModItemTags.AXE_HEADS);
	}
	
	public static PickaxeItem registerPickaxeItem(String name, ToolMaterial material) {
		return (PickaxeItem) registerItem(name, new PickaxeItem(material, new Item.Settings().attributeModifiers(
				PickaxeItem.createAttributeModifiers(material, 1, -2.8f))), ItemTags.PICKAXES, Models.HANDHELD);
	}
	
	public static Item registerPickaxeHeadItem(String name) {
		return registerItem(name, ModItemTags.PICKAXE_HEADS);
	}
	
	public static ShovelItem registerShovelItem(String name, ToolMaterial material) {
		return (ShovelItem) registerItem(name, new ShovelItem(material, new Item.Settings().attributeModifiers(
				ShovelItem.createAttributeModifiers(material, 1.5f, -3.0f))), ItemTags.SHOVELS, Models.HANDHELD);
	}
	
	public static Item registerShovelHeadItem(String name) {
		return registerItem(name, ModItemTags.SHOVEL_HEADS);
	}
	
	public static HoeItem registerHoeItem(String name, ToolMaterial material) {
		return (HoeItem) registerItem(name, new HoeItem(material, new Item.Settings().attributeModifiers(
				HoeItem.createAttributeModifiers(material, -3.0f, 0f))), ItemTags.HOES, Models.HANDHELD);
	}
	
	public static Item registerHoeHeadItem(String name) {
		return registerItem(name, ModItemTags.HOE_HEADS);
	}
	
	
	// --- Core Registration Logic ---
	
	public static Item registerItem(String name) {
		return registerItem(name, new Item(new Item.Settings()), List.of(), Models.GENERATED);
	}
	
	public static Item registerItem(String name, Item item) {
		return registerItem(name, item, List.of(), Models.GENERATED);
	}
	
	public static Item registerItem(String name, Item item, Model model) {
		return registerItem(name, item, List.of(), model);
	}
	
	public static Item registerItem(String name, Model model) {
		return registerItem(name, new Item(new Item.Settings()), List.of(), model);
	}
	
	public static Item registerItem(String name, Item item, TagKey<Item> tag) {
		return registerItem(name, item, List.of(tag), Models.GENERATED);
	}
	
	public static Item registerItem(String name, TagKey<Item> tag) {
		return registerItem(name, new Item(new Item.Settings()), List.of(tag), Models.GENERATED);
	}
	
	public static Item registerItem(String name, Item item, TagKey<Item> tag, Model model) {
		return registerItem(name, item, List.of(tag), model);
	}
	
	public static Item registerItem(String name, TagKey<Item> tag, Model model) {
		return registerItem(name, new Item(new Item.Settings()), List.of(tag), model);
	}
	
	public static Item registerItem(String name, Item item, List<TagKey<Item>> tags) {
		return registerItem(name, item, tags, Models.GENERATED);
	}
	
	public static Item registerItem(String name, List<TagKey<Item>> tags) {
		return registerItem(name, new Item(new Item.Settings()), tags, Models.GENERATED);
	}
	
	public static Item registerItem(String name, Item item, List<TagKey<Item>> tags, Model model) {
		Item registeredItem = Registry.register(Registries.ITEM, Identifier.of(Rase.MOD_ID, name), item);
		
		ALL.add(registeredItem);
		MODELS.computeIfAbsent(model, k -> new ArrayList<>()).add(registeredItem);
		
		tags.forEach(tag -> TAGS.computeIfAbsent(tag, k -> new ArrayList<>()).add(registeredItem));
		
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
		Rase.LOGGER.info("Registering Items for " + Rase.MOD_ID);
		GROUPS.forEach(ModItems::addToItemGroup);
	}
}