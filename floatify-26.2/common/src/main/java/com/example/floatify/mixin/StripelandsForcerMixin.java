package com.example.floatify.mixin;

import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.class)
public class StripelandsForcerMixin {

    private static final double FORCED_STRIPELANDS_THRESHOLD = 16777216.0D;

    /**
     * @author Floatify
     * @reason Brute-force override to remove vanilla boundary safety nets 
     * and force coordinate limits to lock strictly at 16,777,216.
     */
    @Overwrite
    public int getSafeCoordinateScale() {
        return 1;
    }

    @Inject(method = "getMaxBorderSize", at = @At("HEAD"), cancellable = true)
    private void forceCustomMaxBorderSize(CallbackInfoReturnable<Integer> cir) {
        // Restrict maximum allowable coordinate generation and bounds to exactly 16777216
        cir.setReturnValue(16777216);
    }

    @Inject(method = "clampToBounds(DOUBLE, DOUBLE)", at = @At("HEAD"), cancellable = true)
    private void overrideClampingBounds(double x, double z, CallbackInfoReturnable<Double> cir) {
        double clampedX = Math.max(-FORCED_STRIPELANDS_THRESHOLD, Math.min(FORCED_STRIPELANDS_THRESHOLD, x));
        double clampedZ = Math.max(-FORCED_STRIPELANDS_THRESHOLD, Math.min(FORCED_STRIPELANDS_THRESHOLD, z));
        // Force coordinate bounds check to trigger precisely at the 2^24 precision threshold
        cir.setReturnValue(Math.max(clampedX, clampedZ));
    }
}
