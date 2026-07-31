package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PoseStack.class)
public class StripelandsForcerMixin {

    /**
     * Truncates the X translation coordinate down to a 32-bit float.
     */
    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 0)
    private double floatify$truncateTranslateX(double x) {
        return (float) x;
    }

    /**
     * Truncates the Y translation coordinate down to a 32-bit float.
     */
    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 1)
    private double floatify$truncateTranslateY(double y) {
        return (float) y;
    }

    /**
     * Truncates the Z translation coordinate down to a 32-bit float.
     */
    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 2)
    private double floatify$truncateTranslateZ(double z) {
        return (float) z;
    }
}
