pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/releases") {
            name = "KikuGie Releases"
        }
        maven("https://maven.kikugie.dev/snapshots") {
            name = "KikuGie Snapshots"
        }
        maven("https://maven.architectury.dev/") {
            name = "Architectury"
        }
        maven("https://maven.minecraftforge.net/") {
            name = "Forge"
        }
        maven("https://maven.fabricmc.net") {
            name = "Fabric"
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.6"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net") {
            name = "Fabric"
        }
        maven("https://maven.architectury.dev/") {
            name = "Architectury"
        }
        maven("https://maven.neoforged.net/releases") {
            name = "NeoForge"
        }
        maven("https://maven.minecraftforge.net/") {
            name = "Forge"
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
}

// This should match the folder name of the project, or else IDEA may complain (see https://youtrack.jetbrains.com/issue/IDEA-317606)
rootProject.name = "Torchmaster"

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(rootProject) {
        version("1.14.4-fabric", "1.14.4")
        version("1.14.4-forge", "1.14.4")
        version("1.15.1-fabric", "1.15.1")
        version("1.15.1-forge", "1.15.1")
        version("1.15.2-fabric", "1.15.2")
        version("1.15.2-forge", "1.15.2")
        version("1.16.5-fabric", "1.16.5")
        version("1.16.5-forge", "1.16.5")
        version("1.17-fabric", "1.17")
        version("1.17.1-fabric", "1.17.1")
        version("1.17.1-forge", "1.17.1")
        version("1.18.2-fabric", "1.18.2")
        version("1.18.2-forge", "1.18.2")
        version("1.19.2-fabric", "1.19.2")
        version("1.19.2-forge", "1.19.2")
        version("1.19.4-fabric", "1.19.4")
        version("1.19.4-forge", "1.19.4")
        version("1.20.1-fabric", "1.20.1")
        version("1.20.1-forge", "1.20.1")
        version("1.20.6-fabric", "1.20.6")
        version("1.20.6-forge", "1.20.6")
        version("1.20.6-neoforge", "1.20.6")
        version("1.21.1-fabric", "1.21.1")
        version("1.21.1-forge", "1.21.1")
        version("1.21.1-neoforge", "1.21.1")
        version("1.21.11-fabric", "1.21.11")
        version("1.21.11-neoforge", "1.21.11")
        vcsVersion = "1.21.1-fabric"
    }
}
