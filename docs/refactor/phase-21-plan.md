# Phase 21 Completion：Spawn Hook Runtime + Modern Render Target Split + Config Widget Adapter 收尾

## Summary

Phase 21 split the remaining broad glue in three areas without changing user-visible behavior or shared business rules. Spawn event entrypoints now delegate through a narrower Minecraft hook helper, range render target API details are split by backend, and config rows delegate Minecraft widget state to a widget adapter.

## Completed

- Added `MinecraftSpawnEventHooks` as the shared hook body for entity spawn, phantom spawn, and village siege checks.
- Kept `SpawnEventBridge` as a compatibility facade, with event bodies delegated to `MinecraftSpawnEventHooks`.
- Updated Fabric/Forge/NeoForge spawn wiring to call the narrow hook helper where possible.
- Added `TorchmasterLatestRangeRenderTarget` and `TorchmasterVanillaRangeRenderTarget`; `TorchmasterRangeRenderTarget` now delegates line layer and flush API details.
- Added `TorchmasterConfigWidgetAdapter`; config rows no longer directly set widget x/y/visibility or button messages.
- Added focused tests for spawn hook propagation and config widget text behavior.
- Strengthened `StonecutterSourcePolicyTest` for spawn facade, Fabric hook, render target, and widget row boundaries.

## Remaining Coupling

- `SpawnEventBridge` still exists as public compatibility API; it can be removed only after all external and mixin call paths are audited.
- Range render target still has version-specific routing in the shared target; latest and vanilla helpers are split, but the session/target lifecycle is not fully backend-polymorphic.
- Config widget rows still own row classes with Minecraft widget references; only widget state mutation has been pushed into the adapter.
- Storage legacy PersistentState branches were not touched in this phase.

## Verification Targets

- Representative matrix: `:1.21.1-fabric:test`, `:1.14.4-forge:test`, `:1.20.6-neoforge:test`, `:1.21.11-fabric:test`.
- Extra render/spawn coverage: `:1.14.4-fabric:test`, `:1.21.11-fabric:test`.
- Full build, reset active project, reset後 `:1.21.1-fabric:test`, and `git diff --check`.
- Confirm active project and `vcsVersion` remain `1.21.1-fabric`.

## Anti-Regression Rules

- Do not put spawn rules, config lookup, logging, or result mapping switches in loader roots or mixins.
- Do not add line layer, render pipeline, `RenderLayer.getLines()`, or flush details back to `TorchmasterRangeRenderTarget`.
- Do not directly mutate config widget x/y/visible/active/message state from `TorchmasterConfigWidgetRows`.
- Do not move Minecraft, loader, or Stonecutter APIs into `domain` or `port`.
