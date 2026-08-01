package com.example.floatify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Using a string target bypasses the 'common' module restriction preventing us from importing client-only classes!
@Mixin(targets = "net.minecraft.client.renderer.block.BlockRenderDispatcher")
public class StripelandsForcerMixin {

    @Inject(method = "renderBatched", at = @At("HEAD"))
    private void floatify$stretchBlocks(BlockState state, BlockPos pos, BlockGetter level, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, CallbackInfo ci) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        // Check if the absolute block position is past 2^24 (16,777,216 blocks)
        if (Math.abs(x) >= 16777216 || Math.abs(y) >= 16777216 || Math.abs(z) >= 16777216) {
            
            // 1. Calculate how 24-bit precision snaps the boundaries of this specific 1x1x1 block
            float qMinX = (float) x;
            float qMaxX = (float) (x + 1);
            float qMinY = (float) y;
            float qMaxY = (float) (y + 1);
            float qMinZ = (float) z;
            float qMaxZ = (float) (z + 1);

            // 2. Calculate the stretched scale
            float scaleX = qMaxX - qMinX;
            float scaleY = qMaxY - qMinY;
            float scaleZ = qMaxZ - qMinZ;

            // 3. Reconstruct the absolute origin of the current 16x16 chunk
            int chunkX = x - (x & 15);
            int chunkY = y - (y & 15);
            int chunkZ = z - (z & 15);

            // 4. Calculate the degraded local offset inside the chunk
            float newLocalX = qMinX - chunkX;
            float newLocalY = qMinY - chunkY;
            float newLocalZ = qMinZ - chunkZ;

            // 5. Override the PoseStack matrix to force the block to stretch and snap
            Matrix4f mat = poseStack.last().pose();
            mat.identity(); // Clear standard chunk local positioning
            mat.translate(newLocalX, newLocalY, newLocalZ);
            
            // Apply the stretch scale (clamped to 0.001 to prevent lighting crashes)
            mat.scale(Math.max(0.001f, scaleX), Math.max(0.001f, scaleY), Math.max(0.001f, scaleZ));
        }
    }
}
