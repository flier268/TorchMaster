# Phase 4 Completion Record

## Completed

- Added `net.xalcon.torchmaster.minecraft.light.feralflare.FeralFlareLanternLifecycle` for Feral Flare tick orchestration, placement attempts, line-of-sight raycast, child light validation, and child light removal.
- Added `net.xalcon.torchmaster.minecraft.light.feralflare.FeralFlareLightPositions` for NBT child-light position encode/decode loops.
- Reduced `FeralFlareLanternBlockEntity` to state storage, Minecraft NBT override methods, line-of-sight setting, and delegation to the new helpers.
- Added tests for position encode/decode, lifecycle tick decisions, check-index wrapping, missing child removal, and planner placement/removal boundaries.

## Remaining Coupling

- `FeralFlareLanternLifecycle` still contains Minecraft world, heightmap, raycast, light-level, block placement, and build-limit APIs by design.
- `FeralFlareLanternBlockEntity` still owns NBT override signatures and version-specific NBT API branches.
- Client entrypoint/source-root strategy, storage persistent-state branches, and spawn/mixin adapters remain Stonecutter-heavy and should not be broadened during this phase.

## Next Boundary

Continue with `docs/refactor/phase-5-plan.md`. The next phase should focus on client entrypoint/source-root strategy, persistent storage branches, and spawn/mixin Stonecutter hot spots.

## Verification Used

- Run representative targets after implementation: `1.21.1-fabric`, `1.14.4-forge`, `1.20.6-neoforge`, and `1.21.11-fabric`.
- Reset active project to `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Do not move NBT or world/raycast APIs into `domain` or `port`.
- Do not duplicate Feral Flare behavior in Fabric/Forge/NeoForge roots.
- Do not move placement candidate generation, LoS raycast, child-light cleanup loop, or NBT encode/decode loops back into `FeralFlareLanternBlockEntity`.
- End the next phase by updating its completion record and creating a Phase 6 or maintenance follow-up plan.
