# Phase 15 Completion Record

## Completed

- Removed deprecated `TorchmasterRuntime.MegaTorchFilterRegistry` and `DreadLampFilterRegistry`; `TorchmasterEntityFilters` is now the only runtime filter accessor.
- Added `TorchmasterClientEventAdapter` for client tick phase, render stage, and Forge pose stack conversion decisions; client entrypoints now keep event registration/signatures and delegate decisions.
- Added `TorchmasterConfigScreenActions` so config screen bottom buttons are generated from descriptors instead of hand-written geometry.
- Added `SavedLightStoreStateBridge`; `SavedLightStore` PersistentState overrides and `SavedLightStoreStateFactory` now route state read/write through the bridge.
- Expanded source policy tests to block runtime facade fields, direct client entrypoint event decisions, direct bottom-button geometry, and old storage state method names.

## Remaining Coupling

- Client entrypoint classes remain in `src/main` because they still need Stonecutter-processed version and loader event signatures.
- `TorchmasterClientEventAdapter` intentionally contains loader/Minecraft event APIs behind Stonecutter branches.
- `TorchmasterConfigScreen` still owns widget factory wiring and action dispatch after descriptor creation.
- `SavedLightStore` still extends `PersistentState` and keeps version-specific override signatures.

## Phase 16 Direction

- Decide whether client entrypoint classes can be moved or partially split after isolating all Stonecutter-heavy event signatures.
- Continue config widget descriptor cleanup: move widget factory/action dispatch descriptors out of `TorchmasterConfigScreen` without changing UI behavior.
- Explore whether `SavedLightStore` PersistentState overrides can shrink to one-line methods in every supported branch.
- Keep monitoring runtime filter accessor usage now that `TorchmasterRuntime` facade fields are gone.

## Verification

- Required representative matrix:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
  - `./gradlew "Reset active project"`
  - reset後 `./gradlew :1.21.1-fabric:test`
  - `git diff --check`
- Active project must return to `1.21.1-fabric`.

## Anti-Regression

- Do not reintroduce filter globals on `TorchmasterRuntime`.
- Do not put event phase/stage or Forge pose stack conversion decisions back into client entrypoints.
- Do not hand-write config bottom-button geometry in `TorchmasterConfigScreen`.
- Do not use `writeState/readState`, `saveInto/loadFrom`, or direct serializer calls as storage state facades.
