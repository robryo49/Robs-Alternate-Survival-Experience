package robryo49.rase.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.block.entity.custom.BasketBlockEntity;
import robryo49.rase.block.entity.custom.DryingRackBlockEntity;
import robryo49.rase.block.entity.custom.ForgeBlockEntity;


public class ModBlockEntities {
	
	public static final BlockEntityType<ForgeBlockEntity> FORGE_BLOCK_ENTITY_TYPE =
			Registry.register(Registries.BLOCK_ENTITY_TYPE, Rase.getIdentifier("forge_block_entity"),
					BlockEntityType.Builder.create(ForgeBlockEntity::new,
							ModBlocks.PRIMITIVE_FORGE,
							ModBlocks.BASIC_FORGE,
							ModBlocks.REFINED_FORGE,
							ModBlocks.ADVANCED_FORGE,
							ModBlocks.ETHEREAL_FORGE
					).build());
	
	public static final BlockEntityType<BasketBlockEntity> BASKET_BLOCK_ENTITY_TYPE =
			Registry.register(Registries.BLOCK_ENTITY_TYPE, Rase.getIdentifier("basket_block_entity"),
					BlockEntityType.Builder.create(BasketBlockEntity::new,
							ModBlocks.BASKET
					).build());
	
	public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK_BLOCK_ENTITY =
			Registry.register(Registries.BLOCK_ENTITY_TYPE, Rase.getIdentifier("drying_rack_block_entity"),
					BlockEntityType.Builder.create(DryingRackBlockEntity::new,
							ModBlocks.DRYING_RACK
					).build());
	
	public static void registerModBlockEntities() {
		Rase.LOGGER.info("Registering Block Entities for " + Rase.MOD_ID);
	}
}
