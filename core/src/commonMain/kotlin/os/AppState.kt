package tachiyomi.core.os

import kotlinx.coroutines.flow.StateFlow

expect object AppState {

  val networkFlow: StateFlow<Boolean>

  val foregroundFlow: StateFlow<Boolean>

}
