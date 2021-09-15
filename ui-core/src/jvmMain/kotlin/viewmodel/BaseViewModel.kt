package tachiyomi.ui.core.viewmodel

import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import tachiyomi.core.prefs.Preference
import tachiyomi.ui.core.prefs.PreferenceMutableState

actual abstract class BaseViewModel {

  protected actual val scope: CoroutineScope
    get() = TODO("Not yet implemented")

  actual open fun onDestroy() {
  }

  actual fun <T> Preference<T>.asState(): PreferenceMutableState<T> {
    TODO("not implemented")
  }

  actual fun <T> Flow<T>.asState(initialValue: T): State<T> {
    TODO("not implemented")
  }

  actual fun <T> StateFlow<T>.asState(): State<T> {
    TODO("not implemented")
  }

  actual fun <T> Flow<T>.launchWhileActive(): Job {
    TODO("not implemented")
  }

}
