package robryo49.rase.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import robryo49.rase.Rase;
import robryo49.rase.world.ModPlacedFeatures;

import java.util.List;

public class ModOreGeneration {
	
	private static final List<RegistryKey<PlacedFeature>> VANILLA_ORES = List.of(
			
			OrePlacedFeatures.ORE_COAL_LOWER,
			OrePlacedFeatures.ORE_COAL_UPPER,
			
			OrePlacedFeatures.ORE_IRON_SMALL,
			OrePlacedFeatures.ORE_IRON_MIDDLE,
			OrePlacedFeatures.ORE_IRON_UPPER,
			
			OrePlacedFeatures.ORE_GOLD,
			OrePlacedFeatures.ORE_GOLD_LOWER,
			OrePlacedFeatures.ORE_GOLD_EXTRA,
			OrePlacedFeatures.ORE_GOLD_DELTAS,
			
			OrePlacedFeatures.ORE_COPPER,
			OrePlacedFeatures.ORE_COPPER_LARGE,
			
			OrePlacedFeatures.ORE_LAPIS,
			OrePlacedFeatures.ORE_LAPIS_BURIED,
			
			OrePlacedFeatures.ORE_REDSTONE,
			OrePlacedFeatures.ORE_REDSTONE_LOWER,
			
			OrePlacedFeatures.ORE_DIAMOND,
			OrePlacedFeatures.ORE_DIAMOND_LARGE,
			OrePlacedFeatures.ORE_DIAMOND_BURIED,
			OrePlacedFeatures.ORE_DIAMOND_MEDIUM
	);
	
	public static void generateModOresGen() {
		
		BiomeModifications.create(Rase.getIdentifier("remove_vanilla_ores"))
				.add(ModificationPhase.REMOVALS, BiomeSelectors.foundInOverworld(), ctx ->
						VANILLA_ORES.forEach(key ->
								ctx.getGenerationSettings().removeFeature(key)
						)
				);
		
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
				ModPlacedFeatures.TIN_ORE_PLACED_KEY);
		
	}
}
