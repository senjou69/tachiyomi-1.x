plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
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

android {
  kapt {
    arguments {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
  }
}

dependencies {
  implementationProject(Projects.core)
  implementationProject(Projects.domain)
  implementationProject(Projects.sourceApi)

  implementation(Deps.kotlin.coroutines.core)
  implementation(Deps.kotlin.coroutines.android)
  implementation(Deps.androidx.workManager.runtime)
  implementation(Deps.androidx.room.runtime)
  implementation(Deps.androidx.room.ktx)
  kapt(Deps.androidx.room.compiler)
  implementation(Deps.sqlite)

  implementation(Deps.toothpick.runtime)
  implementation(Deps.toothpick.smoothie)
  implementation(Deps.toothpick.ktp)
  kapt(Deps.toothpick.compiler)
}
