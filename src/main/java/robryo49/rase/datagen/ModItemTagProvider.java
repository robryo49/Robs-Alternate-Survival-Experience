package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import robryo49.rase.item.ModItems;
import robryo49.rase.util.ModItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
	
	public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		ModItems.TAGS.forEach(this::addItemsToTag);
		ModItemTags.TAGS.forEach(this::addTagsToTag);
	}
	
	
	private void addItemsToTag(TagKey<Item> tagKey, List<Item> items) {
		FabricTagBuilder builder = getOrCreateTagBuilder(tagKey);
		items.forEach(builder::add);
	}
	
	private void addTagsToTag(TagKey<Item> tagKey, List<TagKey<Item>> tags) {
		FabricTagBuilder builder = getOrCreateTagBuilder(tagKey);
		tags.forEach(builder::addOptionalTag);
	}
}