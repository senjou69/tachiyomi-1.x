plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
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
      kotlin.srcDir("src/commonJvmMain/kotlin")
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
