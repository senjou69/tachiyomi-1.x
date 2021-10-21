plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
  android()
  jvm("desktop")

  sourceSets {
    named("commonMain") {
      dependencies {
        compileOnly(compose.runtime)
        api(Deps.moko.core)
      }
    }
    named("androidMain") {
      dependencies {
        compileOnly(compose.ui)
      }
    }
    named("desktopMain") {
    }
  }
}

multiplatformResources {
  multiplatformResourcesPackage = "tachiyomi.i18n"
}
