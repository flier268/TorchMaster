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
            "Torchmaster(LightRangeDisplay|LightRangeRenderer|LightScreen|ClientLifecycle)");

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
                .filter(StonecutterSourcePolicyTest::hasClientRuntimeDetail)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Keep client render/screen runtime details in shared client helpers; loader roots should only wire lifecycle entrypoints: " + violations);
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

    private static boolean isAllowedForgeLifecycleAdapter(Path path)
    {
        String normalized = path.toString().replace('\\', '/');
        return normalized.endsWith("src/forge/net/xalcon/torchmaster/AbstractTorchmasterForge.java")
                || normalized.endsWith("src/forge/net/xalcon/torchmaster/TorchmasterForge.java");
    }

}
