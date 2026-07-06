# Phase 16 Refactor Plan

## Targets

- Client entrypoint source-root strategy.
  - Audit whether Fabric/Forge/NeoForge client entrypoint classes can move to loader roots after Phase 15 adapter extraction.
  - If event signatures still require Stonecutter processing, keep classes in `src/main` and further narrow adapter methods instead of moving prematurely.
- Config widget descriptor cleanup.
  - Move remaining widget factory/action dispatch wiring into typed descriptors where feasible.
  - Preserve button order, text keys, scroll behavior, field validation, and TOML save format.
- Storage PersistentState branch cleanup.
  - Shrink `SavedLightStore` override bodies while keeping `SavedLightStoreStateFactory` and `SavedLightStoreStateBridge` as the only state glue.
  - Keep NBT, Codec, PersistentState, RegistryWrapper, and serializer details in `minecraft.storage`.
- Runtime accessor monitoring.
  - Keep `TorchmasterEntityFilters` as the only filter registry accessor.
  - Keep policy blocking any new `TorchmasterRuntime` filter facade.

## Verification Plan

- Add focused tests for any entrypoint policy, widget descriptor, or storage bridge change.
- Run:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test` if client/render branches change
  - `./gradlew "Reset active project"`
  - reset後 `./gradlew :1.21.1-fabric:test`
  - `git diff --check`
- Confirm active project returns to `1.21.1-fabric`.

## Anti-Regression Rules

- Business rules stay shared and version-neutral.
- Loader roots must not duplicate client render, storage, filter, or content details.
- Do not use reflection or generated source edits.
- Do not modify staged state outside explicit commit flow.
