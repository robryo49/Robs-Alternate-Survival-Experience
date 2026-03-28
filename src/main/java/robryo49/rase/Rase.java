package robryo49.rase;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.block.custom.forge.ForgeTiers;
import robryo49.rase.block.entity.ModBlockEntities;
import robryo49.rase.item.ModItemGroups;
import robryo49.rase.item.ModItems;
import robryo49.rase.recipe.ModRecipes;
import robryo49.rase.screen.ModScreenHandlers;
import robryo49.rase.util.ModBlockTags;
import robryo49.rase.util.ModItemTags;

public class Rase implements ModInitializer {
	public static final String MOD_ID = "rase";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	@Override
	public void onInitialize() {
		ModBlockTags.registerBlockTags();
		ModItemTags.registerItemTags();
		ModItemGroups.registerModItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ForgeTiers.registerForgeTiers();
		ModBlockEntities.registerModBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModRecipes.registerRecipes();
	}
}