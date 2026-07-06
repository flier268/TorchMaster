# Phase 11 Completion Record

## Completed

- Added `TorchmasterConfigEntries` so config entry declaration order, typed collection, list parsing, and filter validation are shared outside widget inner classes.
- Updated `TorchmasterConfigScreen` so inner entries only own Minecraft widget state and hand typed values to the config collector.
- Added `TorchmasterRangeRenderPlan` and `TorchmasterRangeLineSubmitter` so range snapshot traversal and line submission are separated from renderer buffer/layer setup.
- Kept `SavedLightStoreStateFactory` as the storage factory/type/load boundary and confirmed storage access continues through the helper.
- Expanded policy tests to prevent raw config save-order lists in the screen and direct snapshot loops in the range renderer.

## Remaining Coupling

- `TorchmasterConfigScreen` still owns concrete widget construction, text field visibility, scroll positioning, and version-specific input/render overrides.
- `TorchmasterLightRangeRenderer` still owns render layer selection, camera translation, buffer flushing, and legacy GL setup.
- `SavedLightStore` still contains PersistentState override signatures for supported version ranges.
- Deprecated `TorchmasterRuntime` filter registry fields remain as compatibility facades.

## Verification

- Representative targets for this phase remain:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
  - `./gradlew "Reset active project"`
  - `git diff --check`
- The active project must finish at `1.21.1-fabric`.

## Anti-Regression Rules

- Do not rebuild config save order from raw `List<Integer>`, `List<Boolean>`, or `List<List<String>>` inside screen classes.
- Do not hand-write range snapshot loops in `TorchmasterLightRangeRenderer`; build a `TorchmasterRangeRenderPlan`.
- Do not put widget, render, storage, filter runtime, or content business details into loader roots.
- Keep business rules version-independent and outside Minecraft/loader APIs.
