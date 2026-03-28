package robryo49.rase.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import robryo49.rase.Rase;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
	
	public static final RegistryEntry<ArmorMaterial> BRONZE = registerArmorMaterial("bronze", () -> Ingredient.ofItems(ModItems.BRONZE.INGOT()),
			1, 3, 4, 1, 8, 0.0f, 0.0f);
	
	public static final RegistryEntry<ArmorMaterial> SILVER = registerArmorMaterial("silver", () -> Ingredient.ofItems(ModItems.SILVER.INGOT()),
			1, 3, 5, 2, 25, 0.0f, 0.0f);
	
	public static final RegistryEntry<ArmorMaterial> STEEL = registerArmorMaterial("steel", () -> Ingredient.ofItems(ModItems.STEEL.INGOT()),
			2, 5, 6, 2, 10, 1.0f, 0.0f);
	
	public static final RegistryEntry<ArmorMaterial> TITANIUM = registerArmorMaterial("titanium", () -> Ingredient.ofItems(ModItems.TITANIUM.INGOT()),
			2, 6, 7, 3, 10, 2.0f, 0.1f);
	
	public static final RegistryEntry<ArmorMaterial> COBALT = registerArmorMaterial("cobalt", () -> Ingredient.ofItems(ModItems.COBALT.INGOT()),
			3, 5, 7, 3, 15, 1.0f, 0.0f);
	
	public static final RegistryEntry<ArmorMaterial> SCANDIUM = registerArmorMaterial("scandium", () -> Ingredient.ofItems(ModItems.SCANDIUM.INGOT()),
			3, 6, 8, 3, 12, 3.0f, 0.2f);
	
	public static final RegistryEntry<ArmorMaterial> MYTHRIL = registerArmorMaterial("mythril", () -> Ingredient.ofItems(ModItems.MYTHRIL.INGOT()),
			4, 7, 9, 4, 22, 4.0f, 0.3f);
	
	
	// --- Core Registration Logic ---
	
	
	public static RegistryEntry<ArmorMaterial> registerArmorMaterial(
			String name, Supplier<Ingredient> ingredient,
			int helmetProtection, int chestplateProtection, int leggingsProtection, int bootsProtection,
			int enchantability, float toughness, float knockbackResistance) {
		return registerArmorMaterial(
				name, () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
					map.put(ArmorItem.Type.BOOTS, bootsProtection);
					map.put(ArmorItem.Type.LEGGINGS, leggingsProtection);
					map.put(ArmorItem.Type.CHESTPLATE, chestplateProtection);
					map.put(ArmorItem.Type.HELMET, helmetProtection);
				}), enchantability, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, ingredient,
						List.of(new ArmorMaterial.Layer(Rase.getIdentifier(name))), toughness, knockbackResistance)
		);
	}
	
	public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> material) {
		return Registry.registerReference(Registries.ARMOR_MATERIAL, Rase.getIdentifier(name), material.get());
	}
}
