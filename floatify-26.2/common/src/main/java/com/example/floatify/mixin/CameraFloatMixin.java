package com.yourname.floatify.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraFloatMixin {

    @Inject(method = "setup", at = @At("TAIL"))
    private void makeCameraFloat(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        Camera camera = (Camera) (Object) this;

        // Calculate the drift offset entirely using float primitives and literals
        float tickFloat = (float) entity.tickCount + partialTick;
        float floatOffset = (float) Math.sin(tickFloat * 0.1f) * 0.5f;

        // Cast camera position coordinates to float variables
        float currentX = (float) camera.getPosition().x;
        float currentY = (float) camera.getPosition().y + floatOffset;
        float currentZ = (float) camera.getPosition().z;

        // Apply the float-based coordinates back to the camera
        camera.setPosition(currentX, currentY, currentZ);
    }
}
