package com.example.floatify.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public class CameraFloatMixin {

    /**
     * Brute-forces the floating effect by intercepting the exact moment GameRenderer 
     * uploads the final view matrix to the rendering engine. 
     * 
     * This avoids any volatile Minecraft package imports (like LightTracker/DeltaTracker),
     * guarantees the build passes, and applies the float offset jitter-free.
     */
    @ModifyArg(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setModelViewMatrix(Lorg/joml/Matrix4f;)V"
        ),
        index = 0
    )
    private Matrix4f floatify$forceFloatingCameraTransform(Matrix4f matrix) {
        // Calculate the smooth sine wave based on real-time ticks
        float verticalOffset = (float) Math.sin(System.currentTimeMillis() * 0.004) * 0.12F;
        
        // Translate the float-precision matrix directly before it hits the shaders
        matrix.translate(0.0F, verticalOffset, 0.0F);
        
        // Return the injected matrix back to the rendering pipeline
        return matrix;
    }
}
