# Phase 9 Completion Record

## Completed

- Added domain-level entity filter override parsing and validation so block-list override rules are shared across all versions and loaders.
- Kept `EntityFilterList` as a runtime holder while delegating override validation and add/remove decisions to domain rules.
- Updated TOML config normalization and config screen validation to use domain override rules instead of the runtime filter holder.
- Added `TorchmasterEntityFilterRuntime` to centralize filter reload ordering: clear, register vanilla defaults, then apply config overrides.
- Added `TorchmasterConfigDraft` so the config screen no longer maps `ints`/`booleans`/`lists` indexes directly into save parameters.
- Expanded source policy tests to keep `domain`/`port` Stonecutter-free, prevent config validation from depending on runtime filter holder APIs, and keep filter runtime details out of loader roots.

## Remaining Coupling

- `TorchmasterRuntime` still owns global entity filter registry instances and delegates reload side effects.
- `TorchmasterConfigScreen` still owns Minecraft widget collection and screen rendering branches.
- `TorchmasterLightRangeRenderer` still owns render-layer, buffer, camera translation, and line drawing API branches.
- `SavedLightStore` still owns PersistentState NBT/codec/factory override signatures.

## Verification

- Representative targets for this phase remain:
  - `./gradlew :1.21.1-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
  - `./gradlew "Reset active project"`
  - `git diff --check`
- The active project must finish at `1.21.1-fabric`.

## Anti-Regression Rules

- Do not duplicate entity filter override parsing or validation outside `domain`.
- Do not add Stonecutter conditions to `domain` or `port`.
- Do not make config validation depend on `EntityFilterList` runtime holder APIs.
- Do not put filter reload, screen/render, storage, or content business logic into loader roots.
