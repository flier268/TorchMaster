# Phase 9 Refactor Plan

## Targets

- Split `TorchmasterLightRangeRenderer` further by isolating render-layer/buffer setup from box drawing.
- Move reusable screen render panel drawing primitives out of `TorchmasterLightScreen` and `TorchmasterConfigScreen` without changing override signatures.
- Evaluate a safe adapter for `SavedLightStore` PersistentState NBT/codec/factory branches, but only move signatures if representative versions compile cleanly.
- Reduce `TorchmasterRuntime` global side effects by extracting entity filter reload orchestration into a dedicated runtime helper.

## Known Coupling To Address

- Client render classes still contain multiple Minecraft render API branches.
- Screen classes still mix layout/model state with version-specific render/input method signatures.
- PersistentState signatures remain in `SavedLightStore` because they are version-sensitive.
- Entity filter registries remain global static state on `TorchmasterRuntime`.

## Next Boundary

- Keep render and screen API branches in `client` helpers or Minecraft-facing adapters.
- Keep storage PersistentState branches in `minecraft.storage`; do not move NBT or world APIs into `domain` or `port`.
- Do not create helper classes whose entire body is loader-gated by Stonecutter.
- Keep loader roots limited to lifecycle wiring and loader-specific entrypoints.

## Verification Plan

- Add focused tests for any pure render geometry, panel layout, or filter reload helper.
- Run representative targets: `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`.
- If render or PersistentState signatures are touched, add at least one extra compile target that exercises the touched legacy branch.
- Run `./gradlew "Reset active project"` and confirm active project is `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Do not use reflection to avoid Stonecutter branches.
- Do not add loader-specific copies of screen/render/storage/content logic.
- Do not reintroduce `TorchmasterRuntime.getRegistryForLevel`.
- End Phase 9 by updating this record and creating Phase 10 or a maintenance follow-up plan.
