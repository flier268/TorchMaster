import java.util.Properties
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.JavaExec
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

val javaToolchainLauncher = javaToolchains.launcherFor {
    languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

tasks.withType<JavaExec>().configureEach {
    javaLauncher.set(javaToolchainLauncher)
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
            mixinConfigs(
                *listOfNotNull(
                    "$modId.mixins.json",
                    "$modId.legacy_forge_dev.mixins.json".takeIf { prop("minecraft_version") == "1.16.5" },
                ).toTypedArray()
            )
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
