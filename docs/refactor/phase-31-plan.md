# Phase 31 Completion: Single-Light Settings Domain Boundary

## Summary

Phase 31 moves single-light settings decisions into Minecraft-free domain code and leaves Minecraft, loader, NBT, and packet APIs in adapter layers. Mega Torch and Dread Lamp keep their existing persistence/runtime shapes, but owner, extra access, and range preview visibility are now exposed through the shared `LightControlState` contract. Legacy Fabric and Forge networking paths now send the same remote settings commands that newer payload paths already used.

## Completed

- Added `LightControlState`, `LightSettingsUseCase`, `LightSettingsAction`, and `LightSettingsRequest` under the domain package without Minecraft or loader imports.
- Extended `LightEntry` with a control-state contract so use-case code can read and write owner UUID, allowed players, and range preview visibility without casting to `MinecraftBlockingLight`.
- Reduced `LightSettingsService` to a Minecraft adapter facade. It now resolves worlds, positions, player identity, global config, block type checks, store lookup/save, and online-player name lookup, then delegates settings/access/range decisions to `LightSettingsUseCase`.
- Updated `LightSettingsNetworkHandler` so visible-range queries use the service/use-case path instead of scanning the store and duplicating decision logic.
- Added `TorchmasterLightScreenController` to own snapshot state, pending-state decisions, local service calls, remote packet dispatch, and range preview application. `TorchmasterLightScreen` now primarily builds widgets and forwards events.
- Shared request/action encoding was added to `LightSettingsPacketCodec`, and the codec is now available to legacy networking versions as well as modern payload versions.
- Added Fabric `<1.20.6` request/snapshot networking through the legacy client/server packet registries.
- Added Forge `<1.21` request/snapshot networking through legacy `SimpleChannel` registration and client/server send helpers.
- Kept NeoForge on the existing payload route while it continues to delegate through the shared network handler.
- Added `LightPreviewSyncService` as the server-side range-preview reconciliation point. Loader roots now provide only the per-player snapshot sender, while visible-range query, same-world movement resync, range/radius updates, and light removal all send the same `syncStart` -> present snapshots -> `syncEnd` transaction.
- Client visible-range resync now triggers when the player crosses a chunk boundary as well as when the client world changes. The client marks existing server-synced previews at `syncStart`, confirms entries as present snapshots arrive, and removes only unconfirmed server-synced entries at `syncEnd`, so full refreshes do not flicker or remove manual range displays.
- Added domain unit coverage for restricted/unrestricted permissions, radius clamping, range visibility persistence, visible-range listing, online-player access grants, duplicate access grants, empty names, invalid UUID removal, missing UUID removal, and successful removal.

## Remaining Coupling

- Mega Torch and Dread Lamp still physically store `ownerUuid`, `allowedPlayers`, and `rangeVisible` as concrete fields for serializer compatibility. The use-case boundary no longer depends on those fields directly, but a future cleanup can collapse the concrete storage into a single `LightControlState` value after the multi-version serializer surface is quieter.
- Loader packet/message classes still expose version-specific wrapper fields for Stonecutter compatibility. The shared codec and request/action model are present, but fully replacing every loader wrapper constructor with `LightSettingsRequest` can be a later mechanical cleanup.
- `TorchmasterLightScreen.receiveSnapshot` still applies remote range-preview snapshots even when no screen controller is active, because visible preview updates are a client-global render concern rather than purely a screen concern.
- The chunk-boundary resync trigger still originates on the client tick path. The server-side full-reconcile logic is centralized, but a future loader lifecycle cleanup can replace the client request trigger with shared server player-movement hooks if those hooks are normalized across Fabric, Forge, and NeoForge.

## Next Processing Order

1. Collapse Mega Torch and Dread Lamp concrete control fields into direct `LightControlState` storage once serializer round-trip tests are stable across all supported versions.
2. Convert Fabric, Forge, and NeoForge payload/message wrapper constructors to carry `LightSettingsRequest` internally and keep only byte/payload conversion in loader roots.
3. Add focused serializer round-trip assertions for `LightControlState` default values and explicit range-preview visibility on both Mega Torch and Dread Lamp.
4. Add controller-level tests for remote pending, rejected snapshots, local fallback, and visible-preview application.
5. Normalize server-side player movement/disconnect hooks across loaders, then move preview resync triggering fully out of the client lifecycle while keeping `LightPreviewSyncService` as the single reconciliation point.

## Verification Targets

- Domain/use-case: `./gradlew test --tests net.xalcon.torchmaster.domain.*`
- Minecraft adapter and serializer boundary: `./gradlew test --tests net.xalcon.torchmaster.minecraft.light.*`
- Client controller/render plan: `./gradlew test --tests net.xalcon.torchmaster.client.TorchmasterScreenRenderPlanTest`
- Compile matrix: `./gradlew :1.14.4-fabric:compileJava :1.20.6-fabric:compileJava :1.21.11-fabric:compileJava :1.14.4-forge:compileJava :1.20.1-forge:compileJava :1.21.1-forge:compileJava :1.20.6-neoforge:compileJava :1.21.11-neoforge:compileJava`
- Finish with active project still at `1.21.1-fabric`, then run `git diff --check`.

## Anti-Regression Rules

- Domain and port classes must remain free of Minecraft, Fabric, Forge, NeoForge, NBT, networking, UI, and Stonecutter imports.
- Single-light settings, range-preview visibility, access grants, access removal, permission checks, and radius clamping must stay in `LightSettingsUseCase`.
- Minecraft services may translate `World`, `BlockPos`, `LightType`, `ServerPlayerEntity`, config, store, and online-player directory concerns, but must not duplicate use-case permission rules.
- Loader networking may encode/decode and dispatch packets, but must not scan light stores or decide whether a player can edit a light.
- Missing legacy NBT must keep defaulting to ownerless, empty access, unconfigured settings, and `rangeVisible=false`.
- Range preview visibility is persistent per light and must be broadcast/queried so other clients can render the same preview state.
- Range preview full refreshes must go through `LightPreviewSyncService`; do not reintroduce loader-local player filtering, store scans, or world broadcast loops.
- The 100-chunk visibility window is reconciled as a full server-owned set. `syncStart` must not clear displays immediately; `syncEnd` removes only server-synced entries not confirmed by a present snapshot in the same transaction. Manual range displays must remain client-owned.
- Radius values from clients must always clamp through the server-side use-case path before being saved.
- When `restrictLightSettingsToOwner` is enabled, server-side writes require owner, allowed player, or OP as appropriate; client widget state is only a convenience.
- Access-list management remains owner-or-OP only, even when granted players may edit radius/enabled state.
- Do not add single-light settings to Feral Flare Lantern in this phase.
