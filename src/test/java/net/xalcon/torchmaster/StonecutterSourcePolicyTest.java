package net.xalcon.torchmaster;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StonecutterSourcePolicyTest
{
    private static final Pattern VERSION_CONDITION = Pattern.compile("//\\?\\s*(if|elif|else if)\\b.*(?:[<>]=?|=)\\s*1\\.");

    @Test
    void loaderSourceRootsDoNotContainMinecraftVersionConditions() throws IOException
    {
        List<Path> violations = javaFilesIn("src/fabric", "src/forge", "src/neoforge")
                .filter(StonecutterSourcePolicyTest::hasMinecraftVersionCondition)
                .collect(Collectors.toList());

        assertTrue(violations.isEmpty(), () -> "Move Minecraft-version Stonecutter branches to shared sources: " + violations);
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
}
