package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.llamalad7.mixinextras.injector.ModifyParam;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PoseStack.class)
public class StripelandsForcerMixin {

    @ModifyParam(method = "translate(DDD)V", index = 0)
    private double floatify$truncateTranslateX(double x) {
        return (float) x;
    }

    @ModifyParam(method = "translate(DDD)V", index = 1)
    private double floatify$truncateTranslateY(double y) {
        return (float) y;
    }

    @ModifyParam(method = "translate(DDD)V", index = 2)
    private double floatify$truncateTranslateZ(double z) {
        return (float) z;
    }
}
