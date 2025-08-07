import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            api(fileTree(mapOf("dir" to "libs", "include" to listOf("bare-kit/classes.jar"))))

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.wherefam.kmp.wherefam_kmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.wherefam.kmp.wherefam_kmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("src/androidMain/addons", "libs/bare-kit/jni")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val nodeModulesRoot = rootProject.projectDir

tasks.register<Exec>("bareLink") {
    group = "bare-runtime"
    description = "Links bare-kit native libraries into the Android project."
    workingDir = nodeModulesRoot
    commandLine(
        "node_modules/.bin/bare-link",
        "--preset", "android",
        "--needs", "libbare-kit.so",
        "--out", "composeApp/src/androidMain/addons" // Path relative to workingDir (nodeModulesRoot)
    )
}

tasks.register<Exec>("barePackApp") {
    group = "bare-runtime"
    description = "Packs the main bare-runtime app bundle."
    workingDir = nodeModulesRoot
    commandLine(
        "node_modules/.bin/bare-pack",
        "--preset", "android",
        "--out", "composeApp/src/androidMain/assets/app.bundle",
        "composeApp/src/androidMain/js/app.js"
    )
}

tasks.register<Exec>("barePackPush") {
    group = "bare-runtime"
    description = "Packs the bare-runtime push bundle."
    workingDir = nodeModulesRoot
    commandLine(
        "node_modules/.bin/bare-pack",
        "--preset", "android",
        "--out", "composeApp/src/androidMain/assets/push.bundle",
        "composeApp/src/androidMain/js/push.js"
    )
}

tasks.register("barePack") {
    group = "bare-runtime"
    description = "Runs all bare-runtime packing tasks."
    dependsOn(tasks.named("barePackApp"), tasks.named("barePackPush"))
}

// Ensure bare-runtime tasks run before the Android preBuild task for this module
tasks.named("preBuild").dependsOn(tasks.named("bareLink"), tasks.named("barePack"))

dependencies {
    debugImplementation(compose.uiTooling)
}

