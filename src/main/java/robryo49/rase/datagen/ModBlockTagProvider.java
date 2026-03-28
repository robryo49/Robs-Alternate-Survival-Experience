package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.util.ModBlockTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	public void addBlocksToTag(TagKey<Block> tagKey, List<Block> blocks) {
		FabricTagBuilder tagBuilder = getOrCreateTagBuilder(tagKey);
		for (Block block: blocks) tagBuilder.add(block);
	}
	
	public void addBlocksToTag(TagKey<Block> tagKey, Block block) {
		getOrCreateTagBuilder(tagKey).add(block);
	}
	
	private void addTagsToTag(TagKey<Block> parentTag, List<TagKey<Block>> childrenTags) {
		FabricTagBuilder builder = getOrCreateTagBuilder(parentTag);
		childrenTags.forEach(builder::addOptionalTag);
	}
	
	public void addTagsToTag(TagKey<Block> tagKey, TagKey<Block> tag) {
		getOrCreateTagBuilder(tagKey).addOptionalTag(tag);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		ModBlocks.TAGS.forEach(this::addBlocksToTag);
		ModBlockTags.TAGS.forEach(this::addBlocksToTag);
		ModBlockTags.PARENT_TAGS.forEach(this::addTagsToTag);
	}
}
