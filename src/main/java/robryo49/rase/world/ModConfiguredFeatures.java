package robryo49.rase.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
	
	public static final RegistryKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = registerKey("tin_ore");
	
	public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
		RuleTest stoneReplaceable = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
		RuleTest deepslateReplaceable = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
		RuleTest netherReplaceable = new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER);
		
		List<OreFeatureConfig.Target> tinOres = List.of(
				OreFeatureConfig.createTarget(stoneReplaceable, ModBlocks.TIN.ORE().getDefaultState()),
				OreFeatureConfig.createTarget(deepslateReplaceable, ModBlocks.TIN.DEEPSLATE_ORE().getDefaultState())
		);
		
		register(context, TIN_ORE_KEY, Feature.ORE, new OreFeatureConfig(tinOres, 6));
	}
	
	public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Rase.getIdentifier(id));
	}
	
	private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
	                                                                               RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
		context.register(key, new ConfiguredFeature<>(feature, config));
	}
}
