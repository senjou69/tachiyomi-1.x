plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("com.mikepenz.aboutlibraries.plugin")
  id("kotlin-kapt")
  id("org.jetbrains.compose")
  id("org.jetbrains.gradle.plugin.idea-ext")
}

kotlin {
  android()
  jvm()
  sourceSets {
    named("commonMain") {
      dependencies {
        implementation(Deps.androidx.compose.material)
        implementation(Deps.androidx.compose.tooling)
        implementation(project(Module.uiCore))
      }
    }
    named("jvmMain") {
      dependencies {
        compileOnly(compose.desktop.currentOs)
      }
    }
    named("androidMain") {
      kotlin.srcDir("src/sharedJvmMain/kotlin")
      dependencies {
        implementation(Deps.androidx.browser)
        implementation(Deps.androidx.webkit)
        implementation(Deps.androidx.emoji)

        implementation(Deps.androidx.compose.ui)
        implementation(Deps.androidx.compose.icons)
        implementation(Deps.androidx.compose.navigation)

        implementation(Deps.accompanist.pager)
        implementation(Deps.accompanist.pagerIndicator)
        implementation(Deps.accompanist.flowlayout)
        implementation(Deps.accompanist.insets)
        implementation(Deps.accompanist.systemUiController)
        implementation(Deps.accompanist.swipeRefresh)
        implementation(Deps.accompanist.navAnimation)

        implementation(Deps.coil.core)
        implementation(Deps.coil.compose)

        implementation(Deps.toothpick.runtime)
        implementation(Deps.toothpick.ktp)

        implementation(Deps.aboutLibraries.core)
      }
      project.dependencies.apply {
        implementationProject(Projects.core)
        implementationProject(Projects.sourceApi)
        implementationProject(Projects.domain)
        add("kapt", Deps.toothpick.compiler)
      }
    }
  }
}

idea {
  module {
    (this as ExtensionAware).configure<org.jetbrains.gradle.ext.ModuleSettings> {
      (this as ExtensionAware).configure<org.jetbrains.gradle.ext.PackagePrefixContainer> {
        arrayOf(
          "src/commonMain/kotlin",
          "src/androidMain/kotlin",
          "src/jvmMain/kotlin",
          "src/sharedJvmMain/kotlin",
          "src/sharedAndroidMain/kotlin"
        ).forEach { put(it, "tachiyomi.ui") }
      }
    }
  }
}
