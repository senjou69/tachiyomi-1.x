plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("com.squareup.sqldelight")
}

kotlin {
  jvm()
  android()

  sourceSets {
    named("commonMain") {
      dependencies {
        implementation(project(Module.core))
        implementation(project(Module.domain))
        implementation(Deps.sqldelight.runtime)
        implementation(Deps.sqldelight.coroutines)
      }
    }
    named("jvmMain") {
      dependencies {
        implementation(Deps.sqldelight.jvm)
        implementation(Deps.toothpick.ktp)
      }
    }
    named("androidMain") {
      dependencies {
        implementation(Deps.sqldelight.android)
        implementation(Deps.toothpick.ktp)
      }
    }
  }
}

sqldelight {
  database("Database") {
    packageName = "tachiyomi.data"
  }
}
