# Phase 10 Completion Record

## Completed

- Added shared client primitives for panel fills, config screen layout snapshots, and line-box geometry.
- Updated light/config screens to keep Minecraft render override signatures local while delegating shared panel geometry, colors, and layout calculations.
- Updated the range renderer so snapshot traversal delegates box-to-line geometry and shared range/sample styles to a helper.
- Added `SavedLightStoreStateFactory` so storage access uses a Minecraft storage factory helper instead of reaching into `SavedLightStore` factory fields directly.
- Added `TorchmasterEntityFilters` so command/storage access no longer depends on `TorchmasterRuntime` filter registry globals.
- Expanded source policy tests for runtime filter globals, loader-root duplication, and screen panel fill primitives.

## Remaining Coupling

- `TorchmasterLightRangeRenderer` still owns render-layer, camera translation, legacy GL setup, and MatrixStack/VertexConsumer API branches.
- `TorchmasterConfigScreen` still owns widget entry construction, scroll behavior, and entry value parsing.
- `SavedLightStore` still owns PersistentState override method signatures and NBT method branches.
- `TorchmasterRuntime` still exposes deprecated filter registry fields for compatibility.

## Verification

- Representative targets for this phase remain:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew "Reset active project"`
  - `git diff --check`
- The active project must finish at `1.21.1-fabric`.

## Anti-Regression Rules

- Do not duplicate panel frame/background colors inside screen classes; use `TorchmasterPanelRenderer`.
- Do not duplicate range/sample line-box geometry inside render API branches; use `TorchmasterLineBoxRenderer`.
- Do not add new call sites for `TorchmasterRuntime.MegaTorchFilterRegistry` or `TorchmasterRuntime.DreadLampFilterRegistry`.
- Do not put render, storage, filter runtime, or content business details into loader roots.
