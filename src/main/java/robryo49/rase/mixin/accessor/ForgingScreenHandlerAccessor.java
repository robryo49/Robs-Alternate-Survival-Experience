package robryo49.rase.mixin.accessor;

import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ForgingScreenHandler.class)
public interface ForgingScreenHandlerAccessor {
	@Accessor("context")
	ScreenHandlerContext rase$getContext();
}