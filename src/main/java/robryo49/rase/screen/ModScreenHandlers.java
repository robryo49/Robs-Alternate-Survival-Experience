package robryo49.rase.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import robryo49.rase.Rase;
import robryo49.rase.screen.custom.ForgeScreen;
import robryo49.rase.screen.custom.ForgeScreenHandler;

public class ModScreenHandlers {
	
	public static final ScreenHandlerType<ForgeScreenHandler> FORGE_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Rase.MOD_ID, "forge_screen_handler"),
					new ExtendedScreenHandlerType<>(ForgeScreenHandler::new, BlockPos.PACKET_CODEC));
	
	
	public static void registerScreenHandlers() {
		Rase.LOGGER.info("Registering Screen Handlers for " + Rase.MOD_ID);
		
		ModScreenHandlers.registerScreens();
	}
	
	public static void registerScreens() {
		HandledScreens.register(FORGE_SCREEN_HANDLER, ForgeScreen::new);
	}
}
