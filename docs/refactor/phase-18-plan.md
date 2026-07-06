# Phase 18 Refactor Plan

## Targets

- Event result mapping cleanup.
  - Extract NeoForge `MobSpawnEvent.PositionCheck.Result` and `PlayerSpawnPhantomsEvent.Result` conversion into a shared NeoForge adapter helper.
  - Keep `NeoforgeEventHandler` as lifecycle/event wiring only.
- Fabric wrapper cleanup.
  - Centralize `EventResultContainer` creation and result extraction for Fabric wrapper calls.
  - Keep mixins/wrappers thin and avoid importing domain spawn rules or storage internals.
- Runtime logging/config facade review.
  - Audit remaining direct `TorchmasterRuntime.LOG` and `Services.PLATFORM.getConfig()` usage in Minecraft-facing code.
  - Extract small helpers only where they reduce repeated runtime glue without hiding business rules.

## Verification Plan

- Add focused tests for event result conversion helpers and wrapper result container helpers.
- Run:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
  - `./gradlew build`
  - `./gradlew "Reset active project"`
  - reset後 `./gradlew :1.21.1-fabric:test`
  - `git diff --check`
- Confirm active project returns to `1.21.1-fabric`.

## Anti-Regression Rules

- Business rules stay in `domain` and project-owned ports; event helpers only translate external event APIs.
- Loader roots must not duplicate spawn, render, storage, filter, or content details.
- Do not use reflection or generated source edits.
