/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.core.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

actual abstract class BaseViewModel : androidx.lifecycle.ViewModel(), ViewModelMixin {

  actual override val scope: CoroutineScope
    get() = viewModelScope

  private val activeScope = MutableStateFlow<CoroutineScope?>(null)

  final override fun onCleared() {
    onDestroy()
  }

  actual open fun onDestroy() {
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
