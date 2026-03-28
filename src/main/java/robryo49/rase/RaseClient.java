package robryo49.rase;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.util.Identifier;
import robryo49.rase.item.ModItems;
import robryo49.rase.item.custom.MoldItem;
import robryo49.rase.recipe.ModRecipes;

import java.util.List;

public class RaseClient implements ClientModInitializer {
	
	public void registerMoldSetModelPredicates(ModItems.MoldSet moldSet) {
		registerMoldItemModelPredicate(moldSet.INGOT());
		registerMoldItemModelPredicate(moldSet.PICKAXE());
		registerMoldItemModelPredicate(moldSet.AXE());
		registerMoldItemModelPredicate(moldSet.SWORD());
		registerMoldItemModelPredicate(moldSet.SHOVEL());
		registerMoldItemModelPredicate(moldSet.HOE());
	}
	
	public void registerMoldItemModelPredicate(MoldItem moldItem) {
		ModelPredicateProviderRegistry.register(moldItem, Rase.getIdentifier("filled"),
				(stack, world, entity, seed) -> stack.get(MoldItem.STORED_ITEM_COMPONENT) != null ? 1.0F : 0.0F);
	}
	
	@Override
	public void onInitializeClient() {
		Rase.LOGGER.info("Initializing client for rase");

		ModItems.MOLD_SETS.forEach(this::registerMoldSetModelPredicates);
	}
}
