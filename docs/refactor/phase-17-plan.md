# Phase 17 Completion Record

## Completed

- Spawn event runtime glue moved from `SpawnEventBridge` into Minecraft-facing adapter helpers.
  - `MinecraftSpawnEventContext` carries project-owned spawn context data.
  - `MinecraftSpawnEventRuntime` owns config lookup, `MinecraftConfigView`, event-result application, and debug logging.
- Range render backend decisions moved into `TorchmasterRangeRenderBackend`.
  - `TorchmasterRangeRenderTarget` still owns Minecraft render API calls, but reads line layer, flush target, camera offset, and legacy setup descriptors from the backend helper.
- Storage state bridge now exposes explicit existing-tag and existing-store routes.
  - `SavedLightStore` PersistentState overrides remain version-specific signatures but delegate to `SavedLightStoreStateBridge`.
- Policy tests now block config/logging glue from returning to `SpawnEventBridge`, block backend constants from returning to range target, and block direct NBT bridge/serializer calls from store overrides.

## Remaining Coupling

- `SpawnEventBridge` still holds Minecraft event method signatures, version-specific spawn reason branches, and world/entity extraction.
- NeoForge event handler still repeats event result mapping between NeoForge result enums and TorchMaster `EventResult`.
- Fabric wrappers still create `EventResultContainer` directly and call bridge methods.
- Range render target still owns concrete Minecraft render layer and legacy GL API calls.
- Storage still has legacy PersistentState override method names in `SavedLightStore`.

## Verification

- Required representative matrix:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
  - `./gradlew build`
  - `./gradlew "Reset active project"`
  - reset後 `./gradlew :1.21.1-fabric:test`
  - `git diff --check`
- Confirm `stonecutter.gradle.kts` active project and `settings.gradle.kts` `vcsVersion` are both `1.21.1-fabric`.

## Anti-Regression

- Do not put `Services.PLATFORM.getConfig()`, `TorchmasterRuntime.LOG`, or `new MinecraftConfigView(...)` back into `SpawnEventBridge`.
- Do not copy spawn decisions or result mapping into loader roots.
- Do not put camera offset, line width, or legacy render setup decisions back into `TorchmasterRangeRenderTarget`.
- Do not call `SavedLightStoreNbtBridge` or `SavedLightStoreSerializer` directly from `SavedLightStore` overrides.
