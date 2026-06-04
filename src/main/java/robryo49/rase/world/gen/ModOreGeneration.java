package robryo49.rase.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import robryo49.rase.world.ModPlacedFeatures;

public class ModOreGeneration {
	public static void generateModOresGen() {
		
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
				ModPlacedFeatures.TIN_ORE_PLACED_KEY);
		
	}
}
