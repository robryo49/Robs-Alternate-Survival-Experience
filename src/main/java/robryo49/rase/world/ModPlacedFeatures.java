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
import robryo49.rase.item.ModMaterials;

import java.util.List;

public class ModPlacedFeatures {
	
	public static final RegistryKey<PlacedFeature> TIN_ORE_PLACED_KEY        = registerKey(ModMaterials.TIN);
	public static final RegistryKey<PlacedFeature> MAGNETITE_ORE_PLACED_KEY  = registerKey(ModMaterials.MAGNETITE);
	public static final RegistryKey<PlacedFeature> COAL_ORE_PLACED_KEY       = registerKey(ModMaterials.COAL);
	public static final RegistryKey<PlacedFeature> COPPER_ORE_PLACED_KEY     = registerKey(ModMaterials.COPPER);
	
	public static final RegistryKey<PlacedFeature> SILVER_ORE_PLACED_KEY    = registerKey(ModMaterials.SILVER);
	public static final RegistryKey<PlacedFeature> LEAD_ORE_PLACED_KEY      = registerKey(ModMaterials.LEAD);
	public static final RegistryKey<PlacedFeature> IRON_ORE_PLACED_KEY      = registerKey(ModMaterials.IRON);
	
	public static final RegistryKey<PlacedFeature> PLATINUM_ORE_PLACED_KEY  = registerKey(ModMaterials.PLATINUM);
	public static final RegistryKey<PlacedFeature> TUNGSTEN_ORE_PLACED_KEY  = registerKey(ModMaterials.TUNGSTEN);
	public static final RegistryKey<PlacedFeature> GOLD_ORE_PLACED_KEY      = registerKey(ModMaterials.GOLD);
	public static final RegistryKey<PlacedFeature> LAPIS_ORE_PLACED_KEY     = registerKey(ModMaterials.LAPIS);
	public static final RegistryKey<PlacedFeature> REDSTONE_ORE_PLACED_KEY  = registerKey(ModMaterials.REDSTONE);
	
	public static final RegistryKey<PlacedFeature> PALLADIUM_ORE_PLACED_KEY    = registerKey(ModMaterials.PALLADIUM);
	public static final RegistryKey<PlacedFeature> DIAMOND_ORE_PLACED_KEY      = registerKey(ModMaterials.DIAMOND);
	public static final RegistryKey<PlacedFeature> EMERALD_ORE_PLACED_KEY      = registerKey(ModMaterials.EMERALD);
	
	public static final RegistryKey<PlacedFeature> COBALT_ORE_PLACED_KEY        = registerKey(ModMaterials.COBALT);
	public static final RegistryKey<PlacedFeature> RHEXIS_ORE_PLACED_KEY        = registerKey(ModMaterials.RHEXIS);
	
	public static final RegistryKey<PlacedFeature> MITHRIL_ORE_PLACED_KEY = registerKey(ModMaterials.MITHRIL);
	
	
	public static void bootstrap(Registerable<PlacedFeature> context) {
		RegistryEntryLookup<ConfiguredFeature<?, ?>> cf = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
		
		register(context, cf, TIN_ORE_PLACED_KEY,       ModConfiguredFeatures.TIN_ORE_KEY,       5,   32,  96);
		register(context, cf, MAGNETITE_ORE_PLACED_KEY, ModConfiguredFeatures.MAGNETITE_ORE_KEY, 4,   16,  72);
		register(context, cf, COAL_ORE_PLACED_KEY,      ModConfiguredFeatures.COAL_ORE_KEY,      10,   0, 136);
		register(context, cf, COPPER_ORE_PLACED_KEY,    ModConfiguredFeatures.COPPER_ORE_KEY,    8,   0,  96);
		
		register(context, cf, SILVER_ORE_PLACED_KEY,    ModConfiguredFeatures.SILVER_ORE_KEY,    4,  -16,  48);
		register(context, cf, LEAD_ORE_PLACED_KEY,      ModConfiguredFeatures.LEAD_ORE_KEY,      5,    0,  48);
		register(context, cf, IRON_ORE_PLACED_KEY,      ModConfiguredFeatures.IRON_ORE_KEY,      8, -24,  56);
		
		register(context, cf, PLATINUM_ORE_PLACED_KEY,  ModConfiguredFeatures.PLATINUM_ORE_KEY,  3,  -48,  16);
		register(context, cf, TUNGSTEN_ORE_PLACED_KEY,  ModConfiguredFeatures.TUNGSTEN_ORE_KEY,  3,  -64, -16);
		register(context, cf, GOLD_ORE_PLACED_KEY,      ModConfiguredFeatures.GOLD_ORE_KEY,      4,  -64,  32);
		register(context, cf, LAPIS_ORE_PLACED_KEY,     ModConfiguredFeatures.LAPIS_ORE_KEY,     2,  -32,  32);
		register(context, cf, REDSTONE_ORE_PLACED_KEY,  ModConfiguredFeatures.REDSTONE_ORE_KEY,  7,  -64,  16);
		
		register(context, cf, PALLADIUM_ORE_PLACED_KEY, ModConfiguredFeatures.PALLADIUM_ORE_KEY,        3,  -64, -32);
		register(context, cf, DIAMOND_ORE_PLACED_KEY,   ModConfiguredFeatures.DIAMOND_ORE_KEY,          5,  -64,  16);
		register(context, cf, EMERALD_ORE_PLACED_KEY,   ModConfiguredFeatures.EMERALD_ORE_KEY,          100, -16, 96);
		
		register(context, cf, COBALT_ORE_PLACED_KEY,    ModConfiguredFeatures.COBALT_ORE_KEY,       6,   10, 117);
		register(context, cf, RHEXIS_ORE_PLACED_KEY,    ModConfiguredFeatures.RHEXIS_ORE_KEY,       4,   10,  50);
		
		register(context, cf, MITHRIL_ORE_PLACED_KEY,   ModConfiguredFeatures.MITHRIL_ORE_KEY, 1, -64, -48);
	}
	
	
	public static RegistryKey<PlacedFeature> registerKey(String id) {
		return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Rase.getIdentifier(id));
	}
	
	public static RegistryKey<PlacedFeature> registerKey(ModMaterials material) {
		return registerKey(material.getId() + "_ore");
	}
	public static RegistryKey<PlacedFeature> registerKey(ModMaterials material, String variant) {
		return registerKey(material.getId() + "_ore_" + variant);
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
