package tachiyomi.ui.core.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tachiyomi.core.prefs.Preference
import tachiyomi.ui.core.prefs.PreferenceMutableState

expect abstract class BaseViewModel : ViewModelMixin {

  override val scope: CoroutineScope

  open fun onDestroy()

}

interface ViewModelMixin {

  val scope: CoroutineScope

  fun <T> Preference<T>.asState() = PreferenceMutableState(this, scope)

  fun <T> Flow<T>.asState(initialValue: T): State<T> {
    val state = mutableStateOf(initialValue)
    scope.launch {
      collect { state.value = it }
    }
    return state
  }

  fun <T> StateFlow<T>.asState(): State<T> {
    val state = mutableStateOf(value)
    scope.launch {
      collect { state.value = it }
    }
    return state
  }

}
