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
        if (client != null && client.player != null) {
            double camX = client.player.getX();
            double absX = camX + x;
            if (Math.abs(absX) >= 16777216.0) {
                // Force 24-bit float precision quantization with grid-snapping breakdown
                float degraded = (float) absX;
                return ((double) degraded) - camX;
            }
        }
        return x;
    }

    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 1)
    private double floatify$truncateTranslateY(double y) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.player != null) {
            double camY = client.player.getY();
            double absY = camY + y;
            if (Math.abs(absY) >= 16777216.0) {
                float degraded = (float) absY;
                return ((double) degraded) - camY;
            }
        }
        return y;
    }

    @ModifyVariable(method = "translate(DDD)V", at = @At("HEAD"), ordinal = 2)
    private double floatify$truncateTranslateZ(double z) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.player != null) {
            double camZ = client.player.getZ();
            double absZ = camZ + z;
            if (Math.abs(absZ) >= 16777216.0) {
                float degraded = (float) absZ;
                return ((double) degraded) - camZ;
            }
        }
        return z;
    }
}
