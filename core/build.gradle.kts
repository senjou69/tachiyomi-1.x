plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("kotlin-kapt")
  id("org.jetbrains.gradle.plugin.idea-ext")
  `maven-publish`
  signing
}

kotlin {
  jvm()
  android()

  sourceSets {
    named("commonMain") {
      dependencies {
        api(Deps.kotlin.coroutines.core)
        api(Deps.kotlin.stdlib)
        api(Deps.kotlin.datetime)
        api(Deps.kotlin.serialization.json)
        implementation(Deps.okio)
      }
    }
    named("jvmMain") {
      kotlin.srcDir("src/sharedJvmMain/kotlin")
    }
    named("androidMain") {
      kotlin.srcDir("src/sharedAndroidMain/kotlin")
      dependencies {
        implementation(Deps.androidx.core)
        implementation(Deps.androidx.lifecycle.process)
        implementation(Deps.androidx.dataStore)
        implementation(Deps.quickjs)
      }
    }
    listOf("jvmMain", "androidMain").forEach { name ->
      getByName(name) {
        dependencies {
          api(Deps.okhttp)
          api(Deps.jsoup)
          implementation(Deps.tinylog.api)
          implementation(Deps.tinylog.impl)
          implementation(Deps.toothpick.ktp)
        }
        project.dependencies {
          add("kapt", Deps.toothpick.compiler)
        }
      }
    }
  }
}

val packageVersion = "1.1"

publishing {
  publications {
    create<MavenPublication>("publication") {
      groupId = "org.tachiyomi"
      artifactId = "common"
      version = packageVersion
      pom {
        name.set("Tachiyomi Common")
        description.set("Common classes for Tachiyomi.")
        url.set("https://github.com/tachiyomiorg/tachiyomi-1.x")
        licenses {
          license {
            name.set("Mozilla Public License 2.0")
            url.set("https://www.mozilla.org/en-US/MPL/2.0/")
          }
        }
        developers {
          developer {
            id.set("inorichi")
            name.set("Javier Tomás")
            email.set("len@kanade.eu")
          }
        }
        scm {
          connection.set("scm:git:git:github.com:tachiyomiorg/tachiyomi-1.x.git")
          developerConnection.set("scm:git:github.com:tachiyomiorg/tachiyomi-1.x.git")
          url.set("https://github.com/tachiyomiorg/tachiyomi-1.x")
        }
      }
    }
  }
  repositories {
    maven {
      val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
      val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
      url = uri(if (packageVersion.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

      credentials {
        username = System.getenv("SONATYPE_USER")
        password = System.getenv("SONATYPE_PASS")
      }
    }
  }
}

signing {
  sign(publishing.publications["publication"])
}

idea {
  module {
    (this as ExtensionAware).configure<org.jetbrains.gradle.ext.ModuleSettings> {
      (this as ExtensionAware).configure<org.jetbrains.gradle.ext.PackagePrefixContainer> {
        arrayOf(
          "src/commonMain/kotlin",
          "src/jvmMain/kotlin",
          "src/androidMain/kotlin",
          "src/sharedJvmMain/kotlin",
          "src/sharedAndroidMain/kotlin"
        ).forEach { put(it, "tachiyomi.core") }
      }
    }
  }
}
