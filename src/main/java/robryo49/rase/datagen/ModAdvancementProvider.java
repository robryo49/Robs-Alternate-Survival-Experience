package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.item.ModItems;
import robryo49.rase.util.ModItemTags;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
	
	AdvancementEntry ROOT;
	AdvancementEntry FLINT_AGE;
	AdvancementEntry STONE_AGE;
	
	
	private static final java.util.Map<TagKey<?>, ItemConvertible> TAG_ICONS = java.util.Map.of(
			ItemTags.LOGS, Items.OAK_LOG,
			ItemTags.PLANKS, Items.OAK_PLANKS,
			ItemTags.DIRT, Items.DIRT,
			ModItemTags.INGOT_MOLDS, ModItems.WET_CLAY_MOLD_SET.INGOT()
	);
	
	private static class AdvancementBuilder {
		private final ItemConvertible item;
		private final AdvancementCriterion<?> criterion;
		private String name;
		private String description;
		private final String id;
		private final String itemId;
		private AdvancementFrame type = AdvancementFrame.TASK;
		
		AdvancementBuilder(ItemConvertible item) {
			this.item = item;
			
			itemId = Registries.ITEM.getId(item.asItem()).getPath();
			id = "get_" + itemId;
			name = getName(itemId);
			description = getName(id);
			
			criterion = InventoryChangedCriterion.Conditions.items(item);
		}
		
		AdvancementBuilder(TagKey<Item> itemTag) {
			ItemConvertible item = getIconForTag(itemTag);
			this.item = item;
			
			itemId = Registries.ITEM.getId(item.asItem()).getPath();
			id = "get_" + itemId;
			name = getName(itemId);
			description = getName(id);
			
			criterion = InventoryChangedCriterion.Conditions.items(
					ItemPredicate.Builder.create().tag(itemTag).build());
		}
		
		public AdvancementBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public AdvancementBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public AdvancementBuilder type(AdvancementFrame type) {
			this.type = type;
			return this;
		}
		
		public AdvancementEntry build(Consumer<AdvancementEntry> consumer, AdvancementEntry parent) {
			return Advancement.Builder.create().parent(parent)
					.display(item, Text.literal(name), Text.literal(description), null, type, true, true, false)
					.criterion(id, criterion)
					.build(consumer, Rase.MOD_ID + ":" + id);
		}
	}
	
	private static AdvancementBuilder advancement(Item item) {
		return new AdvancementBuilder(item);
	}
	private static AdvancementBuilder advancement(TagKey<Item> tag) {
		return new AdvancementBuilder(tag);
	}
	
	public static String getName(String id) {
		if (id == null || id.isEmpty()) {
			return id;
		}
		
		StringBuilder titleCase = new StringBuilder();
		String[] words = id.split("_");
		
		for (String word : words) {
			if (!word.isEmpty()) {
				titleCase.append(Character.toUpperCase(word.charAt(0)))
						.append(word.substring(1).toLowerCase())
						.append(" ");
			}
		}
		
		return titleCase.toString().trim();
	}
	public static ItemConvertible getIconForTag(TagKey<?> tag) {
		return TAG_ICONS.getOrDefault(tag, Items.BOOK);
	}
	
	
	public void removeVanillaEnchants(Consumer<AdvancementEntry> consumer) {
		Map<String, List<String>> vanillaAdvancements = Map.of(
				"adventure", List.of(
						"adventuring_time", "arbalistic", "avoid_vibration", "blowback", "brush_armadillo", "bullseye",
						"craft_decorated_pot_using_only_sherds", "crafters_crafting_crafters", "fall_from_world_height",
						"hero_of_the_village", "honey_block_slide", "kill_a_mob", "kill_all_mobs", "kill_mob_near_sculk_catalyst",
						"lighten_up", "lightning_rod_with_villager_no_fire", "minecraft_trials_edition", "ol_betsy", "overoverkill",
						"play_jukebox_in_meadows", "read_power_of_chiseled_bookshelf", "revaulting", "root", "salvage_sherd",
						"shoot_arrow", "sleep_in_bed", "sniper_duel", "spyglass_at_dragon", "spyglass_at_ghast", "spyglass_at_parrot",
						"summon_iron_golem", "throw_trident", "totem_of_undying", "trade", "trade_at_world_height",
						"trim_with_all_exclusive_armor_patterns", "trim_with_any_armor_pattern", "two_birds_one_arrow",
						"under_lock_and_key", "very_very_frightening", "voluntary_exile", "walk_on_powder_snow_with_leather_boots",
						"who_needs_rockets", "whos_the_pillager_now"
				),
				"end", List.of(
						"dragon_breath", "dragon_egg", "elytra", "enter_end_gateway", "find_end_city", "kill_dragon", "levitate", "respawn_dragon", "root"
				),
				"husbandry", List.of(
						"allay_deliver_cake_to_note_block", "allay_deliver_item_to_player", "axolotl_in_a_bucket", "balanced_diet",
						"bred_all_animals", "breed_an_animal", "complete_catalogue", "feed_snifflet", "fishy_business", "froglights",
						"kill_axolotl_target", "leash_all_frog_variants", "make_a_sign_glow", "obtain_netherite_hoe", "obtain_sniffer_egg",
						"plant_any_sniffer_seed", "plant_seed", "remove_wolf_armor", "repair_wolf_armor", "ride_a_boat_with_a_goat",
						"root", "safely_harvest_honey", "silk_touch_nest", "tactical_fishing", "tadpole_in_a_bucket", "tame_an_animal",
						"wax_off", "wax_on", "whole_pack"
				),
				"nether", List.of(
						"all_effects", "all_potions", "brew_potion", "charge_respawn_anchor", "create_beacon", "create_full_beacon",
						"distract_piglin", "explore_nether", "fast_travel", "find_bastion", "find_fortress", "get_wither_skull",
						"loot_bastion", "netherite_armor", "obtain_ancient_debris", "obtain_blaze_rod", "obtain_crying_obsidian",
						"return_to_sender", "ride_strider", "ride_strider_in_overworld_lava", "root", "summon_wither",
						"uneasy_alliance", "use_lodestone"
				),
				"story", List.of(
						"cure_zombie_villager", "deflect_arrow", "enchant_item", "enter_the_end", "enter_the_nether", "follow_ender_eye",
						"form_obsidian", "iron_tools", "lava_bucket", "mine_diamond", "mine_stone", "obtain_armor", "root", "shiny_gear",
						"smelt_iron", "upgrade_tools"
				)
		);
		
		vanillaAdvancements.forEach((folder, files) -> {
			for (String name : files) {
				Advancement.Builder.create()
						.criterion("impossible", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
						.build(consumer, "minecraft:" + folder + "/" + name);
			}
		});
	}
	
	public void generateRootAdvancement(Consumer<AdvancementEntry> consumer, String id, ItemConvertible item, String description, AdvancementFrame type) {
		ROOT = Advancement.Builder.create()
				.display(
						item,
						Text.literal(getName(id)),
						Text.literal(description),
						Identifier.ofVanilla("textures/gui/advancements/backgrounds/adventure.png"),
						type,
						true,
						true,
						false
				)
				.criterion(id, TickCriterion.Conditions.createTick())
				.build(consumer, Rase.MOD_ID + ":" + id);
	}
	
	public AdvancementEntry generateAdvancementChain(Consumer<AdvancementEntry> consumer, List<?> elements, AdvancementEntry parent) {
		for (Object element: elements) {
			if (element instanceof List<?> list) {
				generateAdvancementChain(consumer, list, parent);
			} else if (element instanceof AdvancementBuilder advancementBuilder) {
				parent = advancementBuilder.build(consumer, parent);
			}
		}
		return parent;
	}
	
	
	public ModAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(output, registryLookup);
	}
	@Override
	public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
		
		removeVanillaEnchants(consumer);
		
		generateRootAdvancement(consumer, "rase", Blocks.DIRT, "Good Luck !", AdvancementFrame.CHALLENGE);
		
		FLINT_AGE = generateAdvancementChain(consumer, List.of(
				
				advancement(Items.FLINT).name("Mine"),
				advancement(ModItems.FLINT_SHARD).name("It's Tiny !"),
				advancement(ModItems.FLINT_KNIFE).name("It's Sharp !"),
				advancement(ModItems.PLANT_FIBER).name("U Need Fiber"),
				advancement(ModItems.TWINE).name("Twisted Twine"),
				advancement(ModItems.FLINT_HATCHET).name("Hatchy"),
				List.of(
						advancement(ItemTags.LOGS).name("Logging In"),
						advancement(ItemTags.PLANKS).name("Planked Up"),
						advancement(Items.CRAFTING_TABLE).name("Craft")
				),
				advancement(ModItems.SHARP_FLINT).name("It's Sharper !"),
				advancement(ModItems.FLINT_PICK).name("Picky"),
				
				advancement(ModItems.STONE_PEBBLES.PEBBLE()).name("Rockin' It"),
				advancement(ModItems.STONE_TOOL_SET.PICKAXE_HEAD()).name("Is it a Head ?"),
				advancement(ModItems.STONE_TOOL_SET.PICKAXE()).name("It's a Pickaxe !"),
				
				List.of(
						advancement(Items.COAL).name("I'm Coaled"),
						advancement(ModItems.COAL_COKE).name("Not This Coke")
				),
				List.of(
						advancement(Items.RAW_COPPER).name("Beans ?"),
						advancement(Items.COPPER_INGOT).name("Third Place")
				),
				List.of(
						advancement(ModItems.TIN.RAW()).name("Tin-y Progress"),
						advancement(ModItems.TIN.INGOT()).name("Tined Up")
				),
				List.of(
						advancement(Items.CLAY_BALL).name("Ballin' Around"),
						advancement(ModItems.WET_CLAY_MOLD_SET.BASE()).name("That Ain't Molding Anything"),
						advancement(ModItemTags.INGOT_MOLDS).name("Molding a Brick ?")
				)
		), ROOT);
	}
	
}
