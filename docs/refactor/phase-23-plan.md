# Phase 23 Completion：Light Store Runtime / PersistentState 邊界拆分

## Summary

Phase 23 將 light storage 的共享商業邏輯從 `SavedLightStore` 與 `PersistentState` 適配細節中抽離。所有 light blocking、light 註冊與 light 列表輸出現在都集中在新的 domain runtime，Minecraft storage 層只保留 context 建立、序列化橋接與版本化 `PersistentState` glue。

## Completed

- 新增 `LightStoreRuntime` 與 `LightStoreRuntimeContext`，把 entity spawn block、village siege block、註冊、解除註冊、查詢與 entries 輸出移到共享 domain。
- 簡化 `LightRegistry` 為純資料容器，不再直接組裝 config/filter 規則。
- 將 `SavedLightStore` 改為薄的 `PersistentState + LightStoreBridge` 適配器，內部委派 `LightStoreRuntime`。
- 以 `MinecraftLightStoreRuntimeContext` 取代原本的 `LightStoreConfigView`，集中 Minecraft config 與 runtime filter 到 adapter-local context provider。
- 更新 `SavedLightStoreNbtBridge` 與 `SavedLightStoreStateBridge`，改為讀寫 shared runtime，而不是直接依賴 `SavedLightStore` 內部資料結構。
- 新增 `LightStoreRuntimeTest`，並更新 storage bridge/state 測試與 source policy。

## Remaining Coupling

- `LightStoreBridge` 仍然是 Minecraft-specific 介面，`getLight(...)` 仍暴露 `MinecraftBlockingLight`；下一 phase 可評估是否拆出更乾淨的 project-owned port。
- `SavedLightStoreSerializer` 仍直接依賴 `MinecraftBlockingLight` 與 serializer registry；若要再往下拆，應把 light persistence descriptor 與 serializer lookup 分層。
- `onGlobalTick()` cleanup 仍未抽成 Minecraft-free world port，light store runtime 目前只處理查詢與註冊。
- `MinecraftLightStoreAccess` 仍直接回傳 `LightStoreBridge`；若未來要整合更多 shared runtime，可考慮引入更明確的 runtime accessor/facade。

## Verification Targets

- Representative matrix: `:1.21.1-fabric:test`, `:1.14.4-forge:test`, `:1.20.6-neoforge:test`, `:1.21.11-fabric:test`
- Extra storage regression check: `:1.14.4-fabric:test`
- Full build, `Reset active project`, reset 後 `:1.21.1-fabric:test`, `git diff --check`
- 確認 active project 與 `vcsVersion` 維持 `1.21.1-fabric`

## Anti-Regression Rules

- 不要把 `Services.PLATFORM`、`TorchmasterEntityFilters` 或 `MinecraftConfigView` 拉回 `SavedLightStore`。
- 不要在 `domain` / `port` 新增 Minecraft import 或 Stonecutter 版本條件。
- 不要把 `PersistentState`、NBT、Codec、RegistryWrapper` 細節搬進 `LightStoreRuntime`。
- 不要把 entity/village siege blocking 規則重新塞回 `LightRegistry`。
