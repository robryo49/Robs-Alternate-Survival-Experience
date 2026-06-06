package robryo49.rase.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import robryo49.rase.Rase;
import robryo49.rase.world.ModPlacedFeatures;

import java.util.List;
import java.util.function.Predicate;

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
	
	private static boolean isWarm(net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext ctx) {
		return ctx.getBiome().getTemperature() > 0.8f && ctx.getBiomeRegistryEntry().isIn(
				net.minecraft.registry.tag.BiomeTags.IS_OVERWORLD);
	}
	
	private static boolean isCold(net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext ctx) {
		return ctx.getBiome().getTemperature() < 0.15f && ctx.getBiomeRegistryEntry().isIn(
				net.minecraft.registry.tag.BiomeTags.IS_OVERWORLD);
	}
	
	private static boolean isMountain(net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext ctx) {
		return ctx.getBiomeRegistryEntry().isIn(net.minecraft.registry.tag.BiomeTags.IS_MOUNTAIN);
	}
	
	public static void generateModOresGen() {
		
		BiomeModifications.create(Rase.getIdentifier("remove_vanilla_ores"))
				.add(ModificationPhase.REMOVALS, BiomeSelectors.foundInOverworld(), ctx ->
						VANILLA_ORES.forEach(key ->
								ctx.getGenerationSettings().removeFeature(key)
						)
				);
		
		addOverworld(ModPlacedFeatures.TIN_ORE_PLACED_KEY);
		addOverworld(ModPlacedFeatures.MAGNETITE_ORE_PLACED_KEY);
		addOverworld(ModPlacedFeatures.COAL_ORE_PLACED_KEY);
		addOverworld(ModPlacedFeatures.COPPER_ORE_PLACED_KEY);
		
		addOverworld(ModPlacedFeatures.SILVER_ORE_PLACED_KEY);
		addOverworld(ModPlacedFeatures.LEAD_ORE_PLACED_KEY);
		addOverworld(ModPlacedFeatures.IRON_ORE_PLACED_KEY);
		
		addBiome(ModOreGeneration::isWarm, ModPlacedFeatures.PLATINUM_ORE_PLACED_KEY);
		addBiome(ModOreGeneration::isCold, ModPlacedFeatures.TUNGSTEN_ORE_PLACED_KEY);
		addBiome(ModOreGeneration::isWarm, ModPlacedFeatures.GOLD_ORE_PLACED_KEY);
		addOverworld(ModPlacedFeatures.LAPIS_ORE_PLACED_KEY);
		addOverworld(ModPlacedFeatures.REDSTONE_ORE_PLACED_KEY);
		
		addBiome(ModOreGeneration::isCold, ModPlacedFeatures.PALLADIUM_ORE_PLACED_KEY);
		addOverworld(ModPlacedFeatures.DIAMOND_ORE_PLACED_KEY);
		addBiome(ModOreGeneration::isMountain, ModPlacedFeatures.EMERALD_ORE_PLACED_KEY);
		
		addNether(ModPlacedFeatures.COBALT_ORE_PLACED_KEY);
		addNether(ModPlacedFeatures.RHEXIS_ORE_PLACED_KEY);
		
		addOverworld(ModPlacedFeatures.MITHRIL_ORE_PLACED_KEY);
	}
	
	
	private static void addOverworld(RegistryKey<PlacedFeature> key) {
		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Feature.UNDERGROUND_ORES, key);
	}
	
	private static void addNether(RegistryKey<PlacedFeature> key) {
		BiomeModifications.addFeature(
				BiomeSelectors.foundInTheNether(),
				GenerationStep.Feature.UNDERGROUND_ORES, key);
	}
	
	private static void addBiome(Predicate<BiomeSelectionContext> selector,
	                             RegistryKey<PlacedFeature> key) {
		BiomeModifications.addFeature(selector,
				GenerationStep.Feature.UNDERGROUND_ORES, key);
	}
}
