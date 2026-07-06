# Phase 22 Plan：Spawn Facade Removal Audit + Config Widget Descriptor + Render Backend Lifecycle Split

## Summary

Phase 22 should continue the same boundary direction while avoiding overfocusing on one subsystem. The next high-value work is to audit whether the public spawn facade can be removed, make config widget row state more descriptor-driven, and split range render target lifecycle so backend selection is less centralized.

## Key Changes

- Audit all `SpawnEventBridge` call sites after Phase 21. If only compatibility references remain, remove the facade; otherwise mark the remaining callers and keep it as a thin deprecated bridge.
- Add a small config widget row descriptor/value layer for position, visibility, widget kind, and boolean label state. `TorchmasterConfigWidgetRows` should orchestrate descriptors, while `TorchmasterConfigWidgetAdapter` applies them to Minecraft widgets.
- Split range render target lifecycle into backend helpers for latest, vanilla, and legacy begin/line-buffer/flush/end paths. `TorchmasterRangeRenderSession` should choose a descriptor and delegate target lifecycle without knowing backend API details.
- Keep storage as a secondary cleanup item: inspect remaining legacy PersistentState branches and document whether Phase 23 should shrink them further.
- Extend source policy so spawn facade removal, widget descriptors, and render backend lifecycle split do not regress.

## Tests

- Add or update tests for spawn facade removal/deprecation policy and direct hook equivalence.
- Add config widget descriptor tests for row position, visibility, kind, and boolean label state.
- Add render backend lifecycle descriptor tests covering latest, vanilla, and legacy paths.
- Run the representative matrix, extra Fabric matrix, full build, reset active project, reset後 active test, and `git diff --check`.

## Constraints

- Do not change spawn/light rules, config keys, TOML format, UI behavior, render colors/alpha/line width, NBT schema, or storage id.
- Loader roots remain lifecycle/event wiring only.
- Do not use reflection, edit generated sources, or modify staged state.
