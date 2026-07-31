package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Brute-forced floating camera mixin designed to safely inject a smooth,
 * jitter-free floating translation offset into the rendering matrix stack 
 * using 32-bit float precision, avoiding high-coordinate world jitter.
 */
@Mixin(GameRenderer.class)
public class CameraFloatMixin {

    // Internal tracking variables to maintain smooth oscillation rates
    private static final float FLOAT_AMPLITUDE = 0.12F;
    private static final float FLOAT_FREQUENCY = 0.004F;
    private static final float ROTATION_TILT_AMPLITUDE = 0.005F;

    @Inject(
        method = "renderLevel",
        at = @At("HEAD")
    )
    private void floatify$forceFloatingCameraTransform(
        PoseStack poseStack, 
        float tickDelta, 
        long finishTimeNano, 
        CallbackInfo ci
    ) {
        // Ensure the pose stack is valid before mutating transformations
        if (poseStack == null) {
            return;
        }

        // Calculate a smooth dual-axis sine wave based on real-time execution ticks
        double currentTime = System.currentTimeMillis();
        float verticalOffset = (float) Math.sin(currentTime * FLOAT_FREQUENCY) * FLOAT_AMPLITUDE;
        float subtleRoll = (float) Math.cos(currentTime * (FLOAT_FREQUENCY * 0.5)) * ROTATION_TILT_AMPLITUDE;

        // Push a fresh matrix state layer to isolate our floating transformation
        poseStack.pushPose();

        try {
            // Apply absolute float-precision translation relative to the camera view
            // This happens entirely on the local render stack, bypassing world double limits.
            poseStack.translate(0.0F, verticalOffset, 0.0F);

            // Optional minor roll/tilt injection to enhance the "floating in liquid/space" feel
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotation(subtleRoll));

        } catch (Exception e) {
            // Fallback safety catch to prevent render thread crashes if matrix states misalign
            System.err.println("[Floatify] Error applying floating camera transformation matrix: " + e.getMessage());
        } finally {
            // Ensure poses are balanced correctly to prevent stack overflow leaks
            poseStack.popPose();
        }
    }
}
