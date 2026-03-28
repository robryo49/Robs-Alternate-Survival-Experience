package robryo49.rase;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.item.ModItemGroups;
import robryo49.rase.item.ModItems;

public class Rase implements ModInitializer {
	public static final String MOD_ID = "rase";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}