package robryo49.rase.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.item.ModMaterials;

import java.util.List;

public class ModConfiguredFeatures {
	
	public static final RegistryKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY        = registerKey(ModMaterials.TIN);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MAGNETITE_ORE_KEY  = registerKey(ModMaterials.MAGNETITE);
	public static final RegistryKey<ConfiguredFeature<?, ?>> COAL_ORE_KEY       = registerKey(ModMaterials.COAL);
	public static final RegistryKey<ConfiguredFeature<?, ?>> COPPER_ORE_KEY     = registerKey(ModMaterials.COPPER);
	public static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY     = registerKey(ModMaterials.SILVER);
	public static final RegistryKey<ConfiguredFeature<?, ?>> LEAD_ORE_KEY       = registerKey(ModMaterials.LEAD);
	public static final RegistryKey<ConfiguredFeature<?, ?>> IRON_ORE_KEY       = registerKey(ModMaterials.IRON);
	public static final RegistryKey<ConfiguredFeature<?, ?>> PLATINUM_ORE_KEY   = registerKey(ModMaterials.PLATINUM);
	public static final RegistryKey<ConfiguredFeature<?, ?>> TUNGSTEN_ORE_KEY   = registerKey(ModMaterials.TUNGSTEN);
	public static final RegistryKey<ConfiguredFeature<?, ?>> GOLD_ORE_KEY       = registerKey(ModMaterials.GOLD);
	public static final RegistryKey<ConfiguredFeature<?, ?>> LAPIS_ORE_KEY      = registerKey(ModMaterials.LAPIS);
	public static final RegistryKey<ConfiguredFeature<?, ?>> REDSTONE_ORE_KEY   = registerKey(ModMaterials.REDSTONE);
	public static final RegistryKey<ConfiguredFeature<?, ?>> PALLADIUM_ORE_KEY  = registerKey(ModMaterials.PALLADIUM);
	public static final RegistryKey<ConfiguredFeature<?, ?>> DIAMOND_ORE_KEY    = registerKey(ModMaterials.DIAMOND);
	public static final RegistryKey<ConfiguredFeature<?, ?>> EMERALD_ORE_KEY    = registerKey(ModMaterials.EMERALD);
	public static final RegistryKey<ConfiguredFeature<?, ?>> COBALT_ORE_KEY     = registerKey(ModMaterials.COBALT);
	public static final RegistryKey<ConfiguredFeature<?, ?>> RHEXIS_ORE_KEY     = registerKey(ModMaterials.RHEXIS);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MITHRIL_ORE_KEY    = registerKey(ModMaterials.MITHRIL);
	
	public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
		register(context, TIN_ORE_KEY,          ModBlocks.TIN, 8);
		register(context, MAGNETITE_ORE_KEY,    ModBlocks.MAGNETITE, 6);
		register(context, COAL_ORE_KEY,         ModBlocks.COAL, 17);
		register(context, COPPER_ORE_KEY,       ModBlocks.COPPER, 10);
		register(context, SILVER_ORE_KEY,       ModBlocks.SILVER, 7);
		register(context, LEAD_ORE_KEY,         ModBlocks.LEAD, 8);
		register(context, IRON_ORE_KEY,         ModBlocks.IRON, 9, 0.2f);
		register(context, PLATINUM_ORE_KEY,     ModBlocks.PLATINUM, 5, 0.3f);
		register(context, TUNGSTEN_ORE_KEY,     ModBlocks.TUNGSTEN, 4, 0.5f);
		register(context, GOLD_ORE_KEY,         ModBlocks.GOLD, 9, 0.4f);
		register(context, LAPIS_ORE_KEY,        ModBlocks.LAPIS, 7);
		register(context, REDSTONE_ORE_KEY,     ModBlocks.REDSTONE, 8, 0.3f);
		register(context, PALLADIUM_ORE_KEY,    ModBlocks.PALLADIUM, 4, 0.2f);
		register(context, DIAMOND_ORE_KEY,      ModBlocks.DIAMOND, 7, 0.6f);
		register(context, EMERALD_ORE_KEY,      ModBlocks.EMERALD, 3);
		register(context, COBALT_ORE_KEY,       ModBlocks.COBALT, 6, 0.6f);
		register(context, RHEXIS_ORE_KEY,       ModBlocks.RHEXIS, 4, 0.6f);
		register(context, MITHRIL_ORE_KEY,      ModBlocks.MITHRIL, 3, 1.0f);
	}
	
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.OreBlockSet blockSet, int size) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), blockSet.ORE().getDefaultState()),
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), blockSet.DEEPSLATE_ORE().getDefaultState())),
				size));
	}
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.CrystalBlockSet blockSet, int size) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), blockSet.ORE().getDefaultState()),
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), blockSet.DEEPSLATE_ORE().getDefaultState())),
				size));
	}
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.NetherOreBlockSet blockSet, int size) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER), blockSet.NETHER_ORE().getDefaultState())),
				size));
	}
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.NetherCrystalBlockSet blockSet, int size) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER), blockSet.NETHER_ORE().getDefaultState())),
				size));
	}
	
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.OreBlockSet blockSet, int size, float discardChance) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES),    blockSet.ORE().getDefaultState()),
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), blockSet.DEEPSLATE_ORE().getDefaultState())),
				size, discardChance));
	}
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.CrystalBlockSet blockSet, int size, float discardChance) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES),    blockSet.ORE().getDefaultState()),
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), blockSet.DEEPSLATE_ORE().getDefaultState())),
				size, discardChance));
	}
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.NetherOreBlockSet blockSet, int size, float discardChance) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER), blockSet.NETHER_ORE().getDefaultState())), size, discardChance));
	}
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.NetherCrystalBlockSet blockSet, int size, float discardChance) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER), blockSet.NETHER_ORE().getDefaultState())), size, discardChance));
	}
	
	
	public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Rase.getIdentifier(id));
	}
	
	public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(ModMaterials material) {
		return registerKey(material.getId() + "_ore");
	}
	public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(ModMaterials material, String variant) {
		return registerKey(material.getId() + "_ore_" + variant);
	}
	
	
	private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
	                                                                               RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
		context.register(key, new ConfiguredFeature<>(feature, config));
	}
}
