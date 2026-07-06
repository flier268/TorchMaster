# Phase 14 Refactor Plan

## Targets

- Finish screen class cleanup.
  - Keep `TorchmasterLightScreen` and `TorchmasterConfigScreen` as Minecraft override/lifecycle containers.
  - Move remaining repeated label-loop and status/title render composition into a presenter/helper that consumes layout snapshots and `CompatText`.
  - Keep widget construction and visibility in `TorchmasterConfigWidgetRows`; do not reintroduce raw save-order lists.
- Continue range render pipeline cleanup.
  - Review whether `TorchmasterRangeLineSubmitter` can split latest, modern, and legacy line submission branches into narrow helpers without loader-root copies.
  - Keep geometry/style in `TorchmasterLineBoxRenderer` and `TorchmasterRangeRenderPlan`.
- Tighten storage signature boundary.
  - Check if `SavedLightStore.saveInto/loadFrom` can move fully behind `SavedLightStoreNbtBridge` or `SavedLightStoreStateFactory` without exposing internals.
  - Keep NBT, Codec, PersistentState, RegistryWrapper, and serializer details inside `minecraft.storage`.
- Audit deprecated runtime facades.
  - Confirm whether public compatibility requires `TorchmasterRuntime.MegaTorchFilterRegistry` and `DreadLampFilterRegistry`.
  - If removable, prepare a compatibility note and removal patch; otherwise keep them deprecated and keep policy blocking internal use.

## Verification Plan

- Add tests for any new pure presenter, line-submitter descriptor, or storage bridge behavior.
- Run:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test` if render branches change
  - `./gradlew "Reset active project"`
  - `git diff --check`
- Confirm active project returns to `1.21.1-fabric`.

## Anti-Regression Rules

- Business rules stay shared and version-neutral.
- No `Text`/`String` branching in screen classes; use `CompatText`.
- No render/storage/filter helper details in loader roots.
- No reflection, no loader-specific copies, and no direct edits to generated `versions/.../build/generated` sources.
