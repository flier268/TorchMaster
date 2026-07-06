---
name: minecraft-adapter-boundaries
description: Use when adding, refactoring, or reviewing TorchMaster code that touches Minecraft APIs, loader APIs, platform entrypoints, adapter interfaces, content registration, or core business logic boundaries.
---

# Minecraft Adapter Boundaries

## Purpose

Use this skill to preserve TorchMaster's adapter architecture: business rules stay in project-owned domain and port types, while Minecraft, Fabric, Forge, NeoForge, registry, and version-specific APIs stay in Minecraft adapter, content factory, or loader entrypoint layers.

This skill is especially relevant before changing spawn blocking behavior, light registration, block or item registration, content definitions, config screens, loader entrypoints, or Stonecutter conditional API code.

## Boundary Rules

- `net.xalcon.torchmaster.domain` is business logic. It must not import `net.minecraft`, Fabric, Forge, NeoForge, or loader lifecycle APIs.
- `net.xalcon.torchmaster.port` contains project-owned ports and value objects. Keep it Minecraft-free and loader-free.
- `net.xalcon.torchmaster.content` owns shared content ids and metadata. Item and block ids, creative tab ordering, and content descriptors are written once here.
- `net.xalcon.torchmaster.minecraft.adapter` and `net.xalcon.torchmaster.minecraft.content` translate project-owned definitions into Minecraft API objects. Put `net.minecraft` imports, registry key setup, item/block settings conversion, and version-specific API branches here by default.
- `net.xalcon.torchmaster.platform` owns cross-loader service abstractions. Keep it focused on loader services and registry wrappers, not content details.
- `src/fabric/java`, `src/forge/java`, and `src/neoforge/java` contain loader entrypoints and thin registrar implementations that do not require Stonecutter-transformed version branches. They may wire lifecycle events and delegate to shared runtime/content registration, but must not duplicate item/block details.
- Entry points that still require Stonecutter-transformed version branches may remain in `src/main` until a dedicated source-root refactor moves those branches into processed adapter code.
- `TorchmasterContent` is a compatibility facade for legacy static registry fields. Keep it thin: delegate registration and backfill fields.

## Content Registration Rules

- Define content ids and metadata in shared definitions such as `TorchmasterContentDefinitions`.
- Convert definitions to Minecraft `Block`, `Item`, `BlockEntityType`, settings, and registry keys in a Minecraft content factory.
- Register content through a loader-selected `ContentRegistrar` loaded by ServiceLoader.
- Do not write Fabric, Forge, and NeoForge copies of the same item/block details. Loader-specific registrars should differ only where lifecycle or registry timing requires it.
- Do not move broad business decisions into Minecraft factories just because they can access Minecraft APIs. Factories translate and instantiate; domain classes decide behavior.

## Workflow

1. Start with CodeGraph if `.codegraph/` exists. Explore the target behavior plus nearby adapter/content/domain symbols before editing.
2. Classify the change:
   - Pure rule or behavior change: edit `domain` and tests with fake port objects.
   - Needs Minecraft state/action: add or extend a `port` interface, implement it in `minecraft`, then consume only the interface from `domain`.
   - Needs item/block metadata: edit shared content definitions, then update Minecraft factory conversion if needed.
   - Needs loader lifecycle or registry timing: keep loader-specific code in `src/[fabric|forge|neoforge]` and call shared runtime/content APIs.
   - Needs version-specific Minecraft API: use Stonecutter conditional-comment style inside `minecraft/adapter`, `minecraft/content`, or narrowly scoped loader lifecycle adapters.
3. Check imports before and after edits. `domain` and `port` must remain Minecraft/loader-free.
4. Keep conversions localized. Reuse Minecraft adapter/content helpers rather than scattering registry or settings logic through blocks, items, or runtime behavior.
5. At the end of each refactor phase, update the next-phase plan under `docs/refactor/` with completed work, remaining coupling, verification targets, and anti-regression instructions.

## Testing

- For domain behavior, add or update tests under `src/test/java/net/xalcon/torchmaster/domain` using fake ports.
- For content registration boundaries, test shared definitions, creative tab ordering, derived block-item behavior, and `TorchmasterContent` facade backfill.
- If an adapter or registrar interface changes, update fake/test implementations and all loader implementations together.
- For version or loader API changes, verify the relevant Stonecutter active version and reset to the repo's `vcsVersion` before commit-ready work.

## Review Checklist

- `domain` and `port` imports remain Minecraft/loader-free.
- Shared content details are not duplicated in Fabric, Forge, and NeoForge roots.
- New platform API usage is isolated to `minecraft`, `platform`, or loader entrypoint packages.
- `TorchmasterContent` remains a facade, not the owner of content construction details.
- Stonecutter conditions are concentrated in Minecraft adapters/content factories or narrow loader lifecycle adapters.
- The next-phase refactor plan is present and current.
