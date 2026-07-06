# Phase 12 Completion Record

## Completed

- Extracted config widget row handling into `TorchmasterConfigWidgetRows`.
  - Row creation, positioning, visibility/active state, boolean toggle text, and draft collector reads now live outside `TorchmasterConfigScreen`.
  - `TorchmasterConfigScreen` remains responsible for screen lifecycle, save/reset, layout lookup, and render override signatures.
- Extracted range render session handling into `TorchmasterRangeRenderSession`.
  - Player/sneaking guard, range plan creation, camera translation, line layer selection, flush behavior, and legacy GL session setup are delegated from `TorchmasterLightRangeRenderer`.
  - `TorchmasterRangeLineSubmitter` remains the narrow Minecraft line submission adapter.
- Tightened storage facade boundaries.
  - `MinecraftLightStoreAccess` now delegates state creation/loading branches to `SavedLightStoreStateFactory.get(...)`.
  - `SavedLightStoreSerializer` no longer depends on `TorchmasterRuntime.LOG`; storage logging is local to `minecraft.storage`.
- Added policy tests to prevent config screen widget-state regressions, renderer session-detail regressions, and storage serializer runtime logger coupling.

## Remaining Coupling

- `TorchmasterConfigScreen` still owns version-specific screen render signatures and direct label drawing calls.
- `TorchmasterLightRangeRenderer` still exposes version-specific public render overloads by design.
- `TorchmasterRangeRenderSession` still contains Minecraft render API branches; this is adapter code, not business logic.
- `SavedLightStore` still owns `PersistentState` override signatures and NBT method branches.
- Deprecated `TorchmasterRuntime.MegaTorchFilterRegistry` and `DreadLampFilterRegistry` remain as compatibility facades.

## Verification

- Run representative matrix:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
- Reset active project:
  - `./gradlew "Reset active project"`
- Run `git diff --check`.
- Confirm `stonecutter.gradle.kts` active project is `1.21.1-fabric`.

## Anti-Regression Rules

- Do not put widget position/visibility state management back into `TorchmasterConfigScreen`.
- Do not make `TorchmasterLightRangeRenderer` call `TorchmasterLightRangeDisplay.snapshots(...)`, `RenderLayer.getLines()`, `WorldRenderer.drawBox(...)`, or line-width submission directly.
- Do not make storage serializers depend on `TorchmasterRuntime.LOG`.
- Do not duplicate screen/render/storage/filter logic in loader roots.
- End every later phase by recording completed work and writing the next phase plan.
