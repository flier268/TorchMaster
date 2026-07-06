# Phase 30 Completion: Per-Light Torch Settings

## Summary

Phase 30 adds per-light settings for Mega Torch and Dread Lamp while keeping spawn rules in domain and Minecraft persistence/UI in adapter layers. Each supported light can now carry `enabled`, `radiusX`, `radiusY`, and `radiusZ`; missing persisted fields remain compatible with old saves by resolving to the current global radius and enabled state.

## Completed

- Added `LightSettings` and `LightSettingsView` to model per-light enabled state and three-axis radii without Minecraft imports.
- Updated `LightRules` and `LightStoreRuntime` so disabled lights are skipped and range checks use cuboid X/Y/Z limits; natural-spawn chunk precheck uses X/Z only and position checks still validate Y.
- Extended Mega Torch and Dread Lamp persisted entries and serializers with optional `enabled`, `radiusX`, `radiusY`, `radiusZ`, and `ownerUuid` NBT fields; old data remains unconfigured and ownerless until edited or replaced.
- Preserved existing per-light settings and owner metadata when block lifecycle re-registers an existing light, including Mega Torch diamond-base state updates.
- Recorded the placing player's UUID for newly placed Mega Torches and Dread Lamps; per-light updates are editable by the placer or any OP.
- Added per-light extra access entries stored as UUID plus last known player name; owner/OP can grant or revoke access for currently online players.
- Changed the gear screen copy and controller behavior to global settings; remote multiplayer opens it read-only and disables save/reset/edit controls.
- Added a global `restrictLightSettingsToOwner` switch, enabled by default, to control whether per-light settings require the placer or an OP.
- Added individual light UI controls for enabled state and X/Y/Z radii, plus apply/reset behavior and cuboid range rendering.
- Extended the individual light UI with an extra access list, online-player name entry, autocomplete, add, and remove controls. Granted players can edit settings but cannot manage the list.
- Made the individual light UI shorter and height-aware so controls remain inside the panel on smaller GUI scales; autocomplete is rendered as a clickable dropdown sourced from the online player list.
- Added snapshot-driven unit display for diamond-base Mega Torches, showing chunk-based range summaries while keeping radius inputs in block values.
- Added server-side update service and loader packet paths. Server validation checks placer-or-OP permission, block position/type, clamps to global maximum, updates `SavedLightStore`, and marks it dirty.
- Replaced the remote command fallback with packet query/update/snapshot flow. Fabric `>=1.20.6`, Forge `>=1.21`, and NeoForge nodes now send `LightSettings` packets; older Fabric/Forge nodes keep no-op networking and remote per-light editing remains read-only there.

## Remaining Coupling

- Fabric and Forge pre-payload generations are currently no-op for remote per-light networking. They compile and keep singleplayer behavior, but remote editing is not enabled until legacy channel branches are added.
- `TorchmasterLightScreen` still contains Minecraft client/server bridging and snapshot application logic; keep that boundary client-only and do not move it into domain or storage.
- Loader roots own packet registration and byte/payload conversion. They must continue delegating validation to `LightSettingsService` rather than duplicating permission or block checks.

## Next Processing Order

1. Add legacy Fabric channel support for `<1.20.6` using the older networking API if remote editing is required on those versions.
2. Add legacy Forge SimpleChannel support for `<1.21` if remote editing is required on those versions.
3. Add UI tests for remote snapshot pending/editable/rejected states.
4. Consider extracting repeated payload encode/decode shape shared by Fabric and NeoForge once all version branches are stable.

## Verification Targets

- Focused behavior: `./gradlew test --tests net.xalcon.torchmaster.domain.LightRulesTest --tests net.xalcon.torchmaster.domain.LightStoreRuntimeTest --tests net.xalcon.torchmaster.minecraft.light.megatorch.MegatorchSerializerTest --tests net.xalcon.torchmaster.minecraft.light.dreadlamp.DreadLampSerializerTest --tests net.xalcon.torchmaster.client.TorchmasterConfigScreenControllerTest --tests net.xalcon.torchmaster.client.TorchmasterScreenRenderPlanTest`
- Packet/UI compile boundary: `./gradlew :1.14.4-fabric:compileJava :1.20.6-fabric:compileJava :1.21.11-fabric:compileJava :1.20.1-forge:compileJava :1.21.1-forge:compileJava :1.20.6-neoforge:compileJava :1.21.11-neoforge:compileJava`
- Representative loader matrix: include at least one packet-enabled Fabric, Forge, and NeoForge node after any screen, service, or networking change.
- Finish with active project still at `1.21.1-fabric`, then run `git diff --check`.

## Anti-Regression Rules

- Domain and port classes must remain free of Minecraft, loader, command, UI, NBT, networking, and Stonecutter imports.
- Per-light radii must always clamp to `0..globalRadius`; never trust client-provided values.
- Missing NBT settings must remain unconfigured and resolve to global-equivalent behavior without immediately writing override fields.
- Missing `ownerUuid` must remain ownerless; do not assign legacy lights to the first player who opens the GUI.
- Missing `allowedPlayers` must remain empty; do not infer access from owner, OP status, or first GUI opener.
- When `restrictLightSettingsToOwner` is enabled, single-light update permission must continue to be enforced on the server as placer-or-OP, not only by client widget state.
- Granted players may edit enabled/radius settings, but only owner/OP may add or remove access entries.
- Extra access autocomplete must remain a dropdown of online player names, with server-side name-to-UUID resolution as the final authority.
- Single-light GUI controls must stay inside the panel at compact GUI heights; reduce visible access rows before allowing bottom action buttons to overflow.
- Diamond-base Mega Torch snapshots must keep exposing chunk-aligned display state so the UI does not label chunk-based behavior as plain block range only.
- When `restrictLightSettingsToOwner` is disabled, server validation must still check player presence, block position/type, and radius clamping.
- Do not allow global settings writes from remote multiplayer UI.
- Do not add per-light settings to Feral Flare Lantern in this phase; keep scope limited to Mega Torch and Dread Lamp.
