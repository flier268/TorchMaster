# Phase 19 Completion Record

## Completed

- Moved Minecraft-facing spawn helpers into `net.xalcon.torchmaster.minecraft.spawn`.
- Renamed runtime service glue to `MinecraftSpawnRuntimeServices` so the service boundary is spawn-specific, not a generic locator.
- Kept `SpawnEventBridge` public signatures stable while removing direct `container.getResult()` usage from bridge bodies.
- Added `FabricSpawnEventHooks` as the shared Fabric spawn hook implementation and removed the old `MobWrapper` / `NaturalSpawnerWrapper` facades.
- Kept Forge and NeoForge event result conversion loader-local and rule-free.
- Split range render backend choices into a descriptor/value helper consumed by `TorchmasterRangeRenderTarget`.
- Updated tests and source policy coverage for spawn package boundaries, thin wrappers, bridge container-result access, and render backend decisions.

## Remaining Coupling

- `SpawnEventBridge` still owns several Minecraft version branches for entity/world/position extraction.
- Fabric mixins now reference `FabricSpawnEventHooks` directly; no legacy wrapper facade remains.
- Range render target still contains Minecraft render API calls and legacy GL setup; only backend decision data has been separated.
- `TorchmasterConfigScreenController` still reaches `TorchmasterRuntime.getConfig()` through its local runtime facade.

## Next Phase

Phase 20 should prioritize higher-maintenance areas instead of broad package churn:

- Thin `SpawnEventBridge` further by extracting context construction helpers.
- Deepen legacy render branch split around target/session setup.
- Converge config runtime access behind a dedicated client config runtime facade.

## Verification Matrix

- `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
- `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
- `./gradlew build`
- `./gradlew "Reset active project"`
- Reset後 `./gradlew :1.21.1-fabric:test`
- `git diff --check`

## Anti-Regression Notes

- Do not put spawn blocking rules, config lookup, or logging glue in loader roots, mixins, or `utils` helpers.
- Do not reintroduce direct `container.getResult()` calls in `SpawnEventBridge`.
- Do not add loader-specific copies of spawn, render, storage, content, or config rules.
- Keep domain and port packages free of Minecraft, loader, and Stonecutter APIs.
