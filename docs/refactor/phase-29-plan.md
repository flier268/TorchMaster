# Phase 29 Completion：移植 Diamond Base Mega Torch

## Summary

Phase 29 將 `557d4ff8` 的鑽石底座 Mega Torch 移植到重構後架構。Mega Torch 現在可用 `diamond_base` 方塊狀態切換自然生怪跳過模式，狀態會寫入 persisted light NBT；domain 只處理 project-owned light entry 與 spawn range 規則，Minecraft 方塊互動、NBT、資源與 Fabric early chunk hook 留在 adapter/loader 層。

## Completed

- 新增 `MegaTorchBlock`，用鑽石磚切換 `diamond_base`，並在 state toggle 時重新註冊 light。
- `MegatorchBlockingLight` 與 serializer 支援 `diamondBase`，舊 NBT 缺欄位時預設 `false`。
- `LightStoreRuntime`、`LightRules`、`LightStoreBridge`、`MinecraftSpawnBlocker` 增加自然生怪 position/chunk 查詢。
- Fabric `NaturalSpawnerMixin` 在 chunk spawn 入口早期取消 diamond-base 覆蓋範圍，並依版本分支對應 `SpawnHelper.spawn` 的 `ZZZ` 與 `List<SpawnGroup>` descriptors；Forge/NeoForge 沿用現有 spawn event path 並透過 natural spawn reason 套用 position 規則。
- Range rendering 會讀取 Mega Torch 的 `diamond_base` block state；鑽石底座模式使用 chunk-aligned X/Z 工作 box，Y 軸覆蓋整個 world build height，一般模式維持中心半徑 box。
- Mega Torch blockstate/model/loot table 支援 `diamond_base=true` 顯示與掉落鑽石磚。

## Remaining Coupling

- Fabric early chunk hook 仍直接依賴 `SpawnHelper.spawn(...)` 的版本簽名；目前已確認 `1.16.5` 到 `1.21.1` 使用 `ZZZ`，`1.21.11` 使用 `List<SpawnGroup>`，後續若 Mojang/Yarn 更動自然生怪入口，需優先調整此 loader mixin。
- Forge/NeoForge 目前沒有同等 chunk-level early short-circuit，只透過現有 spawn event 阻擋候選位置；若 loader API 提供更早入口，應在 loader root 補薄轉接並重用 `MinecraftSpawnBlocker`。
- `MegaTorchBlock` 仍承擔玩家互動與 state mutation；不要把鑽石磚 item/block API 移入 domain。

## Verification Targets

- Focused: `./gradlew :1.21.1-fabric:test --tests net.xalcon.torchmaster.domain.LightRulesTest --tests net.xalcon.torchmaster.domain.LightStoreRuntimeTest --tests net.xalcon.torchmaster.minecraft.adapter.MinecraftSpawnBlockerTest --tests net.xalcon.torchmaster.minecraft.light.BlockingLightLifecycleTest --tests net.xalcon.torchmaster.minecraft.light.megatorch.MegatorchSerializerTest --tests net.xalcon.torchmaster.StonecutterSourcePolicyTest`
- Representative matrix: `./gradlew :1.14.4-fabric:test :1.14.4-forge:test :1.20.6-neoforge:test :1.21.11-fabric:test`
- Fabric mixin matrix: run `compileJava` for every registered Fabric node after any `NaturalSpawnerMixin` change, and verify generated active `method =` descriptors against `javap -p -s net.minecraft.world.SpawnHelper`.
- Finish with `./gradlew "Reset active project"`, confirm `stonecutter active "1.21.1-fabric"`, then `git diff --check`.

## Anti-Regression Rules

- `domain` and `port` must remain free of Minecraft, loader, item, block, and Stonecutter imports.
- Do not restore old `common.logic.entityblocking` capability classes or old Gradle MixinGradle wiring.
- Keep `diamond_base` resources in shared content assets and keep item/block ids centralized; do not duplicate loader-specific Mega Torch metadata.
- Fabric mixins should delegate through `minecraft.spawn` helpers or adapter facades rather than importing storage/runtime internals.
