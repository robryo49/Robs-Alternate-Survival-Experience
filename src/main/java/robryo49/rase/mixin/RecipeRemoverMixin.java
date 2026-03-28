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
		ids.add("minecraft:stick");
		ids.add("minecraft:stick_from_bamboo_item");
		
		for (String type : new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo"}) {
			ids.add(type + "_planks");
		}
		
		return ids;
	}
	
	@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"))
	private void removeRecipes(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
		map.keySet().removeIf(id -> {
			for (String removedId: getRemovedIds()) {
				if (id.toString().equals(removedId)) return true;
			}
			return false;
		});
	}
}