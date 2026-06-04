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
	
	public static final RegistryKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = registerKey(ModMaterials.TIN);
	
	
	public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
		
		register(context, TIN_ORE_KEY, ModBlocks.TIN, 6);
	}
	
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.OreBlockSet blockSet, int size) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), blockSet.ORE().getDefaultState()),
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), blockSet.DEEPSLATE_ORE().getDefaultState())), size));
	}
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.CrystalBlockSet blockSet, int size) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), blockSet.ORE().getDefaultState()),
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), blockSet.DEEPSLATE_ORE().getDefaultState())), size));
	}
	
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.NetherOreBlockSet blockSet, int size) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER), blockSet.NETHER_ORE().getDefaultState())), size));
	}
	public static void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, ModBlocks.NetherCrystalBlockSet blockSet, int size) {
		register(context, key, Feature.ORE, new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER), blockSet.NETHER_ORE().getDefaultState())), size));
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
