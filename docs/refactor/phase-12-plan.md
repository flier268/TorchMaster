# Phase 12 Refactor Plan

## Targets

- Extract a widget adapter for config screen text fields/buttons so widget positioning, active/visible toggles, and version-specific setters are not repeated in screen inner classes.
- Split range renderer buffer/layer/camera setup into a Minecraft render adapter while preserving existing public render overloads.
- Evaluate whether `SavedLightStore` PersistentState signatures can be isolated further without changing NBT schema or storage ids.
- Audit and decide removal timing for deprecated `TorchmasterRuntime` filter registry facades.

## Known Coupling To Address

- Config screen still mixes entry widget objects with scroll visibility and position updates.
- Range renderer still contains layer selection, camera translation, flush behavior, and legacy GL state.
- Storage state still keeps version-specific PersistentState method signatures in one class.
- Runtime logger/config facade remains broadly referenced, so only filter registry facades are candidates for removal.

## Verification Plan

- Add focused tests for any extracted widget adapter or render adapter pure planning helper.
- Run representative targets: `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`.
- If render adapter branches change, also run `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`.
- Run `./gradlew "Reset active project"` and confirm active project is `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Do not move Minecraft render, widget, NBT, Codec, or PersistentState API types into `domain` or `port`.
- Do not introduce reflection or loader-specific copies for screen/render/storage/filter logic.
- Do not add new call sites for deprecated `TorchmasterRuntime` filter registry fields.
- End Phase 12 by updating this record and creating Phase 13 or a maintenance follow-up plan.
