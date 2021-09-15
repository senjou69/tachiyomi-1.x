plugins {
  kotlin("multiplatform")
  id("com.android.application")
  id("kotlin-kapt")
  id("org.jetbrains.compose")
  id("org.jetbrains.gradle.plugin.idea-ext")
}

kotlin {
  jvm()
  android()
  sourceSets {
    named("commonMain") {
      dependencies {
        compileOnly(Deps.androidx.compose.material)
        compileOnly(Deps.androidx.compose.tooling)
        implementation(project(Module.presentation))
      }
    }
    named("jvmMain") {
      dependencies {
        implementation(compose.desktop.currentOs)
      }
    }
    named("androidMain") {
      kotlin.srcDir("src/sharedJvmMain/kotlin")
      dependencies {
        implementation(Deps.androidx.core)
        implementation(Deps.androidx.emoji)
        implementation(Deps.androidx.appCompat)
        implementation(Deps.androidx.compose.activity)

        implementation(Deps.toothpick.runtime)
        implementation(Deps.toothpick.smoothie)
        implementation(Deps.toothpick.ktp)

        implementation(Deps.tinylog.impl)
      }
      project.dependencies.apply {
        implementationProject(Projects.core)
        implementationProject(Projects.domain)
        implementationProject(Projects.data)
        add("kapt", Deps.toothpick.compiler)
      }
    }
  }
}

android {
  defaultConfig {
    applicationId = Config.applicationId
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFile(getDefaultProguardFile("proguard-android.txt"))
      proguardFile(file("proguard-rules.pro"))
    }
  }
}

compose.desktop {
  application {
    mainClass = "tachiyomi.app.AppKt"
  }
}

idea {
  module {
    (this as ExtensionAware).configure<org.jetbrains.gradle.ext.ModuleSettings> {
      (this as ExtensionAware).configure<org.jetbrains.gradle.ext.PackagePrefixContainer> {
        arrayOf(
          "src/commonMain/kotlin",
          "src/androidMain/kotlin",
          "src/jvmMain/kotlin",
          "src/sharedJvmMain/kotlin",
          "src/sharedAndroidMain/kotlin"
        ).forEach { put(it, "tachiyomi.app") }
      }
    }
  }
}
