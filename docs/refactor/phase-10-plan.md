# Phase 10 Refactor Plan

## Targets

- Extract reusable screen panel rendering primitives from `TorchmasterLightScreen` and `TorchmasterConfigScreen` while keeping version-specific override method signatures local.
- Split `TorchmasterLightRangeRenderer` by isolating render-layer/buffer setup from range box drawing.
- Evaluate whether `SavedLightStore` PersistentState NBT/codec/factory branches can be wrapped in a storage state factory helper without moving signatures into domain or port.
- Reduce `TorchmasterRuntime` global state further by moving filter registry ownership behind a narrow runtime accessor if call sites allow it.

## Known Coupling To Address

- Screen classes still mix layout/model state with Minecraft render/input API branches.
- Range renderer still mixes snapshot traversal, buffer selection, camera translation, and line drawing.
- PersistentState signatures remain version-sensitive inside `SavedLightStore`.
- Entity filter registries are still public static fields for legacy command/storage access.

## Next Boundary

- Keep business rules in `domain` and project-owned ports; Minecraft helpers may only translate and apply those rules.
- Keep render/screen/storage API branches in `client` or `minecraft.storage` helpers, not loader roots.
- Do not create helper classes whose package/import/class body is fully loader-gated by Stonecutter.

## Verification Plan

- Add focused tests for any pure panel geometry, render primitive, or storage factory helper.
- Run representative targets: `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`.
- If render or PersistentState signatures are touched, add at least one extra compile target for the touched legacy branch.
- Run `./gradlew "Reset active project"` and confirm active project is `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Do not use reflection to avoid Stonecutter branches.
- Do not add loader-specific copies of screen/render/storage/content/filter logic.
- Do not reintroduce business rules into Minecraft or loader API classes when a domain helper exists.
- End Phase 10 by updating this record and creating Phase 11 or a maintenance follow-up plan.
