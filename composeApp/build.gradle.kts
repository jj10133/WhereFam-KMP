import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.skie)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
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

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation("com.google.zxing:core:3.5.3")
            implementation(libs.revenuecat)
            implementation(libs.revenuecat.ui)

//            implementation(libs.play.services.location)
            implementation("org.maplibre.gl:android-sdk:11.13.1")
            implementation("org.maplibre.gl:android-plugin-annotation-v9:3.0.2")
            implementation("androidx.core:core-splashscreen:1.0.1")
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

            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            api(libs.koin.core)

            implementation(libs.kotlinx.serialization)
            implementation("co.touchlab:kermit:2.0.4")

            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.0-beta05")

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            api(libs.kotlinx.datetime)

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
        "composeApp/src/commonMain/js/app.js"
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
        "composeApp/src/commonMain/js/push.js"
    )
}

tasks.register("barePack") {
    group = "bare-runtime"
    description = "Runs all bare-runtime packing tasks."
    dependsOn(tasks.named("barePackApp"), tasks.named("barePackPush"))
}

// Ensure bare-runtime tasks run before the Android preBuild task for this module
tasks.named("preBuild").dependsOn(tasks.named("bareLink"), tasks.named("barePack"))

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

