package robryo49.rase.block.custom.forge;



public enum SmithingAnvilMaterials {
	STONE("stone", 1, 10),
	LEAD("lead", 2, 20),
	TITANIUM("titanium", 3, 30),
	TUNGSTEN("tungsten", 4, 40)
	;
	
	private final String name;
	private final int tier;
	private final int durability;
	
	SmithingAnvilMaterials(String name, int tier, int durability) {
		this.name = name;
		this.tier = tier;
		this.durability = durability;
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
}