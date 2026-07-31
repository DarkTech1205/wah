package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PoseStack.class)
public class StripelandsForcerMixin {

    /**
     * Forces 24-bit mantissa precision loss (classic Stripelands) on world geometry 
     * when absolute coordinates exceed 2^24 (16,777,216).
     */
    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 0)
    private double floatify$truncateTranslateX(double x) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            double absX = Math.abs(client.player.getX() + x);
            if (absX >= 16777216.0) {
                // Quantize based on 24-bit float precision limits relative to magnitude
                float scale = (float) Math.pow(2.0, Math.floor(Math.log(absX) / Math.log(2.0)) - 23);
                return Math.round(x / scale) * scale;
            }
        }
        return x;
    }

    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 1)
    private double floatify$truncateTranslateY(double y) {
        // Y usually doesn't hit 16M blocks, but keeping symmetry ensures vertical tearing if reached
        return y;
    }

    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 2)
    private double floatify$truncateTranslateZ(double z) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            double absZ = Math.abs(client.player.getZ() + z);
            if (absZ >= 16777216.0) {
                float scale = (float) Math.pow(2.0, Math.floor(Math.log(absZ) / Math.log(2.0)) - 23);
                return Math.round(z / scale) * scale;
            }
        }
        return z;
    }
}
