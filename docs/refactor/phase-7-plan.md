# Phase 7 Completion Record

## Completed

- Added a shared client lifecycle helper for light screen opener setup, client tick handling, and current range rendering.
- Kept Fabric, Forge, and NeoForge client entrypoints in `src/main` because they still require Stonecutter-processed Minecraft version branches.
- Moved repeated spawn event result resolution into a small Minecraft-facing helper and kept mixins focused on redirect/inject plumbing.
- Kept `MobWrapper` and `NaturalSpawnerWrapper` as the Minecraft-facing spawn event bridges for Fabric mixins, including event container creation and Minecraft position differences.
- Moved light range block identity checks behind `LightType` so `TorchmasterLightRangeDisplay` no longer repeats per-light content checks.
- Expanded source policy tests to guard client runtime duplication in loader roots and prevent mixins from importing spawn business/storage internals.

## Remaining Coupling

- Client render code still contains version-specific render API branches inside `TorchmasterLightRangeRenderer`.
- `TorchmasterLightScreen` and `TorchmasterConfigScreen` still own Minecraft screen API branches.
- `SavedLightStore` still owns persistent-state overrides and config lookup around the serializer boundary.
- `TorchmasterRuntime.getRegistryForLevel` remains as a legacy facade for older call sites.

## Verification

- Representative targets for this phase remain:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew "Reset active project"`
  - `git diff --check`
- The active project must finish at `1.21.1-fabric`.

## Anti-Regression Rules

- Do not move client entrypoints out of `src/main` while they still need Stonecutter-transformed version branches.
- Do not duplicate client render/screen runtime details in `src/fabric`, `src/forge`, or `src/neoforge`.
- Do not import `SpawnBlockingRules`, storage internals, or `MinecraftSpawnBlocker` directly from mixins.
- Do not add new shared classes whose entire package/import/class body is loader-gated by Stonecutter; keep unavoidable loader-only mixin plumbing in the existing loader-specific wrapper/mixin boundary.
- Do not copy content definitions, storage serializers, Feral Flare lifecycle, or spawn blocker logic into loader roots.
