# Phase 20 Completion Record

## Completed

- Added `MinecraftSpawnEventContextFactory` so `SpawnEventBridge` no longer hand-builds spawn contexts from Minecraft ids, positions, reasons, and event containers.
- Kept `SpawnEventBridge` public signatures stable while thinning method bodies to Minecraft world/entity extraction, context factory calls, and runtime deny application.
- Added `TorchmasterLegacyRangeRenderTarget` for `<1.15` GL/Tessellator target setup and teardown.
- Kept `TorchmasterRangeRenderTarget` focused on modern/latest line buffer, flush, camera translation, and legacy helper delegation.
- Added `TorchmasterConfigRuntimeAccess` as the config screen specific runtime facade and removed direct `TorchmasterRuntime` usage from `TorchmasterConfigScreenController`.
- Added tests and source policy checks for spawn context construction, legacy render target boundaries, and config runtime access.

## Remaining Coupling

- `SpawnEventBridge` still exposes public Minecraft event signatures and still owns world/entity extraction.
- `FabricSpawnEventHooks` still calls the public spawn bridge facade instead of a narrower spawn runtime adapter.
- `TorchmasterRangeRenderTarget` still owns modern/latest render layer and flush API branches.
- `TorchmasterConfigScreenController` still owns save/reset/status orchestration, though runtime lookup is now behind a client-local facade.

## Verification Matrix

- `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
- `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
- `./gradlew build`
- `./gradlew "Reset active project"`
- Reset後 `./gradlew :1.21.1-fabric:test`
- `git diff --check`

## Anti-Regression Notes

- Do not rebuild spawn event contexts directly in `SpawnEventBridge`; use `MinecraftSpawnEventContextFactory`.
- Do not restore Fabric spawn wrapper facades or put spawn rules in mixins.
- Do not put legacy GL/Tessellator setup back into `TorchmasterRangeRenderTarget`.
- Do not make config screen controller import or call `TorchmasterRuntime` directly.
