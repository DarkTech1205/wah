package com.example.floatify.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * THIS FILE IS IDENTICAL ON FABRIC AND NEOFORGE.
 *
 * Before 26.x, Fabric mods were written against Yarn mappings and Forge mods
 * against Mojang's official mappings, so the same logic needed two different
 * source files with different class/method names. Since 1.21.11 the client
 * itself ships unobfuscated, and Yarn is retired - every loader now builds on
 * Mojang's own names. That means this one file compiles unmodified in both
 * the /fabric and /neoforge modules below (see their build.gradle files -
 * only the mapping/dependency plumbing differs, not this code).
 *
 * What it does: every entity (players, mobs, items, arrows, TNT, boats...)
 * ultimately routes its position and velocity through Entity#setPosRaw and
 * Entity#setDeltaMovement, both of which construct a `new Vec3(x, y, z)`
 * internally. We redirect that construction through a float cast first, so
 * position/velocity permanently lose double precision from then on - real
 * float-rounding jitter in movement, knockback, and projectile arcs, not a
 * cosmetic effect.
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Redirect(
        method = "setPosRaw(DDD)V",
        at = @At(value = "NEW", target = "(DDD)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 floatify$truncatePosition(double x, double y, double z) {
        return new Vec3((float) x, (float) y, (float) z);
    }

    @Redirect(
        method = "setDeltaMovement(DDD)V",
        at = @At(value = "NEW", target = "(DDD)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 floatify$truncateVelocity(double x, double y, double z) {
        return new Vec3((float) x, (float) y, (float) z);
    }
}
