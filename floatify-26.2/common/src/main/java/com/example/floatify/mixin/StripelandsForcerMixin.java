package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PoseStack.class)
public class StripelandsForcerMixin {

    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 0)
    private double floatify$truncateTranslateX(double x) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.gameRenderer != null) {
            double camX = client.gameRenderer.getMainCamera().getPosition().x();
            double absX = camX + x;
            if (Math.abs(absX) >= 16777216.0) {
                return ((double) (float) absX) - camX;
            }
        }
        return x;
    }

    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 1)
    private double floatify$truncateTranslateY(double y) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.gameRenderer != null) {
            double camY = client.gameRenderer.getMainCamera().getPosition().y();
            double absY = camY + y;
            if (Math.abs(absY) >= 16777216.0) {
                return ((double) (float) absY) - camY;
            }
        }
        return y;
    }

    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 2)
    private double floatify$truncateTranslateZ(double z) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.gameRenderer != null) {
            double camZ = client.gameRenderer.getMainCamera().getPosition().z();
            double absZ = camZ + z;
            if (Math.abs(absZ) >= 16777216.0) {
                return ((double) (float) absZ) - camZ;
            }
        }
        return z;
    }
}
