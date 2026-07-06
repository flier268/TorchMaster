# Phase 28 Completion：抽出 Feral Flare Tick Decision

## Summary

Phase 28 將 Feral Flare lantern 的純 tick decision 與 child-light check index 規則移到 `domain.FeralFlareLightPlanner`。Minecraft lifecycle 仍負責世界查詢、隨機目標、surface/world-height clamp、line-of-sight、方塊放置與 child-light 實體清理，不改 NBT 格式或 block entity 對外回傳契約。

## Completed

- 新增 `FeralFlareLightPlanner.beginTick(...)` 與 `nextCheckIndex(...)`，集中 server/client tick、tick-rate、hardcap overflow、check-index wrap 規則。
- `FeralFlareLanternLifecycle.tick(...)` 改為使用 domain planner 的 tick decision，再轉回既有 `TickOutcome`。
- 將 begin-tick 與 check-index 測試移到 `FeralFlareLightPlannerTest`，保留 Minecraft lifecycle 測試只覆蓋 `BlockPos` 轉接與 tracked child removal。
- 補強 `StonecutterSourcePolicyTest`，禁止 `FeralFlareLanternLifecycle` 重新宣告 `beginTick` 或 `nextCheckIndex` 規則。

## Remaining Coupling

- `FeralFlareLanternLifecycle` 仍混合 Minecraft 世界操作與 placement orchestration；下一階段可考慮抽出 target candidate/clamp policy 的 port-friendly value model，但不要把 raycast 或 `World` side effects 移進 domain。
- `canPlaceLight(...)` 仍在 lifecycle 讀取 Minecraft air state 與 block light level，再呼叫 domain placement rule；這是可接受的 adapter boundary，直到有更完整的 world-query port。
- `FeralFlareLanternBlockEntity` 仍直接從 `Services.PLATFORM` 取得 config 並呼叫 lifecycle；若後續處理 config/runtime wiring，應獨立成另一 phase。

## Verification Targets

- Domain planner: `./gradlew :1.21.1-fabric:test --tests net.xalcon.torchmaster.domain.FeralFlareLightPlannerTest`
- Lifecycle/policy: `./gradlew :1.21.1-fabric:test --tests net.xalcon.torchmaster.minecraft.light.feralflare.FeralFlareLanternLifecycleTest --tests net.xalcon.torchmaster.StonecutterSourcePolicyTest`
- Representative matrix: `:1.14.4-fabric:test`, `:1.14.4-forge:test`, `:1.20.6-neoforge:test`, `:1.21.11-fabric:test`
- Finish with `Reset active project`, confirm `1.21.1-fabric`, then `git diff --check`

## Anti-Regression Rules

- 不要把 `beginTick` 或 check-index wrap 規則放回 Minecraft lifecycle；純 tick decision 屬於 domain planner。
- 不要讓 `domain` 或 `port` 匯入 Minecraft、Fabric、Forge、NeoForge 或 Stonecutter conditionals。
- 不要改 Feral Flare child-light NBT encoding、tracked light list semantics、或 existing `TickOutcome` contract。
- 下一階段拆 placement 時，Minecraft world state queries 和 side effects 必須留在 adapter/lifecycle 層。
