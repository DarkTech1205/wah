package net.floatcraft.mixin.client;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class FloatingCameraMixin {

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void applyFloatingCamera(float tickDelta, long finishTimeNano, MatrixStack matrices, CallbackInfo ci) {
        // Continuous floating bob calculation using a smooth sine wave
        float floatOffset = (float) Math.sin(System.currentTimeMillis() * 0.003) * 0.08F;

        // Translate the render matrix stack directly. 
        // This operates in float-precision and shifts the view smoothly 
        // without causing jitter at extreme world coordinates.
        matrices.translate(0.0F, floatOffset, 0.0F);
    }
}
