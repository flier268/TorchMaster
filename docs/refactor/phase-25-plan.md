# Phase 25 Completion：移除空轉 Light-Store Tick 路徑

## Summary

Phase 25 把已經完全沒有行為的 light-store server tick 鏈路移除，只保留 `SpawnEventBridge.onServerLevelTickEnd(...)` 當作相容入口。這一階段刻意不再往 storage persistence、serializer registry、或 `LightType` 繼續抽象，避免為了乾淨而過度封裝。

## Completed

- 從 `LightStoreBridge` 移除 `onGlobalTick(...)`。
- 從 `SavedLightStore` 移除對應 override 與空的 cleanup TODO，不再保留 fake extension point。
- 從 `MinecraftSpawnBlocker` 移除 `tickStores(MinecraftServer)`，因為它原本只會呼叫空的 store tick。
- 保留 `SpawnEventBridge.onServerLevelTickEnd(MinecraftServer, BooleanSupplier)` 公開簽名，但改成明確的 compatibility no-op，讓 mixin wiring 不需要同步改介面。
- 更新 `MinecraftSpawnBlockerTest`、`BlockingLightLifecycleTest`，移除 fake store 的空 tick 實作需求。
- 補強 `StonecutterSourcePolicyTest`，禁止 `LightStoreBridge` 恢復 global tick hook、禁止 `SpawnEventBridge` 再呼叫 store tick、禁止 `SavedLightStore` 保留 cleanup placeholder。

## Remaining Coupling

- `SpawnEventBridge.onServerLevelTickEnd(...)` 仍保留為 public compatibility facade；若未來確認沒有任何外部相容需求，才適合在獨立 phase 移除。
- `PersistedLightEntry`、`LightSerializerRegistry`、`BlockingLightLifecycle` 與 `LightType` 維持 Phase 24 後的邊界，不再進一步拆分。
- light-store 線目前沒有已知高價值重構點；除非未來出現實際功能需求，否則應優先轉向其他耦合面。

## Verification Targets

- Representative matrix: `:1.21.1-fabric:test`, `:1.14.4-forge:test`, `:1.20.6-neoforge:test`, `:1.21.11-fabric:test`
- Extra regression check: `:1.14.4-fabric:test`
- Full build, `Reset active project`, reset 後 `:1.21.1-fabric:test`, `git diff --check`
- 確認 active project 與 `vcsVersion` 維持 `1.21.1-fabric`

## Anti-Regression Rules

- 不要把空的 light-store global tick API 加回 `LightStoreBridge`。
- 不要讓 `SpawnEventBridge.onServerLevelTickEnd(...)` 再回頭連到 storage ticking。
- 不要在 `SavedLightStore` 留下沒有實作的 cleanup placeholder 或 TODO 入口。
- 這條線先停止重構；後續若再動，必須有實際功能需求或明確的呼叫面證據。
