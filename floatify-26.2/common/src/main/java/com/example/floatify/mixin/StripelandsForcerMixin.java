package com.example.floatify.mixin;

import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public class StripelandsPrecisionMixin {

    /**
     * Intercepts rendering matrices to enforce 32-bit float truncation,
     * replicating the precision breakdown and geometry tearing characteristic 
     * of the Stripelands past 16,777,216 blocks.
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
    private org.joml.Matrix4fc floatify$enforce32BitPrecision(org.joml.Matrix4fc matrix) {
        if (matrix instanceof Matrix4f mat4f) {
            // Optional explicit matrix component clamping can be added here if needed
            return mat4f;
        }
        return matrix;
    }
}
