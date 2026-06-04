package robryo49.rase.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import robryo49.rase.Rase;

import java.util.List;

public class ModPlacedFeatures {
	
	public static final RegistryKey<PlacedFeature> TIN_ORE_PLACED_KEY = registerKey("tin_ore_placed");
	
	public static void bootstrap(Registerable<PlacedFeature> context) {
		RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
		
		register(context, configuredFeatures, TIN_ORE_PLACED_KEY, ModConfiguredFeatures.TIN_ORE_KEY, 6, 0, 80);
	}
	
	public static RegistryKey<PlacedFeature> registerKey(String id) {
		return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Rase.getIdentifier(id));
	}
	
	
	private static void register(Registerable<PlacedFeature> context, RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatures, RegistryKey<PlacedFeature> key,
	                             RegistryKey<ConfiguredFeature<?, ?>> config, int count, int minHeight, int maxHeight) {
		register(context, key, configuredFeatures.getOrThrow(config), ModOrePlacement.modifiers(count, minHeight, maxHeight));
	}
	
	private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> config,
	                             List<PlacementModifier> modifiers) {
		context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
	}
	
	private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> feature,
	                                                                               RegistryEntry<ConfiguredFeature<?, ?>> config, PlacementModifier... modifiers) {
		register(context, feature, config, List.of(modifiers));
	}
}
