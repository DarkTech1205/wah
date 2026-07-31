package com.example.floatify.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class CameraFloatMixin {

    private static final float FLOAT_AMPLITUDE = 0.12F;
    private static final float FLOAT_FREQUENCY = 0.004F;

    @Inject(
        method = "render",
        at = @At("HEAD")
    )
    private void floatify$forceFloatingCameraTransform(
        DeltaTracker deltaTracker,
        boolean renderBlockOutline,
        Camera camera,
        GameRenderer gameRenderer,
        LightTexture lightTexture,
        Matrix4f projectionMatrix,
        Matrix4f modelViewMatrix,
        CallbackInfo ci
    ) {
        if (modelViewMatrix == null) {
            return;
        }

        // Calculate a smooth vertical sine-wave offset based on system time
        double currentTime = System.currentTimeMillis();
        float verticalOffset = (float) Math.sin(currentTime * FLOAT_FREQUENCY) * FLOAT_AMPLITUDE;

        // Directly translate the model-view matrix using float precision
        modelViewMatrix.translate(0.0F, verticalOffset, 0.0F);
    }
}
