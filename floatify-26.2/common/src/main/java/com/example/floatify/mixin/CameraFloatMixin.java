package com.example.floatify.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraFloatMixin {

    @Shadow
    private Vec3 position;

    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    @Inject(method = "setup", at = @At("TAIL"))
    private void makeCameraFloat(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        // Calculate the drift offset entirely using float primitives and literals
        float tickFloat = (float) entity.tickCount + partialTick;
        float floatOffset = (float) Math.sin(tickFloat * 0.1f) * 0.5f;

        // Store coordinates in explicit float variables
        float currentX = (float) position.x;
        float currentY = (float) position.y + floatOffset;
        float currentZ = (float) position.z;

        // Apply the float-based coordinates back via the shadowed method
        this.setPosition(currentX, currentY, currentZ);
    }
}
