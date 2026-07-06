# Phase 2 Refactor Completion

## Completed

- Added domain-owned light metadata through `LightDefinition`.
- Moved Mega Torch and Dread Lamp key generation, display names, and block translation keys out of Minecraft-facing `blocks.LightType`.
- Kept `blocks.LightType` as the Minecraft view for shapes, particle offsets, and concrete `MinecraftBlockingLight` creation.
- Updated content definitions to use the existing domain `LightKind` instead of a nested content-specific enum.
- Updated client light screen model and concrete Minecraft light display names to delegate to light metadata.
- Preserved `TorchmasterContent` public fields and existing `LightType.MegaTorch` / `LightType.DreadLamp` enum names for compatibility.

## Remaining Coupling

- `EntityBlockingLightBlock` still wires Minecraft lifecycle hooks directly to registry mutation.
- `FeralFlareLanternBlockEntity` still combines Minecraft block entity lifecycle, world scanning, NBT/version handling, and planner calls.
- Version-conditional client entrypoints remain in `src/main` because `src/[loader]/java` roots are not Stonecutter-transformed.
- Storage, client screen rendering, and spawn/mixin code still contain substantial Stonecutter conditionals.

## Validation

- Run representative target tests: active Fabric, legacy Forge, NeoForge, and latest Fabric.
- Run `git diff --check`.
- Reset active project to `vcsVersion` before committing or handing off.

## Anti-Regression Notes

- Keep light key generation in `domain.LightDefinition`, not in `blocks.LightType`.
- Do not add another light-kind enum in content, client, or Minecraft packages.
- Keep loader-specific Java in `src/[loader]/java` unless it needs Stonecutter-transformed version branches.
