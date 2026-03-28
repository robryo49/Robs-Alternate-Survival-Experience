package robryo49.rase.item.custom;


public enum MoldMaterials {
	WET_CLAY("wet_clay", 0, 1, 1),
	CLAY("clay", 1, 3, 1.5f),
	LEAD("lead", 2, 6, 1f),
	TITANIUM("titanium", 3, 10, 0.8f),
	TUNGSTEN("tungsten", 4, 16, 0.6f),
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