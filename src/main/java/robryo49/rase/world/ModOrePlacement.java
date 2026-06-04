package robryo49.rase.world;

import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModOrePlacement {
	public static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
		return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
	}
	
	public static List<PlacementModifier> modifiers(int count, PlacementModifier heightModifier) {
		return modifiers(CountPlacementModifier.of(count), heightModifier);
	}
	
	public static List<PlacementModifier> modifiers(int count, int minHeight, int maxHeight) {
		return modifiers(count, HeightRangePlacementModifier.uniform(YOffset.fixed(minHeight), YOffset.fixed(maxHeight)));
	}
}
