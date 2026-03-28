package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.TranslatableTextContent;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.item.ModItemGroups;
import robryo49.rase.item.ModItems;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModLanguageProvider extends FabricLanguageProvider {
	public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}
	
	public static String convertTranslationKey(String input) {
		return Arrays.stream(input.substring(input.lastIndexOf('.') + 1).split("_"))
				.map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
				.collect(Collectors.joining(" "));
	}
	
	public String translate(Item item) {return convertTranslationKey(item.getTranslationKey());}
	public String translate(Block block) {return convertTranslationKey(block.getTranslationKey());}
	public String translate(ItemGroup group) {return convertTranslationKey(((TranslatableTextContent) group.getDisplayName().getContent()).getKey());}
	
	public void generateItemTranslations(TranslationBuilder translationBuilder, List<Item> items) {
		for (Item item : items) translationBuilder.add(item, translate(item));
	}
	
	public void generateBlockTranslations(TranslationBuilder translationBuilder, List<Block> blocks) {
		for (Block block : blocks) translationBuilder.add(block, translate(block));
	}
	
	public void generateItemGroupTranslations(TranslationBuilder translationBuilder, List<ItemGroup> itemGroups) {
		itemGroups.forEach(group ->
				Registries.ITEM_GROUP.getKey(group).ifPresent(key ->
						translationBuilder.add(key, translate(group))
				)
		);
	}
	
	
	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
		generateItemTranslations(translationBuilder, ModItems.TRANSLATABLE);
		generateBlockTranslations(translationBuilder, ModBlocks.TRANSLATABLE);
		generateItemGroupTranslations(translationBuilder, ModItemGroups.TRANSLATABLE);
	}
}
