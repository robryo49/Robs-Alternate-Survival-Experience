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
	FLINT(ModMaterials.FLINT, 4, 0.8f, 0.0f, 2),
	STONE(ModMaterials.STONE, 8, 1.0f, 0.5f, 5),
	
	// TIER 1: The Bronze Age
	BRONZE(ModMaterials.BRONZE, 30, 2.5f, 1.2f, 10),
	
	// TIER 2: The Iron/Steel Age
	SILVER(ModMaterials.SILVER, 20, 8.0f, 1.0f, 25),
	STEEL(ModMaterials.STEEL, 150, 4.0f, 2.0f, 12),
	
	// TIER 3: High-Temperature Metallurgy
	TITANIUM(ModMaterials.TITANIUM, 350, 5.5f, 2.5f, 10),
	
	// TIER 4: Exotic Alloys
	COBALT(ModMaterials.COBALT, 280, 9.0f, 3.2f, 15),
	SCANDIUM(ModMaterials.SCANDIUM, 650, 6.5f, 3.0f, 12),
	
	// TIER 5: Mythic
	MYTHRIL(ModMaterials.MYTHRIL, 1200, 8.5f, 4.0f, 22)
	
	;
	
	private final String name;
	private final int tier;
	private final TagKey<Block> inverseTag;
	private final int itemDurability;
	private final float miningSpeed;
	private final float attackDamage;
	private final int enchantability;
	private final Supplier<Ingredient> repairIngredient;
	
	ModToolMaterials(
			final ModMaterials material,
			final int itemDurability,
			final float miningSpeed,
			final float attackDamage,
			final int enchantability
	) {
		this.name = material.getId();
		this.tier = material.getTier();
		this.inverseTag = ModBlockTags.getIncorrectForTier(material.getTier());
		this.itemDurability = itemDurability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairIngredient = Suppliers.memoize(() -> Ingredient.ofItems(material.getMaterialItem()));
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
	
	public String getName() {
		return name;
	}
	
	public int getTier() {
		return tier;
	}
}
