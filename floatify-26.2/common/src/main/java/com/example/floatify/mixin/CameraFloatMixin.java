package net.floatcraft.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class CameraFloatMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void applyFloatCameraBob(CallbackInfo ci) {
        // Your offset calculations remain strictly float-based and smooth
        float floatOffset = (float) Math.sin(System.currentTimeMillis() * 0.005f) * 0.5f;

        // If you need to translate the matrix stack using floats:
        // PoseStack operations natively accept float values.
    }
}
