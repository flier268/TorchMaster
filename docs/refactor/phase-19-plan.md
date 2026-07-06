# Phase 19 Refactor Plan

## Targets

- Spawn adapter package cleanup.
  - Review whether `events.SpawnEventBridge`, `utils.MobWrapper`, and `utils.NaturalSpawnerWrapper` should move toward a clearer Minecraft spawn adapter package.
  - Keep public signatures stable unless all mixin and loader call sites are updated together.
- Client/render backend cleanup.
  - Continue splitting render backend descriptors from Minecraft render submission without changing range colors, alpha, or line width.
  - Keep loader roots free of render backend decisions.
- Runtime facade review.
  - Audit remaining `TorchmasterRuntime.getConfig()` and direct runtime logger usage in client/storage/command paths.
  - Extract helpers only where they remove repeated glue; do not hide business rules inside generic service locators.

## Required Tests

- Add source policy coverage for any spawn package move or alias facade.
- Add focused tests for new render descriptors or runtime helper decisions.
- Run the representative matrix, full build, reset active project, reset後 active test, and `git diff --check`.

## Constraints

- Business rules remain shared across all versions in `domain` or project-owned ports.
- No loader-specific copies of spawn, render, storage, content, or config rules.
- No reflection, generated source edits, or staged-state mutation.
