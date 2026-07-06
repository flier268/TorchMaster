# Phase 24 Completion：Light Store Bridge 收窄 + Persistence 介面抽離

## Summary

Phase 24 進一步縮小 storage adapter 的公開面，清掉已經沒有正式呼叫點的 dead code，並把 persistence serializer 從 `MinecraftBlockingLight` 直接依賴改成較窄的 `PersistedLightEntry`。這讓 `LightStoreBridge` 不再暴露 Minecraft light 型別，而 storage serializer 也不需要再知道 minecraft light package。

## Completed

- 新增 `PersistedLightEntry`，讓 storage persistence 依賴集中在 storage 契約，而不是 `minecraft.light` 套件。
- 讓 `MinecraftBlockingLight` 改為實作 `PersistedLightEntry`，保留既有 light 行為不變。
- 將 `LightNbtSerializer` 的輸入/輸出改成 `PersistedLightEntry`。
- 更新 `MegatorchSerializer`、`DreadLampSerializer` 與所有 storage 測試 serializer，移除對 `MinecraftBlockingLight` 的直接方法簽名依賴。
- 收窄 `LightStoreBridge`：
  - `registerLight(...)` 改吃 `LightEntry`
  - 移除未被正式程式碼使用的 `getLight(...)`
- 更新 `SavedLightStore` 以符合新 bridge 契約，死碼不保留。
- 更新 `SavedLightStoreSerializer` 改用 `PersistedLightEntry`，不再直接 import `MinecraftBlockingLight`。
- 補強 `StonecutterSourcePolicyTest`，禁止 `LightStoreBridge` 暴露 `MinecraftBlockingLight`，也禁止 storage serializer 再 import 它。

## Remaining Coupling

- `PersistedLightEntry` 仍位於 `minecraft.storage`，代表 persistence 契約還屬於 Minecraft adapter 層，尚未抽成更一般的 project-owned persistence port。
- `LightSerializerRegistry` 仍是全域靜態 registry；若未來要進一步降低耦合，可考慮把 serializer lookup 改成明確 service/provider。
- `BlockingLightLifecycle` 仍直接持有 `MinecraftBlockingLight` 建立流程，因為 `LightType` 仍在 Minecraft block/content 邊界內；這部分目前屬合理 adapter 耦合。

## Verification Targets

- Representative matrix: `:1.21.1-fabric:test`, `:1.14.4-forge:test`, `:1.20.6-neoforge:test`, `:1.21.11-fabric:test`
- Extra regression check: `:1.14.4-fabric:test`
- Full build, `Reset active project`, reset 後 `:1.21.1-fabric:test`, `git diff --check`
- 確認 active project 與 `vcsVersion` 維持 `1.21.1-fabric`

## Anti-Regression Rules

- 不要把 `MinecraftBlockingLight` 再加回 `LightStoreBridge` 或 `SavedLightStoreSerializer` 的公開/直接依賴。
- 不要恢復 `getLight(...)` 這種沒有正式呼叫面的 storage bridge API。
- 不要把 serializer key lookup 或 NBT 細節推回 domain runtime。
- 若新增新的 persisted light 類型，先實作 `PersistedLightEntry` 與對應 serializer，再接入 storage registry。
