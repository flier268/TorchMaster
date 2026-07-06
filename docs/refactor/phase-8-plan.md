# Phase 8 Refactor Plan

## Targets

- Split client render API differences from range display data so renderer branches are smaller and easier to verify across Minecraft versions.
- Extract screen-opening/config-screen version branches from `TorchmasterLightScreen` and `TorchmasterConfigScreen` into Minecraft-facing screen helpers.
- Continue storage cleanup by moving persistent-state override glue and config lookup out of `SavedLightStore` where a narrow adapter boundary is practical.
- Audit remaining use of `TorchmasterRuntime.getRegistryForLevel` and replace call sites with `MinecraftLightStoreAccess` or explicit adapter helpers.

## Known Coupling To Address

- `TorchmasterLightRangeRenderer` still mixes render pipeline selection, line drawing implementation, camera translation, and snapshot rendering.
- Client screen classes still mix UI model behavior with version-specific Minecraft screen lifecycle APIs.
- `SavedLightStore` is smaller after serializer extraction but still combines persistent-state lifecycle with runtime configuration access.
- Some legacy runtime facade call sites may remain because older Minecraft branches still need careful Stonecutter handling.

## Next Boundary

- Keep render and screen API branches in `client` or `minecraft/adapter` helpers, not in `domain`, `port`, or loader roots.
- Keep loader roots as lifecycle wiring only; do not place range display, screen model, storage, or spawn logic there.
- Prefer small helper extraction over broad render rewrites unless the representative version matrix is expanded first.

## Verification Plan

- Add focused tests for any new pure render/screen decision helper.
- Run representative targets: `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`.
- If render API branches are changed, also compile at least one Forge client-heavy version that exercises the touched branch.
- Run `./gradlew "Reset active project"` and confirm active project is `1.21.1-fabric`.
- Run `git diff --check`.

## Anti-Regression Rules

- Do not move Minecraft render, screen, persistent-state, world, or NBT APIs into `domain` or `port`.
- Do not use reflection to avoid Stonecutter branches.
- Do not add loader-specific copies of item/block/content, spawn, storage, or client runtime details.
- End Phase 8 by updating this record and creating Phase 9 or a maintenance follow-up plan.
