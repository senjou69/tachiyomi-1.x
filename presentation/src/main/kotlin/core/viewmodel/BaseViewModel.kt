/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.core.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tachiyomi.core.prefs.Preference
import tachiyomi.ui.core.prefs.PreferenceMutableState

abstract class BaseViewModel : ViewModel() {

  protected val scope
    get() = viewModelScope

  private val activeScope = MutableStateFlow<CoroutineScope?>(null)

  final override fun onCleared() {
    onDestroy()
  }

  open fun onDestroy() {
  }

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

  fun <T> Flow<T>.launchWhileActive(): Job {
    return activeScope
      .filterNotNull()
      .onEach { launchIn(it) }
      .launchIn(scope)
  }

  internal fun setActive() {
    val currScope = activeScope.value
    if (currScope != null) return
    activeScope.value = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
  }

  internal fun setInactive() {
    val currScope = activeScope.value
    currScope?.cancel()
    activeScope.value = null
  }

}
