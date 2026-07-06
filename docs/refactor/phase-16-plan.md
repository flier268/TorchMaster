# Phase 16 Completion Record

## Completed

- Forge and NeoForge client entrypoints now initialize through `TorchmasterClientEventAdapter`, so loader roots only keep loader annotations, base event types, and adapter delegation.
- Config screen save/reset/scroll/status orchestration moved into `TorchmasterConfigScreenController`; `TorchmasterConfigScreen` keeps Minecraft screen lifecycle, widget factory, render, keyboard, mouse, and close overrides.
- `SavedLightStoreStateBridge` now owns state creation and NBT load/write glue; `SavedLightStoreStateFactory` only selects the version-specific PersistentState API path.
- Policy tests now block client entrypoints from importing `TorchmasterClientLifecycle`, block config screen save/reset orchestration from returning to the screen class, and block state factory direct store construction/read glue.

## Remaining Coupling

- `TorchmasterClientEventAdapter` still contains Fabric/Forge/NeoForge render/tick version branches; this is intentional because loader roots are not Stonecutter-processed.
- `TorchmasterConfigScreen` still owns Minecraft widget construction through `WidgetFactory` and screen override signatures.
- `SavedLightStore` still owns PersistentState override signatures, including legacy NBT method names.
- Spawn event bridge still mixes runtime config lookup, logging, Minecraft spawn event values, and shared spawn decision calls.

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

- Do not import `TorchmasterClientLifecycle` from loader client entrypoints; initialize through `TorchmasterClientEventAdapter`.
- Do not move config save/reset/status orchestration back into `TorchmasterConfigScreen`.
- Do not create loader-specific screen, render, storage, content, or filter logic copies.
- Do not use reflection, generated source edits, or direct staged-state git operations.
