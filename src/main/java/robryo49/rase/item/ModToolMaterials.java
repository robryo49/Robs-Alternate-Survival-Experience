package robryo49.rase.item;

import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import robryo49.rase.util.ModBlockTags;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {
	FLINT(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 4, 0.8f, 0.0f, 2, () -> Ingredient.ofItems(ModItems.FLINT_SHARD)),
	STONE(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 8, 1.0f, 0.5f, 5, () -> Ingredient.ofItems(ModItems.STONE_PEBBLES.PEBBLE())),
	
	// TIER 1: The Bronze Age
	BRONZE(ModBlockTags.INCORRECT_FOR_BRONZE_TOOL, 30, 2.5f, 1.2f, 10, () -> Ingredient.ofItems(ModItems.BRONZE.INGOT())),
	
	// TIER 2: The Iron/Steel Age
	SILVER(BlockTags.INCORRECT_FOR_IRON_TOOL, 20, 8.0f, 1.0f, 25, () -> Ingredient.ofItems(ModItems.SILVER.INGOT())),
	STEEL(BlockTags.INCORRECT_FOR_IRON_TOOL, 150, 4.0f, 2.0f, 12, () -> Ingredient.ofItems(ModItems.STEEL.INGOT())),
	
	// TIER 3: High-Temperature Metallurgy
	TITANIUM(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 350, 5.5f, 2.5f, 10, () -> Ingredient.ofItems(ModItems.TITANIUM.INGOT())),
	
	// TIER 4: Exotic Alloys
	COBALT(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 280, 9.0f, 3.2f, 15, () -> Ingredient.ofItems(ModItems.COBALT.INGOT())),
	SCANDIUM(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 650, 6.5f, 3.0f, 12, () -> Ingredient.ofItems(ModItems.SCANDIUM.INGOT())),
	
	// TIER 5: Mythic
	MYTHRIL(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1200, 8.5f, 4.0f, 22, () -> Ingredient.ofItems(ModItems.MYTHRIL.INGOT()))
	
	;
	
	private final TagKey<Block> inverseTag;
	private final int itemDurability;
	private final float miningSpeed;
	private final float attackDamage;
	private final int enchantability;
	private final Supplier<Ingredient> repairIngredient;
	
	ModToolMaterials(
			final TagKey<Block> inverseTag,
			final int itemDurability,
			final float miningSpeed,
			final float attackDamage,
			final int enchantability,
			final Supplier<Ingredient> repairIngredient
	) {
		this.inverseTag = inverseTag;
		this.itemDurability = itemDurability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairIngredient = Suppliers.memoize(repairIngredient::get);
	}
	
	@Override
	public int getDurability() {
		return this.itemDurability;
	}
	
	@Override
	public float getMiningSpeedMultiplier() {
		return this.miningSpeed;
	}
	
	@Override
	public float getAttackDamage() {
		return this.attackDamage;
	}
	
	@Override
	public TagKey<Block> getInverseTag() {
		return this.inverseTag;
	}
	
	@Override
	public int getEnchantability() {
		return this.enchantability;
	}
	
	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}
}
