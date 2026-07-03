import java.util.Properties
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    `java-library`
    `maven-publish`
    id("dev.architectury.loom") apply false
}

val activeProject = stonecutter.current.project
val minecraftVersion = stonecutter.current.version
val rootBuildRequested = gradle.startParameter.taskNames.any { it == "build" || it == ":build" }
val activeProjectTaskRequested = gradle.startParameter.taskNames.any { it.startsWith(":$activeProject:") }
val configureMinecraft = rootBuildRequested || activeProjectTaskRequested || stonecutter.current.isActive

fun loadProperties(file: File): Properties =
    Properties().also { properties ->
        if (file.exists()) {
            file.inputStream().use(properties::load)
        }
    }

val rootProperties = loadProperties(rootProject.file("gradle.properties"))
val versionProperties = loadProperties(rootProject.file("versions/$activeProject/gradle.properties"))
val buildProperties = Properties().also { properties ->
    properties.putAll(rootProperties)
    properties.putAll(versionProperties)
    properties.putIfAbsent("minecraft_version", minecraftVersion)
    properties.putIfAbsent("loader", activeProject.substringAfterLast("-"))
    properties.setProperty(
        "minecraft_version_range",
        versionProperties.getProperty("minecraft_version_range")
            ?: "[${properties.getProperty("minecraft_version", minecraftVersion)}]"
    )
    if (properties.getProperty("loader") == "forge" && !versionProperties.containsKey("forge_loader_version_range")) {
        properties.setProperty("forge_loader_version_range", "[${properties.getProperty("forge_version").substringBefore(".")},)")
    }
}

fun prop(name: String, fallback: String = ""): String = buildProperties.getProperty(name, fallback)

val activeLoader = prop("loader")
val isFabric = activeLoader == "fabric"
val isForge = activeLoader == "forge"
val isNeoForge = activeLoader == "neoforge"
val javaVersion = prop("java_version", "21").toInt()
val nightConfigVersion = prop("night_config_version")
val modId = prop("mod_id")

fun loom(): LoomGradleExtensionAPI = extensions.getByType(LoomGradleExtensionAPI::class.java)

fun DependencyHandler.addMinecraftAndMappings() {
    add("minecraft", "com.mojang:minecraft:${prop("minecraft_version")}")
    add("mappings", loom().officialMojangMappings())
}

fun DependencyHandler.addMixinDependencies(includeJsr305: Boolean = false) {
    add("compileOnly", "org.spongepowered:mixin:${prop("mixin_version")}")
    add("compileOnly", "io.github.llamalad7:mixinextras-common:${prop("mixinextras_version")}")
    add("annotationProcessor", "io.github.llamalad7:mixinextras-common:${prop("mixinextras_version")}")
    if (includeJsr305) {
        add("compileOnly", "com.google.code.findbugs:jsr305:3.0.0")
    }
}

fun Jar.addRenamedLicense() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${prop("mod_name")}" }
    }
}

if (configureMinecraft) {
    apply(plugin = "dev.architectury.loom")
}

group = prop("group")
version = prop("mod_version", prop("version"))
description = prop("description")

base {
    archivesName.set("$modId-$activeProject")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net") {
        name = "Fabric"
    }
    maven("https://maven.architectury.dev/") {
        name = "Architectury"
    }
    maven("https://maven.minecraftforge.net/") {
        name = "Forge"
    }
    maven("https://maven.neoforged.net/releases") {
        name = "NeoForge"
    }
    maven("https://repo.spongepowered.org/repository/maven-public") {
        name = "Sponge"
    }
    maven("https://maven.blamejared.com") {
        name = "BlameJared"
    }
    maven("https://maven.wispforest.io") {
        name = "WispForest"
    }
    maven("https://maven.terraformersmc.com/") {
        name = "Terraformers"
    }
}

sourceSets {
    main {
        if (configureMinecraft) {
            java.srcDir(rootProject.file("adapter/src/main/java"))
            java.srcDir(rootProject.file("core/src/main/java"))
            java.srcDir(rootProject.file("src/$activeLoader"))
            if (isFabric) {
                java.exclude("compat/**")
                java.srcDir(rootProject.file(
                    when {
                        stonecutter.eval(minecraftVersion, ">=1.21.2") -> "src/fabric/compat/platform/block-entity-builder"
                        stonecutter.eval(minecraftVersion, ">=1.20") -> "src/fabric/compat/platform/current"
                        else -> "src/fabric/compat/platform/legacy"
                    }
                ))
                java.srcDir(rootProject.file(if (stonecutter.eval(minecraftVersion, ">=1.21.8")) "src/fabric/compat/client-render/rendertype-package" else "src/fabric/compat/client-render/current"))
                java.srcDir(rootProject.file(
                    when {
                        stonecutter.eval(minecraftVersion, ">=1.21.9") -> "src/fabric/compat/phantom/single-flag-tick"
                        stonecutter.eval(minecraftVersion, ">=1.21.5") -> "src/fabric/compat/phantom/double-flag-void-tick"
                        else -> "src/fabric/compat/phantom/double-flag-tick"
                    }
                ))
                java.srcDir(rootProject.file(if (stonecutter.eval(minecraftVersion, ">=1.21.2")) "src/fabric/compat/spawn/entity-spawn-reason" else "src/fabric/compat/spawn/mob-spawn-type"))
                java.srcDir(rootProject.file(
                    when {
                        stonecutter.eval(minecraftVersion, ">=1.21.11") -> "src/fabric/compat/registration/identifier"
                        stonecutter.eval(minecraftVersion, ">=1.21.2") -> "src/fabric/compat/registration/reference"
                        stonecutter.eval(minecraftVersion, ">=1.19.3") -> "src/fabric/compat/registration/current"
                        else -> "src/fabric/compat/registration/legacy"
                    }
                ))
            } else if (isForge) {
                java.exclude("compat/**")
                java.srcDir(rootProject.file(if (stonecutter.eval(minecraftVersion, ">=1.19.4")) "src/forge/compat/spawn/finalize" else "src/forge/compat/spawn/legacy"))
                java.srcDir(rootProject.file(if (stonecutter.eval(minecraftVersion, ">=1.19")) "src/forge/compat/world-load/level-event" else "src/forge/compat/world-load/world-event"))
                java.srcDir(rootProject.file("src/forge/compat/phantom/current"))
                java.srcDir(rootProject.file(
                    when {
                        stonecutter.eval(minecraftVersion, ">=1.19") -> "src/forge/compat/registration/resource-key"
                        stonecutter.eval(minecraftVersion, ">=1.18") -> "src/forge/compat/registration/1.18"
                        stonecutter.eval(minecraftVersion, ">=1.17") -> "src/forge/compat/registration/1.17"
                        else -> "src/forge/compat/registration/1.16"
                    }
                ))
                java.srcDir(rootProject.file(
                    when {
                        stonecutter.eval(minecraftVersion, ">=1.20") -> "src/forge/compat/platform/builder"
                        stonecutter.eval(minecraftVersion, ">=1.19.3") -> "src/forge/compat/platform/builder-row"
                        stonecutter.eval(minecraftVersion, ">=1.17") -> "src/forge/compat/platform/legacy-tab"
                        else -> "src/forge/compat/platform/1.16"
                    }
                ))
            } else if (isNeoForge) {
                java.exclude("compat/**")
                java.srcDir(rootProject.file(
                    when {
                        stonecutter.eval(minecraftVersion, ">=1.21.11") -> "src/neoforge/compat/platform/block-entity-constructor"
                        stonecutter.eval(minecraftVersion, ">=1.21.9") -> "src/neoforge/compat/platform/block-entity-constructor-current-loader-instance"
                        stonecutter.eval(minecraftVersion, ">=1.21.2") -> "src/neoforge/compat/platform/block-entity-constructor-current"
                        else -> "src/neoforge/compat/platform/block-entity-builder"
                    }
                ))
                java.srcDir(rootProject.file(if (stonecutter.eval(minecraftVersion, ">=1.21.11")) "src/neoforge/compat/registration/identifier" else "src/neoforge/compat/registration/resource-location"))
            }
            resources.srcDir(rootProject.file("src/$activeLoader"))
            resources.exclude("**/*.java")
        } else {
            java.setSrcDirs(emptyList<File>())
            resources.setSrcDirs(emptyList<File>())
        }
    }
    test {
        if (!configureMinecraft) {
            java.setSrcDirs(emptyList<File>())
            resources.setSrcDirs(emptyList<File>())
        }
    }
}

if (configureMinecraft) {
    extensions.configure<LoomGradleExtensionAPI>("loom") {
        silentMojangMappingsLicense()
    }
}

dependencies {
    if (configureMinecraft) {
        implementation("com.electronwill.night-config:toml:$nightConfigVersion")
        addMixinDependencies(includeJsr305 = isFabric)
        addMinecraftAndMappings()

        when {
            isFabric -> {
                add("modImplementation", "net.fabricmc:fabric-loader:${prop("fabric_loader_version")}")
                add("modImplementation", "net.fabricmc.fabric-api:fabric-api:${prop("fabric_version")}")
                add("include", "com.electronwill.night-config:core:$nightConfigVersion")
                add("include", "com.electronwill.night-config:toml:$nightConfigVersion")
            }
            isNeoForge -> add("neoForge", "net.neoforged:neoforge:${prop("neoforge_version")}")
            isForge -> add("forge", "net.minecraftforge:forge:${prop("minecraft_version")}-${prop("forge_version")}")
            else -> throw GradleException("Unsupported loader '$activeLoader' for Stonecutter project '$activeProject'")
        }
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

if (configureMinecraft) {
    extensions.configure<LoomGradleExtensionAPI>("loom") {
        if (isFabric) {
            runs {
                maybeCreate("client").apply {
                    client()
                    programArg("--username")
                    programArg("Xalcon")
                    name("Fabric Client")
                    runDir("runs/client")
                }
                maybeCreate("server").apply {
                    server()
                    name("Fabric Server")
                    runDir("runs/server")
                }
            }
            mixin {
                defaultRefmapName.set("$modId.refmap.json")
            }
        } else if (isForge) {
            forge {
                mixinConfigs("$modId.mixins.json", "$modId.forge.mixins.json")
                convertAccessWideners.set(true)
            }
        } else if (isNeoForge) {
            neoForge {
                val accessTransformerFile = file("src/main/resources/META-INF/accesstransformer.cfg")
                if (accessTransformerFile.exists()) {
                    accessTransformer(accessTransformerFile)
                }
            }
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.isDeprecation = true
    if (javaVersion >= 9) {
        options.release.set(javaVersion)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<ProcessResources>("processResources") {
    val resourcePropertyNames = listOf(
        "minecraft_version",
        "minecraft_version_range",
        "fabric_version",
        "fabric_loader_version",
        "mod_name",
        "mod_author",
        "mod_id",
        "license",
        "neoforge_version",
        "neoforge_loader_version_range",
        "forge_loader_version_range",
        "credits",
        "java_version",
    )
    val expandProps = resourcePropertyNames.associateWith { prop(it) } + mapOf(
        "version" to project.version.toString(),
        "group" to project.group.toString(),
        "description" to (project.description ?: ""),
        "java_compatibility_level" to "JAVA_${prop("java_version")}",
    )

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
        expand(expandProps)
    }
    inputs.properties(expandProps)
}

tasks.named<Jar>("jar") {
    addRenamedLicense()

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to prop("mod_name"),
                "Specification-Vendor" to prop("mod_author"),
                "Specification-Version" to archiveVersion.get(),
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to prop("mod_author"),
                "Built-On-Minecraft" to prop("minecraft_version"),
            )
        )
    }
}

tasks.named<Jar>("sourcesJar") {
    addRenamedLicense()
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = System.getenv("local_maven_url")?.let { uri(it) }
                ?: layout.buildDirectory.dir("repo").get().asFile.toURI()
        }
    }
}
