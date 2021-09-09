/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

@file:Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package tachiyomi.ui.core.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import tachiyomi.core.di.AppScope
import tachiyomi.core.di.close
import toothpick.ktp.binding.module

@Composable
inline fun <reified VM : BaseViewModel> viewModel(): VM {
  val factory = ViewModelFactory()
  return viewModel(VM::class.java, factory)
}

@Composable
inline fun <reified VM : BaseViewModel> viewModel(
  crossinline binding: @DisallowComposableCalls () -> Any,
): VM {
  val state = remember { binding() }
  val factory = remember { ViewModelWithStateFactory(state) }
  return viewModel(VM::class.java, factory)
}

@Composable
inline fun <reified VM : BaseViewModel, S : Any> viewModel(
  noinline initialState: () -> S,
  saver: Saver<S, Any>? = null
): VM {
  val state = if (saver != null) {
    rememberSaveable(init = initialState, saver = saver)
  } else {
    remember(calculation = initialState)
  }

  val factory = ViewModelWithStateFactory(state = state)
  return viewModel(VM::class.java, factory)
}

@Composable
private fun <VM : BaseViewModel> viewModel(
  vmClass: Class<VM>,
  factory: ViewModelProvider.Factory,
  viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
): VM {
  return ViewModelProvider(viewModelStoreOwner, factory).get(vmClass)
}

internal class ViewModelFactory : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return AppScope.getInstance(modelClass)
  }
}

internal class ViewModelWithStateFactory(
  private val state: Any
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    val submodule = module {
      bind(state.javaClass).toInstance(state)
    }
    val subscope = AppScope.subscope(submodule).apply {
      installModules(submodule)
    }

    val viewModel = subscope.getInstance(modelClass)

    callbackFlow<Nothing> {
      awaitClose { subscope.close() }
    }.launchIn(viewModel.viewModelScope)

    return viewModel
  }

}
