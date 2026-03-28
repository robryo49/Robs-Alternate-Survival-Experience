package robryo49.rase.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robryo49.rase.block.custom.forge.SmithingAnvilBlock;
import robryo49.rase.mixin.accessor.ForgingScreenHandlerAccessor;
import robryo49.rase.recipe.custom.AnvilSmithingRecipe;
import robryo49.rase.recipe.ModRecipes;

import java.util.Optional;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends HandledScreen<AnvilScreenHandler> {
	
	public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	
	@Unique
	private boolean rase$isCustomRecipe() {
		if (this.client == null || this.client.world == null) {
			return false;
		}
		
		ScreenHandlerContext context =
				((ForgingScreenHandlerAccessor) this.handler).rase$getContext();
		
		Optional<Integer> anvilTier = context.get((world, pos) -> {
			Block block = world.getBlockState(pos).getBlock();
			if (block instanceof SmithingAnvilBlock smithingAnvil) {
				return smithingAnvil.getMaterial().getTier();
			}
			return -1;
		});
		
		if (anvilTier.isEmpty() || anvilTier.get() < 0) {
			return false;
		}
		
		int tier = anvilTier.get();
		
		ItemStack left = this.handler.getSlot(0).getStack();
		ItemStack right = this.handler.getSlot(1).getStack();
		
		if (left.isEmpty() || right.isEmpty()) {
			return false;
		}
		
		return this.client.world.getRecipeManager().getFirstMatch(
				ModRecipes.ANVIL_SMITHING_RECIPE_TYPE,
				new AnvilSmithingRecipe.Input(left, right),
				this.client.world
		).map(entry -> tier >= entry.value().tier()).orElse(false);
	}
}