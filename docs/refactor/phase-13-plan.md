# Phase 13 Completion Record

## Completed

- Added `TorchmasterScreenRenderAdapter` as the shared Minecraft-facing screen draw primitive helper.
- Moved light/config screen panel frame, fill, centered label, and text label drawing behind the adapter while keeping screen render override signatures local.
- Added `TorchmasterRangeRenderTarget` for range render layer, buffer, flush, camera translation, and legacy GL target setup.
- Reduced `TorchmasterRangeRenderSession` to player guard, render plan creation, and plan submission flow.
- Added `SavedLightStoreNbtBridge` so `SavedLightStore` PersistentState override branches delegate NBT save/load glue instead of directly calling the serializer.
- Expanded source policy tests to block screen draw primitive, range target, storage serializer, loader-root detail, and facade call-site regressions.

## Remaining Coupling

- Screen classes still own widget event signatures, render override signatures, and layout-specific label loops.
- `TorchmasterRangeLineSubmitter` still owns final Minecraft line submission API branches, including `WorldRenderer.drawBox` and latest vertex line-width calls.
- `SavedLightStore` still owns PersistentState method signatures and package-private `saveInto/loadFrom` bridge methods for state factory compatibility.
- Deprecated runtime filter facade fields remain public for compatibility, with policy blocking new internal call sites.

## Phase 14 Direction

- Evaluate a screen widget/render presenter split so screen classes keep only Minecraft override signatures and lifecycle.
- Decide whether `TorchmasterRangeLineSubmitter` can be split into version-specific narrow adapters without adding loader-specific copies.
- Revisit `SavedLightStore` legacy branch shape after representative version compilation, but keep NBT/PersistentState APIs in `minecraft.storage`.
- Audit external compatibility requirements before removing deprecated `TorchmasterRuntime` filter facade fields.

## Verification

- Required representative matrix:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test`
  - `./gradlew "Reset active project"`
  - `git diff --check`
- Active project must return to `1.21.1-fabric`.

## Anti-Regression

- Do not put `Text`/`String` branching back into screen classes; use `CompatText` plus `TorchmasterScreenRenderAdapter`.
- Do not put render layer, flush, or legacy GL target setup back into `TorchmasterRangeRenderSession`.
- Do not call `SavedLightStoreSerializer` directly from `SavedLightStore`.
- Do not duplicate client render, storage, filter, or content details in Fabric/Forge/NeoForge source roots.
