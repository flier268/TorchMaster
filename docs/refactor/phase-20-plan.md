# Phase 20 Plan：Spawn Bridge 薄化 + Render Legacy Branch 深拆 + Config Runtime Facade 收斂

## Summary

Phase 20 should target higher-impact maintenance hotspots after Phase 19's package cleanup. The focus is to make `SpawnEventBridge`, range legacy render handling, and config runtime access thinner without changing player-visible behavior or business rules.

## Key Changes

- **Spawn Bridge 薄化**
  - Add Minecraft-facing context builder helpers under `minecraft.spawn`.
  - Keep `SpawnEventBridge` public signatures stable, but reduce each method body to world/entity extraction, context builder call, and runtime deny application.
  - Keep spawn/blocking rules in `domain` and shared runtime helpers; do not move rules into mixins, wrappers, or loader handlers.

- **Render Legacy Branch 深拆**
  - Split `<1.15` legacy GL setup/teardown from `TorchmasterRangeRenderTarget` into a narrow legacy target helper.
  - Keep `TorchmasterRangeRenderBackendDescriptor` as the source of line width, camera offset, layer, flush, and legacy flags.
  - Keep `TorchmasterRangeRenderSession` free of render layer, buffer flush, and GL branch decisions.

- **Config Runtime Facade 收斂**
  - Add a client-local config runtime facade for config screen controller access to config/reload.
  - `TorchmasterConfigScreenController` should depend on that facade instead of directly importing `TorchmasterRuntime`.
  - Do not merge this with storage or spawn runtime services; each helper should stay purpose-specific.

## Tests

- Add source policy checks that `SpawnEventBridge` does not hand-build full context rules beyond Minecraft value extraction.
- Add focused tests for context builder output and config runtime facade behavior using test doubles.
- Extend range render target/backend tests for legacy target descriptor behavior.
- Run the same representative matrix, full build, reset active project, reset後 active test, and `git diff --check`.

## Constraints

- Do not change spawn/light rules, config keys, TOML format, render color/alpha/line width, NBT schema, or storage id.
- Do not move Minecraft APIs into `domain` or `port`.
- Do not use reflection, edit generated sources, or modify staged state.
