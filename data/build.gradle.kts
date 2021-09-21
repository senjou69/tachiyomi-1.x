plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("kotlin-kapt")
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
      }
    }
    named("androidMain") {
      dependencies {
        implementation(Deps.sqldelight.android)
        implementation(Deps.requerySqlite)
        implementation(Deps.androidx.workManager.runtime)
      }
      project.dependencies {
        compileOnly(Deps.androidSqlite)
        debugImplementation(Deps.androidSqlite)
      }
    }
    listOf("jvmMain", "androidMain").forEach {
      getByName(it) {
        dependencies {
          implementation(project(Module.sourceApi))
          implementation(Deps.toothpick.ktp)
        }
        project.dependencies {
          add("kapt", Deps.toothpick.compiler)
        }
      }
    }
  }
}

sqldelight {
  database("Database") {
    packageName = "tachiyomi.data"
    dialect = "sqlite:3.24"
  }
}
