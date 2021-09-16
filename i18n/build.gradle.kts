plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
  jvm()
  android()

  sourceSets {
    named("commonMain") {
      dependencies {
        compileOnly(compose.runtime)
        api(Deps.moko.core)
      }
    }
    named("jvmMain") {
    }
    named("androidMain") {
      dependencies {
        compileOnly(compose.ui)
      }
    }
  }
}

multiplatformResources {
  multiplatformResourcesPackage = "tachiyomi.i18n"
}
