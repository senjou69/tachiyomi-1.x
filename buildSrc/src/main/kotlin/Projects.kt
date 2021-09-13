import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

@Suppress("EnumEntryName")
enum class Projects(val path: String) {
  // Core modules
  common(":common"),
  domain(":domain"),
  sourceApi(":source-api"),
  uiCore(":ui-core"),

  // Tachiyomi specific modules
  `data`(":data"),
  presentation(":presentation"),
  app(":app")
}

fun DependencyHandler.apiProject(lib: Projects) {
  add("api", project(lib.path))
}

fun DependencyHandler.implementationProject(lib: Projects) {
  add("implementation", project(lib.path))
}
