package robryo49.rase.block;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.data.client.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import robryo49.rase.Rase;
import robryo49.rase.block.custom.*;
import robryo49.rase.block.custom.forge.*;
import robryo49.rase.block.custom.smithing_anvil.SmithingAnvilBlock;
import robryo49.rase.block.custom.smithing_anvil.SmithingAnvilMaterials;
import robryo49.rase.item.ModMaterials;
import robryo49.rase.util.ModBlockTags;

import java.util.*;
import java.util.function.BiConsumer;

public class ModBlocks {
	
	// --- Collections for DataGen and Registry ---
	
	
	public static final List<Block> ALL = new ArrayList<>();
	public static final Map<BlockModels, List<Block>> MODELS = new EnumMap<>(BlockModels.class);
	public static final Map<TagKey<Block>, List<Block>> TAGS = new HashMap<>();
	
	
	// --- Block Registrations ---
	
	public static final Block BASKET = registerBlock("basket", new BasketBlock(AbstractBlock.Settings.create().strength(1.0f, 3.0f).sounds(BlockSoundGroup.BIG_DRIPLEAF)), List.of(), BlockModels.BASKET);
	public static final Block DRYING_RACK = registerBlock("drying_rack", new DryingRackBlock(AbstractBlock.Settings.create().strength(1.0f).sounds(BlockSoundGroup.WOOD).nonOpaque()), List.of(), BlockModels.SCAFFOLDING);
	public static final Block MINING_DRILL = registerDrill("mining_drill");
	public static final Block MOTOR = registerMotor("motor");
	
	public static final Block CRACKED_STONE = registerBlock("cracked_stone", 1.5f, 6.0f, BlockSoundGroup.STONE);
	public static final Block CRACKED_DEEPSLATE = registerBlock("cracked_deepslate", 2.0f, 6.0f, BlockSoundGroup.DEEPSLATE);
	public static final Block CRACKED_GRANITE = registerBlock("cracked_granite", 1.5f, 6.0f, BlockSoundGroup.STONE);
	public static final Block CRACKED_ANDESITE = registerBlock("cracked_andesite", 1.5f, 6.0f, BlockSoundGroup.STONE);
	public static final Block CRACKED_DIORITE = registerBlock("cracked_diorite", 1.5f, 6.0f, BlockSoundGroup.STONE);
	public static final Block CRACKED_TUFF = registerBlock("cracked_tuff", 1.5f, 6.0f, BlockSoundGroup.STONE);
	
	public static final Block OBSIDIAN_BRICKS = registerBlock("obsidian_bricks", new Block(AbstractBlock.Settings.create().requiresTool().strength(50.0F, 1200.0F)), List.of(ModBlockTags.ETHEREAL_FORGE_SHELL), BlockModels.CUBE_ALL);
	public static final Block CRYING_OBSIDIAN_BRICKS = registerBlock("crying_obsidian_bricks", new Block(AbstractBlock.Settings.create().requiresTool().strength(50.0F, 1200.0F).luminance((state) -> 10)), List.of(), BlockModels.CUBE_ALL);
	
	public static final Block CRUSHER = registerBlock("crusher", new CrusherBlock(AbstractBlock.Settings.create().strength(1.0f)), List.of(), BlockModels.CUBE_ALL);
	public static final Block WORKBENCH = registerBlock("workbench", new WorkbenchBlock(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE)), List.of(), BlockModels.WORKBENCH);
	
	public static final Block PRIMITIVE_FORGE = registerForge("primitive_forge", ForgeTiers.PRIMITIVE, 3.5f, 10.0f);
	public static final Block BASIC_FORGE = registerForge("basic_forge", ForgeTiers.BASIC, 7.0f, 50.0f);
	public static final Block REFINED_FORGE = registerForgeWithBottom("refined_forge", ForgeTiers.REFINED, 10.0f, 80.0f);
	public static final Block ADVANCED_FORGE = registerForgeWithBottom("advanced_forge", ForgeTiers.ADVANCED, 15.0f, 100.0f);
	public static final Block ETHEREAL_FORGE = registerForgeWithBottom("ethereal_forge", ForgeTiers.ETHEREAL, 20.0f, 1200.0f);
	
	public static final Block PRIMITIVE_FORGE_CORE = registerBlock("primitive_forge_core", 3.5f, 10.0f, BlockSoundGroup.STONE, ModBlockTags.PRIMITIVE_FORGE_CORE);
	public static final Block BASIC_FORGE_CORE = registerBlock("basic_forge_core", 7.0f, 50.0f, BlockSoundGroup.STONE, ModBlockTags.BASIC_FORGE_CORE);
	public static final Block REFINED_FORGE_CORE = registerBlock("refined_forge_core", 10.0f, 80.0f, BlockSoundGroup.STONE,  ModBlockTags.REFINED_FORGE_CORE);
	public static final Block ADVANCED_FORGE_CORE = registerBlock("advanced_forge_core", 15.0f, 100.0f, BlockSoundGroup.STONE,  ModBlockTags.ADVANCED_FORGE_CORE);
	public static final Block ETHEREAL_FORGE_CORE = registerBlock("ethereal_forge_core", 20.0f, 1200.0f, BlockSoundGroup.STONE,   ModBlockTags.ETHEREAL_FORGE_CORE);
	
	public static final OreBlockSet TIN = registerOreBlockSet(ModMaterials.TIN, 3.0f, 3.0f);
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
	public static final OreBlockSet MITHRIL = registerOreBlockSet(ModMaterials.MITHRIL, 30.0f, 1200.0f);
	public static final NetherCrystalBlockSet RHEXIS = registerNetherCrystalBlockSet(ModMaterials.RHEXIS, 35.0f, 1200.0f);
	
	public static final CrystalBlockSet COAL = new CrystalBlockSet(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.COAL_BLOCK);
	public static final OreBlockSet IRON = new OreBlockSet(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.IRON_BLOCK, Blocks.RAW_IRON_BLOCK);
	public static final OreBlockSet COPPER = new OreBlockSet(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.COPPER_BLOCK, Blocks.RAW_COPPER_BLOCK);
	public static final OreBlockSet GOLD = new OreBlockSet(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.GOLD_BLOCK, Blocks.RAW_GOLD_BLOCK);
	public static final CrystalBlockSet REDSTONE = new CrystalBlockSet(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.REDSTONE_BLOCK);
	public static final CrystalBlockSet EMERALD = new CrystalBlockSet(Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.EMERALD_BLOCK);
	public static final CrystalBlockSet DIAMOND = new CrystalBlockSet(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DIAMOND_BLOCK);
	public static final CrystalBlockSet LAPIS = new CrystalBlockSet(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.LAPIS_BLOCK);
	
	public static final SmithingAnvilBlockSet STONE_ANVIL = registerSmithingAnvilBlockSet(SmithingAnvilMaterials.STONE);
	public static final SmithingAnvilBlockSet LEAD_ANVIL = registerSmithingAnvilBlockSet(SmithingAnvilMaterials.LEAD);
	public static final SmithingAnvilBlockSet TITANIUM_ANVIL = registerSmithingAnvilBlockSet(SmithingAnvilMaterials.TITANIUM);
	public static final SmithingAnvilBlockSet TUNGSTEN_ANVIL = registerSmithingAnvilBlockSet(SmithingAnvilMaterials.TUNGSTEN);
	
	// --- Helper Logic & Registration Wrappers ---
	
	private static Block registerDrill(String id) {
		return registerBlock(id, new MiningDrillBlock(AbstractBlock.Settings.create().strength(3.5f, 6.0f)
				.sounds(BlockSoundGroup.METAL).requiresTool().pistonBehavior(PistonBehavior.NORMAL)), List.of(), BlockModels.ORIENTABLE_3D_WITH_BOTTOM);
	}
	
	private static Block registerMotor(String id) {
		return registerBlock(id, new MotorBlock(AbstractBlock.Settings.create().strength(2.5f, 6.0f).sounds(BlockSoundGroup.METAL).requiresTool()),
				List.of(BlockTags.PICKAXE_MINEABLE), BlockModels.MOTOR
		);
	}
	
	private static Block registerForge(String name, ForgeTiers tier, float strength, float resistance) {
		return registerBlock(name, new ForgeBlock(AbstractBlock.Settings.create()
						.strength(strength, resistance)
						.luminance(state -> state.get(ForgeBlock.LIT) ? 13 : 0), tier),
				List.of(BlockTags.PICKAXE_MINEABLE, ModBlockTags.FORGES), BlockModels.COOKER);
	}
	private static Block registerForgeWithBottom(String name, ForgeTiers tier, float strength, float resistance) {
		return registerBlock(name, new ForgeBlock(AbstractBlock.Settings.create()
						.strength(strength, resistance)
						.luminance(state -> state.get(ForgeBlock.LIT) ? 13 : 0), tier),
				List.of(BlockTags.PICKAXE_MINEABLE, ModBlockTags.FORGES), BlockModels.COOKER_WITH_BOTTOM);
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
		return registerBlock(name, new Block(AbstractBlock.Settings.create().strength(strength, resistance).sounds(sound)), List.of(), BlockModels.CUBE_ALL);
	}
	
	public static Block registerBlock(String name, float strength, float resistance, BlockSoundGroup sound, List<TagKey<Block>> tags) {
		return registerBlock(name, new Block(AbstractBlock.Settings.create().strength(strength, resistance).sounds(sound).requiresTool()), tags, BlockModels.CUBE_ALL);
	}
	
	public static Block registerBlock(String name, float strength, float resistance, BlockSoundGroup sound, TagKey<Block> tag) {
		return registerBlock(name, new Block(AbstractBlock.Settings.create().strength(strength, resistance).sounds(sound).requiresTool()), List.of(tag), BlockModels.CUBE_ALL);
	}
	
	public static Block registerBlock(String name, Block block, List<TagKey<Block>> tags, BlockModels model) {
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
				(SmithingAnvilBlock) registerBlock(name + "_anvil", new SmithingAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL), material), List.of(BlockTags.ANVIL), BlockModels.ANVIL),
				(SmithingAnvilBlock) registerBlock("chipped_" + name + "_anvil", new SmithingAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL), material), List.of(BlockTags.ANVIL), BlockModels.ANVIL),
				(SmithingAnvilBlock) registerBlock("damaged_" + name + "_anvil", new SmithingAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL), material), List.of(BlockTags.ANVIL), BlockModels.ANVIL)
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
	
	public enum BlockModels {
		CUBE_ALL(BlockStateModelGenerator::registerSimpleCubeAll),
		ORIENTABLE((generator, block) -> generator.registerNorthDefaultHorizontalRotated(block, TexturedModel.ORIENTABLE)),
		ORIENTABLE_WITH_BOTTOM((generator, block) -> generator.registerNorthDefaultHorizontalRotated(block, TexturedModel.ORIENTABLE_WITH_BOTTOM)),
		ORIENTABLE_3D((generator, block) -> registerOrientable3D(generator, block, false)),
		ORIENTABLE_3D_WITH_BOTTOM((generator, block) -> registerOrientable3D(generator, block, true)),
		MOTOR((generator, block) -> {
			Identifier id = Registries.BLOCK.getId(block);
			String ns = id.getNamespace();
			String path = id.getPath();
			
			TextureMap inactiveTextures = new TextureMap()
					.put(TextureKey.PARTICLE, Identifier.of(ns, "block/" + path + "_side"))
					.put(TextureKey.UP,       Identifier.of(ns, "block/" + path + "_side"))
					.put(TextureKey.DOWN,     Identifier.of(ns, "block/" + path + "_side"))
					.put(TextureKey.NORTH,    Identifier.of(ns, "block/" + path + "_top"))
					.put(TextureKey.SOUTH,    Identifier.of(ns, "block/" + path + "_bottom"))
					.put(TextureKey.EAST,     Identifier.of(ns, "block/" + path + "_side_rotated"))
					.put(TextureKey.WEST,     Identifier.of(ns, "block/" + path + "_side_rotated"));
			
			TextureMap activeTextures = new TextureMap()
					.put(TextureKey.PARTICLE, Identifier.of(ns, "block/" + path + "_side_active"))
					.put(TextureKey.UP,       Identifier.of(ns, "block/" + path + "_side_active"))
					.put(TextureKey.DOWN,     Identifier.of(ns, "block/" + path + "_side_active"))
					.put(TextureKey.NORTH,    Identifier.of(ns, "block/" + path + "_top_active"))
					.put(TextureKey.SOUTH,    Identifier.of(ns, "block/" + path + "_bottom"))
					.put(TextureKey.EAST,     Identifier.of(ns, "block/" + path + "_side_active_rotated"))
					.put(TextureKey.WEST,     Identifier.of(ns, "block/" + path + "_side_active_rotated"));
			
			Identifier modelId = Models.CUBE.upload(block, inactiveTextures, generator.modelCollector);
			Identifier activeModelId = Models.CUBE.upload(block, "_active", activeTextures, generator.modelCollector);
			
			var variants = BlockStateVariantMap.create(MotorBlock.FACING, MotorBlock.ACTIVE);
			for (Direction dir : Direction.values()) {
				variants.register(dir, false, createRotatedVariant(modelId, dir));
				variants.register(dir, true,  createRotatedVariant(activeModelId, dir));
			}
			
			generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
			generator.registerParentedItemModel(block, modelId);
		}),
		COOKER((generator, block) -> generator.registerCooker(block, TexturedModel.ORIENTABLE)),
		COOKER_WITH_BOTTOM((generator, block) -> generator.registerCooker(block, TexturedModel.ORIENTABLE_WITH_BOTTOM)),
		ANVIL((generator, block) -> {
			TextureKey BODY = TextureKey.of("body");
			TextureKey TOP = TextureKey.of("top");
			Identifier id = Registries.BLOCK.getId(block);
			String ns = id.getNamespace();
			String cleanPath = id.getPath().replace("chipped_", "").replace("damaged_", "");
			
			TextureMap textures = new TextureMap()
					.put(BODY, Identifier.of(ns, "block/" + cleanPath))
					.put(TOP,  Identifier.of(ns, "block/" + id.getPath() + "_top"));
			
			Model model = new Model(Optional.of(Identifier.of("minecraft", "block/anvil")), Optional.empty(), BODY, TOP);
			Identifier modelId = model.upload(block, textures, generator.modelCollector);
			
			generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
					.coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
		}),
		WORKBENCH((generator, block) -> {
			Identifier id = Registries.BLOCK.getId(block);
			String ns = id.getNamespace();
			String path = id.getPath();
			
			TextureMap textures = new TextureMap()
					.put(TextureKey.TOP,      Identifier.of(ns, "block/" + path + "_top"))
					.put(TextureKey.BOTTOM,   Identifier.of(ns, "block/" + path + "_bottom"))
					.put(TextureKey.SIDE,     Identifier.of(ns, "block/" + path + "_side"))
					.put(TextureKey.PARTICLE, Identifier.of(ns, "block/" + path + "_side"));
			
			Identifier modelId = Models.CUBE_BOTTOM_TOP.upload(block, textures, generator.modelCollector);
			generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));
			generator.registerParentedItemModel(block, modelId);
		}),
		BASKET((generator, block) -> {
			Identifier id = Registries.BLOCK.getId(block);
			String ns = id.getNamespace();
			String path = id.getPath();
			
			Identifier closedModel = Identifier.of(ns, "block/" + path);
			Identifier openModel   = Identifier.of(ns, "block/" + path + "_open");
			
			generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
					.coordinate(BlockStateVariantMap.create(BasketBlock.OPEN)
							.register(false, BlockStateVariant.create().put(VariantSettings.MODEL, closedModel))
							.register(true,  BlockStateVariant.create().put(VariantSettings.MODEL, openModel)))
			);
			
			Models.CUBE_BOTTOM_TOP.upload(closedModel, new TextureMap()
					.put(TextureKey.ALL,    Identifier.of(ns, "block/" + path))
					.put(TextureKey.TOP,    Identifier.of(ns, "block/" + path + "_top"))
					.put(TextureKey.BOTTOM, Identifier.of(ns, "block/" + path + "_bottom"))
					.put(TextureKey.SIDE,   Identifier.of(ns, "block/" + path + "_side")), generator.modelCollector);
			
			Models.CUBE_BOTTOM_TOP.upload(openModel, new TextureMap()
					.put(TextureKey.ALL,    Identifier.of(ns, "block/" + path))
					.put(TextureKey.TOP,    Identifier.of(ns, "block/" + path + "_top_open"))
					.put(TextureKey.BOTTOM, Identifier.of(ns, "block/" + path + "_bottom"))
					.put(TextureKey.SIDE,   Identifier.of(ns, "block/" + path + "_side")), generator.modelCollector);
		}),
		SCAFFOLDING((generator, block) -> {
			Identifier id = Registries.BLOCK.getId(block);
			String ns = id.getNamespace();
			String path = id.getPath();
			
			TextureMap textures = new TextureMap()
					.put(TextureKey.PARTICLE, Identifier.of(ns, "block/" + path + "_top"))
					.put(TextureKey.TOP,      Identifier.of(ns, "block/" + path + "_top"))
					.put(TextureKey.BOTTOM,   Identifier.of(ns, "block/" + path + "_bottom"))
					.put(TextureKey.SIDE,     Identifier.of(ns, "block/" + path + "_side"));
			
			Model scaffoldingParent = new Model(
					Optional.of(Identifier.of("minecraft", "block/scaffolding_stable")),
					Optional.empty(),
					TextureKey.PARTICLE, TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE
			);
			
			Identifier modelId = scaffoldingParent.upload(block, textures, generator.modelCollector);
			generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));
			generator.registerParentedItemModel(block, modelId);
		});
		
		private final BiConsumer<BlockStateModelGenerator, Block> generator;
		
		BlockModels(BiConsumer<BlockStateModelGenerator, Block> generator) {
			this.generator = generator;
		}
		
		public void generate(BlockStateModelGenerator gen, Block block) {
			this.generator.accept(gen, block);
		}
		
		public void generate(BlockStateModelGenerator gen, List<Block> blocks) {
			blocks.forEach(block -> generate(gen, block));
		}
		
		private static void registerOrientable3D(BlockStateModelGenerator generator, Block block, boolean withBottom) {
			Identifier id = Registries.BLOCK.getId(block);
			String ns = id.getNamespace();
			String path = id.getPath();
			
			TextureMap textures = new TextureMap()
					.put(TextureKey.PARTICLE, Identifier.of(ns, "block/" + path + "_side"))
					.put(TextureKey.UP,        Identifier.of(ns, "block/" + path + "_top"))
					.put(TextureKey.DOWN,      Identifier.of(ns, "block/" + path + (withBottom ? "_bottom" : "_top")))
					.put(TextureKey.NORTH,     Identifier.of(ns, "block/" + path + "_front"))
					.put(TextureKey.SOUTH,     Identifier.of(ns, "block/" + path + "_side"))
					.put(TextureKey.EAST,      Identifier.of(ns, "block/" + path + "_side"))
					.put(TextureKey.WEST,      Identifier.of(ns, "block/" + path + "_side"));
			
			Identifier modelId = Models.CUBE.upload(block, textures, generator.modelCollector);
			
			var variants = BlockStateVariantMap.create(Properties.FACING);
			for (Direction dir : Direction.values()) {
				variants.register(dir, createRotatedVariant(modelId, dir));
			}
			
			generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
			generator.registerParentedItemModel(block, modelId);
		}
		private static BlockStateVariant createRotatedVariant(Identifier modelId, Direction direction) {
			BlockStateVariant variant = BlockStateVariant.create().put(VariantSettings.MODEL, modelId);
			switch (direction) {
				case SOUTH -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
				case WEST  -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
				case EAST  -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
				case UP    -> variant.put(VariantSettings.X, VariantSettings.Rotation.R270);
				case DOWN  -> variant.put(VariantSettings.X, VariantSettings.Rotation.R90);
				default    -> {} // NORTH requires no rotation adjustments
			}
			return variant;
		}
	}
	
	public static void registerModBlocks() {
		Rase.LOGGER.info("Registering Blocks for " + Rase.MOD_ID);
	}
}