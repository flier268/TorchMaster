# Phase 21 Plan：Spawn Public Facade 再縮小 + Config Widget Adapter 收尾 + Modern Render Target 拆分

## Summary

Phase 21 should continue reducing version/API glue while keeping business behavior shared. The focus is to narrow public spawn facade usage, finish config widget adapter boundaries, and split modern/latest range render target details.

## Key Changes

- Add a narrower Minecraft spawn hook runtime so `FabricSpawnEventHooks` and loader handlers can avoid depending on broad `SpawnEventBridge` where possible.
- Keep existing `SpawnEventBridge` public methods as compatibility facade until all event/mixin call sites are migrated together.
- Move remaining config widget action/position helper code out of screen/controller classes when it is still Minecraft widget API glue.
- Split modern/latest render target choices from `TorchmasterRangeRenderTarget` into backend-specific helpers, similar to the legacy target split.
- Strengthen source policy so loader roots, mixins, and screen classes cannot regain duplicated spawn/config/render decisions.

## Tests

- Add tests for the narrow spawn hook runtime and compatibility facade equivalence.
- Extend config widget row/controller tests around action dispatch, visibility, and typed read flow.
- Add render target tests for latest/custom pipeline and vanilla line layer helper selection.
- Run the representative matrix, extra Fabric matrix, full build, reset active project, reset後 active test, and `git diff --check`.

## Constraints

- Do not change spawn/light rules, config keys, TOML format, UI behavior, render colors/alpha/line width, NBT schema, or storage id.
- Do not move Minecraft, loader, or Stonecutter APIs into `domain` or `port`.
- Do not use reflection, edit generated sources, or modify staged state.
