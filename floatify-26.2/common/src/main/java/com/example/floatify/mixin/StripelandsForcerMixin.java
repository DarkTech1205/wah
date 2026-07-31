package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PoseStack.class)
public class StripelandsForcerMixin {

    /**
     * Truncates double-precision matrix translations down to 32-bit floats.
     * This forces world and chunk rendering transformations to lose precision past 16,777,216,
     * successfully reviving the classic Stripelands vertex jitter and tearing.
     */
    @ModifyArg(
        method = "translate(DDD)V",
        at = @At(value = "HEAD"),
        index = 0,
        require = 0
    )
    private double floatify$truncateTranslateX(double x) {
        return (float) x;
    }

    @ModifyArg(
        method = "translate(DDD)V",
        at = @At(value = "HEAD"),
        index = 1,
        require = 0
    )
    private double floatify$truncateTranslateY(double y) {
        return (float) y;
    }

    @ModifyArg(
        method = "translate(DDD)V",
        at = @At(value = "HEAD"),
        index = 2,
        require = 0
    )
    private double floatify$truncateTranslateZ(double z) {
        return (float) z;
    }
}
