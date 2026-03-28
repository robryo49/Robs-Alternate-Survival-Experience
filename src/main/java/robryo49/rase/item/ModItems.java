package robryo49.rase.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.Rase;
import robryo49.rase.item.custom.FireStarterItem;
import robryo49.rase.item.custom.MoldItem;
import robryo49.rase.item.custom.MoldMaterials;
import robryo49.rase.util.ModItemTags;

import java.util.*;

import static net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent;
import static net.minecraft.item.Item.BASE_ATTACK_DAMAGE_MODIFIER_ID;

public class ModItems {
	
	// --- Collections ---
	public static final List<Item> ALL = new ArrayList<>();
	public static final Map<Model, List<Item>> MODELS = new HashMap<>();
	public static final Map<RegistryKey<ItemGroup>, List<Item>> GROUPS = new HashMap<>();
	public static final Map<TagKey<Item>, List<Item>> TAGS = new HashMap<>();
	
	public static final List<MoldSet> MOLD_SETS = new ArrayList<>();
	public static final List<MeatSet> MEAT_SETS = new ArrayList<>();
	public static final List<PebbleSet> PEBBLE_SETS = new ArrayList<>();
	public static final List<HideSet> HIDE_SETS = new ArrayList<>();
	
	// --- Items ---
	public static final Item FLINT_SHARD = registerItem("flint_shard");
	public static final Item SHARP_FLINT = registerItem("sharp_flint", new Item(new Item.Settings().attributeModifiers(AttributeModifiersComponent.builder()
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 1.0,
					EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build())));
	
	public static final Item PLANT_FIBER = registerItem("plant_fiber");
	public static final Item TWINE = registerItem("twine", List.of(ModItemTags.STRINGS));
	public static final Item LEATHER_STRAP = registerItem("leather_strap", List.of(ModItemTags.STRINGS));
	public static final Item HANDLE = registerItem("handle");
	public static final Item FIRE_STARTER = registerItem("fire_starter", new FireStarterItem(new Item.Settings().maxDamage(64), 80, 0.02f));
	
	// --- Material Sets ---
	
	public static final OreMaterialSet TIN = registerOreMaterialSet("tin");
	public static final OreMaterialSet ZINC = registerOreMaterialSet("zinc");
	public static final OreMaterialSet MAGNETITE = registerOreMaterialSet("magnetite");
	public static final AlloyMaterialSet BRONZE = registerAlloyMaterialSet("bronze");
	public static final OreMaterialSet SILVER = registerOreMaterialSet("silver");
	public static final OreMaterialSet LEAD = registerOreMaterialSet("lead");
	public static final AlloyMaterialSet STEEL = registerAlloyMaterialSet("steel");
	public static final OreMaterialSet TITANIUM = registerOreMaterialSet("titanium");
	public static final OreMaterialSet PLATINUM = registerOreMaterialSet("platinum");
	public static final OreMaterialSet TUNGSTEN = registerOreMaterialSet("tungsten");
	public static final OreMaterialSet PALLADIUM = registerOreMaterialSet("palladium");
	public static final OreMaterialSet COBALT = registerOreMaterialSet("cobalt");
	public static final AlloyMaterialSet SCANDIUM = registerAlloyMaterialSet("scandium");
	public static final OreMaterialSet MYTHRIL = registerOreMaterialSet("mythril");
	public static final CrystalMaterialSet RHEXIS = registerCrystalMaterialSet("rhexis");
	
	
	// --- Tool Sets ---
	
	public static final Item FLINT_KNIFE = registerSwordItem("flint_knife", ModToolMaterials.FLINT, true);
	public static final Item FLINT_HATCHET = registerAxeItem("flint_hatchet", ModToolMaterials.FLINT, true);
	public static final Item FLINT_PICK = registerPickaxeItem("flint_pick", ModToolMaterials.FLINT, true);
	public static final ToolSet STONE_TOOL_SET = registerToolSet("stone", ModToolMaterials.STONE, true);
	public static final ToolSet BRONZE_TOOL_SET = registerToolSet("bronze", ModToolMaterials.BRONZE);
	public static final ToolSet SILVER_TOOL_SET = registerToolSet("silver", ModToolMaterials.SILVER);
	public static final ToolSet STEEL_TOOL_SET = registerToolSet("steel", ModToolMaterials.STEEL);
	public static final ToolSet TITANIUM_TOOL_SET = registerToolSet("titanium", ModToolMaterials.TITANIUM);
	public static final ToolSet COBALT_TOOL_SET = registerToolSet("cobalt", ModToolMaterials.COBALT);
	public static final ToolSet SCANDIUM_TOOL_SET = registerToolSet("scandium", ModToolMaterials.SCANDIUM);
	public static final ToolSet MYTHRIL_TOOL_SET = registerToolSet("mythril", ModToolMaterials.MYTHRIL);
	
	
	// --- Armor Sets ---
	
	public static final ArmorSet BRONZE_ARMOR_SET = registerArmorSet("bronze", ModArmorMaterials.BRONZE, 15);
	public static final ArmorSet SILVER_ARMOR_SET = registerArmorSet("silver", ModArmorMaterials.SILVER, 12);
	public static final ArmorSet STEEL_ARMOR_SET = registerArmorSet("steel", ModArmorMaterials.STEEL, 25);
	public static final ArmorSet TITANIUM_ARMOR_SET = registerArmorSet("titanium", ModArmorMaterials.TITANIUM, 35);
	public static final ArmorSet COBALT_ARMOR_SET = registerArmorSet("cobalt", ModArmorMaterials.COBALT, 30);
	public static final ArmorSet SCANDIUM_ARMOR_SET = registerArmorSet("scandium", ModArmorMaterials.SCANDIUM, 45);
	public static final ArmorSet MYTHRIL_ARMOR_SET = registerArmorSet("mythril", ModArmorMaterials.MYTHRIL, 50);
	
	
	// --- Molds ---
	
	public static final MoldSet WET_CLAY_MOLD_SET = registerMoldSet("wet_clay", MoldMaterials.WET_CLAY, Items.CLAY_BALL);
	public static final MoldSet CLAY_MOLD_SET = registerMoldSet("clay", MoldMaterials.CLAY, WET_CLAY_MOLD_SET);
	public static final MoldSet LEAD_MOLD_SET = registerMoldSet("lead", MoldMaterials.LEAD, LEAD.INGOT);
	public static final MoldSet TITANIUM_MOLD_SET = registerMoldSet("titanium", MoldMaterials.TITANIUM, TITANIUM.INGOT);
	public static final MoldSet TUNGSTEN_MOLD_SET = registerMoldSet("tungsten", MoldMaterials.TUNGSTEN, TUNGSTEN.INGOT);
	
	
	// --- Food ---
	
	public static final MeatSet HORSE_MEAT = registerMeatSet(EntityType.HORSE, 2, 6, 0.4f);
	public static final MeatSet CAMEL_MEAT = registerMeatSet(EntityType.CAMEL, 2, 6, 0.4f);
	public static final MeatSet LLAMA_MEAT = registerMeatSet(EntityType.LLAMA, 2, 5, 0.3f);
	public static final MeatSet DOLPHIN_MEAT = registerMeatSet(EntityType.DOLPHIN, 2, 5, 0.3f);
	
	public static final MeatSet WOLF_MEAT = registerMeatSet(EntityType.WOLF, 1, 4, 0.2f);
	public static final MeatSet GOAT_MEAT = registerMeatSet(EntityType.GOAT, 2, 5, 0.4f);
	public static final MeatSet FOX_MEAT = registerMeatSet(EntityType.FOX, 1, 3, 0.2f);
	
	public static final MeatSet CAT_MEAT = registerMeatSet(EntityType.CAT, 1, 3, 0.2f, true);
	public static final MeatSet BAT_MEAT = registerMeatSet(EntityType.BAT, 1, 2, 0.1f, true);
	public static final MeatSet FROG_MEAT = registerMeatSet(EntityType.FROG, 1, 3, 0.2f, true);
	public static final MeatSet PARROT_MEAT = registerMeatSet(EntityType.PARROT, 1, 3, 0.2f, true);
	public static final MeatSet AXOLOTL_MEAT = registerMeatSet(EntityType.AXOLOTL, 1, 3, 0.2f, true);
	public static final MeatSet SQUID_MEAT = registerMeatSet(EntityType.SQUID, 1, 3, 0.2f, true);
	
	public static final MeatSet STRIDER_MEAT = registerMeatSet(EntityType.STRIDER, 2, 6, 0.5f); // High risk, high reward
	public static final MeatSet TURTLE_MEAT = registerMeatSet(EntityType.TURTLE, 2, 6, 0.5f);
	
	public static final MeatSet POLAR_BEAR_MEAT = registerMeatSet(EntityType.POLAR_BEAR, 3, 8, 0.6f);
	public static final MeatSet SNIFFER_MEAT = registerMeatSet(EntityType.SNIFFER, 4, 10, 0.7f); // Ancient meat is the best
	
	public static final HideSet COW_HIDE = registerHideItem(EntityType.COW);
	public static final HideSet DONKEY_HIDE = registerHideItem(EntityType.DONKEY);
	public static final HideSet HORSE_HIDE = registerHideItem(EntityType.HORSE);
	public static final HideSet LLAMA_HIDE = registerHideItem(EntityType.LLAMA);
	public static final HideSet MOOSHROOM_HIDE = registerHideItem(EntityType.MOOSHROOM);
	public static final HideSet MULE_HIDE = registerHideItem(EntityType.MULE);
	public static final HideSet OCELOT_HIDE = registerHideItem(EntityType.OCELOT);
	public static final HideSet PIG_HIDE = registerHideItem(EntityType.PIG);
	public static final HideSet POLAR_BEAR_HIDE = registerHideItem(EntityType.POLAR_BEAR);
	public static final HideSet WOLF_HIDE = registerHideItem(EntityType.WOLF);
	
	
	// --- Pebbles ---
	
	public static final PebbleSet ANDESITE_PEBBLES = registerPebbleSet(Blocks.ANDESITE);
	public static final PebbleSet BASALT_PEBBLES = registerPebbleSet(Blocks.BASALT);
	public static final PebbleSet CALCITE_PEBBLES = registerPebbleSet(Blocks.CALCITE);
	public static final PebbleSet DEEPSLATE_PEBBLES = registerPebbleSet(Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
	public static final PebbleSet DIORITE_PEBBLES = registerPebbleSet(Blocks.DIORITE);
	public static final PebbleSet END_STONE_PEBBLES = registerPebbleSet(Blocks.END_STONE);
	public static final PebbleSet GRANITE_PEBBLES = registerPebbleSet(Blocks.GRANITE);
	public static final PebbleSet RED_SANDSTONE_PEBBLES = registerPebbleSet(Blocks.RED_SANDSTONE);
	public static final PebbleSet SANDSTONE_PEBBLES = registerPebbleSet(Blocks.SANDSTONE);
	public static final PebbleSet STONE_PEBBLES = registerPebbleSet(Blocks.STONE, Blocks.COBBLESTONE);
	
	// --- HELPER RECORDS ---
	
	public record HideSet(EntityType<?> entity, Item HIDE) {}
	public record MeatSet(EntityType<?> entity, Item RAW, Item COOKED) {}
	public record PebbleSet(Item PEBBLE, Block SOURCE_BLOCK, Block RECONSTRUCTED_BLOCK) {}
	
	public record OreMaterialSet(String name, Item INGOT, Item NUGGET, Item RAW) {}
	public record AlloyMaterialSet(String name, Item INGOT, Item NUGGET) {}
	public record CrystalMaterialSet(String name, Item CRYSTAL) {}
	
	public record ArmorSet(String materialName, ArmorItem HELMET, ArmorItem CHESTPLATE, ArmorItem LEGGINGS, ArmorItem BOOTS) {}
	
	public record ToolSet(String materialName, SwordItem SWORD, Item SWORD_BLADE, AxeItem AXE, Item AXE_HEAD, PickaxeItem PICKAXE, Item PICKAXE_HEAD, ShovelItem SHOVEL, Item SHOVEL_HEAD, HoeItem HOE, Item HOE_HEAD) {}
	
	public record MoldSet(Item BASE, MoldItem INGOT, MoldItem PICKAXE, MoldItem AXE, MoldItem SWORD, MoldItem SHOVEL, MoldItem HOE, @Nullable MoldSet FROM_SMELTING, @Nullable ItemConvertible INGREDIENT) {
		public MoldMaterials getMaterial() { return INGOT.getMaterial(); }
	}
	
	
	// --- REGISTRATION LOGIC ---
	
	
	public static Item registerItem(String name, Item item, List<TagKey<Item>> tags, Model model) {
		Item registeredItem = Registry.register(Registries.ITEM, Rase.getIdentifier(name), item);
		ALL.add(registeredItem);
		MODELS.computeIfAbsent(model, k -> new ArrayList<>()).add(registeredItem);
		tags.forEach(tag -> TAGS.computeIfAbsent(tag, k -> new ArrayList<>()).add(registeredItem));
		return registeredItem;
	}
	
	
	// Overloads for easier registration
	
	
	public static Item registerItem(String name) { return registerItem(name, new Item(new Item.Settings()), List.of(), Models.GENERATED); }
	public static Item registerItem(String name, Item item) { return registerItem(name, item, List.of(), Models.GENERATED); }
	public static Item registerItem(String name, TagKey<Item> tag) { return registerItem(name, new Item(new Item.Settings()), List.of(tag), Models.GENERATED); }
	public static Item registerItem(String name, List<TagKey<Item>> tags) { return registerItem(name, new Item(new Item.Settings()), tags, Models.GENERATED); }
	public static Item registerItem(String name, Item item, TagKey<Item> tag) { return registerItem(name, item, List.of(tag), Models.GENERATED); }
	public static Item registerItem(String name, Item item, List<TagKey<Item>> tags) { return registerItem(name, item, tags, Models.GENERATED); }
	
	
	public static OreMaterialSet registerOreMaterialSet(String name) {
		return new OreMaterialSet(name, registerItem(name + "_ingot", ModItemTags.INGOTS), registerItem(name + "_nugget"), registerItem("raw_" + name));
	}
	
	public static AlloyMaterialSet registerAlloyMaterialSet(String name) {
		return new AlloyMaterialSet(name, registerItem(name + "_ingot", ModItemTags.INGOTS), registerItem(name + "_nugget"));
	}
	
	public static CrystalMaterialSet registerCrystalMaterialSet(String name) {
		return new CrystalMaterialSet(name, registerItem(name));
	}
	
	
	public static ArmorSet registerArmorSet(String name, RegistryEntry<ArmorMaterial> material, int multiplier) {
		return new ArmorSet(name, registerArmorItem(name + "_helmet", material, multiplier, ArmorItem.Type.HELMET, ItemTags.HEAD_ARMOR),
				registerArmorItem(name + "_chestplate", material, multiplier, ArmorItem.Type.CHESTPLATE, ItemTags.CHEST_ARMOR),
				registerArmorItem(name + "_leggings", material, multiplier, ArmorItem.Type.LEGGINGS, ItemTags.LEG_ARMOR),
				registerArmorItem(name + "_boots", material, multiplier, ArmorItem.Type.BOOTS, ItemTags.FOOT_ARMOR));
	}
	
	private static ArmorItem registerArmorItem(String name, RegistryEntry<ArmorMaterial> material, int multiplier, ArmorItem.Type type, TagKey<Item> tag) {
		return (ArmorItem) registerItem(name, new ArmorItem(material, type, new Item.Settings().maxDamage(type.getMaxDamage(multiplier))), tag);
	}
	
	
	public static ToolSet registerToolSet(String materialName, ToolMaterial material) {
		return registerToolSet(materialName, material, false);
	}
	
	public static ToolSet registerToolSet(String materialName, ToolMaterial material, boolean isPrimitive) {
		return new ToolSet(materialName,
				registerSwordItem(materialName + "_sword", material, isPrimitive), registerItem(materialName + "_sword_blade", ModItemTags.SWORD_BLADES),
				registerAxeItem(materialName + "_axe", material, isPrimitive), registerItem(materialName + "_axe_head", ModItemTags.AXE_HEADS),
				registerPickaxeItem(materialName + "_pickaxe", material, isPrimitive), registerItem(materialName + "_pickaxe_head", ModItemTags.PICKAXE_HEADS),
				registerShovelItem(materialName + "_shovel", material, isPrimitive), registerItem(materialName + "_shovel_head", ModItemTags.SHOVEL_HEADS),
				registerHoeItem(materialName + "_hoe", material, isPrimitive), registerItem(materialName + "_hoe_head", ModItemTags.HOE_HEADS));
	}
	
	
	// Individual Tool Registers
	
	
	public static SwordItem registerSwordItem(String name, ToolMaterial material, boolean isPrimitive) {
		SwordItem item = (SwordItem) registerItem(name, new SwordItem(material, new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(material, 3, -2.4f))), List.of(ItemTags.SWORDS), Models.HANDHELD);
		if (isPrimitive) TAGS.computeIfAbsent(ModItemTags.PRIMITIVE_TOOLS, k -> new ArrayList<>()).add(item);
		return item;
	}
	
	public static PickaxeItem registerPickaxeItem(String name, ToolMaterial material, boolean isPrimitive) {
		PickaxeItem item = (PickaxeItem) registerItem(name, new PickaxeItem(material, new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(material, 1, -2.8f))), List.of(ItemTags.PICKAXES), Models.HANDHELD);
		if (isPrimitive) TAGS.computeIfAbsent(ModItemTags.PRIMITIVE_TOOLS, k -> new ArrayList<>()).add(item);
		return item;
	}
	
	public static AxeItem registerAxeItem(String name, ToolMaterial material, boolean isPrimitive) {
		AxeItem item = (AxeItem) registerItem(name, new AxeItem(material, new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(material, 5, -3.0f))), List.of(ItemTags.AXES), Models.HANDHELD);
		if (isPrimitive) TAGS.computeIfAbsent(ModItemTags.PRIMITIVE_TOOLS, k -> new ArrayList<>()).add(item);
		return item;
	}
	
	public static ShovelItem registerShovelItem(String name, ToolMaterial material, boolean isPrimitive) {
		ShovelItem item = (ShovelItem) registerItem(name, new ShovelItem(material, new Item.Settings().attributeModifiers(ShovelItem.createAttributeModifiers(material, 1.5f, -3.0f))), List.of(ItemTags.SHOVELS), Models.HANDHELD);
		if (isPrimitive) TAGS.computeIfAbsent(ModItemTags.PRIMITIVE_TOOLS, k -> new ArrayList<>()).add(item);
		return item;
	}
	
	public static HoeItem registerHoeItem(String name, ToolMaterial material, boolean isPrimitive) {
		HoeItem item = (HoeItem) registerItem(name, new HoeItem(material, new Item.Settings().attributeModifiers(HoeItem.createAttributeModifiers(material, -3.0f, 0f))), List.of(ItemTags.HOES), Models.HANDHELD);
		if (isPrimitive) TAGS.computeIfAbsent(ModItemTags.PRIMITIVE_TOOLS, k -> new ArrayList<>()).add(item);
		return item;
	}
	
	
	// Fauna logic
	
	
	private static HideSet registerHideItem(EntityType<?> entity) {
		HideSet item = new HideSet(entity, registerItem(entity.getUntranslatedName() + "_hide", ModItemTags.HIDES));
		HIDE_SETS.add(item);
		return item;
	}
	
	public static MeatSet registerMeatSet(EntityType<?> entity, int nutritionRaw, int nutritionCooked, float saturationMultiplier) {
		return registerMeatSet(entity, nutritionRaw, nutritionCooked, saturationMultiplier, false);
	}
	
	public static MeatSet registerMeatSet(EntityType<?> entity, int nutritionRaw, int nutritionCooked, float saturationMultiplier, boolean isSnack) {
		String name = entity.getUntranslatedName();
		MeatSet set = new MeatSet(entity,
				registerFoodItem("raw_" + name, nutritionRaw, saturationMultiplier * 0.5f, false),
				registerFoodItem("cooked_" + name, nutritionCooked, saturationMultiplier, false));
		MEAT_SETS.add(set);
		return set;
	}
	
	public static Item registerFoodItem(String name, int nutrition, float saturation, boolean isSnack) {
		FoodComponent.Builder builder = new FoodComponent.Builder().nutrition(nutrition).saturationModifier(saturation);
		if (isSnack) builder.snack();
		return registerItem(name, new Item(new Item.Settings().food(builder.build())));
	}
	
	
	// Pebbles & Molds
	
	
	public static PebbleSet registerPebbleSet(Block block) {
		return registerPebbleSet(block, block);
	}
	
	public static PebbleSet registerPebbleSet(Block source, Block recon) {
		String name = Registries.BLOCK.getId(source).getPath().replace("_block", "");
		PebbleSet set = new PebbleSet(registerItem(name + "_pebbles"), source, recon);
		PEBBLE_SETS.add(set);
		return set;
	}
	
	public static MoldSet registerMoldSet(String materialName, MoldMaterials moldMaterial, ItemConvertible ingredient) {
		MoldSet moldset = new MoldSet(registerBaseMoldItem(moldMaterial),
				registerMoldItem("ingot", moldMaterial, ModItemTags.INGOTS, ModItemTags.INGOT_MOLDS),
				registerMoldItem("pickaxe", moldMaterial, ModItemTags.PICKAXE_HEADS, ModItemTags.PICKAXE_HEAD_MOLDS),
				registerMoldItem("axe", moldMaterial, ModItemTags.AXE_HEADS, ModItemTags.AXE_HEAD_MOLDS),
				registerMoldItem("sword", moldMaterial, ModItemTags.SWORD_BLADES, ModItemTags.SWORD_BLADE_MOLDS),
				registerMoldItem("shovel", moldMaterial, ModItemTags.SHOVEL_HEADS, ModItemTags.SHOVEL_HEAD_MOLDS),
				registerMoldItem("hoe", moldMaterial, ModItemTags.HOE_HEADS, ModItemTags.HOE_HEAD_MOLDS),
				null, ingredient);
		MOLD_SETS.add(moldset);
		return moldset;
	}
	
	public static MoldSet registerMoldSet(String materialName, MoldMaterials moldMaterial, MoldSet fromSmelting) {
		MoldSet moldset = new MoldSet(
				registerBaseMoldItem(moldMaterial),
				registerMoldItem("ingot", moldMaterial, ModItemTags.INGOTS, ModItemTags.INGOT_MOLDS),
				registerMoldItem("pickaxe", moldMaterial, ModItemTags.PICKAXE_HEADS, ModItemTags.PICKAXE_HEAD_MOLDS),
				registerMoldItem("axe", moldMaterial, ModItemTags.AXE_HEADS, ModItemTags.AXE_HEAD_MOLDS),
				registerMoldItem("sword", moldMaterial, ModItemTags.SWORD_BLADES, ModItemTags.SWORD_BLADE_MOLDS),
				registerMoldItem("shovel", moldMaterial, ModItemTags.SHOVEL_HEADS, ModItemTags.SHOVEL_HEAD_MOLDS),
				registerMoldItem("hoe", moldMaterial, ModItemTags.HOE_HEADS, ModItemTags.HOE_HEAD_MOLDS),
				fromSmelting, null);
		MOLD_SETS.add(moldset);
		return moldset;
	}
	
	public static Item registerBaseMoldItem(MoldMaterials moldMaterial) { return registerItem(moldMaterial.getName() + "_mold_base"); }
	public static MoldItem registerMoldItem(String pattern, MoldMaterials material, TagKey<Item> accepted, TagKey<Item> tag) {
		return (MoldItem) registerItem(material.getName() + "_" + pattern + "_mold", new MoldItem(new Item.Settings(), material, accepted, pattern), tag);
	}
	
	
	// Initialization
	
	
	public static void registerModItems() {
		Rase.LOGGER.info("Hardening the player: Registering Items for " + Rase.MOD_ID);
		GROUPS.forEach((key, list) -> modifyEntriesEvent(key).register(entries -> list.forEach(entries::add)));
	}
}