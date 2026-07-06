# Phase 13 Refactor Plan

## Targets

- Split remaining screen render primitive calls behind a client screen render adapter.
  - Keep `TorchmasterLightScreen` and `TorchmasterConfigScreen` public override signatures in place.
  - Move centered label/text label draw calls into helper methods that consume `CompatText`.
- Continue range render pipeline cleanup.
  - Evaluate whether `TorchmasterRangeRenderSession` can separate layer/flush descriptors from direct submission without changing colors, alpha, line width, or visibility behavior.
  - Keep Minecraft render types in client adapter helpers only.
- Revisit storage signature boundary.
  - Check whether `SavedLightStore` save/load override branches can be reduced to tiny delegating methods.
  - Do not move NBT, Codec, PersistentState, or RegistryWrapper types outside `minecraft.storage`.
- Audit deprecated runtime filter facades.
  - If no external compatibility requirement remains, prepare a removal plan for `TorchmasterRuntime.MegaTorchFilterRegistry` and `DreadLampFilterRegistry`.
  - If they must stay, keep policy tests blocking new call sites.

## Verification Plan

- Add focused tests for any new pure render descriptors or storage helper decisions.
- Run:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew :1.14.4-fabric:test :1.21.11-fabric:test` if render branches change.
  - `./gradlew "Reset active project"`
  - `git diff --check`
- Confirm active project returns to `1.21.1-fabric`.

## Anti-Regression Rules

- Business rules remain shared and version-neutral.
- Do not reintroduce `Text`/`String` branching in screen classes; use `CompatText`.
- Do not copy client render, storage, or filter details into Fabric/Forge/NeoForge roots.
- Do not use reflection instead of Stonecutter branches.
