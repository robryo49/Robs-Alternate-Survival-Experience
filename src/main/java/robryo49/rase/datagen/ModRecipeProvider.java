package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import robryo49.rase.item.ModItems;
import robryo49.rase.util.ModItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	
	public static void generateForgeRecipe(
			RecipeExporter exporter, String name, List<Item> ingredients, Item result, TagKey<Item> mold,
			int smeltingTime, int coolingTime, int forgeTier, float experience
	) {
		ForgeRecipeJsonBuilder builder = ForgeRecipeJsonBuilder.create();
		ingredients.forEach(builder::ingredient);
		builder.result(result)
				.mold(mold)
				.smeltingTime(smeltingTime)
				.coolingTime(coolingTime)
				.minTier(forgeTier)
				.experience(experience)
				.offerTo(exporter, name);
	}
	
	@Override
	public void generate(RecipeExporter exporter) {
		
		generateForgeRecipe(
				exporter, "bronze_ingot",
				List.of(Items.COPPER_INGOT, Items.COPPER_INGOT, Items.COPPER_INGOT, ModItems.TIN.INGOT()),
				ModItems.BRONZE.INGOT(), ModItemTags.INGOT_MOLDS,
				200, 1000, 1, 1
		);
		
		generateForgeRecipe(
				exporter, "bronze_axe_head",
				List.of(ModItems.BRONZE.INGOT(), ModItems.BRONZE.INGOT(), ModItems.BRONZE.INGOT()),
				ModItems.BRONZE_TOOL_SET.AXE_HEAD(), ModItemTags.AXE_HEAD_MOLDS,
				200, 1000, 1, 1
		);
		
		generateForgeRecipe(
				exporter, "netherite_ingot",
				List.of(Items.GOLD_INGOT, Items.COPPER_INGOT, Items.IRON_INGOT, ModItems.BRONZE.INGOT()),
				Items.NETHERITE_INGOT, ModItemTags.INGOT_MOLDS,
				400, 1000, 2, 1
		);
		
	}
	
}
