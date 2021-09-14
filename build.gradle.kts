buildscript {
  repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.0.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
    classpath("org.jetbrains.kotlin:kotlin-serialization:${Deps.kotlin.version}")
    classpath("org.jetbrains.compose:compose-gradle-plugin:1.0.0-alpha4-build331")
    classpath(Deps.aboutLibraries.plugin)
  }
}

plugins {
  id("com.github.ben-manes.versions") version "0.39.0"
  id("org.jetbrains.gradle.plugin.idea-ext") version "1.0.1"
}

allprojects {
  repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven { setUrl("https://jitpack.io") }
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
  }
}

subprojects {
  tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile> {
    kotlinOptions {
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
        "-Xuse-experimental=kotlinx.coroutines.FlowPreview",
        "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-Xuse-experimental=kotlinx.serialization.ExperimentalSerializationApi",
        "-Xuse-experimental=androidx.compose.foundation.ExperimentalFoundationApi"
      )
      jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
  }
  tasks.withType<Test> {
    useJUnitPlatform()
  }

  plugins.withType<com.android.build.gradle.BasePlugin> {
    configure<com.android.build.gradle.BaseExtension> {
      compileSdkVersion(Config.compileSdk)
      defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode(Config.versionCode)
        versionName(Config.versionName)
        ndk {
          version = Config.ndk
        }
      }
      compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
      }
      sourceSets {
        named("main") {
          val altManifest = file("src/androidMain/AndroidManifest.xml")
          if (altManifest.exists()) {
            manifest.srcFile(altManifest.path)
          }
        }
      }
      dependencies {
        add("coreLibraryDesugaring", Deps.desugarJdkLibs)
      }
    }
  }

  plugins.withType<JacocoPlugin> {
    configure<JacocoPluginExtension> {
      toolVersion = "0.8.7"
    }
  }

  afterEvaluate {
    tasks.withType<JacocoReport> {
      reports {
        xml.required.set(true)
        html.required.set(false)
      }
    }
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
