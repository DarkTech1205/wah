package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PoseStack.class)
public class StripelandsForcerMixin {

    /**
     * Truncates all three translation axes (X, Y, Z) of the PoseStack down to 
     * 32-bit floats simultaneously, forcing the Stripelands vertex tearing at high coordinates.
     */
    @ModifyArgs(
        method = "translate(DDD)V",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack$Pose;translate(DDD)Lcom/mojang/blaze3d/vertex/PoseStack$Pose;")
    )
    private static void floatify$truncatePoseTranslation(Args args) {
        double x = args.get(0);
        double y = args.get(1);
        double z = args.get(2);

        args.set(0, (double) (float) x);
        args.set(1, (double) (float) y);
        args.set(2, (double) (float) z);
    }
}
