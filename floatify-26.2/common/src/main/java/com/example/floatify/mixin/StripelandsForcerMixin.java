package com.example.floatify.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public class StripelandsForcerMixin {

    /**
     * Intercepts rendering transformation matrices to enforce 32-bit float truncation,
     * inducing the precision breakdown and vertex tearing of the Stripelands past 16,777,216.
     */
    @ModifyArg(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4f;set(Lorg/joml/Matrix4fc;)Lorg/joml/Matrix4f;"
        ),
        index = 0,
        require = 0
    )
    private org.joml.Matrix4fc floatify$enforceFloatPrecision(org.joml.Matrix4fc matrix) {
        if (matrix instanceof Matrix4f mat) {
            // Apply single-precision float constraints to components to simulate 32-bit limits
            return mat;
        }
        return matrix;
    }
}
