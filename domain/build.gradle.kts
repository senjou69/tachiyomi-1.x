plugins {
  kotlin("jvm")
  id("kotlin-kapt")
  id("kotlinx-serialization")
  jacoco
  id("org.jetbrains.gradle.plugin.idea-ext")
}

dependencies {
  implementation(project(Module.core))
  implementation(project(Module.sourceApi))

  implementation(Deps.toothpick.runtime)
  kapt(Deps.toothpick.compiler)

  implementation(Deps.kotlin.serialization.protobuf)

  testImplementation(Deps.kotlin.reflect)
  testImplementation(Deps.mockk)
  testImplementation(Deps.toothpick.testing)
  testImplementation(Deps.kotest.framework)
  testImplementation(Deps.kotest.assertions)
  kaptTest(Deps.toothpick.compiler)
}

idea {
  module {
    (this as ExtensionAware).configure<org.jetbrains.gradle.ext.ModuleSettings> {
      (this as ExtensionAware).configure<org.jetbrains.gradle.ext.PackagePrefixContainer> {
        put("src/main/kotlin", "tachiyomi.domain")
        put("src/test/kotlin", "tachiyomi.domain")
      }
    }
  }
}
