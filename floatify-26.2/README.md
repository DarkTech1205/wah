# Floatify (26.2, Fabric + NeoForge)

## What changed from the 1.20.1 version

Same effect as before - `Entity#setPosRaw` and `Entity#setDeltaMovement` both
build a `new Vec3(x, y, z)` internally, and the shared Mixin in
`common/src/main/java/.../mixin/EntityMixin.java` redirects that construction
through a float cast first. Position and velocity permanently lose double
precision from then on for every entity: players, mobs, items, arrows, TNT,
boats, minecarts.

The real change is structural: since 1.21.11, Minecraft ships unobfuscated
and Yarn is retired, so Fabric and NeoForge both build against Mojang's own
class/method names now. That means **one Mixin source file works unmodified
on both loaders** - `common/` holds it once, and `fabric/` and `neoforge/`
each just add their own thin loader glue (mod metadata + entrypoint class)
around it.

```
floatify-26.2/
  common/           <- EntityMixin.java + floatify.mixins.json (shared)
  fabric/           <- fabric.mod.json, FloatifyFabric.java, build.gradle
  neoforge/         <- neoforge.mods.toml, FloatifyNeoForge.java, build.gradle
  settings.gradle
  gradle.properties
```

## Before you build - things I could not verify from here

I don't have network access in this sandbox, so none of this has been
compiled or run against the real 26.2 dependencies. Specifically:

- **Loom's mapping story for 26.2 is genuinely new** (this whole
  unobfuscated-client transition happened after my training cutoff and is
  only weeks old). I've commented in `fabric/build.gradle` where a
  `mappings loom.officialMojangMappings()` call might still be required
  depending on which exact Loom point-release you use - check the current
  Fabric porting docs at https://docs.fabricmc.net/develop/porting/ against
  whatever Loom version resolves for you.
- **NeoForge mixin registration via `[[mixins]]` in `neoforge.mods.toml`**
  is the modern convention, but double-check it against NeoForge's current
  migration primer for 26.2 (linked from the same Fabric porting page) in
  case the block name or location shifted.
- Both `fabric-loader:0.19.3`, `fabric-api:0.155.3+26.3`,
  `neoforge:26.2.0.32-beta`, and `neogradle.userdev:7.1.38` were the current
  published versions as of this session (late July 2026) - by the time you
  build, newer patch versions will likely exist; bump them.
- Java 25 toolchain is required for 26.1+ on both loaders - confirm your
  JDK matches before running Gradle.

## Building

1. Open `floatify-26.2/` as a Gradle project (IntelliJ IDEA handles this
   multi-module layout natively).
2. `./gradlew :fabric:build` for the Fabric jar, `./gradlew :neoforge:build`
   for the NeoForge jar. Each ends up in that module's `build/libs/`.
3. Drop the matching jar into your Prism Launcher instance's `mods/` folder
   for whichever loader that instance uses - Fabric jars only work on a
   Fabric instance, NeoForge jars only on a NeoForge instance, same as
   always. (This part hasn't changed with unobfuscation - loaders still
   aren't interchangeable.)

## Why not still just edit minecraft.jar directly, now that it's readable?

Readable class names make it *easier to understand* what you're editing,
but splicing modified `.class` files into the jar has the same downsides
it always did: no stacking with other mods, no re-application when Mojang
ships the next patch, and you're merging every conflict by hand. Mixin
reaches the identical bytecode with none of that - unobfuscation just means
the Mixin target strings above (`setPosRaw(DDD)V`, etc.) are now guaranteed
to be Mojang's actual, permanent names instead of community-maintained
mappings that could drift.

## Going further

Same offer as before: fall damage math, particle spawn positions, camera
interpolation - tell me which system and I'll add a mixin for it to
`common/`, and it'll cover both loaders at once.
