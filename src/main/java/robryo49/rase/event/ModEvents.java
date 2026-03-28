package robryo49.rase.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import robryo49.rase.Rase;
import robryo49.rase.block.ModBlocks;
import robryo49.rase.item.ModItems;
import robryo49.rase.util.ModItemTags;

public class ModEvents {
	
	public static void registerFlintBreakingEvent() {
		UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
			if (world.isClient || hand != Hand.MAIN_HAND) {
				return ActionResult.PASS;
			}
			
			ItemStack stack = player.getStackInHand(hand);
			var blockState = world.getBlockState(hitResult.getBlockPos());
			
			if (stack.isOf(Items.FLINT) && blockState.getHardness(world, hitResult.getBlockPos()) >= 1.5f && hitResult.getSide() == Direction.UP) {
				
				if (world.random.nextFloat() < 0.2f) {
					stack.decrement(1);
					Block.dropStack(world, hitResult.getBlockPos().up(), new ItemStack(ModItems.FLINT_SHARD, 2));
				}
				
				world.playSound(null, hitResult.getBlockPos(),
						SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);
				player.swingHand(hand);
				return ActionResult.SUCCESS;
			}
			
			return ActionResult.PASS;
		}));
	}
	
	public static void registerNoBlockPunchingEvent() {
		AttackBlockCallback.EVENT.register(
				((player, world, hand, pos, direction) -> {
					BlockState block = world.getBlockState(pos);
					ItemStack stack = player.getStackInHand(hand);
					
					if (player.isCreative() || block.getHardness(world, pos) <= 0.5) return ActionResult.PASS;
					if (block.isIn(BlockTags.AXE_MINEABLE) || block.isIn(BlockTags.PICKAXE_MINEABLE)) {
						return stack.isSuitableFor(block) ? ActionResult.PASS : ActionResult.SUCCESS;
					}
					
					return ActionResult.PASS;
				})
		);
		
		
		AttackBlockCallback.EVENT.register(
				((player, world, hand, pos, direction) -> {
					BlockState block = world.getBlockState(pos);
					ItemStack stack = player.getStackInHand(hand);
					
					if (player.getStackInHand(hand).isIn(ModItemTags.PRIMITIVE_TOOLS) && !player.isCreative()) {
						if (getReplacingBlock(block.getBlock()) == block.getBlock()) return ActionResult.SUCCESS;
					}
					
					return ActionResult.PASS;
				})
		);
	}
	
	public static void registerLogCuttingEvent() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);
			ItemStack stack = player.getStackInHand(hand);
			
			if (stack.isIn(ItemTags.AXES) && hitResult.getSide() == Direction.UP &&
					(state.isIn(BlockTags.LOGS) && !world.getBlockState(pos.down()).isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS))) {
				
				world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f);
				player.swingHand(hand);
				
				if (!world.isClient) {
					ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
					ServerWorld serverWorld = (ServerWorld) world;
					stack.damage(1, serverWorld, serverPlayer, (item) ->
							serverPlayer.sendEquipmentBreakStatus(item, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND)
					);

					if (state.isIn(BlockTags.LOGS)) {
						Item plankItem = getPlankForLog(state.getBlock());
						if (world.breakBlock(pos, false)) {
							Block.dropStack(world, pos, new ItemStack(plankItem, 2));
						}
					} else if (state.isIn(BlockTags.PLANKS)) {
						if (world.breakBlock(pos, false)) {
							Block.dropStack(world, pos, new ItemStack(Items.STICK, 2));
						}
					}
				}
				return ActionResult.SUCCESS;
			}
			
			return ActionResult.PASS;
		});
	}
	
	public static void registerPlayerSlowDown() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof PlayerEntity player) {
				var instance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
				if (instance != null) {
					Identifier modifierId = Rase.getIdentifier("player_slowdown");
					instance.removeModifier(modifierId);
					instance.addTemporaryModifier(new EntityAttributeModifier(
							modifierId, -0.03, EntityAttributeModifier.Operation.ADD_VALUE
					));
				}
			}
		});
	}
	
	
	private static Item getPlankForLog(Block log) {
		if (log.getDefaultState().isIn(BlockTags.OAK_LOGS)) return Items.OAK_PLANKS;
		if (log.getDefaultState().isIn(BlockTags.SPRUCE_LOGS)) return Items.SPRUCE_PLANKS;
		if (log.getDefaultState().isIn(BlockTags.BIRCH_LOGS)) return Items.BIRCH_PLANKS;
		if (log.getDefaultState().isIn(BlockTags.JUNGLE_LOGS)) return Items.JUNGLE_PLANKS;
		if (log.getDefaultState().isIn(BlockTags.ACACIA_LOGS)) return Items.ACACIA_PLANKS;
		if (log.getDefaultState().isIn(BlockTags.DARK_OAK_LOGS)) return Items.DARK_OAK_PLANKS;
		if (log.getDefaultState().isIn(BlockTags.MANGROVE_LOGS)) return Items.MANGROVE_PLANKS;
		if (log.getDefaultState().isIn(BlockTags.CHERRY_LOGS)) return Items.CHERRY_PLANKS;
		
		return Items.OAK_PLANKS;
	}
	
	public static Block getReplacingBlock(Block block) {
		String name = block.getTranslationKey();
		
		if (block == Blocks.DEEPSLATE || block == ModBlocks.CRACKED_DEEPSLATE ||
				(block.getTranslationKey().contains("deepslate_") && block.getTranslationKey().contains("_ore"))) return ModBlocks.CRACKED_DEEPSLATE;
		
		if (block == Blocks.STONE || block == ModBlocks.CRACKED_STONE || block.getTranslationKey().contains("_ore")) return ModBlocks.CRACKED_STONE;
		if (block == Blocks.GRANITE || block == ModBlocks.CRACKED_GRANITE) return ModBlocks.CRACKED_GRANITE;
		if (block == Blocks.ANDESITE || block == ModBlocks.CRACKED_ANDESITE) return ModBlocks.CRACKED_ANDESITE;
		if (block == Blocks.DIORITE || block == ModBlocks.CRACKED_DIORITE) return ModBlocks.CRACKED_DIORITE;
		if (block == Blocks.TUFF || block == ModBlocks.CRACKED_TUFF) return ModBlocks.CRACKED_TUFF;
		
		return null;
	}
	
	
	public static void registerModEvents() {
		registerFlintBreakingEvent();
		registerNoBlockPunchingEvent();
		registerLogCuttingEvent();
		registerPlayerSlowDown();
	}
}
