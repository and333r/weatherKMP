
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0-RC1"
}

kotlin {
    task("testClasses")
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation("io.ktor:ktor-client-core:2.3.10")
            implementation("io.ktor:ktor-client-cio:2.3.10")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")

        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:2.3.10")

        }
    }
}

android {
    namespace = "com.andercarotfg.weatherappkmp.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
dependencies {
}
