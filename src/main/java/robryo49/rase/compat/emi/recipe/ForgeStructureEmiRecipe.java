package robryo49.rase.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.block.Block;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import robryo49.rase.Rase;
import robryo49.rase.block.custom.forge.ForgeBlock;
import robryo49.rase.block.custom.forge.ForgeTiers;
import robryo49.rase.compat.emi.RaseEmiPlugin;

import java.util.List;

public class ForgeStructureEmiRecipe implements EmiRecipe {
	
	private final Identifier id;
	private final Block forgeBlock;
	private final EmiIngredient shellIngredient;
	private final EmiIngredient coreIngredient;
	
	public ForgeStructureEmiRecipe(Block forgeBlock) {
		this.forgeBlock = forgeBlock;
		ForgeTiers tier = ((ForgeBlock) forgeBlock).getTier();
		this.id = Rase.getIdentifier("/" + forgeBlock.getTranslationKey().split("\\.")[2]);
		this.shellIngredient = EmiIngredient.of(tier.getShellTag());
		this.coreIngredient = EmiIngredient.of(tier.getCoreTag());
	}
	
	@Override public EmiRecipeCategory getCategory() { return RaseEmiPlugin.FORGE_STRUCTURE_CATEGORY; }
	@Override public @Nullable Identifier getId() { return id; }
	@Override public List<EmiIngredient> getInputs() { return List.of(EmiIngredient.of(Ingredient.ofItems(forgeBlock)), shellIngredient, coreIngredient); }
	@Override public List<EmiStack> getOutputs() { return List.of(EmiStack.of(forgeBlock)); }
	@Override public int getDisplayWidth() { return 90; }
	@Override public int getDisplayHeight() { return 46; }
	
	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addSlot(EmiStack.of(forgeBlock), 70, 14).recipeContext(this);
		
		widgets.addTexture(EmiTexture.PLUS, 50, 16);
		
		widgets.addSlot(shellIngredient, 2, 4);
		widgets.addText(Text.literal("Shell"), 22, 10, 0x444444, false);
		
		widgets.addSlot(coreIngredient, 2, 24);
		widgets.addText(Text.literal("Core"), 22, 30, 0x444444, false);
	}
}