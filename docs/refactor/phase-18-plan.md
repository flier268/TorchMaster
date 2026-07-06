# Phase 18 Completion: Spawn Event Adapter + Runtime Glue Cleanup

## Completed

- Added shared spawn event container helpers in `MinecraftSpawnEventContainers`.
- Moved spawn runtime config/logging glue behind `MinecraftRuntimeServices`.
- Updated Fabric spawn wrappers and village siege mixin to delegate container creation/result extraction.
- Added NeoForge and Forge result mapping helpers so event handlers stay lifecycle-focused.
- Removed the unregistered NeoForge template title-screen mixin.
- Strengthened source policy tests for wrapper container handling, loader result mapping, spawn runtime platform glue, and template mixin cleanup.

## Remaining Coupling

- Spawn adapter classes still live across `events`, `utils`, and `minecraft.adapter`; naming/package boundaries can be tightened.
- Forge spawn event classes remain Stonecutter-processed in `src/main` because old/new Forge event APIs need version branches.
- Mixin redirect methods still decide the fallback method shape; only event result decisions are centralized.
- Runtime config/logging helpers are still purpose-specific and should not be merged across unrelated storage/client paths without a clear boundary.

## Verification Matrix

- `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
- `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
- `./gradlew build`
- `./gradlew "Reset active project"`
- reset後 `./gradlew :1.21.1-fabric:test`
- `git diff --check`

## Anti-Regression Notes

- Do not recreate `EventResultContainer` handling in Fabric wrappers or mixins.
- Do not hand-write Forge/NeoForge event result switch mapping in event handlers.
- Do not make `MinecraftSpawnEventRuntime` directly import platform services or runtime logger facades.
- Do not re-add unregistered template mixins under loader roots.
