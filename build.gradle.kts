import java.util.Properties
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    `java-library`
    `maven-publish`
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.17.487"
    id("architectury-plugin") version "3.5.169"
}

val activeProject = stonecutter.current.project
val minecraftVersion = stonecutter.current.version

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

fun Jar.addRenamedLicense() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${prop("mod_name")}" }
    }
}

fun versionMatches(predicate: String): Boolean = stonecutter.eval(minecraftVersion, predicate)

fun selectedCompatSourceDirs(): List<String> = when {
    isFabric -> listOf(
        when {
            versionMatches(">=1.21.2") -> "src/fabric/compat/platform/block-entity-builder"
            versionMatches(">=1.20") -> "src/fabric/compat/platform/current"
            else -> "src/fabric/compat/platform/legacy"
        },
        if (versionMatches(">=1.21.8")) {
            "src/fabric/compat/client-render/rendertype-package"
        } else {
            "src/fabric/compat/client-render/current"
        },
        when {
            versionMatches(">=1.21.9") -> "src/fabric/compat/phantom/single-flag-tick"
            versionMatches(">=1.21.5") -> "src/fabric/compat/phantom/double-flag-void-tick"
            else -> "src/fabric/compat/phantom/double-flag-tick"
        },
        if (versionMatches(">=1.21.2")) {
            "src/fabric/compat/spawn/entity-spawn-reason"
        } else {
            "src/fabric/compat/spawn/mob-spawn-type"
        },
        when {
            versionMatches(">=1.21.11") -> "src/fabric/compat/registration/identifier"
            versionMatches(">=1.21.2") -> "src/fabric/compat/registration/reference"
            versionMatches(">=1.19.3") -> "src/fabric/compat/registration/current"
            else -> "src/fabric/compat/registration/legacy"
        },
    )
    isForge -> listOf(
        if (versionMatches(">=1.19.4")) {
            "src/forge/compat/spawn/finalize"
        } else {
            "src/forge/compat/spawn/legacy"
        },
        if (versionMatches(">=1.19")) {
            "src/forge/compat/world-load/level-event"
        } else {
            "src/forge/compat/world-load/world-event"
        },
        when {
            versionMatches(">=1.19") -> "src/forge/compat/registration/resource-key"
            versionMatches(">=1.18") -> "src/forge/compat/registration/1.18"
            versionMatches(">=1.17") -> "src/forge/compat/registration/1.17"
            else -> "src/forge/compat/registration/1.16"
        },
        when {
            versionMatches(">=1.20") -> "src/forge/compat/platform/builder"
            versionMatches(">=1.19.3") -> "src/forge/compat/platform/builder-row"
            versionMatches(">=1.17") -> "src/forge/compat/platform/legacy-tab"
            else -> "src/forge/compat/platform/1.16"
        },
    )
    isNeoForge -> listOf(
        when {
            versionMatches(">=1.21.11") -> "src/neoforge/compat/platform/block-entity-constructor"
            versionMatches(">=1.21.9") -> "src/neoforge/compat/platform/block-entity-constructor-current-loader-instance"
            versionMatches(">=1.21.2") -> "src/neoforge/compat/platform/block-entity-constructor-current"
            else -> "src/neoforge/compat/platform/block-entity-builder"
        },
        if (versionMatches(">=1.21.11")) {
            "src/neoforge/compat/registration/identifier"
        } else {
            "src/neoforge/compat/registration/resource-location"
        },
    )
    else -> throw GradleException("Unsupported loader '$activeLoader' for Stonecutter project '$activeProject'")
}

stonecutter {
    constants.match(activeLoader, "fabric", "forge", "neoforge")
}

group = prop("group")
version = prop("mod_version", prop("version"))
description = prop("description")

base {
    archivesName.set("$modId-$activeProject")
}

architectury {
    platformSetupLoomIde()
    when {
        isFabric -> fabric()
        isForge -> forge()
        isNeoForge -> neoForge()
        else -> throw GradleException("Unsupported loader '$activeLoader' for Stonecutter project '$activeProject'")
    }
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
        java {
            srcDir(rootProject.file("src/$activeLoader"))
            exclude("compat/**")
            selectedCompatSourceDirs().forEach { srcDir(rootProject.file(it)) }
        }
        resources {
            srcDir(rootProject.file("src/$activeLoader"))
            exclude("**/*.java")
        }
    }
}

extensions.configure<LoomGradleExtensionAPI>("loom") {
    silentMojangMappingsLicense()
    mods {
        create(modId) {
            sourceSet(sourceSets["main"])
        }
    }

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
            mixinConfigs("$modId.mixins.json")
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

dependencies {
    if (isFabric) {
        implementation("com.electronwill.night-config:toml:$nightConfigVersion")
    } else {
        compileOnly("com.electronwill.night-config:core:$nightConfigVersion")
        compileOnly("com.electronwill.night-config:toml:$nightConfigVersion")
    }
    compileOnly("org.spongepowered:mixin:${prop("mixin_version")}")
    compileOnly("io.github.llamalad7:mixinextras-common:${prop("mixinextras_version")}")
    annotationProcessor("io.github.llamalad7:mixinextras-common:${prop("mixinextras_version")}")

    if (isFabric) {
        compileOnly("com.google.code.findbugs:jsr305:3.0.0")
    }

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom().officialMojangMappings())

    when {
        isFabric -> {
            modImplementation("net.fabricmc:fabric-loader:${prop("fabric_loader_version")}")
            modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("fabric_version")}")
            include("com.electronwill.night-config:core:$nightConfigVersion")
            include("com.electronwill.night-config:toml:$nightConfigVersion")
        }
        isForge -> "forge"("net.minecraftforge:forge:${prop("minecraft_version")}-${prop("forge_version")}")
        isNeoForge -> "neoForge"("net.neoforged:neoforge:${prop("neoforge_version")}")
        else -> throw GradleException("Unsupported loader '$activeLoader' for Stonecutter project '$activeProject'")
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
