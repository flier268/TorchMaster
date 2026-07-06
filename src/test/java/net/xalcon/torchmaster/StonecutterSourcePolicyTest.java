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
    private static final Pattern MIXIN_FORBIDDEN_SPAWN_IMPORT = Pattern.compile(
            "^import\\s+net\\.xalcon\\.torchmaster\\.(domain\\.SpawnBlockingRules|minecraft\\.storage\\.|minecraft\\.adapter\\.MinecraftSpawnBlocker)");
    private static final Pattern CLIENT_RUNTIME_DETAIL = Pattern.compile(
            "Torchmaster(LightRangeDisplay|LightRangeRenderer|LightScreen|ClientLifecycle|ScreenCompat|RangeBoxes)");
    private static final Pattern STORAGE_RUNTIME_DETAIL = Pattern.compile(
            "(SavedLightStore|MinecraftLightStoreAccess|LightStoreBridge|LightStoreConfigView)");
    private static final Pattern RUNTIME_REGISTRY_FACADE = Pattern.compile("getRegistryForLevel\\s*\\(");
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
                .filter(path -> hasClientRuntimeDetail(path) || hasStorageRuntimeDetail(path))
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep client/storage runtime details in shared helpers; loader roots should only wire lifecycle entrypoints: " + violations);
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
