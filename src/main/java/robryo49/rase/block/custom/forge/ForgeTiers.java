package robryo49.rase.block.custom.forge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

import java.util.ArrayList;
import java.util.List;

public class ForgeTiers {
	
	// --- Collections ---
	
	
	public static final List<ForgeTier> ALL = new ArrayList<>();
	public static final List<ForgeTier> TRANSLATABLE = ALL;
	
	
	// --- Forge Tier Registration ---
	
	public static final ForgeTier DEFAULT = registerForgeTier("default", 0, 1, 1);
	public static final ForgeTier PRIMITIVE = registerForgeTier("primitive", 1, 1, 1);
	public static final ForgeTier ADVANCED = registerForgeTier("advanced", 2, 1, 1);
	
	
	// --- Core Forge Tier Registration Logic ---
	
	
	private static ForgeTier registerForgeTier(String name, int tier, float speed, float fuel) {
		ForgeTier t = new ForgeTier(
				name, tier, speed, fuel,
				blockTag(name + "_forge_shell"),
				blockTag(name + "_forge_core")
		);
		ALL.add(t);
		return t;
	}
	
	private static TagKey<Block> blockTag(String name) {
		return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Rase.MOD_ID, name));
	}
	
	public record ForgeTier(
			String name,
			int tier,
			float speedMultiplier,
			float fuelEfficiency,
			TagKey<Block> shellTag,
			TagKey<Block> coreTag
	) {
		public int getSmeltingTime(int baseTime) { return Math.round(baseTime / speedMultiplier); }
		public int getFuelTime(int rawFuelTime) { return Math.round(rawFuelTime * fuelEfficiency); }
		public boolean canSmelt(int minTier) { return this.tier >= minTier; }
		
		public boolean isShell(BlockState state) { return state.isIn(shellTag); }
		public boolean isCore(BlockState state) { return state.isIn(coreTag); }
		
		public boolean isValidShellBlock(BlockState state) {
			return isShell(state);
		}
		public boolean isValidCoreBlock(BlockState state) {
			return isCore(state);
		}
		
		public String getTranslationKey() {
			return "forge_tier." + name + "_forge";
		}
		
		public Text getDisplayName() {
			return Text.translatable(getTranslationKey());
		}
	}
	
	public static void registerForgeTiers() {
		Rase.LOGGER.info("Registering Forge Tiers for " + Rase.MOD_ID);
	}
}
