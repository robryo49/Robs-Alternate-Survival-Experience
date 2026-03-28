package robryo49.rase.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.block.entity.custom.ForgeBlockEntity;


public class ModBlockEntities {
	
	public static final BlockEntityType<ForgeBlockEntity> FORGE_BLOCK_ENTITY =
			Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Rase.MOD_ID, "forge_block_entity"),
					BlockEntityType.Builder.create(ForgeBlockEntity::new,
							ModBlocks.PRIMITIVE_FORGE,
							ModBlocks.ADVANCED_FORGE
					).build());
	
	public static void registerModBlockEntities() {
		Rase.LOGGER.info("Registering Block Entities for " + Rase.MOD_ID);
	}
}
