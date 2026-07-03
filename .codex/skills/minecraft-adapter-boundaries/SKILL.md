---
name: minecraft-adapter-boundaries
description: Use when adding, refactoring, or reviewing AutoHarvestMod code that touches Minecraft APIs, loader APIs, platform entrypoints, adapter interfaces, or core business logic boundaries.
---

# Minecraft Adapter Boundaries

## Purpose

Use this skill to preserve AutoHarvestMod's adapter architecture: core business logic depends on project-owned ports and value objects, while Minecraft, Fabric, Forge, NeoForge, and version-specific APIs stay in adapter or entrypoint layers.

This skill is especially relevant before changing harvesting behavior, player/world/inventory access, crop catalogs, interactions, config screens, loader entrypoints, or Stonecutter conditional API code.

## Boundary Rules

- `dev.flier268.autofarmer.core` is business logic. It must depend on `dev.flier268.autofarmer.adapter` abstractions and config/domain types, not `net.minecraft`, Fabric, Forge, NeoForge, or loader lifecycle APIs.
- `dev.flier268.autofarmer.adapter` contains ports, views, and small value objects such as positions, vectors, directions, hands, hit targets, item keys, and world/player/block/item views. Keep these stable and Minecraft-free.
- `dev.flier268.autofarmer.minecraft` implements adapter ports and translates between project-owned types and Minecraft API types. Put `net.minecraft` imports, registry lookups, item/block wrappers, and version-specific API branches here by default.
- Loader packages such as `fabric`, `forge`, and `neoforge` should stay thin: initialization, event wiring, keybind/screen hooks, and calls into `AutoFarmerRuntime` or the core engine.
- Runtime composition is the bridge. `AutoFarmerRuntime` may wire concrete Minecraft adapters into `AutoFarmerEngine`; core classes should receive abstractions through constructors or methods.

## Workflow

1. Start with CodeGraph if `.codegraph/` exists. Explore the target behavior plus nearby adapter/core symbols before editing.
2. Classify the change:
   - Pure rule or behavior change: edit `core` and tests with fake adapter objects.
   - Needs Minecraft state/action: add or extend an interface in `adapter`, implement it in `minecraft`, then consume only the interface from `core`.
   - Needs loader lifecycle or UI hook: keep loader-specific code in the loader package and call runtime/core APIs.
   - Needs version-specific Minecraft API: use the existing Stonecutter conditional-comment style inside adapter or loader code, not inside `core`.
3. Check imports before and after edits. No `core` or `adapter` file should import `net.minecraft`, Fabric, Forge, NeoForge, or loader-specific classes.
4. Keep conversions localized. Use or extend helper adapters such as `MinecraftAdapters` and `MinecraftRegistry` rather than scattering conversion or registry logic through business behavior.
5. Prefer null/availability handling in concrete Minecraft adapters when it reflects client/world/player absence; keep core logic focused on domain decisions.

## Design Guidance

- Add the smallest adapter method that expresses the domain need. Avoid exposing raw Minecraft objects or leaking names like `MinecraftClient`, `BlockState`, `ItemStack`, `Registry`, `Identifier`, or loader event types through adapter interfaces.
- Return project-owned views or values from ports. If a business rule needs a new fact, model that fact as a method on an existing view or a new small value type.
- If a concrete adapter must downcast from an interface, keep the downcast inside `minecraft` and fail safely when the value is not the expected Minecraft wrapper.
- Keep crop/block/item identity logic behind `CropCatalog`, `BlockStateView`, `ItemStackView`, `ItemKey`, or related ports. Core code should compare domain IDs or ask catalog methods, not inspect Minecraft registries directly.
- Do not move broad behavior into adapter classes just because they can access Minecraft APIs. Adapter code should translate and perform platform operations; decisions belong in `core`.

## Testing

- For core behavior, add or update tests under `src/test/java/dev/flier268/autofarmer/core` using fake implementations of adapter interfaces.
- Test domain decisions and requested adapter calls, not Minecraft internals.
- If an adapter interface changes, update all fake/test implementations and the Minecraft implementation together.
- For version or loader API changes, verify the relevant Stonecutter active version and conditional comments; reset to the repo's VCS version before commit-ready work.

## Review Checklist

- `core` and `adapter` imports remain Minecraft/loader-free.
- New platform API usage is isolated to `minecraft` or loader entrypoint packages.
- Core behavior is constructor-injected or method-injected through adapter ports.
- Conversion helpers are reused instead of duplicated.
- Tests cover the changed business behavior with fake adapters.
