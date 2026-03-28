package robryo49.rase.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.block.custom.forge.*;
import robryo49.rase.item.ModMaterials;
import robryo49.rase.util.ModBlockTags;

import java.util.*;
import java.util.function.BiConsumer;

public class ModBlocks {
	
	// --- Collections for DataGen and Registry ---
	public static final List<Block> ALL = new ArrayList<>();
	public static final Map<Models, List<Block>> MODELS = new EnumMap<>(Models.class);
	public static final Map<TagKey<Block>, List<Block>> TAGS = new HashMap<>();
	
	// --- 1. Primitive & Utility Blocks ---
	public static final Block CHIPPED_STONE = registerBlock("chipped_stone", 2.5f, 6.0f, BlockSoundGroup.STONE);
	
	// --- 2. Forges (Luminance + Resistance) ---
	public static final Block PRIMITIVE_FORGE = registerForge("primitive_forge", ForgeTiers.PRIMITIVE, 3.5f, 10.0f);
	public static final Block BASIC_FORGE = registerForge("basic_forge", ForgeTiers.BASIC, 7.0f, 50.0f);
	public static final Block REFINED_FORGE = registerForge("refined_forge", ForgeTiers.REFINED, 10.0f, 80.0f);
	public static final Block ADVANCED_FORGE = registerForge("advanced_forge", ForgeTiers.ADVANCED, 15.0f, 100.0f);
	public static final Block ETHEREAL_FORGE = registerForge("ethereal_forge", ForgeTiers.ETHEREAL, 20.0f, 1200.0f);
	
	
	
	// --- 3. Material Sets (Hardness / Resistance / Tool Gating) ---
	
	public static final OreBlockSet TIN = registerOreBlockSet(ModMaterials.TIN, 3.0f, 3.0f);
	public static final OreBlockSet ZINC = registerOreBlockSet(ModMaterials.ZINC, 3.0f, 3.0f);
	public static final OreBlockSet MAGNETITE = registerOreBlockSet(ModMaterials.MAGNETITE, 4.0f, 6.0f);
	
	public static final AlloyBlockSet BRONZE = registerAlloyBlockSet(ModMaterials.BRONZE, 6.0f, 15.0f);
	public static final OreBlockSet SILVER = registerOreBlockSet(ModMaterials.SILVER, 5.0f, 6.0f);
	public static final OreBlockSet LEAD = registerOreBlockSet(ModMaterials.LEAD, 6.5f, 30.0f);
	
	public static final AlloyBlockSet STEEL = registerAlloyBlockSet(ModMaterials.STEEL, 8.0f, 45.0f);
	public static final OreBlockSet TITANIUM = registerOreBlockSet(ModMaterials.TITANIUM, 10.0f, 35.0f);
	public static final OreBlockSet PLATINUM = registerOreBlockSet(ModMaterials.PLATINUM, 8.0f, 20.0f);
	
	public static final OreBlockSet TUNGSTEN = registerOreBlockSet(ModMaterials.TUNGSTEN, 20.0f, 80.0f);
	public static final OreBlockSet PALLADIUM = registerOreBlockSet(ModMaterials.PALLADIUM, 12.0f, 25.0f);
	public static final NetherOreBlockSet COBALT = registerNetherOreBlockSet(ModMaterials.COBALT, 15.0f, 20.0f);
	
	public static final AlloyBlockSet SCANDIUM = registerAlloyBlockSet(ModMaterials.SCANDIUM, 22.0f, 70.0f);
	public static final OreBlockSet MYTHRIL = registerOreBlockSet(ModMaterials.MYTHRIL, 30.0f, 1200.0f);
	public static final NetherCrystalBlockSet RHEXIS = registerNetherCrystalBlockSet(ModMaterials.RHEXIS, 35.0f, 1200.0f);
	
	// --- 4. Anvils ---
	public static final SmithingAnvilBlockSet STONE_ANVIL = registerSmithingAnvilBlockSet(SmithingAnvilMaterials.STONE);
	public static final SmithingAnvilBlockSet LEAD_ANVIL = registerSmithingAnvilBlockSet(SmithingAnvilMaterials.LEAD);
	public static final SmithingAnvilBlockSet TITANIUM_ANVIL = registerSmithingAnvilBlockSet(SmithingAnvilMaterials.TITANIUM);
	public static final SmithingAnvilBlockSet TUNGSTEN_ANVIL = registerSmithingAnvilBlockSet(SmithingAnvilMaterials.TUNGSTEN);
	
	// --- Helper Logic & Registration Wrappers ---
	
	private static Block registerForge(String name, ForgeTiers tier, float strength, float resistance) {
		return registerBlock(name, new ForgeBlock(AbstractBlock.Settings.create()
						.strength(strength, resistance)
						.luminance(state -> state.get(ForgeBlock.LIT) ? 13 : 0), tier),
				List.of(BlockTags.PICKAXE_MINEABLE, ModBlockTags.FORGES), Models.COOKER);
	}
	
	public static AlloyBlockSet registerAlloyBlockSet(ModMaterials material, float strength, float resistance) {
		String id = material.getId();
		TagKey<Block> toolTag = ModBlockTags.getNeedsTier(material.getInferiorTier());
		return new AlloyBlockSet(registerBlock(id + "_block", strength, resistance,
				BlockSoundGroup.METAL, List.of(BlockTags.PICKAXE_MINEABLE, toolTag)));
	}
	
	public static OreBlockSet registerOreBlockSet(ModMaterials material, float strength, float resistance) {
		String id = material.getId();
		TagKey<Block> toolTag = ModBlockTags.getNeedsTier(material.getInferiorTier());
		return new OreBlockSet(
				registerBlock(id + "_ore", strength, resistance,
						BlockSoundGroup.STONE, List.of(BlockTags.PICKAXE_MINEABLE, ModBlockTags.STONE_ORES, toolTag)),
				registerBlock("deepslate_" + id + "_ore", strength * 1.6f, resistance * 1.6f,
						BlockSoundGroup.DEEPSLATE, List.of(BlockTags.PICKAXE_MINEABLE, ModBlockTags.DEEPSLATE_ORES, toolTag)),
				registerBlock(id + "_block", strength, resistance + 5.0f,
						BlockSoundGroup.METAL, List.of(BlockTags.PICKAXE_MINEABLE, toolTag)),
				registerBlock("raw_" + id + "_block", strength, resistance,
						BlockSoundGroup.STONE, List.of(BlockTags.PICKAXE_MINEABLE, toolTag))
		);
	}
	
	public static NetherOreBlockSet registerNetherOreBlockSet(ModMaterials material, float strength, float resistance) {
		String id = material.getId();
		TagKey<Block> toolTag = ModBlockTags.getNeedsTier(material.getInferiorTier());
		return new NetherOreBlockSet(
				registerBlock("nether_" + id + "_ore", strength, resistance,
						BlockSoundGroup.NETHER_ORE, List.of(BlockTags.PICKAXE_MINEABLE, ModBlockTags.NETHER_ORES, toolTag)),
				registerBlock(id + "_block", strength, resistance + 10.0f,
						BlockSoundGroup.METAL, List.of(BlockTags.PICKAXE_MINEABLE, toolTag)),
				registerBlock("raw_" + id + "_block", strength, resistance,
						BlockSoundGroup.STONE, List.of(BlockTags.PICKAXE_MINEABLE, toolTag))
		);
	}
	
	public static NetherCrystalBlockSet registerNetherCrystalBlockSet(ModMaterials material, float strength, float resistance) {
		String id = material.getId();
		TagKey<Block> toolTag = ModBlockTags.getNeedsTier(material.getInferiorTier());
		return new NetherCrystalBlockSet(
				registerBlock("nether_" + id + "_ore", strength, resistance,
						BlockSoundGroup.NETHER_ORE, List.of(BlockTags.PICKAXE_MINEABLE, ModBlockTags.NETHER_ORES, toolTag)),
				registerBlock(id + "_block", strength, resistance + 10.0f,
						BlockSoundGroup.METAL, List.of(BlockTags.PICKAXE_MINEABLE, toolTag))
		);
	}
	
	// --- Core Registration Logic ---
	
	public static Block registerBlock(String name, float strength, float resistance, BlockSoundGroup sound) {
		return registerBlock(name, new Block(AbstractBlock.Settings.create().strength(strength, resistance).sounds(sound)), List.of(), Models.CUBE_ALL);
	}
	
	public static Block registerBlock(String name, float strength, float resistance, BlockSoundGroup sound, List<TagKey<Block>> tags) {
		return registerBlock(name, new Block(AbstractBlock.Settings.create().strength(strength, resistance).sounds(sound).requiresTool()), tags, Models.CUBE_ALL);
	}
	
	public static Block registerBlock(String name, Block block, List<TagKey<Block>> tags, Models model) {
		Identifier id = Rase.getIdentifier(name);
		
		// Register Block and Item
		Block registeredBlock = Registry.register(Registries.BLOCK, id, block);
		Registry.register(Registries.ITEM, id, new BlockItem(registeredBlock, new Item.Settings()));
		
		// Internal Tracking
		ALL.add(registeredBlock);
		MODELS.computeIfAbsent(model, k -> new ArrayList<>()).add(registeredBlock);
		tags.forEach(tag -> TAGS.computeIfAbsent(tag, k -> new ArrayList<>()).add(registeredBlock));
		
		return registeredBlock;
	}
	
	// --- Anvil Implementation ---
	
	public static SmithingAnvilBlockSet registerSmithingAnvilBlockSet(SmithingAnvilMaterials material) {
		String name = material.getName();
		return new SmithingAnvilBlockSet(
				(SmithingAnvilBlock) registerBlock(name + "_anvil", new SmithingAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL), material), List.of(BlockTags.ANVIL), Models.ANVIL),
				(SmithingAnvilBlock) registerBlock("chipped_" + name + "_anvil", new SmithingAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL), material), List.of(BlockTags.ANVIL), Models.ANVIL),
				(SmithingAnvilBlock) registerBlock("damaged_" + name + "_anvil", new SmithingAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL), material), List.of(BlockTags.ANVIL), Models.ANVIL)
		);
	}
	
	public record SmithingAnvilBlockSet(SmithingAnvilBlock NORMAL, SmithingAnvilBlock CHIPPED, SmithingAnvilBlock DAMAGED) {
		public SmithingAnvilBlockSet(SmithingAnvilBlock NORMAL, SmithingAnvilBlock CHIPPED, SmithingAnvilBlock DAMAGED) {
			this.NORMAL = NORMAL; this.CHIPPED = CHIPPED; this.DAMAGED = DAMAGED;
			this.NORMAL.setAnvilBlockSet(this); this.CHIPPED.setAnvilBlockSet(this); this.DAMAGED.setAnvilBlockSet(this);
		}
	}
	
	public record AlloyBlockSet(Block BLOCK) {}
	public record CrystalBlockSet(Block ORE, Block DEEPSLATE_ORE, Block BLOCK) {}
	public record OreBlockSet(Block ORE, Block DEEPSLATE_ORE, Block BLOCK, Block RAW_BLOCK) {}
	public record NetherOreBlockSet(Block NETHER_ORE, Block BLOCK, Block RAW_BLOCK) {}
	public record NetherCrystalBlockSet(Block NETHER_ORE, Block BLOCK) {}
	
	// --- Model Enumeration Logic ---
	
	public enum Models {
		CUBE_ALL(BlockStateModelGenerator::registerSimpleCubeAll),
		ORIENTABLE((generator, block) -> generator.registerNorthDefaultHorizontalRotated(block, TexturedModel.ORIENTABLE)),
		COOKER((generator, block) -> generator.registerCooker(block, TexturedModel.ORIENTABLE)),
		ANVIL((generator, block) -> {
			TextureKey BODY = TextureKey.of("body");
			TextureKey TOP = TextureKey.of("top");
			Identifier blockId = Registries.BLOCK.getId(block);
			String path = blockId.getPath();
			
			TextureMap textures = new TextureMap()
					.put(BODY, Identifier.of(blockId.getNamespace(), "block/" + path.replace("chipped_", "").replace("damaged_", "")))
					.put(TOP, Identifier.of(blockId.getNamespace(), "block/" + path + "_top"));
			
			Model model = new Model(Optional.of(Identifier.of("minecraft", "block/anvil")), Optional.empty(), BODY, TOP);
			Identifier modelId = model.upload(block, textures, generator.modelCollector);
			generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
					.coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
		});
		
		private final BiConsumer<BlockStateModelGenerator, Block> generator;
		Models(BiConsumer<BlockStateModelGenerator, Block> generator) { this.generator = generator; }
		public void generate(BlockStateModelGenerator gen, Block block) { this.generator.accept(gen, block); }
		public void generate(BlockStateModelGenerator gen, List<Block> blocks) { blocks.forEach(block -> generate(gen, block)); }
	}
	
	public static void registerModBlocks() {
		Rase.LOGGER.info("Hardening the world: Registering Blocks for " + Rase.MOD_ID);
	}
}