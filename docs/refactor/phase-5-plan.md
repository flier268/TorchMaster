# Phase 5 Completion Record

## Completed

- Added `net.xalcon.torchmaster.minecraft.storage.MinecraftLightStoreAccess` for world-to-light-store lookup, dimension id derivation, and persistent-state version branches.
- Reduced `TorchmasterRuntime.getRegistryForLevel` to a compatibility facade that delegates to `MinecraftLightStoreAccess`.
- Added `net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlocker` for spawn, phantom, village-siege, and global tick light-store queries.
- Reduced `SpawnEventBridge` to event plumbing, config skip checks, logging, and delegation to `MinecraftSpawnBlocker`.
- Added focused tests for storage id stability and spawn blocker delegation with fake `LightStoreBridge`.

## Remaining Coupling

- Client screen/render classes still carry dense version branches and may depend on active source-root processing.
- `SavedLightStore` still combines NBT serializer loops, domain registry delegation, config lookup, and version-specific persistent-state APIs.
- Fabric/Forge/NeoForge spawn hooks and mixins still contain loader event plumbing, but shared spawn-blocking light-store queries now live in `MinecraftSpawnBlocker`.
- `TorchmasterRuntime.getRegistryForLevel` is still present for existing callers, but it no longer owns persistent-state lookup details.

## Next Boundary

Continue with `docs/refactor/phase-6-plan.md`. The next phase should focus on client entrypoint/source-root strategy and deeper `SavedLightStore` NBT serialization split.

## Verification Used

- Run representative targets after implementation: `1.21.1-fabric`, `1.14.4-forge`, `1.20.6-neoforge`, and `1.21.11-fabric`.
- Reset active project to `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Do not reintroduce loader entrypoints into `src/main` unless they require Stonecutter-transformed version branches.
- Do not use reflection to avoid Stonecutter or loader-specific adapter code.
- Do not move Minecraft NBT, persistent-state, world, or raycast APIs into `domain` or `port`.
- Do not move persistent-state lookup branches back into `TorchmasterRuntime`.
- Do not query light stores directly from `SpawnEventBridge` when `MinecraftSpawnBlocker` can own that adapter responsibility.
- End Phase 6 by updating its completion record and creating a Phase 7 or maintenance follow-up plan.
