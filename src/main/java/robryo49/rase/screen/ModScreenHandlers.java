package robryo49.rase.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import robryo49.rase.Rase;
import robryo49.rase.screen.custom.*;

public class ModScreenHandlers {
	
	public static final ScreenHandlerType<ForgeScreenHandler> FORGE_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, Rase.getIdentifier("forge_screen_handler"),
					new ExtendedScreenHandlerType<>(ForgeScreenHandler::new, BlockPos.PACKET_CODEC));
	// ModScreenHandlers.java
	public static final ScreenHandlerType<BasketScreenHandler> BASKET_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, Rase.getIdentifier("basket_screen_handler"),
					new ScreenHandlerType<>(BasketScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
	public static final ScreenHandlerType<WorkbenchScreenHandler> WORKBENCH_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, Rase.getIdentifier("workbench_screen_handler"),
					new ScreenHandlerType<>(WorkbenchScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
	
	public static void registerScreenHandlers() {
		Rase.LOGGER.info("Registering Screen Handlers for " + Rase.MOD_ID);
		
		ModScreenHandlers.registerScreens();
	}
	
	public static void registerScreens() {
		HandledScreens.register(FORGE_SCREEN_HANDLER, ForgeScreen::new);
		HandledScreens.register(BASKET_SCREEN_HANDLER, BasketScreen::new);
		HandledScreens.register(WORKBENCH_SCREEN_HANDLER, WorkbenchScreen::new);
	}
}
