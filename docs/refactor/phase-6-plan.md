# Phase 6 Completion Record

## Completed

- Added `net.xalcon.torchmaster.minecraft.storage.SavedLightStoreSerializer` for light registry NBT list encode/decode.
- Reduced `SavedLightStore` to persistent-state lifecycle methods plus domain registry delegation; NBT light serializer loops now live in the storage helper.
- Added serializer tests covering saved metadata keys, serializer type preservation, and registry reload.
- Added a source-root policy test that allows `src/main` client entrypoints only while they still require Stonecutter version processing.

## Remaining Coupling

- Client screen/render and entrypoint code still carry dense version-specific Stonecutter branches.
- `SavedLightStore` still owns domain registry delegation, config lookup, and version-specific persistent-state override methods.
- `TorchmasterRuntime` still exposes `getRegistryForLevel` as a legacy facade used by existing adapters.
- Mixins and loader event hooks still route through shared bridge APIs, but their source-root strategy has not been fully audited.

## Next Boundary

Continue with `docs/refactor/phase-7-plan.md`. The next phase should focus on client render/screen adapter split and a deeper audit of mixin/source-root strategy.

## Verification Used

- Run representative targets after implementation: `1.21.1-fabric`, `1.14.4-forge`, `1.20.6-neoforge`, and `1.21.11-fabric`.
- Reset active project to `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Do not place loader entrypoints in `src/main` unless they require Stonecutter-transformed branches.
- Do not use reflection to avoid Stonecutter or typed loader/Minecraft APIs.
- Do not move Minecraft NBT, persistent-state, world, or raycast APIs into `domain` or `port`.
- Do not move light NBT list encode/decode loops back into `SavedLightStore`.
- End Phase 7 by updating its completion record and creating a Phase 8 or maintenance follow-up plan.
