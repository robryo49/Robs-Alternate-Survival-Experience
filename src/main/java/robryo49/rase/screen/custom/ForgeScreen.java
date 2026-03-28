package robryo49.rase.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robryo49.rase.Rase;

public class ForgeScreen extends HandledScreen<ForgeScreenHandler> {
	
	private static final Identifier GUI_TEXTURE   = Rase.getIdentifier("textures/gui/forge/forge_gui.png");
	private static final Identifier ARROW_TEXTURE = Rase.getIdentifier("textures/gui/forge/burn_progress.png");
	private static final Identifier FLAME_TEXTURE = Rase.getIdentifier("textures/gui/forge/lit_progress.png");
	
	public ForgeScreen(ForgeScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	
	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.setShaderTexture(0, GUI_TEXTURE);
		
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		
		context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
		drawCookProgress(context, x, y);
		drawBurnProgress(context, x, y);
	}
	
	private void drawCookProgress(DrawContext context, int x, int y) {
		if (!handler.isCooking()) return;
		int progress = handler.getScaledCookTime();
		context.drawTexture(ARROW_TEXTURE, x + 95, y + 34, 0, 0, progress, 16, 24, 16);
	}
	
	private void drawBurnProgress(DrawContext context, int x, int y) {
		if (!handler.isBurning()) return;
		int height = handler.getScaledBurnTime();
		context.drawTexture(FLAME_TEXTURE, x + 47, y + 36 + 14 - height, 0, 14 - height, 14, height, 14, 14);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}
}