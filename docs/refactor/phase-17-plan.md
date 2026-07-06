# Phase 17 Refactor Plan

## Targets

- Spawn event bridge boundary.
  - Extract runtime config lookup and debug logging glue from `SpawnEventBridge` into a Minecraft-facing helper.
  - Keep `SpawnBlockingRules` as the shared business decision owner and keep mixins thin.
- Range render backend split.
  - Continue shrinking `TorchmasterClientEventAdapter` and range render backend branches without moving version-specific render API into loader roots.
  - Keep pure range geometry in `TorchmasterRangeBoxes`, `TorchmasterLineBoxRenderer`, and `TorchmasterRangeRenderPlan`.
- Storage legacy branch cleanup.
  - Audit legacy `SavedLightStore` PersistentState overrides and reduce duplicated read/write branch bodies where possible.
  - Keep NBT, Codec, PersistentState, RegistryWrapper, and serializer details inside `minecraft.storage`.

## Verification Plan

- Add focused tests for any spawn bridge helper, range backend descriptor, or storage legacy branch change.
- Run:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test` if render branches change
  - `./gradlew build`
  - `./gradlew "Reset active project"`
  - reset後 `./gradlew :1.21.1-fabric:test`
  - `git diff --check`
- Confirm active project returns to `1.21.1-fabric`.

## Anti-Regression Rules

- Business rules stay shared and version-neutral; Minecraft event/config/logging adapters only translate inputs and outputs.
- Loader roots must not duplicate spawn, render, storage, filter, or content details.
- Mixin classes must stay redirect/inject wrappers and delegate decisions to shared helpers.
- Do not use reflection or generated source edits.
