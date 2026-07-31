package com.example.floatify.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class CameraFloatMixin {

    /**
     * Brute-forces the floating effect by intercepting the exact moment the 
     * JOML float matrix applies the camera's yaw (rotationY). 
     * This avoids needing to import volatile Minecraft classes that break the build.
     */
    @Redirect(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4f;rotationY(F)Lorg/joml/Matrix4f;"
        )
    )
    private Matrix4f floatify$applyFloatingCameraTransform(Matrix4f matrix, float angle) {
        // 1. Let vanilla apply the normal camera rotation first
        matrix.rotationY(angle);
        
        // 2. Brute-force our float-precision translation directly into the matrix
        float verticalOffset = (float) Math.sin(System.currentTimeMillis() * 0.004) * 0.12F;
        matrix.translate(0.0F, verticalOffset, 0.0F);
        
        // 3. Return the modified matrix to the rendering pipeline
        return matrix;
    }
}
