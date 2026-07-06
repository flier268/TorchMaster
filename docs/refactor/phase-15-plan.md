# Phase 15 Refactor Plan

## Targets

- Decide deprecated runtime facade removal timing.
  - Audit `TorchmasterRuntime.MegaTorchFilterRegistry` and `DreadLampFilterRegistry` compatibility requirements.
  - If removal is acceptable, remove the fields and update policy; otherwise keep them deprecated and continue blocking internal call sites.
- Continue client boundary cleanup.
  - Review client entrypoints and lifecycle adapters for branches that can move behind shared client helper methods without relocating entrypoints yet.
  - Keep loader roots free of duplicated client render/content/storage details.
- Tighten storage legacy adapter shape.
  - Check whether `SavedLightStore` PersistentState override branches can shrink further while `SavedLightStoreStateFactory` remains the only factory/load glue.
  - Keep NBT, Codec, PersistentState, RegistryWrapper, and serializer details in `minecraft.storage`.
- Review config widget presenter opportunity.
  - Evaluate moving bottom-button layout and widget creation descriptors into typed helpers.
  - Do not change UI text, order, scroll behavior, TOML format, or validation rules.

## Verification Plan

- Add focused tests for any facade removal policy, client lifecycle helper, storage adapter, or widget descriptor change.
- Run:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test` if client/render branches change
  - `./gradlew "Reset active project"`
  - reset後 `./gradlew :1.21.1-fabric:test`
  - `git diff --check`
- Confirm active project returns to `1.21.1-fabric`.

## Anti-Regression Rules

- Business rules stay shared and version-neutral.
- No `Text`/`String` branching in screen classes; use `CompatText`.
- No direct loader-specific copies of client render, storage, filter, or content details.
- No reflection, no generated source edits, and no staged-state changes outside explicit commit flow.
