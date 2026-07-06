# Phase 3 Completion Record

## Completed

- Added `net.xalcon.torchmaster.minecraft.light.BlockingLightLifecycle` as the Minecraft-facing adapter for blocking-light registration and removal.
- Updated `EntityBlockingLightBlock` so block lifecycle callbacks delegate to `BlockingLightLifecycle` instead of directly resolving `TorchmasterRuntime.getRegistryForLevel` and mutating the light store.
- Added focused unit coverage for the helper's store delegation behavior without requiring a Minecraft `World`.

## Remaining Coupling

- `FeralFlareLanternBlockEntity` still mixes child light storage, world scanning, raycast/version branches, and domain planner calls.
- Client entrypoint and screen/render code still carry version-specific Stonecutter branches.
- Storage and spawn/mixin adapters still have dense version conditionals.
- `BlockingLightLifecycle` still uses `TorchmasterRuntime.getRegistryForLevel` because the persistent-state lookup remains Minecraft-version-specific; keep that access in adapter/helper code, not in block lifecycle code.

## Next Boundary

Continue with `docs/refactor/phase-4-plan.md`. The next phase should focus on extracting Feral Flare placement/removal orchestration while keeping NBT, world access, and raycast API branches Minecraft-specific.

## Verification Used

- Run representative targets after implementation: `1.21.1-fabric`, `1.14.4-forge`, `1.20.6-neoforge`, and `1.21.11-fabric`.
- Reset active project to `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Do not put light store lookup or mutation calls back into `EntityBlockingLightBlock`.
- Do not duplicate blocking-light registration behavior per loader.
- Keep future registry lifecycle decisions in `minecraft/light` or a narrower Minecraft adapter, not in domain or loader entrypoints.
