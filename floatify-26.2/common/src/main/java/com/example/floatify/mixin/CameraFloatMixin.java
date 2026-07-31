package com.example.floatify.mixin;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Camera.class)
public class CameraFloatMixin {

    /**
     * Truncates the camera's X position to 32-bit float.
     * This destroys double-precision relative rendering, reviving the 
     * classic vertex jitter/Stripelands effect at high coordinates.
     */
    @ModifyVariable(method = "setPosition(DDD)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double floatify$truncateX(double x) {
        return (float) x;
    }

    /**
     * Truncates the camera's Y position to 32-bit float.
     */
    @ModifyVariable(method = "setPosition(DDD)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private double floatify$truncateY(double y) {
        return (float) y;
    }

    /**
     * Truncates the camera's Z position to 32-bit float.
     */
    @ModifyVariable(method = "setPosition(DDD)V", at = @At("HEAD"), ordinal = 2, argsOnly = true)
    private double floatify$truncateZ(double z) {
        return (float) z;
    }
}
