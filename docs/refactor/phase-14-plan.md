# Phase 14 Completion Record

## Completed

- Added `TorchmasterScreenRenderPlan` plus light/config screen presenters so screen classes keep render signatures and lifecycle while render composition lives in shared presenter helpers.
- Extended `TorchmasterScreenRenderAdapter` to render whole screen plans; screens now delegate frame, labels, status, and title drawing through plans.
- Split range line submission into latest, modern WorldRenderer, and legacy BufferBuilder helpers while keeping `TorchmasterRangeLineSubmitter` as a thin coordinator.
- Renamed storage state bridge methods from `saveInto/loadFrom` to `writeState/readState`; `SavedLightStoreStateFactory` no longer depends on the old facade names.
- Added focused tests for screen render plans, range line submission invariants, storage state factory bridge, and source policy regressions.

## Remaining Coupling

- Screen classes still own Minecraft override signatures, background render call order, widget construction, and input event signatures.
- `TorchmasterScreenRenderAdapter` still contains version-specific GUI draw APIs; this is intentional until a deeper widget/render adapter split.
- The latest, modern, and legacy range line submitter helpers still contain Minecraft render API branches.
- `SavedLightStore` still extends `PersistentState` and owns version-specific override signatures.
- Deprecated `TorchmasterRuntime.MegaTorchFilterRegistry` and `DreadLampFilterRegistry` remain as public compatibility facades.

## Phase 15 Direction

- Audit whether deprecated runtime filter facade fields can be removed without breaking supported public/internal compatibility.
- Evaluate client entrypoint/source-root strategy for remaining Stonecutter-heavy client lifecycle branches.
- Reduce `SavedLightStore` legacy PersistentState branch surface if a narrower state adapter can keep behavior unchanged.
- Review whether config screen widget creation can move behind a typed widget presenter without touching UI behavior.

## Verification

- Required representative matrix:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
  - `./gradlew "Reset active project"`
  - reset後 `./gradlew :1.21.1-fabric:test`
  - `git diff --check`
- Active project must return to `1.21.1-fabric`.

## Anti-Regression

- Do not put render title/status/label composition back into screen classes; use presenters and `TorchmasterScreenRenderPlan`.
- Do not put `WorldRenderer.drawBox`, latest `.lineWidth(...)`, or legacy `buffer.vertex(...)` back into `TorchmasterRangeLineSubmitter`.
- Do not call old `SavedLightStore.saveInto/loadFrom` names from state factory code.
- Do not duplicate client render, storage, filter, or content details in Fabric/Forge/NeoForge roots.
