plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
  id("com.google.devtools.ksp") version "1.5.30-1.0.0-beta08"
  id("org.jetbrains.gradle.plugin.idea-ext")
}

val removeDomainClasses by tasks.registering(Delete::class) {
  doFirst {
    delete(fileTree("$buildDir/tmp/kotlin-classes") {
      include("*/tachiyomi/domain/**/*.class")
    })
  }
}
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
  finalizedBy(removeDomainClasses)
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
  implementation(project(Module.core))
  implementation(project(Module.domain))
  implementation(project(Module.sourceApi))

  implementation(Deps.kotlin.coroutines.core)
  implementation(Deps.kotlin.coroutines.android)
  implementation(Deps.androidx.workManager.runtime)
  implementation(Deps.androidx.room.runtime)
  implementation(Deps.androidx.room.ktx)
  ksp(Deps.androidx.room.compiler)
  implementation(Deps.sqlite)

  implementation(Deps.toothpick.runtime)
  implementation(Deps.toothpick.smoothie)
  implementation(Deps.toothpick.ktp)
  kapt(Deps.toothpick.compiler)
}

idea {
  module {
    (this as ExtensionAware).configure<org.jetbrains.gradle.ext.ModuleSettings> {
      (this as ExtensionAware).configure<org.jetbrains.gradle.ext.PackagePrefixContainer> {
        put("src/main/kotlin", "tachiyomi.data")
      }
    }
  }
}
