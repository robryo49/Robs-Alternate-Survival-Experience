package robryo49.rase.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robryo49.rase.block.ModBlocks;

@Mixin(PistonBlock.class)
public class MiningDrillPistonMixin {

    @Inject(method = "isMovable", at = @At("HEAD"), cancellable = true)
    private static void allowDrillPush(BlockState state, World world, BlockPos pos,
                                       Direction direction, boolean canBreak, Direction pistonDir,
                                       CallbackInfoReturnable<Boolean> cir) {
        if (state.isOf(ModBlocks.MINING_DRILL)) {
            cir.setReturnValue(true);
        }
    }
}