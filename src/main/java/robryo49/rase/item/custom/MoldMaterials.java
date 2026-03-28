package robryo49.rase.item.custom;


import robryo49.rase.item.ModMaterials;

public enum MoldMaterials {
	WET_CLAY("wet_clay", 0, 1, 1),
	CLAY("clay", 1, 4, 1.5f),
	LEAD(ModMaterials.LEAD, 6, 1f),
	TITANIUM(ModMaterials.TITANIUM, 10, 0.8f),
	TUNGSTEN(ModMaterials.TUNGSTEN, 16, 0.6f),
	OBSIDIAN("obsidian", 5, 4, 0.5f)
	;
	
	private final String name;
	private final int tier;
	private final int durability;
	private final float coolingFactor;
	
	MoldMaterials(
			String name,
			int tier,
			int durability,
			float coolingFactor
	) {
		this.name = name;
		this.tier = tier;
		this.durability = durability;
		this.coolingFactor = coolingFactor;
	}
	
	MoldMaterials(
			ModMaterials material,
			int durability,
			float coolingFactor
	) {
		this.name = material.getId();
		this.tier = material.getSuperiorTier();
		this.durability = durability;
		this.coolingFactor = coolingFactor;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTier() {
		return tier;
	}
	
	public int getDurability() {
		return durability;
	}
	
	public float getCoolingFactor() {
		return coolingFactor;
	}
}