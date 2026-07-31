package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(VertexConsumer.class)
public interface StripelandsForcerMixin {

    /**
     * Intercepts vertex position submission in the rendering pipeline,
     * forcing 32-bit float truncation on X, Y, and Z coordinates. 
     * This induces the classic Stripelands vertex snapping and geometry tearing at extreme coordinates.
     */
    @ModifyArg(
        method = {
            "m_253250_", // Obfuscated/mapped name variations for vertex position methods across versions
            "vertex(DDD)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            "addVertex"
        },
        at = @At("HEAD"),
        index = 0,
        require = 0
    )
    private double floatify$truncateVertexX(double x) {
        return (float) x;
    }
}
