# Phase 27 Completion：移除舊距離邏輯耦合

## Summary

Phase 27 移除已無呼叫者的 legacy `net.xalcon.torchmaster.logic` 距離介面，避免 Minecraft `BlockPos` 版本的 cubic/cylinder 判斷留在商業邏輯附近造成未來分叉。距離規則維持由 `domain.DistanceLogic` 透過 `port.BlockPosView` 與 `port.Vec3View` 提供，`LightRules` 行為不變。

## Completed

- 刪除 `IDistanceLogic` 與 `VolumeLogic`，移除舊 `BlockPos` 距離邏輯 duplication。
- 保留 `domain.DistanceLogic` 作為 entity blocking range 判斷來源。
- 補強 `StonecutterSourcePolicyTest`，禁止 `src/main/java/net/xalcon/torchmaster/logic` legacy package 回流。

## Remaining Coupling

- `FeralFlareLanternLifecycle` 仍同時協調 Minecraft 世界互動、隨機目標、surface/world-height clamp、line-of-sight 與 planner 規則；後續若繼續拆分，應先抽出純 tick decision/placement candidate policy，再讓 Minecraft adapter 負責世界查詢與放置。
- `LightType` 仍負責把 Minecraft block position 轉成 domain light entry metadata；目前此耦合屬於 Minecraft block adapter 層，不應把 `LightEntry` 建構邏輯複製到 loader roots。
- `ContentRegistration` 仍是 legacy compatibility facade，含 Minecraft registry handle types；不要把 shared content ids 或 metadata 搬回 Fabric/Forge/NeoForge registrar。

## Verification Targets

- Domain rules: `./gradlew :1.21.1-fabric:test --tests net.xalcon.torchmaster.domain.*`
- Policy: `./gradlew :1.21.1-fabric:test --tests net.xalcon.torchmaster.StonecutterSourcePolicyTest`
- Representative matrix: `:1.14.4-fabric:test`, `:1.14.4-forge:test`, `:1.20.6-neoforge:test`, `:1.21.11-fabric:test`
- Finish with `Reset active project`, confirm `1.21.1-fabric`, then `git diff --check`

## Anti-Regression Rules

- 不要恢復 `net.xalcon.torchmaster.logic` package；距離商業規則放在 `domain.DistanceLogic`。
- 不要把 Minecraft `BlockPos`、`Vec3d` 或 loader API 引入 `domain`、`port`。
- 不要新增第二套 range 判斷；所有版本與 loader 應共用同一份 domain distance semantics。
- 下一階段若拆 `FeralFlareLanternLifecycle`，先保持 Minecraft 世界副作用在 adapter/lifecycle 層，純判斷才進 domain。
