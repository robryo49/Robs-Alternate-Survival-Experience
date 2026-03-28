package robryo49.rase.item.custom;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.block.custom.forge.ForgeTiers;

public class MoldMaterials {
	
	// --- Forge Tier Registration ---
	
	public static final MoldMaterial WET_CLAY = registerForgeTier("wet_clay", ForgeTiers.DEFAULT, 5, 1);
	public static final MoldMaterial CLAY = registerForgeTier("clay", ForgeTiers.PRIMITIVE, 5, 1);
	
	
	// --- Core Forge Tier Registration Logic ---
	
	
	private static MoldMaterial registerForgeTier(String name, ForgeTiers.ForgeTier forgeTier, int durability, float coolingFactor) {
		return new MoldMaterial(name, forgeTier, durability, coolingFactor);
	}
	
	private static TagKey<Block> blockTag(String name) {
		return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Rase.MOD_ID, name));
	}
	
	public record MoldMaterial(
			String name,
			ForgeTiers.ForgeTier forgeTier,
			int durability,
			float coolingFactor
	) { }
	
	public static void registerForgeTiers() {
		Rase.LOGGER.info("Registering Forge Tiers for " + Rase.MOD_ID);
	}
}