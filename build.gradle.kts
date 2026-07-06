import java.util.Properties
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.AbstractArchiveTask
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

fun resourcePackFormat(version: String): Int = when (version) {
    "1.14.4" -> 4
    "1.15.1", "1.15.2" -> 5
    "1.16.5" -> 6
    "1.17", "1.17.1" -> 7
    "1.18.2" -> 8
    "1.19.2" -> 9
    "1.19.4" -> 13
    "1.20.1" -> 15
    "1.20.6" -> 32
    "1.21.1" -> 34
    "1.21.11" -> 75
    else -> throw GradleException("Unknown resource pack format for Minecraft $version")
}

fun dataPackFormat(version: String): Int = when (version) {
    "1.14.4" -> 4
    "1.15.1", "1.15.2" -> 5
    "1.16.5" -> 6
    "1.17", "1.17.1" -> 7
    "1.18.2" -> 9
    "1.19.2" -> 10
    "1.19.4" -> 12
    "1.20.1" -> 15
    "1.20.6" -> 41
    "1.21.1" -> 48
    "1.21.11" -> 94
    else -> throw GradleException("Unknown data pack format for Minecraft $version")
}

fun minecraftVersionRange(version: String): String = when (version) {
    "1.14.4" -> "[1.14,1.15)"
    "1.15.1" -> "[1.15,1.15.2)"
    "1.15.2" -> "[1.15.2,1.16)"
    "1.16.5" -> "[1.16,1.17)"
    "1.17" -> "[1.17,1.17.1)"
    "1.17.1" -> "[1.17.1,1.18)"
    "1.18.2" -> "[1.18,1.19)"
    "1.19.2" -> "[1.19,1.19.3)"
    "1.19.4" -> "[1.19.3,1.20)"
    "1.20.1" -> "[1.20,1.20.5)"
    "1.20.6" -> "[1.20.5,1.21)"
    "1.21.1" -> "[1.21,1.21.11)"
    "1.21.11" -> "[1.21.11,26.1)"
    else -> throw GradleException("Unknown Minecraft version range for Minecraft $version")
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
            ?: minecraftVersionRange(properties.getProperty("minecraft_version", minecraftVersion))
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
val registrationFactoryClass = when {
    isFabric -> "net.xalcon.torchmaster.platform.FabricRegistrationFactory"
    isForge -> "net.xalcon.torchmaster.platform.ForgeRegistrationFactory"
    isNeoForge -> "net.xalcon.torchmaster.platform.NeoforgeRegistrationFactory"
    else -> throw GradleException("Unsupported loader '$activeLoader' for Stonecutter project '$activeProject'")
}
val platformHelperClass = when {
    isFabric -> "net.xalcon.torchmaster.platform.FabricPlatformHelper"
    isForge -> "net.xalcon.torchmaster.platform.ForgePlatformHelper"
    isNeoForge -> "net.xalcon.torchmaster.platform.NeoForgePlatformHelper"
    else -> throw GradleException("Unsupported loader '$activeLoader' for Stonecutter project '$activeProject'")
}
val contentRegistrarClass = when {
    isFabric -> "net.xalcon.torchmaster.content.FabricContentRegistrar"
    isForge -> "net.xalcon.torchmaster.content.ForgeContentRegistrar"
    isNeoForge -> "net.xalcon.torchmaster.content.NeoforgeContentRegistrar"
    else -> throw GradleException("Unsupported loader '$activeLoader' for Stonecutter project '$activeProject'")
}
val resourcePackFormat = resourcePackFormat(minecraftVersion)
val dataPackFormat = dataPackFormat(minecraftVersion)
val supportedPackFormats = "[${minOf(resourcePackFormat, dataPackFormat)}, ${maxOf(resourcePackFormat, dataPackFormat)}]"

layout.buildDirectory.dir("loom-cache/remapped_working").get().asFile.mkdirs()

fun loom(): LoomGradleExtensionAPI = extensions.getByType(LoomGradleExtensionAPI::class.java)

fun Jar.addRenamedLicense() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${prop("mod_name")}" }
    }
}

stonecutter {
    constants {
        match(activeLoader, "fabric", "forge", "neoforge")
    }
}

group = prop("group")
version = prop("mod_version", prop("version"))
description = prop("description")

base {
    archivesName.set("$modId-${project.version}-mc$minecraftVersion-$activeLoader")
}

tasks.withType<AbstractArchiveTask>().configureEach {
    archiveVersion.set("")
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
    if (isForge) {
        doFirst {
            file("out/production/classes").mkdirs()
            file("out/production/resources").mkdirs()
        }
    }
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
            srcDir(rootProject.file("src/$activeLoader/java"))
        }
        if (isFabric) {
            output.setResourcesDir(java.classesDirectory)
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
    if (isNeoForge) {
        mappings(loom().layered {
            mappings("net.fabricmc:yarn:${prop("yarn_mappings")}:v2")
            mappings("dev.architectury:yarn-mappings-patch-neoforge:${prop("yarn_mappings_patch_neoforge_version")}")
        })
    } else {
        mappings("net.fabricmc:yarn:${prop("yarn_mappings")}:v2")
    }

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
        "resource_pack_format" to resourcePackFormat.toString(),
        "supported_pack_formats" to supportedPackFormats,
    )
    val serviceImplementations = mapOf(
        "net.xalcon.torchmaster.platform.RegistrationProvider\$Factory" to registrationFactoryClass,
        "net.xalcon.torchmaster.platform.services.IPlatformHelper" to platformHelperClass,
        "net.xalcon.torchmaster.content.ContentRegistrar" to contentRegistrarClass,
    )

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
        expand(expandProps)
    }
    inputs.properties(expandProps)
    inputs.properties(serviceImplementations.mapKeys { (service, _) -> "service.$service" })

    if (isFabric) {
        exclude("META-INF/mods.toml", "META-INF/neoforge.mods.toml")
    } else {
        exclude("fabric.mod.json", "$modId.fabric.mixins.json")
        if (isForge) {
            exclude("META-INF/neoforge.mods.toml", "$modId.neoforge.mixins.json")
        }
        if (isNeoForge) {
            exclude("META-INF/mods.toml")
        }
    }
    if (!isNeoForge) {
        exclude("$modId.neoforge.mixins.json")
    }

    doLast {
        val servicesDir = destinationDir.resolve("META-INF/services")
        servicesDir.mkdirs()
        serviceImplementations.forEach { (service, implementation) ->
            servicesDir.resolve(service).writeText("$implementation\n")
        }
    }
}

tasks.named<Jar>("jar") {
    addRenamedLicense()

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to prop("mod_name"),
                "Specification-Vendor" to prop("mod_author"),
                "Specification-Version" to project.version.toString(),
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version.toString(),
                "Implementation-Vendor" to prop("mod_author"),
                "Built-On-Minecraft" to prop("minecraft_version"),
            )
        )
    }
}

tasks.named<RemapJarTask>("remapJar") {
    eachFile {
        val sourcePath = file.toPath().toString().replace(File.separatorChar, '/')
        if (path == "pack.mcmeta" && sourcePath.contains("build/generated/stonecutter/")) {
            exclude()
        }
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
