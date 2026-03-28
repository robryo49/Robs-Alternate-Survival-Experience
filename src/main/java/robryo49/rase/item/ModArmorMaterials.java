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
	public static final RegistryEntry<ArmorMaterial> BRONZE = registerArmorMaterial("bronze", ModItems.BRONZE.INGOT(),
			2, 6, 4, 2, 4, 15, 0, 0);
	
	
	// --- Material Registration ---
	
	
	// --- Core Registration Logic ---
	
	
	public static RegistryEntry<ArmorMaterial> registerArmorMaterial(
			String name, Item ingredient,
			int helmetProtection, int chestplateProtection, int leggingsProtection, int bootsProtection, int bodyProtection,
			int enchantability, float toughness, float knockbackResistance) {
		return registerArmorMaterial(
				name, () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
					map.put(ArmorItem.Type.BOOTS, bootsProtection);
					map.put(ArmorItem.Type.LEGGINGS, leggingsProtection);
					map.put(ArmorItem.Type.CHESTPLATE, chestplateProtection);
					map.put(ArmorItem.Type.HELMET, helmetProtection);
					map.put(ArmorItem.Type.BODY, bodyProtection);
				}), enchantability, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, () -> Ingredient.ofItems(ingredient),
						List.of(new ArmorMaterial.Layer(Identifier.of(Rase.MOD_ID, name))), toughness, knockbackResistance)
		);
	}
	
	public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> material) {
		return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(Rase.MOD_ID, name), material.get());
	}
}
