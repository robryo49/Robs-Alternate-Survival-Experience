package robryo49.rase.mixin;

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

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends HandledScreen<AnvilScreenHandler> {
	
	@Shadow
	private TextFieldWidget nameField;
	
	@Shadow
	@Final
	private static Identifier TEXT_FIELD_DISABLED_TEXTURE;
	
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
		
		boolean validAnvil = context.get((world, pos) ->
				world.getBlockState(pos).getBlock() instanceof SmithingAnvilBlock
		).orElse(false);
		
		if (!validAnvil) {
			return false;
		}
		
		ItemStack left = this.handler.getSlot(0).getStack();
		ItemStack right = this.handler.getSlot(1).getStack();
		
		if (left.isEmpty() || right.isEmpty()) {
			return false;
		}
		
		return this.client.world.getRecipeManager().getFirstMatch(
				ModRecipes.ANVIL_SMITHING_RECIPE_TYPE,
				new AnvilSmithingRecipe.Input(left, right),
				this.client.world
		).isPresent();
	}
	
	@Inject(method = "drawBackground", at = @At("TAIL"))
	private void rase$drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		if (rase$isCustomRecipe()) {
			context.drawGuiTexture(TEXT_FIELD_DISABLED_TEXTURE, this.x + 59, this.y + 20, 110, 16);
		}
	}
	
	@Inject(method = "onSlotUpdate", at = @At("TAIL"))
	private void rase$onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci) {
		if (slotId < 0 || slotId > 2) {
			return;
		}
		
		ItemStack left = handler.getSlot(0).getStack();
		boolean isCustom = rase$isCustomRecipe();
		
		
		if (isCustom) {
			nameField.setChangedListener(s -> {});
			nameField.setText("");
			nameField.setEditable(false);
			nameField.setFocused(false);
			nameField.setChangedListener(this::rase$onNameChanged);
		}
	}
	
	@Unique
	private void rase$onNameChanged(String newName) {
		if (!rase$isCustomRecipe()) {
			this.handler.setNewItemName(newName);
		}
	}
}