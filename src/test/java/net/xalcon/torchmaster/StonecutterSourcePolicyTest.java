package net.xalcon.torchmaster;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StonecutterSourcePolicyTest
{
    private static final Pattern VERSION_CONDITION = Pattern.compile("//\\?\\s*(if|elif|else if)\\b.*(?:[<>]=?|=)\\s*1\\.");
    private static final Pattern PLATFORM_IMPORT = Pattern.compile(
            "^import\\s+(net\\.minecraft|net\\.fabricmc|net\\.minecraftforge|net\\.neoforged)\\.");
    private static final Pattern STONECUTTER_CONDITION = Pattern.compile("//\\?\\s*(if|elif|else if|else)\\b");
    private static final Pattern MIXIN_FORBIDDEN_SPAWN_IMPORT = Pattern.compile(
            "^import\\s+net\\.xalcon\\.torchmaster\\.(domain\\.SpawnBlockingRules|minecraft\\.storage\\.|minecraft\\.adapter\\.MinecraftSpawnBlocker)");
    private static final Pattern CLIENT_RUNTIME_DETAIL = Pattern.compile(
            "Torchmaster(LightRangeDisplay|LightRangeRenderer|LightScreen|LightScreenPresenter|ClientLifecycle|ClientEventAdapter|ScreenCompat|ScreenRenderAdapter|ScreenRenderPlan|RangeBoxes|PanelRenderer|LineBoxRenderer|RangeRenderPlan|RangeLineSubmitter|LatestLineSubmitter|WorldRendererLineSubmitter|LegacyLineSubmitter|RangeRenderSession|RangeRenderTarget|LatestRangeRenderTarget|VanillaRangeRenderTarget|LegacyRangeRenderTarget|ConfigScreenLayout|ConfigEntries|ConfigWidgetRows|ConfigWidgetAdapter|ConfigScreenActions|ConfigScreenPresenter|ConfigRuntimeAccess)");
    private static final Pattern STORAGE_RUNTIME_DETAIL = Pattern.compile(
            "(SavedLightStore|SavedLightStoreStateFactory|SavedLightStoreStateBridge|SavedLightStoreNbtBridge|MinecraftLightStoreAccess|LightStoreBridge|MinecraftLightStoreRuntimeContext|PersistedLightEntry)");
    private static final Pattern RUNTIME_REGISTRY_FACADE = Pattern.compile("getRegistryForLevel\\s*\\(");
    private static final Pattern RUNTIME_FILTER_GLOBAL = Pattern.compile("TorchmasterRuntime\\.(MegaTorchFilterRegistry|DreadLampFilterRegistry)");
    private static final Pattern RUNTIME_FILTER_VALIDATION = Pattern.compile("EntityFilterList::IsValidFilterString|EntityFilterList\\.IsValidFilterString");
    private static final Pattern FILTER_RUNTIME_DETAIL = Pattern.compile("TorchmasterEntityFilters|TorchmasterEntityFilterRuntime|EntityFilterOverrideRules");
    private static final Pattern SCREEN_PANEL_RAW_FILL_COLOR = Pattern.compile("0xAA101010|0xFF404040|0xFF202020");
    private static final Pattern CONFIG_SCREEN_RAW_SAVE_COLLECTION = Pattern.compile("List<\\s*(Integer|Boolean|List<String>)\\s*>\\s+\\w+\\s*=\\s*new\\s+ArrayList|fromEntries\\s*\\(");
    private static final Pattern RANGE_RENDERER_DIRECT_SNAPSHOT_LOOP = Pattern.compile("for\\s*\\([^)]*RangeSnapshot[^)]*:\\s*TorchmasterLightRangeDisplay\\.snapshots");
    private static final Pattern CONFIG_SCREEN_DIRECT_WIDGET_STATE = Pattern.compile("setWidget[XY]\\s*\\(|\\.visible\\s*=|\\.active\\s*=");
    private static final Pattern RANGE_RENDERER_DIRECT_SESSION_DETAIL = Pattern.compile("TorchmasterLightRangeDisplay\\.snapshots\\s*\\(|RenderLayer\\.getLines\\s*\\(|WorldRenderer\\.drawBox\\s*\\(|\\.lineWidth\\s*\\(");
    private static final Pattern STORAGE_SERIALIZER_RUNTIME_LOGGER = Pattern.compile("TorchmasterRuntime\\.LOG");
    private static final Pattern SCREEN_DIRECT_TEXT_DRAW = Pattern.compile("drawCenteredText|drawCenteredString|drawText\\s*\\(|drawString\\s*\\(");
    private static final Pattern RANGE_SESSION_RENDER_TARGET_DETAIL = Pattern.compile("RenderLayer\\.getLines\\s*\\(|LINE_LAYER|bufferSource\\.draw\\s*\\(|RenderPipelines|RenderSetup|GlStateManager\\.");
    private static final Pattern SAVED_LIGHT_STORE_DIRECT_SERIALIZER = Pattern.compile("SavedLightStoreSerializer\\.");
    private static final Pattern SCREEN_DIRECT_RENDER_COMPOSITION = Pattern.compile("TorchmasterScreenRenderAdapter\\.(centered|label|frame)\\s*\\(");
    private static final Pattern LINE_SUBMITTER_DIRECT_API = Pattern.compile("WorldRenderer\\.drawBox\\s*\\(|\\.lineWidth\\s*\\(|buffer\\.vertex\\s*\\(");
    private static final Pattern STORAGE_FACTORY_OLD_STATE_FACADE = Pattern.compile("\\.(saveInto|loadFrom)\\s*\\(");
    private static final Pattern RUNTIME_FILTER_GLOBAL_FIELD = Pattern.compile("\\b(MegaTorchFilterRegistry|DreadLampFilterRegistry)\\b");
    private static final Pattern CLIENT_ENTRYPOINT_DIRECT_EVENT_DECISION = Pattern.compile("event\\.phase\\s*!=|event\\.getStage\\s*\\(\\)\\s*!=|TickEvent\\.Phase\\.END|Stage\\.AFTER_TRANSLUCENT_BLOCKS|new\\s+MatrixStack\\s*\\(|multiplyPositionMatrix\\s*\\(");
    private static final Pattern CONFIG_SCREEN_DIRECT_BOTTOM_BUTTON_GEOMETRY = Pattern.compile("bottomButtonWidth\\s*\\(|buttonGap|totalButtonWidth|buttonX\\s*=|button\\([^\\n]*(screen\\.torchmaster\\.config\\.(save|reset)|gui\\.done)");
    private static final Pattern STORAGE_STATE_OLD_BRIDGE_NAMES = Pattern.compile("\\b(writeState|readState)\\s*\\(");
    private static final Pattern LOADER_GATED_FILE_START = Pattern.compile("//\\?\\s*(if|elif|else if)\\b.*\\b(fabric|forge|neoforge)\\b.*\\{");
    private static final Pattern STONECUTTER_CLOSED_BLOCK_START = Pattern.compile("//\\?\\s*(if|elif|else if)\\b.*\\{\\s*");
    private static final Pattern STONECUTTER_BLOCK_CLOSE = Pattern.compile("(?:\\*/)?//\\?\\}|\\*///\\?\\}");
    private static final Pattern TYPE_DECLARATION = Pattern.compile("\\b(class|interface|enum)\\s+\\w+");
    private static final Pattern LOADER_CLIENT_ENTRYPOINT_LIFECYCLE_IMPORT = Pattern.compile("TorchmasterClientLifecycle");
    private static final Pattern CONFIG_SCREEN_DIRECT_ACTION_OR_SAVE = Pattern.compile("private\\s+void\\s+(save|reset)\\s*\\(|setStatus\\s*\\(|TorchmasterTomlConfig|TorchmasterConfigEntries\\.collector\\s*\\(|TorchmasterRuntime\\.onWorldLoaded\\s*\\(");
    private static final Pattern STORAGE_FACTORY_DIRECT_STATE_GLUE = Pattern.compile("new\\s+SavedLightStore\\s*\\(|SavedLightStore::new|SavedLightStoreStateBridge\\.read\\s*\\(");
    private static final Pattern SPAWN_BRIDGE_DIRECT_RUNTIME_GLUE = Pattern.compile("Services\\.PLATFORM\\.getConfig\\s*\\(|TorchmasterRuntime\\.LOG|new\\s+MinecraftConfigView\\s*\\(");
    private static final Pattern RANGE_TARGET_DIRECT_BACKEND_DECISION = Pattern.compile("new\\s+(CameraOffset|LegacySessionState)\\s*\\(|TorchmasterLineBoxRenderer\\.LINE_WIDTH");
    private static final Pattern STORAGE_OVERRIDE_DIRECT_NBT_BRIDGE = Pattern.compile("SavedLightStore(NbtBridge|Serializer)\\.");
    private static final Pattern FABRIC_WRAPPER_DIRECT_EVENT_CONTAINER = Pattern.compile("EventResultContainer|new\\s+EventResultContainer\\s*\\(|\\.getResult\\s*\\(");
    private static final Pattern SPAWN_BRIDGE_DIRECT_CONTAINER_RESULT = Pattern.compile("\\.getResult\\s*\\(");
    private static final Pattern UTILS_SPAWN_HOOK_IMPLEMENTATION = Pattern.compile("SpawnEventBridge|MinecraftSpawnEventContainers|MinecraftSpawnBlocker");
    private static final Pattern LOADER_HANDLER_DIRECT_RESULT_MAPPING = Pattern.compile("EventResult\\.|switch\\s*\\(");
    private static final Pattern SPAWN_RUNTIME_DIRECT_PLATFORM_GLUE = Pattern.compile(
            "^import\\s+net\\.xalcon\\.torchmaster\\.(TorchmasterRuntime|platform\\.Services)|new\\s+MinecraftConfigView\\s*\\(");
    private static final Pattern SPAWN_BRIDGE_DIRECT_CONTEXT_BUILD = Pattern.compile("MinecraftSpawnEventContext\\.(entity|phantom|villageSiege)\\s*\\(|MinecraftAdapterViews\\.(entityTypeKey|vec3)\\s*\\(");
    private static final Pattern RANGE_TARGET_DIRECT_LEGACY_API = Pattern.compile("GlStateManager\\.|Tessellator\\.getInstance\\s*\\(|VertexFormats\\.POSITION_COLOR|buffer\\.begin\\s*\\(");
    private static final Pattern CONFIG_CONTROLLER_DIRECT_RUNTIME = Pattern.compile("^import\\s+net\\.xalcon\\.torchmaster\\.TorchmasterRuntime|TorchmasterRuntime\\.");
    private static final Pattern SPAWN_BRIDGE_DIRECT_HOOK_BODY = Pattern.compile("MinecraftSpawnEvent(ContextFactory|Runtime)|shouldBlock(Entity|VillageSiege)\\s*\\(");
    private static final Pattern FABRIC_SPAWN_HOOK_DIRECT_BRIDGE = Pattern.compile("SpawnEventBridge");
    private static final Pattern RANGE_TARGET_DIRECT_LINE_LAYER_API = Pattern.compile("RenderLayer\\.getLines\\s*\\(|LINE_LAYER|RenderPipelines|RenderSetup|bufferSource\\.draw\\s*\\(");
    private static final Pattern CONFIG_ROWS_DIRECT_WIDGET_STATE = Pattern.compile("TorchmasterScreenCompat\\.setWidget|\\.setMessage\\s*\\(");
    private static final Pattern SAVED_LIGHT_STORE_DIRECT_PLATFORM_GLUE = Pattern.compile("Services\\.PLATFORM|TorchmasterEntityFilters|new\\s+MinecraftConfigView\\s*\\(");
    private static final Pattern LIGHT_STORE_BRIDGE_DIRECT_MINECRAFT_LIGHT = Pattern.compile("MinecraftBlockingLight");
    private static final Pattern STORAGE_SERIALIZER_DIRECT_MINECRAFT_LIGHT = Pattern.compile("^import\\s+net\\.xalcon\\.torchmaster\\.minecraft\\.light\\.MinecraftBlockingLight");
    private static final Pattern LIGHT_STORE_BRIDGE_DIRECT_GLOBAL_TICK = Pattern.compile("onGlobalTick\\s*\\(");
    private static final Pattern SPAWN_BRIDGE_DIRECT_STORE_TICK = Pattern.compile("tickStores\\s*\\(");
    private static final Pattern SAVED_LIGHT_STORE_DIRECT_CLEANUP_PLACEHOLDER = Pattern.compile("Rate limit cleanup|cleanup has a Minecraft-free world port|onGlobalTick\\s*\\(");
    private static final Pattern FERAL_FLARE_LIFECYCLE_DIRECT_TICK_DECISION = Pattern.compile("static\\s+(?:TickDecision|int)\\s+(beginTick|nextCheckIndex)\\s*\\(");

    @Test
    void loaderSourceRootsDoNotContainMinecraftVersionConditions() throws IOException
    {
        List<Path> violations = javaFilesIn("src/fabric", "src/forge", "src/neoforge")
                .filter(path -> !isAllowedLoaderRootVersionCondition(path))
                .filter(StonecutterSourcePolicyTest::hasMinecraftVersionCondition)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Move Minecraft-version Stonecutter branches to shared adapter/content code unless they are loader lifecycle adapters: " + violations);
    }

    @Test
    void domainAndPortRemainPlatformFree() throws IOException
    {
        List<Path> violations = javaFilesIn(
                "src/main/java/net/xalcon/torchmaster/domain",
                "src/main/java/net/xalcon/torchmaster/port")
                .filter(StonecutterSourcePolicyTest::hasPlatformImport)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep domain and port packages Minecraft/loader-free: " + violations);
    }

    @Test
    void domainAndPortRemainStonecutterFree() throws IOException
    {
        List<Path> violations = javaFilesIn(
                "src/main/java/net/xalcon/torchmaster/domain",
                "src/main/java/net/xalcon/torchmaster/port")
                .filter(StonecutterSourcePolicyTest::hasStonecutterCondition)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep domain and port version/loader branch-free: " + violations);
    }

    @Test
    void legacyDistanceLogicPackageStaysRemoved()
    {
        Path path = Paths.get("src/main/java/net/xalcon/torchmaster/logic");

        assertTrue(!Files.exists(path), () -> "Use domain.DistanceLogic with port views instead of restoring Minecraft BlockPos distance logic: " + path);
    }

    @Test
    void mainClientEntrypointsStayOnlyWhenStonecutterProcessed() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterFabricClient.java"),
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterNeoforgeClient.java"))
                .stream()
                .filter(Files::exists)
                .filter(path -> !hasMinecraftVersionCondition(path))
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Move branch-free client entrypoints to loader roots; src/main client entrypoints must need Stonecutter processing: " + violations);
    }

    @Test
    void movedLoaderEntrypointsDoNotReturnToMainSource()
    {
        List<Path> violations = Arrays.asList(
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterFabricClient.java"),
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterForge.java"),
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterForgeClient.java"),
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterNeoforgeClient.java"))
                .stream()
                .filter(Files::exists)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep moved loader entrypoints in src/[loader]/java and keep version APIs in shared adapters: " + violations);
    }

    @Test
    void mixinsDoNotImportSpawnBusinessOrStorageInternals() throws IOException
    {
        List<Path> violations = javaFilesIn("src/main/java/net/xalcon/torchmaster/mixin")
                .filter(StonecutterSourcePolicyTest::hasForbiddenSpawnImport)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Mixin classes should delegate through wrapper/adapter helpers instead of importing spawn rules, storage, or spawn blocker internals: " + violations);
    }

    @Test
    void loaderRootsDoNotDuplicateClientRuntimeDetails() throws IOException
    {
        List<Path> violations = javaFilesIn("src/fabric", "src/forge", "src/neoforge")
                .filter(path -> !isAllowedLoaderEntrypointAdapter(path))
                .filter(path -> hasClientRuntimeDetail(path) || hasStorageRuntimeDetail(path) || hasFilterRuntimeDetail(path))
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep client/storage runtime details in shared helpers; loader roots should only wire lifecycle entrypoints: " + violations);
    }

    @Test
    void configValidationDoesNotDependOnRuntimeFilterHolder() throws IOException
    {
        List<Path> violations = javaFilesIn("src/main/java/net/xalcon/torchmaster/config", "src/main/java/net/xalcon/torchmaster/client")
                .filter(StonecutterSourcePolicyTest::hasRuntimeFilterValidation)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use domain EntityFilterOverrideRules for config validation instead of EntityFilterList runtime holder: " + violations);
    }

    @Test
    void runtimeRegistryFacadeHasNoCallSites() throws IOException
    {
        List<Path> violations = javaFilesIn("src/main/java")
                .filter(path -> !path.endsWith(Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterRuntime.java")))
                .filter(StonecutterSourcePolicyTest::hasRuntimeRegistryFacadeCall)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use MinecraftLightStoreAccess directly instead of TorchmasterRuntime.getRegistryForLevel: " + violations);
    }

    @Test
    void runtimeFilterGlobalsHaveNoNewCallSites() throws IOException
    {
        List<Path> violations = javaFilesIn("src/main/java")
                .filter(path -> !path.endsWith(Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterRuntime.java")))
                .filter(StonecutterSourcePolicyTest::hasRuntimeFilterGlobalCall)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use TorchmasterEntityFilters instead of TorchmasterRuntime filter globals: " + violations);
    }

    @Test
    void screensDoNotHandRollPanelFillColors() throws IOException
    {
        List<Path> violations = javaFilesIn("src/main/java/net/xalcon/torchmaster/client")
                .filter(path -> path.endsWith(Paths.get("TorchmasterLightScreen.java")) || path.endsWith(Paths.get("TorchmasterConfigScreen.java")))
                .filter(StonecutterSourcePolicyTest::hasRawPanelFillColor)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use TorchmasterPanelRenderer for panel background/frame fills: " + violations);
    }

    @Test
    void configScreenDoesNotCollectRawSaveOrderLists() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterConfigScreen.java");

        assertTrue(!hasConfigScreenRawSaveCollection(path), () -> "Use TorchmasterConfigEntries collector instead of raw save-order lists in config screen: " + path);
    }

    @Test
    void rangeRendererDoesNotLoopSnapshotsDirectly() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterLightRangeRenderer.java");

        assertTrue(!hasRangeRendererDirectSnapshotLoop(path), () -> "Use TorchmasterRangeRenderPlan instead of hand-written snapshot loops: " + path);
    }

    @Test
    void configScreenDoesNotSetWidgetStateDirectly() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterConfigScreen.java");

        assertTrue(!hasConfigScreenDirectWidgetState(path), () -> "Use TorchmasterConfigWidgetRows/TorchmasterScreenCompat for widget position and visibility state: " + path);
    }

    @Test
    void rangeRendererDelegatesSessionDetails() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterLightRangeRenderer.java");

        assertTrue(!hasRangeRendererDirectSessionDetail(path), () -> "Use TorchmasterRangeRenderSession and line submitter helpers for render session details: " + path);
    }

    @Test
    void storageSerializerDoesNotUseRuntimeLoggerFacade() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStoreSerializer.java");

        assertTrue(!hasStorageSerializerRuntimeLogger(path), () -> "Use storage-local logging instead of TorchmasterRuntime.LOG in storage serializer: " + path);
    }

    @Test
    void screensDelegateTextDrawingToRenderAdapter() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterLightScreen.java"),
                        sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterConfigScreen.java"))
                .stream()
                .filter(StonecutterSourcePolicyTest::hasDirectScreenTextDraw)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use TorchmasterScreenRenderAdapter for centered/text labels in screen classes: " + violations);
    }

    @Test
    void rangeRenderSessionDelegatesRenderTargetDetails() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterRangeRenderSession.java");

        assertTrue(!hasRangeSessionRenderTargetDetail(path), () -> "Use TorchmasterRangeRenderTarget for render layer, flush, and legacy GL target details: " + path);
    }

    @Test
    void savedLightStoreDelegatesNbtSerialization() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStore.java");

        assertTrue(!hasSavedLightStoreDirectSerializer(path), () -> "Use SavedLightStoreNbtBridge from PersistentState signatures instead of direct serializer calls: " + path);
    }

    @Test
    void screensDelegateRenderCompositionToPresenters() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterLightScreen.java"),
                        sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterConfigScreen.java"))
                .stream()
                .filter(StonecutterSourcePolicyTest::hasScreenDirectRenderComposition)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use screen presenters and render plans instead of hand-written label/frame composition in screen classes: " + violations);
    }

    @Test
    void rangeLineSubmitterStaysThinCoordinator() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterRangeLineSubmitter.java");

        assertTrue(!hasLineSubmitterDirectApi(path), () -> "Keep direct line submission API in latest/world-renderer/legacy helpers, not the coordinator: " + path);
    }

    @Test
    void savedLightStoreStateFactoryDoesNotUseOldStateFacadeNames() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStoreStateFactory.java");

        assertTrue(!hasStorageFactoryOldStateFacade(path), () -> "Use writeState/readState bridge names from state factory instead of old saveInto/loadFrom facade calls: " + path);
    }

    @Test
    void runtimeDoesNotExposeFilterGlobalFacades() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/TorchmasterRuntime.java");

        assertTrue(!hasRuntimeFilterGlobalField(path), () -> "Use TorchmasterEntityFilters accessors instead of TorchmasterRuntime filter global fields: " + path);
    }

    @Test
    void clientEntrypointsDelegateEventDecisions() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        sourcePath("src/fabric/java/net/xalcon/torchmaster/TorchmasterFabricClient.java"),
                        sourcePath("src/forge/java/net/xalcon/torchmaster/TorchmasterForgeClient.java"),
                        sourcePath("src/neoforge/java/net/xalcon/torchmaster/TorchmasterNeoforgeClient.java"))
                .stream()
                .filter(Files::exists)
                .filter(StonecutterSourcePolicyTest::hasClientEntrypointDirectEventDecision)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Delegate client event phase/stage and pose conversion decisions to TorchmasterClientEventAdapter: " + violations);
    }

    @Test
    void loaderClientEntrypointsDoNotImportClientLifecycle() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        sourcePath("src/forge/java/net/xalcon/torchmaster/TorchmasterForgeClient.java"),
                        sourcePath("src/neoforge/java/net/xalcon/torchmaster/TorchmasterNeoforgeClient.java"))
                .stream()
                .filter(Files::exists)
                .filter(StonecutterSourcePolicyTest::hasLoaderClientEntrypointLifecycleImport)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Loader client entrypoints should initialize through TorchmasterClientEventAdapter, not TorchmasterClientLifecycle: " + violations);
    }

    @Test
    void configScreenDelegatesBottomButtonGeometry() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterConfigScreen.java");

        assertTrue(!hasConfigScreenDirectBottomButtonGeometry(path), () -> "Use TorchmasterConfigScreenActions for bottom button keys/order/geometry: " + path);
    }

    @Test
    void storageUsesStateBridgeInsteadOfOldStateMethods() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStore.java"),
                        sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStoreStateFactory.java"))
                .stream()
                .filter(StonecutterSourcePolicyTest::hasStorageStateOldBridgeNames)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use SavedLightStoreStateBridge instead of writeState/readState methods: " + violations);
    }

    @Test
    void configScreenDelegatesActionsAndSaveToController() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterConfigScreen.java");

        assertTrue(!hasConfigScreenDirectActionOrSave(path), () -> "Use TorchmasterConfigScreenController for config save/reset/status orchestration: " + path);
    }

    @Test
    void savedLightStoreStateFactoryDelegatesStateGlueToBridge() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStoreStateFactory.java");

        assertTrue(!hasStorageFactoryDirectStateGlue(path), () -> "Use SavedLightStoreStateBridge for state create/load glue from the factory: " + path);
    }

    @Test
    void spawnEventBridgeDelegatesRuntimeGlue() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/events/SpawnEventBridge.java");

        assertTrue(!hasSpawnBridgeDirectRuntimeGlue(path), () -> "Use MinecraftSpawnEventRuntime for config, config view, and logging glue: " + path);
    }

    @Test
    void rangeRenderTargetDelegatesBackendDecisions() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterRangeRenderTarget.java");

        assertTrue(!hasRangeTargetDirectBackendDecision(path), () -> "Use TorchmasterRangeRenderBackend for camera offset, legacy state, and line width decisions: " + path);
    }

    @Test
    void savedLightStoreOverridesDoNotCallNbtBridgeDirectly() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStore.java");

        assertTrue(!hasStorageOverrideDirectNbtBridge(path), () -> "Use SavedLightStoreStateBridge from PersistentState overrides, not NBT bridge or serializer directly: " + path);
    }

    @Test
    void savedLightStoreDelegatesPlatformGlueToRuntimeContextProvider() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStore.java");

        assertTrue(!hasSavedLightStoreDirectPlatformGlue(path), () -> "Keep Services.PLATFORM, TorchmasterEntityFilters, and MinecraftConfigView inside MinecraftLightStoreRuntimeContext: " + path);
    }

    @Test
    void lightStoreBridgeDoesNotExposeMinecraftLightType() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/LightStoreBridge.java");

        assertTrue(!hasLightStoreBridgeDirectMinecraftLight(path), () -> "Keep MinecraftBlockingLight out of LightStoreBridge; expose project-owned LightEntry only: " + path);
    }

    @Test
    void storageSerializerDoesNotImportMinecraftLightType() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStoreSerializer.java");

        assertTrue(!hasStorageSerializerDirectMinecraftLight(path), () -> "Use PersistedLightEntry in storage serializer instead of importing MinecraftBlockingLight: " + path);
    }

    @Test
    void lightStoreBridgeDoesNotRestoreGlobalTickHook() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/LightStoreBridge.java");

        assertTrue(!hasLightStoreBridgeDirectGlobalTick(path), () -> "Do not restore empty light-store global tick hooks to LightStoreBridge: " + path);
    }

    @Test
    void spawnEventBridgeDoesNotCallStoreTickBridge() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/events/SpawnEventBridge.java");

        assertTrue(!hasSpawnBridgeDirectStoreTick(path), () -> "Keep SpawnEventBridge.onServerLevelTickEnd as a compatibility no-op; do not route back into store ticking: " + path);
    }

    @Test
    void savedLightStoreDoesNotKeepCleanupPlaceholder() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/storage/SavedLightStore.java");

        assertTrue(!hasSavedLightStoreDirectCleanupPlaceholder(path), () -> "Remove empty cleanup placeholders from SavedLightStore instead of keeping fake extension points: " + path);
    }

    @Test
    void fabricWrappersDelegateEventContainers() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        sourcePath("src/fabric/java/net/xalcon/torchmaster/mixin/VillageSiegeMixin.java"))
                .stream()
                .filter(Files::exists)
                .filter(StonecutterSourcePolicyTest::hasFabricWrapperDirectEventContainer)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use MinecraftSpawnEventContainers from Fabric wrappers/mixins instead of direct EventResultContainer handling: " + violations);
    }

    @Test
    void spawnEventBridgeDoesNotReadContainerResultDirectly() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/events/SpawnEventBridge.java");

        assertTrue(!hasSpawnBridgeDirectContainerResult(path), () -> "Use MinecraftSpawnEventContainers.portResult from SpawnEventBridge instead of direct container.getResult(): " + path);
    }

    @Test
    void spawnEventBridgeDelegatesContextConstruction() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/events/SpawnEventBridge.java");

        assertTrue(!hasSpawnBridgeDirectContextBuild(path), () -> "Use MinecraftSpawnEventContextFactory from SpawnEventBridge instead of hand-built context conversion: " + path);
    }

    @Test
    void rangeRenderTargetDelegatesLegacyApi() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterRangeRenderTarget.java");

        assertTrue(!hasRangeTargetDirectLegacyApi(path), () -> "Use TorchmasterLegacyRangeRenderTarget for legacy GL/Tessellator setup: " + path);
    }

    @Test
    void configScreenControllerDelegatesRuntimeAccess() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterConfigScreenController.java");

        assertTrue(!hasConfigControllerDirectRuntime(path), () -> "Use TorchmasterConfigRuntimeAccess instead of TorchmasterRuntime from config screen controller: " + path);
    }

    @Test
    void spawnEventBridgeStaysCompatibilityFacade() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/events/SpawnEventBridge.java");

        assertTrue(!hasSpawnBridgeDirectHookBody(path), () -> "Keep SpawnEventBridge as a compatibility facade over MinecraftSpawnEventHooks: " + path);
    }

    @Test
    void fabricSpawnHooksDoNotRouteThroughBroadBridge() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/spawn/FabricSpawnEventHooks.java");

        assertTrue(!hasFabricSpawnHookDirectBridge(path), () -> "Fabric spawn hooks should call MinecraftSpawnEventHooks directly, not broad SpawnEventBridge: " + path);
    }

    @Test
    void rangeRenderTargetDelegatesLineLayerApi() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterRangeRenderTarget.java");

        assertTrue(!hasRangeTargetDirectLineLayerApi(path), () -> "Use latest/vanilla range render targets for line layer and flush API details: " + path);
    }

    @Test
    void configWidgetRowsDelegateMinecraftWidgetState() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/client/TorchmasterConfigWidgetRows.java");

        assertTrue(!hasConfigRowsDirectWidgetState(path), () -> "Use TorchmasterConfigWidgetAdapter for Minecraft widget position, visibility, and message state: " + path);
    }

    @Test
    void utilsSpawnWrappersStayThinFacades() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        sourcePath("src/main/java/net/xalcon/torchmaster/utils/MobWrapper.java"),
                        sourcePath("src/main/java/net/xalcon/torchmaster/utils/NaturalSpawnerWrapper.java"))
                .stream()
                .filter(Files::exists)
                .filter(StonecutterSourcePolicyTest::hasUtilsSpawnHookImplementation)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep Fabric spawn hook implementation in minecraft.spawn helpers, not utils wrapper facades: " + violations);
    }

    @Test
    void oldFabricSpawnWrappersStayRemoved()
    {
        List<Path> violations = Arrays.asList(
                        Paths.get("src/main/java/net/xalcon/torchmaster/utils/MobWrapper.java"),
                        Paths.get("src/main/java/net/xalcon/torchmaster/utils/NaturalSpawnerWrapper.java"))
                .stream()
                .filter(Files::exists)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Use FabricSpawnEventHooks directly from Fabric mixins instead of restoring old wrapper facades: " + violations);
    }

    @Test
    void loaderSpawnHandlersDelegateResultMapping() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        sourcePath("src/neoforge/java/net/xalcon/torchmaster/NeoforgeEventHandler.java"),
                        sourcePath("src/main/java/net/xalcon/torchmaster/ForgeEventHandler.java"),
                        sourcePath("src/main/java/net/xalcon/torchmaster/ForgeVillageEventHandler.java"))
                .stream()
                .filter(Files::exists)
                .filter(StonecutterSourcePolicyTest::hasLoaderHandlerDirectResultMapping)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep loader handlers thin; put event result conversion in loader-local adapter helpers: " + violations);
    }

    @Test
    void spawnRuntimeDelegatesPlatformGlue() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/spawn/MinecraftSpawnEventRuntime.java");

        assertTrue(!hasSpawnRuntimeDirectPlatformGlue(path), () -> "Use MinecraftSpawnRuntimeServices for config view and logging glue: " + path);
    }

    @Test
    void neoforgeExampleTitleScreenMixinStaysRemoved()
    {
        Path direct = Paths.get("src/neoforge/java/net/xalcon/torchmaster/mixin/MixinTitleScreen.java");
        Path fallback = Paths.get("../..").resolve(direct).normalize();

        assertTrue(!Files.exists(direct) && !Files.exists(fallback), () -> "Remove unregistered NeoForge template title screen mixin: " + direct);
    }

    @Test
    void sharedHelpersDoNotGateWholeClassesByLoader() throws IOException
    {
        List<Path> violations = javaFilesIn(
                "src/main/java/net/xalcon/torchmaster/client",
                "src/main/java/net/xalcon/torchmaster/minecraft",
                "src/main/java/net/xalcon/torchmaster/content")
                .filter(StonecutterSourcePolicyTest::startsWithLoaderGatedClass)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Do not add shared helper classes whose whole class body is loader-gated by Stonecutter: " + violations);
    }

    @Test
    void javaSourcesDoNotWrapWholeTypesInInitialStonecutterBlocks() throws IOException
    {
        List<Path> violations = javaFilesIn(
                "src/main/java",
                "src/fabric/java",
                "src/forge/java",
                "src/neoforge/java",
                "src/test/java")
                .filter(StonecutterSourcePolicyTest::startsWithWholeTypeStonecutterBlock)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep package and type declarations outside initial Stonecutter blocks: " + violations);
    }

    @Test
    void modernInvisibleLightPropertiesRemainReplaceable() throws IOException
    {
        Path source = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/adapter/MinecraftBlockProperties.java");
        String text = new String(Files.readAllBytes(source));

        int branchStart = text.indexOf("//? if >=1.20 {", text.indexOf("invisibleLight()"));
        int branchEnd = text.indexOf("//?} else if fabric && >=1.18", branchStart);

        assertTrue(branchStart >= 0 && branchEnd > branchStart, "Expected an explicit >=1.20 invisibleLight branch");
        String branch = text.substring(branchStart, branchEnd);
        assertTrue(branch.contains(".replaceable()"), "MC >=1.20 invisible light settings must remain replaceable");
    }

    @Test
    void feralFlareLifecycleDoesNotOwnTickDecisionRules() throws IOException
    {
        Path path = sourcePath("src/main/java/net/xalcon/torchmaster/minecraft/light/feralflare/FeralFlareLanternLifecycle.java");

        assertTrue(!hasFeralFlareLifecycleDirectTickDecision(path), () -> "Keep Feral Flare tick/check-index rules in domain.FeralFlareLightPlanner: " + path);
    }

    @Test
    void megaTorchBlockstateDeclaresDiamondBaseVariants() throws IOException
    {
        Path path = sourcePath("src/main/resources/assets/torchmaster/blockstates/megatorch.json");
        String text = new String(Files.readAllBytes(path));

        assertTrue(text.contains("\"diamond_base=false\""), "Mega Torch blockstate must keep the normal diamond_base=false variant");
        assertTrue(text.contains("\"diamond_base=true\""), "Mega Torch blockstate must keep the diamond_base=true variant");
        assertTrue(text.contains("torchmaster:block/megatorch_diamond_base"), "Diamond-base Mega Torch variant must use the diamond-base model");
    }

    @Test
    void megaTorchDiamondStateStaysOutOfDomainSources() throws IOException
    {
        List<Path> violations = javaFilesIn("src/main/java/net/xalcon/torchmaster/domain")
                .filter(path -> containsText(path, "diamond"))
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep diamond-base item/block state details in Minecraft block/light adapter code, not domain rules: " + violations);
    }

    private static Stream<Path> javaFilesIn(String... roots) throws IOException
    {
        Stream.Builder<Path> files = Stream.builder();
        for (String root : roots) {
            Path rootPath = Paths.get(root);
            if (!Files.exists(rootPath)) {
                continue;
            }
            try (Stream<Path> paths = Files.walk(rootPath)) {
                paths.filter(path -> path.toString().endsWith(".java")).forEach(files::add);
            }
        }
        return files.build();
    }

    private static Path sourcePath(String path)
    {
        Path direct = Paths.get(path);
        if (Files.exists(direct)) {
            return direct;
        }
        return Paths.get("../..").resolve(path).normalize();
    }

    private static boolean hasMinecraftVersionCondition(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> VERSION_CONDITION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasPlatformImport(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> PLATFORM_IMPORT.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasStonecutterCondition(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> STONECUTTER_CONDITION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasForbiddenSpawnImport(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> MIXIN_FORBIDDEN_SPAWN_IMPORT.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasClientRuntimeDetail(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> CLIENT_RUNTIME_DETAIL.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasStorageRuntimeDetail(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> STORAGE_RUNTIME_DETAIL.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRuntimeRegistryFacadeCall(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RUNTIME_REGISTRY_FACADE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRuntimeFilterValidation(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RUNTIME_FILTER_VALIDATION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRuntimeFilterGlobalCall(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RUNTIME_FILTER_GLOBAL.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRawPanelFillColor(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SCREEN_PANEL_RAW_FILL_COLOR.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasConfigScreenRawSaveCollection(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> CONFIG_SCREEN_RAW_SAVE_COLLECTION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRangeRendererDirectSnapshotLoop(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RANGE_RENDERER_DIRECT_SNAPSHOT_LOOP.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasConfigScreenDirectWidgetState(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> CONFIG_SCREEN_DIRECT_WIDGET_STATE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRangeRendererDirectSessionDetail(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RANGE_RENDERER_DIRECT_SESSION_DETAIL.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasStorageSerializerRuntimeLogger(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> STORAGE_SERIALIZER_RUNTIME_LOGGER.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasDirectScreenTextDraw(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SCREEN_DIRECT_TEXT_DRAW.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRangeSessionRenderTargetDetail(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RANGE_SESSION_RENDER_TARGET_DETAIL.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSavedLightStoreDirectSerializer(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SAVED_LIGHT_STORE_DIRECT_SERIALIZER.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasScreenDirectRenderComposition(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SCREEN_DIRECT_RENDER_COMPOSITION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasLineSubmitterDirectApi(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> LINE_SUBMITTER_DIRECT_API.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasStorageFactoryOldStateFacade(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> STORAGE_FACTORY_OLD_STATE_FACADE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRuntimeFilterGlobalField(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RUNTIME_FILTER_GLOBAL_FIELD.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasClientEntrypointDirectEventDecision(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> CLIENT_ENTRYPOINT_DIRECT_EVENT_DECISION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasConfigScreenDirectBottomButtonGeometry(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> CONFIG_SCREEN_DIRECT_BOTTOM_BUTTON_GEOMETRY.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasStorageStateOldBridgeNames(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> STORAGE_STATE_OLD_BRIDGE_NAMES.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasLoaderClientEntrypointLifecycleImport(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> LOADER_CLIENT_ENTRYPOINT_LIFECYCLE_IMPORT.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasConfigScreenDirectActionOrSave(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> CONFIG_SCREEN_DIRECT_ACTION_OR_SAVE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasStorageFactoryDirectStateGlue(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> STORAGE_FACTORY_DIRECT_STATE_GLUE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSpawnBridgeDirectRuntimeGlue(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SPAWN_BRIDGE_DIRECT_RUNTIME_GLUE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRangeTargetDirectBackendDecision(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RANGE_TARGET_DIRECT_BACKEND_DECISION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasStorageOverrideDirectNbtBridge(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> STORAGE_OVERRIDE_DIRECT_NBT_BRIDGE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSavedLightStoreDirectPlatformGlue(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SAVED_LIGHT_STORE_DIRECT_PLATFORM_GLUE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasLightStoreBridgeDirectMinecraftLight(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> LIGHT_STORE_BRIDGE_DIRECT_MINECRAFT_LIGHT.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasStorageSerializerDirectMinecraftLight(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> STORAGE_SERIALIZER_DIRECT_MINECRAFT_LIGHT.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasLightStoreBridgeDirectGlobalTick(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> LIGHT_STORE_BRIDGE_DIRECT_GLOBAL_TICK.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSpawnBridgeDirectStoreTick(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SPAWN_BRIDGE_DIRECT_STORE_TICK.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSavedLightStoreDirectCleanupPlaceholder(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SAVED_LIGHT_STORE_DIRECT_CLEANUP_PLACEHOLDER.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasFabricWrapperDirectEventContainer(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> FABRIC_WRAPPER_DIRECT_EVENT_CONTAINER.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSpawnBridgeDirectContainerResult(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SPAWN_BRIDGE_DIRECT_CONTAINER_RESULT.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSpawnBridgeDirectContextBuild(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SPAWN_BRIDGE_DIRECT_CONTEXT_BUILD.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRangeTargetDirectLegacyApi(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RANGE_TARGET_DIRECT_LEGACY_API.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasConfigControllerDirectRuntime(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> CONFIG_CONTROLLER_DIRECT_RUNTIME.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSpawnBridgeDirectHookBody(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SPAWN_BRIDGE_DIRECT_HOOK_BODY.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasFabricSpawnHookDirectBridge(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> FABRIC_SPAWN_HOOK_DIRECT_BRIDGE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasRangeTargetDirectLineLayerApi(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> RANGE_TARGET_DIRECT_LINE_LAYER_API.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasConfigRowsDirectWidgetState(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> CONFIG_ROWS_DIRECT_WIDGET_STATE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasUtilsSpawnHookImplementation(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> UTILS_SPAWN_HOOK_IMPLEMENTATION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasLoaderHandlerDirectResultMapping(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> LOADER_HANDLER_DIRECT_RESULT_MAPPING.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasSpawnRuntimeDirectPlatformGlue(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> SPAWN_RUNTIME_DIRECT_PLATFORM_GLUE.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasFeralFlareLifecycleDirectTickDecision(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> FERAL_FLARE_LIFECYCLE_DIRECT_TICK_DECISION.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean containsText(Path path, String text)
    {
        try {
            return new String(Files.readAllBytes(path)).contains(text);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean hasFilterRuntimeDetail(Path path)
    {
        try {
            return Files.lines(path).anyMatch(line -> FILTER_RUNTIME_DETAIL.matcher(line).find());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean startsWithLoaderGatedClass(Path path)
    {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> !line.startsWith("package "))
                    .findFirst()
                    .map(line -> LOADER_GATED_FILE_START.matcher(line).find())
                    .orElse(false);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean startsWithWholeTypeStonecutterBlock(Path path)
    {
        try {
            List<String> lines = Files.readAllLines(path);
            int packageLine = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().startsWith("package ")) {
                    packageLine = i;
                    break;
                }
            }
            if (packageLine < 0) {
                return false;
            }

            int firstCodeLine = packageLine + 1;
            while (firstCodeLine < lines.size() && lines.get(firstCodeLine).trim().isEmpty()) {
                firstCodeLine++;
            }
            if (firstCodeLine >= lines.size() || !STONECUTTER_CLOSED_BLOCK_START.matcher(lines.get(firstCodeLine).trim()).matches()) {
                return false;
            }

            for (int i = firstCodeLine + 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (STONECUTTER_BLOCK_CLOSE.matcher(line).find()) {
                    return false;
                }
                if (TYPE_DECLARATION.matcher(line).find()) {
                    return true;
                }
            }
            return false;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }

    private static boolean isAllowedLoaderRootVersionCondition(Path path)
    {
        String normalized = path.toString().replace('\\', '/');
        return normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/TorchmasterFabricClient.java")
                || normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/mixin/BaseSpawnerMixin.java")
                || normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/mixin/LevelRendererMixin.java")
                || normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/mixin/NaturalSpawnerMixin.java")
                || normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/mixin/PhantomSpawnerMixin.java")
                || normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/mixin/SpawnUtilMixin.java")
                || normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/platform/FabricPlatformHelper.java")
                || normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/platform/FabricRegistrationFactory.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/AbstractTorchmasterForge.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/TorchmasterForgeClient.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/ForgeEventHandler.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/mixin/ForgeNaturalSpawnerMixin.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/ForgeSpawnEventResults.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/ForgeVillageEventHandler.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/TorchmasterForgeLifecycleAdapter.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/platform/AbstractLegacyForgeRegistrationFactory.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/platform/ForgePlatformHelper.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/platform/ForgeRegistrationFactory.java")
                || normalized.endsWith("src/neoforge/java/net/xalcon/torchmaster/TorchmasterNeoforgeClient.java")
                || normalized.endsWith("src/neoforge/java/net/xalcon/torchmaster/mixin/NeoforgeNaturalSpawnerMixin.java")
                || normalized.endsWith("src/neoforge/java/net/xalcon/torchmaster/platform/NeoForgePlatformHelper.java");
    }

    private static boolean isAllowedLoaderEntrypointAdapter(Path path)
    {
        String normalized = path.toString().replace('\\', '/');
        return normalized.endsWith("src/fabric/java/net/xalcon/torchmaster/TorchmasterFabricClient.java")
                || normalized.endsWith("src/forge/java/net/xalcon/torchmaster/TorchmasterForgeClient.java")
                || normalized.endsWith("src/neoforge/java/net/xalcon/torchmaster/TorchmasterNeoforgeClient.java");
    }

}
