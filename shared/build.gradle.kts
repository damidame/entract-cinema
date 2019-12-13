import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    compileSdkVersion(Android.compileSdkVersion)

    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
        resConfigs("fr")
    }

    sourceSets {
        getByName("main").apply {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
        }
    }
}


kotlin {
    targets {
        android()
        val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
                ::iosArm64
            else
                ::iosX64

        iOSTarget("ios") {
            binaries {
                framework {
                    baseName = "shared"
                }
            }
        }
    }

    sourceSets {
        getByName("commonMain").dependencies {
            implementation(kotlin("stdlib-common", Versions.kotlin))
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.14.0")
            implementation("io.ktor:ktor-client-core:1.2.6")
            implementation("io.ktor:ktor-client-json:1.2.6")
            implementation("io.ktor:ktor-client-serialization:1.2.6")
        }

        getByName("androidMain").dependencies {
            implementation(kotlin("stdlib", Versions.kotlin))
            implementation(project(":core"))
            implementation(Libs.coroutinesAndroid)
            implementation(Libs.appCompat)
            implementation(Libs.coreKtx)
            implementation(Libs.jsr310)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
            implementation("io.ktor:ktor-client-okhttp:1.2.6")
            implementation("io.ktor:ktor-client-json-jvm:1.2.6")
            implementation("io.ktor:ktor-client-serialization-jvm:1.2.6")
        }

        getByName("iosMain").dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.coroutines}")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14.0")
            implementation("io.ktor:ktor-client-ios:1.2.6")
            implementation("io.ktor:ktor-client-json-native:1.2.6")
            implementation("io.ktor:ktor-client-serialization-iosx64:1.2.6")
        }
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText(
            "#!/bin/bash\n"
                    + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                    + "cd '${rootProject.rootDir}'\n"
                    + "./gradlew \$@\n"
        )
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)
