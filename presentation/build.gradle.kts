plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
}

android {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    useIR = true
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = Deps.androidx.compose.version
  }
}

dependencies {
  implementationProject(Projects.core)
  implementationProject(Projects.sourceApi)
  implementationProject(Projects.domain)

  implementation(Deps.androidx.appCompat)
  implementation(Deps.androidx.browser)

  implementation(Deps.androidx.compose.compiler)
  implementation(Deps.androidx.compose.ui)
  implementation(Deps.androidx.compose.tooling)
  implementation(Deps.androidx.compose.material)
  implementation(Deps.androidx.compose.icons)
  implementation(Deps.androidx.compose.navigation)

  implementation(Deps.accompanist.pager)
  implementation(Deps.accompanist.pagerIndicator)
  implementation(Deps.accompanist.flowlayout)
  implementation(Deps.accompanist.insets)
  implementation(Deps.accompanist.systemUiController)
  implementation(Deps.accompanist.swipeRefresh)

  implementation(Deps.coil.core)
  implementation(Deps.coil.compose)

  implementation(Deps.toothpick.runtime)
  implementation(Deps.toothpick.ktp)
  kapt(Deps.toothpick.compiler)
}
