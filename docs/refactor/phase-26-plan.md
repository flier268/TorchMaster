# Phase 26 Plan：移除整類 Stonecutter 包覆

## Summary

Phase 26 移除 `package` 後用 Stonecutter closed block 包住整個 `import` 與 `class` 的來源形態。loader-only 類改放到既有 loader source root，Gradle 會讓 active loader source root 先經 Stonecutter 生成後再接回 main compile；Minecraft 版本差異保留在類內的 imports、method signatures、annotations、method bodies 或小型 helper 分支。

## Completed

- Fabric spawn/render mixin 移到 `src/fabric/java/net/xalcon/torchmaster/mixin`，保留原 package、class name 與 mixin JSON 引用。
- Fabric、Forge、NeoForge platform helper 與 registration factory 移到各自 loader source root，保留 service loader implementation FQCN。
- Forge event handlers 與 lifecycle adapter 移到 `src/forge/java`，保留事件行為與 version-specific event API 分支。
- `build.gradle.kts` 建立 active loader source set，並把 `build/generated/stonecutter/<loader>/java` 加進 main source set，避免 raw loader root 直接編譯。
- `MinecraftBlockProperties` 合併為單一固定類別，版本差異集中在 settings factory 方法與 helper 方法內。
- `StonecutterSourcePolicyTest` 新增全 source 掃描，禁止第一個 Stonecutter closed block 包住 type declaration。

## Remaining Coupling

- Fabric mixin 仍直接依賴 Minecraft method descriptors；後續若修改 spawn 行為，必須保留 mixin class name 與 JSON 註冊穩定。
- Loader source roots 仍允許少數必要 Minecraft-version Stonecutter 分支，尤其 registration factory、platform helper、Forge lifecycle、Fabric mixin。
- `MinecraftBlockProperties` 仍是跨 loader/版本 adapter；不要把 block metadata 複製回 loader-specific content registrar。

## Verification Targets

- Representative matrix: `:1.21.1-fabric:test`, `:1.14.4-fabric:test`, `:1.14.4-forge:test`, `:1.20.6-neoforge:test`, `:1.21.11-fabric:test`
- Full active-project check: `./gradlew test`
- Finish with `Reset active project`, confirm `1.21.1-fabric`, then `git diff --check`

## Anti-Regression Rules

- 不要在任何 Java 檔案的 `package` 後用第一個 Stonecutter closed block 包住 type declaration。
- 不要為了 loader-only code 在 `src/main/java` 留空殼或整檔註解；放到 `src/fabric/java`、`src/forge/java`、`src/neoforge/java`。
- 不要用 Gradle source-set 邏輯切 Minecraft 版本檔案；版本差異仍用窄範圍 Stonecutter comments。
