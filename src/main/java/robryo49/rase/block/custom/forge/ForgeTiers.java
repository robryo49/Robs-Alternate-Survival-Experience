package robryo49.rase.block.custom.forge;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import robryo49.rase.Rase;
import robryo49.rase.util.ModBlockTags;

public enum ForgeTiers {
	PRIMITIVE("primitive", 1, 1, 1, ModBlockTags.PRIMITIVE_FORGE_SHELL, ModBlockTags.PRIMITIVE_FORGE_CORE),
	ADVANCED("advanced", 1, 1, 1, ModBlockTags.ADVANCED_FORGE_SHELL, ModBlockTags.ADVANCED_FORGE_CORE);
	
	private final String name;
	private final int tier;
	private final float speedMultiplier;
	private final float fuelEfficiency;
	private final TagKey<Block> shellTag;
	private final TagKey<Block> coreTag;
	
	ForgeTiers(
			String name,
			int tier,
			float speedMultiplier,
			float fuelEfficiency,
			TagKey<Block> shellTag,
			TagKey<Block> coreTag
	) {
		this.name = name;
		this.tier = tier;
		
		this.speedMultiplier = speedMultiplier;
		this.fuelEfficiency = fuelEfficiency;
		
		this.shellTag = shellTag;
		this.coreTag = coreTag;
		
	}
	
	public int getTier() {
		return tier;
	}
	
	public String getName() {
		return name;
	}
	
	public float getFuelEfficiency() {
		return fuelEfficiency;
	}
	
	public float getSpeedMultiplier() {
		return speedMultiplier;
	}
	
	public TagKey<Block> getCoreTag() {
		return coreTag;
	}
	
	public TagKey<Block> getShellTag() {
		return shellTag;
	}
	
	public int getSmeltingTime(int baseTime) {
		return Math.round(baseTime / speedMultiplier);
	}
	
	public int getFuelTime(int rawFuelTime) {
		return Math.round(rawFuelTime * fuelEfficiency);
	}
	
	public boolean canSmelt(int minTier) {
		return this.tier >= minTier;
	}
	
	public boolean isShell(BlockState state) {
		return state.isIn(shellTag);
	}
	
	public boolean isCore(BlockState state) {
		return state.isIn(coreTag);
	}
	
	public boolean isValidShellBlock(BlockState state) {
		return isShell(state);
	}
	
	public boolean isValidCoreBlock(BlockState state) {
		return isCore(state);
	}
	
	public String getTranslationKey() {
		return "rase.forge_tier." + name + "_forge";
	}
	
	public Text getDisplayName() {
		return Text.translatable(getTranslationKey());
	}
}