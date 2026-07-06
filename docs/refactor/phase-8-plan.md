# Phase 8 Completion Record

## Completed

- Added `TorchmasterScreenCompat` as a shared Minecraft-facing screen base helper for opening screens, returning to parent screens, widget registration, widget clearing, button construction, text construction, and text field construction.
- Updated `TorchmasterLightScreen` and `TorchmasterConfigScreen` to delegate common screen API differences to the compat helper while keeping render/key/mouse override signatures in the concrete screen classes.
- Added `TorchmasterRangeBoxes` to centralize range and sample box geometry used by the light range renderer.
- Updated `TorchmasterLightRangeRenderer` to keep Minecraft render API branches while delegating box coordinate calculation to the geometry helper.
- Replaced remaining `TorchmasterRuntime.getRegistryForLevel` call sites with direct `MinecraftLightStoreAccess.get(level)` usage and removed the runtime facade method.
- Added `LightStoreConfigView` so `SavedLightStore` no longer directly calls `Services.PLATFORM.getConfig()`.
- Expanded source policy checks for runtime facade call sites, loader root client/storage detail duplication, and whole-class loader-gated shared helpers.

## Remaining Coupling

- `TorchmasterLightRangeRenderer` still owns Minecraft buffer, line drawing, camera translation, and render-layer API branches.
- `TorchmasterLightScreen` and `TorchmasterConfigScreen` still own render override branches and input event signature branches.
- `SavedLightStore` still owns PersistentState NBT/codec/factory override signatures.
- `TorchmasterRuntime` still owns global config reload side effects and entity filter registry lifecycle.

## Verification

- Representative targets for this phase remain:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew "Reset active project"`
  - `git diff --check`
- The active project must finish at `1.21.1-fabric`.

## Anti-Regression Rules

- Do not reintroduce `TorchmasterRuntime.getRegistryForLevel` call sites.
- Do not add shared helper classes whose package/import/class body is fully loader-gated by Stonecutter.
- Do not move Minecraft render, screen, persistent-state, world, or NBT APIs into `domain` or `port`.
- Do not add loader-specific copies of item/block/content, spawn, storage, screen, or render runtime details.
