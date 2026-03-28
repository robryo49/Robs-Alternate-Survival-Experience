package robryo49.rase.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.FurnaceSmeltLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModEntityLootTableProvider extends SimpleFabricLootTableProvider {
	
	private final RegistryWrapper.WrapperLookup registryLookup;
	
	public ModEntityLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(output, registryLookup, LootContextTypes.ENTITY);
		this.registryLookup = registryLookup.join();
	}
	
	@Override
	public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
		exporter.accept(EntityType.COW.getLootTableId(), LootTable.builder()
				.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.with(ItemEntry.builder(Items.BEEF)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
								.apply(FurnaceSmeltLootFunction.builder()
										.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS,
												EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true)))))
								.apply(EnchantedCountIncreaseLootFunction.builder(registryLookup, UniformLootNumberProvider.create(0, 1)))
						)
				)
		);
		
		exporter.accept(EntityType.MOOSHROOM.getLootTableId(), LootTable.builder()
				.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.with(ItemEntry.builder(Items.BEEF)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)))
								.apply(FurnaceSmeltLootFunction.builder()
										.conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS,
												EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true)))))
								.apply(EnchantedCountIncreaseLootFunction.builder(registryLookup, UniformLootNumberProvider.create(0, 1)))
						)
				)
		);
		
		exporter.accept(EntityType.HORSE.getLootTableId(), LootTable.builder());
		exporter.accept(EntityType.DONKEY.getLootTableId(), LootTable.builder());
		exporter.accept(EntityType.MULE.getLootTableId(), LootTable.builder());
		exporter.accept(EntityType.TRADER_LLAMA.getLootTableId(), LootTable.builder());
		
		exporter.accept(EntityType.LLAMA.getLootTableId(), LootTable.builder()
				.pool(LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1))
						.with(ItemEntry.builder(Items.WHITE_CARPET)
								.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f)))
						)
				)
		);
	}
}