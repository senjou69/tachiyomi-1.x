plugins {
  kotlin("multiplatform")
  id("com.android.application")
  id("kotlin-kapt")
  id("org.jetbrains.compose")
  id("org.jetbrains.gradle.plugin.idea-ext")
}

kotlin {
  android()
  sourceSets {
    named("commonMain") {
      dependencies {
        implementation(Deps.androidx.compose.material)
        implementation(Deps.androidx.compose.tooling)
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
        implementationProject(Projects.presentation)
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
