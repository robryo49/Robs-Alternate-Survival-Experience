package robryo49.rase;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.event.ModEvents;
import robryo49.rase.item.ModItemGroups;
import robryo49.rase.item.ModItems;
import robryo49.rase.loot_table.ModLootTableModifiers;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.screen.ModScreenHandlers;
import robryo49.rase.util.ModBlockTags;
import robryo49.rase.util.ModItemTags;
import robryo49.rase.world.gen.ModOreGeneration;
import robryo49.rase.world.gen.ModWorldGeneration;

public class Rase implements ModInitializer {
	public static final String MOD_ID = "rase";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	@Override
	public void onInitialize() {
		ModBlockTags.registerModBlockTags();
		ModItemTags.registerModItemTags();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemGroups.registerModItemGroups();
		
		ModEvents.registerModEvents();
		ModBlockEntities.registerModBlockEntities();
		ModScreenHandlers.registerModScreenHandlers();
		ModRecipes.registerModRecipes();
		ModLootTableModifiers.modifyLootTables();
		
		ModWorldGeneration.generateModWorldGen();
	}
	
	public static Identifier getIdentifier(String id) {
		return Identifier.of(MOD_ID, id);
	}
}