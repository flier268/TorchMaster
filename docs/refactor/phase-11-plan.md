# Phase 11 Refactor Plan

## Targets

- Deepen client render boundary by separating render-layer/buffer selection from range snapshot traversal and line submission.
- Extract config screen entry models so integer, boolean, and list parsing/validation are no longer owned by widget inner classes.
- Evaluate a version adapter for `SavedLightStore` PersistentState signatures, while keeping NBT schema and storage ids unchanged.
- Decide whether deprecated `TorchmasterRuntime` filter registry facade can be removed after remaining call sites and compatibility needs are audited.

## Known Coupling To Address

- Client range rendering still contains version-specific render API branches and drawing submission in one class.
- Config screen entry classes still mix widget state, scroll visibility, validation, and draft collection.
- Storage state still keeps multiple PersistentState override signatures in `SavedLightStore`.
- Deprecated runtime filter globals remain public for compatibility.

## Verification Plan

- Add focused tests for any extracted config entry model or storage signature adapter helper.
- Run representative targets: `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`.
- If render layer branches are changed, also run at least one legacy Fabric target and one latest Fabric target.
- Run `./gradlew "Reset active project"` and confirm active project is `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Business rules remain version-independent and stay in `domain` or project-owned runtime helpers.
- Do not move Minecraft render/storage API types into `domain` or `port`.
- Do not introduce reflection or loader-specific copies for screen/render/storage/filter logic.
- End Phase 11 by updating this record and creating Phase 12 or a maintenance follow-up plan.
