buildscript {
    val agp_version by extra("8.9.0")
    repositories {
        // other repositories...
        mavenCentral()
        google()
    }
    dependencies {
        // other plugins...
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath( "org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
        classpath("com.android.tools.build:gradle:$agp_version")
        classpath("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.

subprojects {
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
        kotlinOptions {
            if (project.findProperty("noteApp.enableComposeCompilerReports") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                            project.buildDir.absolutePath + "/compose_metrics"
                )
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                            project.buildDir.absolutePath + "/compose_metrics"
                )
            }
        }
    }
}

plugins {
    id("com.android.application").version("7.4.2").apply(false)
    id("com.android.library").version("7.4.2").apply(false)
    id("org.jetbrains.kotlin.android").version("2.0.20").apply(false)
    id("com.google.dagger.hilt.android").version("2.48").apply(false)
    id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false


    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
}