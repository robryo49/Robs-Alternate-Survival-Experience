package robryo49.rase.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.block.custom.forge.ForgeBlock;
import robryo49.rase.block.custom.forge.ForgeTiers;
import robryo49.rase.util.ModBlockTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModBlocks {
	
	// --- Collections ---
	
	
	public static final List<Block> ALL = new ArrayList<>();
	public static final List<Block> TRANSLATABLE = ALL;
	
	public static final Map<Models, List<Block>> MODELS = new HashMap<>();
	public static final Map<RegistryKey<ItemGroup>, List<Block>> GROUPS = new HashMap<>();
	public static final Map<TagKey<Block>, List<Block>> TAGS = new HashMap<>();
	
	public enum Models {
		
		CUBE_ALL(BlockStateModelGenerator::registerSimpleCubeAll),
		ORIENTABLE((generator, block) -> generator.registerNorthDefaultHorizontalRotated(block, TexturedModel.ORIENTABLE)),
		COOKER((generator, block) -> generator.registerCooker(block, TexturedModel.ORIENTABLE));
		
		private final BiConsumer<BlockStateModelGenerator, Block> generator;
		
		Models(BiConsumer<BlockStateModelGenerator, Block> generator) {
			this.generator = generator;
		}
		
		public void generate(BlockStateModelGenerator generator, Block block) {
			this.generator.accept(generator, block);
		}
		
		public void generate(BlockStateModelGenerator generator, List<Block> blocks) {
			blocks.forEach((block) -> generate(generator, block));
		}
	}
	
	
	// --- Block Registrations ---
	
	
	public static final Block PRIMITIVE_FORGE = registerBlock("primitive_forge",
			new ForgeBlock(AbstractBlock.Settings.create().luminance(state -> state.get(ForgeBlock.LIT) ? 13 : 0),
					ForgeTiers.PRIMITIVE), List.of(BlockTags.PICKAXE_MINEABLE), Models.COOKER);
	
	public static final Block ADVANCED_FORGE = registerBlock("advanced_forge",
			new ForgeBlock(AbstractBlock.Settings.create().luminance(state -> state.get(ForgeBlock.LIT) ? 13 : 0),
					ForgeTiers.ADVANCED), List.of(BlockTags.PICKAXE_MINEABLE), Models.COOKER);
	
	
	public static final Block BRONZE_BLOCK = registerMaterialBlock("bronze", 1, ModBlockTags.NEEDS_BRONZE_TOOL);
	public static final OreBlockSet TIN = registerOreBlockSet("tin", 1, ModBlockTags.NEEDS_BRONZE_TOOL);
	
	// --- Ore Specific Registration Methods ---
	
	
	public record OreBlockSet(Block ORE, Block DEEPSLATE_ORE, Block BLOCK, Block RAW_BLOCK) {}
	public record NetherOreBlockSet(Block NETHER_ORE, Block BLOCK, Block RAW_BLOCK) {}
	
	private static Block registerMaterialBlock(String materialName, float strength, TagKey<Block> toolTier) {
		return registerBlock(materialName + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.METAL)),
				List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL);
	}
	
	private static OreBlockSet registerOreBlockSet(String materialName, float strength, TagKey<Block> toolTier) {
		return new OreBlockSet(
				registerBlock(materialName + "_ore", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.STONE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock("deepslate_" + materialName + "_ore", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.DEEPSLATE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock(materialName + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.METAL)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock("raw_" + materialName + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.STONE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL)
		);
	}
	
	private static NetherOreBlockSet registerNetherOreBlockSet(String name, float strength, TagKey<Block> toolTier) {
		return new NetherOreBlockSet(
				registerBlock("nether_" + name + "_ore", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.NETHER_ORE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock(name + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.METAL)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL),
				registerBlock("raw_" + name + "_block", new Block(AbstractBlock.Settings.create().strength(strength).requiresTool().sounds(BlockSoundGroup.STONE)),
						List.of(BlockTags.PICKAXE_MINEABLE, toolTier), Models.CUBE_ALL)
		);
	}
	
	
	// --- Core Registration Logic ---
	
	
	private static Block registerBlock(String name, Block block) {
		return registerBlock(name, block, List.of(), Models.CUBE_ALL);
	}
	
	private static Block registerBlock(String name, Block block, List<TagKey<Block>> tags) {
		return registerBlock(name, block, tags, Models.CUBE_ALL);
	}
	
	private static Block registerBlock(String name, Block block, Models model) {
		return registerBlock(name, block, List.of(), model);
	}
	
	private static Block registerBlock(String name, Block block, List<TagKey<Block>> tags, Models model) {
		registerBlockItem(name, block);
		
		Block registeredBlock = Registry.register(Registries.BLOCK, Identifier.of(Rase.MOD_ID, name), block);
		
		ALL.add(registeredBlock);
		MODELS.computeIfAbsent(model, k -> new ArrayList<>()).add(registeredBlock);
		
		tags.forEach(tagKey -> TAGS.computeIfAbsent(tagKey, k -> new ArrayList<>()).add(registeredBlock));
		
		return registeredBlock;
	}
	
	private static void registerBlockItem(String name, Block block) {
		Registry.register(Registries.ITEM, Identifier.of(Rase.MOD_ID, name), new BlockItem(block, new Item.Settings()));
	}
	
	
	// --- Item Group Logic ---
	
	
	
	
	private static void addToItemGroup(RegistryKey<ItemGroup> groupRegistryKey, List<Block> blocks) {
		ItemGroupEvents.modifyEntriesEvent(groupRegistryKey).register(entries -> blocks.forEach(entries::add));
	}
	
	public static void registerModBlocks() {
		Rase.LOGGER.info("Registering Blocks for " + Rase.MOD_ID);
		GROUPS.forEach(ModBlocks::addToItemGroup);
	}
}