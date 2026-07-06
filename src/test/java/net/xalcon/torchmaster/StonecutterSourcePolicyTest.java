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
            "Torchmaster(LightRangeDisplay|LightRangeRenderer|LightScreen|ClientLifecycle|ScreenCompat|RangeBoxes|PanelRenderer|LineBoxRenderer|RangeRenderPlan|RangeLineSubmitter|RangeRenderSession|ConfigScreenLayout|ConfigEntries|ConfigWidgetRows)");
    private static final Pattern STORAGE_RUNTIME_DETAIL = Pattern.compile(
            "(SavedLightStore|SavedLightStoreStateFactory|MinecraftLightStoreAccess|LightStoreBridge|LightStoreConfigView)");
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
    private static final Pattern LOADER_GATED_FILE_START = Pattern.compile("//\\?\\s*(if|elif|else if)\\b.*\\b(fabric|forge|neoforge)\\b.*\\{");

    @Test
    void loaderSourceRootsDoNotContainMinecraftVersionConditions() throws IOException
    {
        List<Path> violations = javaFilesIn("src/fabric", "src/forge", "src/neoforge")
                .filter(path -> !isAllowedForgeLifecycleAdapter(path))
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
    void mainClientEntrypointsStayOnlyWhenStonecutterProcessed() throws IOException
    {
        List<Path> violations = Arrays.asList(
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterFabricClient.java"),
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterForgeClient.java"),
                        Paths.get("src/main/java/net/xalcon/torchmaster/TorchmasterNeoforgeClient.java"))
                .stream()
                .filter(Files::exists)
                .filter(path -> !hasMinecraftVersionCondition(path))
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Move branch-free client entrypoints to loader roots; src/main client entrypoints must need Stonecutter processing: " + violations);
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

    private static boolean isAllowedForgeLifecycleAdapter(Path path)
    {
        String normalized = path.toString().replace('\\', '/');
        return normalized.endsWith("src/forge/net/xalcon/torchmaster/AbstractTorchmasterForge.java")
                || normalized.endsWith("src/forge/net/xalcon/torchmaster/TorchmasterForge.java");
    }

}
