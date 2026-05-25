package robryo49.rase.mixin;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeRemoverMixin {
	
	@Unique
	public List<String> getRemovedIds() {
		List<String> ids = new ArrayList<>();
		ids.add("stick");
		ids.add("stick_from_bamboo_item");
		ids.add("brick");
		ids.add("bread");
		
		for (String type : new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo"}) {
			ids.add(type + "_planks");
		}
		
		ids.addAll(List.of(
				"gold_ingot_from_blasting_deepslate_gold_ore",
				"gold_ingot_from_blasting_gold_ore",
				"gold_ingot_from_blasting_nether_gold_ore",
				"gold_ingot_from_blasting_raw_gold",
				"gold_ingot_from_smelting_deepslate_gold_ore",
				"gold_ingot_from_smelting_gold_ore",
				"gold_ingot_from_smelting_nether_gold_ore",
				"gold_ingot_from_smelting_raw_gold",
				"gold_nugget_from_blasting",
				"gold_nugget_from_smelting",
				
				"iron_ingot_from_blasting_deepslate_iron_ore",
				"iron_ingot_from_blasting_iron_ore",
				"iron_ingot_from_blasting_raw_iron",
				"iron_ingot_from_smelting_deepslate_iron_ore",
				"iron_ingot_from_smelting_iron_ore",
				"iron_ingot_from_smelting_raw_iron",
				"iron_nugget_from_blasting",
				"iron_nugget_from_smelting",
				
				"redstone_from_blasting_deepslate_redstone_ore",
				"redstone_from_blasting_redstone_ore",
				"redstone_from_smelting_deepslate_redstone_ore",
				"redstone_from_smelting_redstone_ore",
				
				"lapis_lazuli_from_blasting_deepslate_lapis_ore",
				"lapis_lazuli_from_blasting_lapis_ore",
				"lapis_lazuli_from_smelting_deepslate_lapis_ore",
				"lapis_lazuli_from_smelting_lapis_ore",
				
				"emerald_from_blasting_deepslate_emerald_ore",
				"emerald_from_blasting_emerald_ore",
				"emerald_from_smelting_deepslate_emerald_ore",
				"emerald_from_smelting_emerald_ore",
				
				"diamond_from_blasting_deepslate_diamond_ore",
				"diamond_from_blasting_diamond_ore",
				"diamond_from_smelting_deepslate_diamond_ore",
				"diamond_from_smelting_diamond_ore",
				
				"copper_ingot_from_blasting_copper_ore",
				"copper_ingot_from_blasting_deepslate_copper_ore",
				"copper_ingot_from_blasting_raw_copper",
				"copper_ingot_from_smelting_copper_ore",
				"copper_ingot_from_smelting_deepslate_copper_ore",
				"copper_ingot_from_smelting_raw_copper",
				
				"coal_from_blasting_coal_ore",
				"coal_from_blasting_deepslate_coal_ore",
				"coal_from_smelting_coal_ore",
				"coal_from_smelting_deepslate_coal_ore",
				
				"quartz",
				"quartz_from_blasting",
				
				"netherite_ingot",
				"netherite_scrap_from_blasting"
		));
		
		return ids;
	}
	
	@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"))
	private void removeRecipes(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
		map.keySet().removeIf(id -> {
			for (String removedId: getRemovedIds()) {
				if (id.getPath().equals(removedId) && id.getNamespace().equals("minecraft")) return true;
			}
			return false;
		});
	}
}