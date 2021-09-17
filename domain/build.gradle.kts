plugins {
  kotlin("multiplatform")
  id("kotlin-kapt")
  id("kotlinx-serialization")
  id("com.android.library")
  jacoco
  id("org.jetbrains.gradle.plugin.idea-ext")
}

kotlin {
  jvm()
  android()

  sourceSets {
    named("commonMain") {
    }
    named("jvmMain") {
    }
    named("androidMain") {
    }
    listOf("jvmMain", "androidMain").forEach {
      getByName(it) {
        dependencies {
          implementation(project(Module.core))
          implementation(project(Module.sourceApi))
          implementation(Deps.toothpick.runtime)
          implementation(Deps.kotlin.serialization.protobuf)
        }
        project.dependencies {
          add("kapt", Deps.toothpick.compiler)
        }
      }
    }
    named("jvmTest") {
      dependencies {
        implementation(Deps.kotlin.reflect)
        implementation(Deps.mockk)
        implementation(Deps.toothpick.testing)
        implementation(Deps.kotest.framework)
        implementation(Deps.kotest.assertions)
      }
      project.dependencies {
        add("kapt", Deps.toothpick.compiler)
      }
    }
  }
}

idea {
  module {
    (this as ExtensionAware).configure<org.jetbrains.gradle.ext.ModuleSettings> {
      (this as ExtensionAware).configure<org.jetbrains.gradle.ext.PackagePrefixContainer> {
        put("src/main/kotlin", "tachiyomi.domain")
        put("src/test/kotlin", "tachiyomi.domain")
        arrayOf(
          "src/commonMain/kotlin",
          "src/jvmMain/kotlin",
          "src/androidMain/kotlin",
          "src/sharedJvmMain/kotlin",
          "src/sharedAndroidMain/kotlin",
          "src/jvmTest/kotlin"
        ).forEach { put(it, "tachiyomi.domain") }
      }
    }
  }
}
