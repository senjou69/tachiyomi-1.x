plugins {
  kotlin("multiplatform")
  id("com.android.library")
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
        implementation(Deps.androidx.compose.navigation)
        implementation(Deps.androidx.emoji)
        implementation(Deps.toothpick.ktp)
      }
      project.dependencies.apply {
        implementationProject(Projects.core)
      }
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
        ).forEach { put(it, "tachiyomi.ui.core") }
      }
    }
  }
}
