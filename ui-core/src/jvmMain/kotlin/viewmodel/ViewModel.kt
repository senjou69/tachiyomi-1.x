/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.core.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver

@Composable
actual inline fun <reified VM : BaseViewModel> viewModel(): VM {
  TODO("not implemented")
}

@Composable
actual inline fun <reified VM : BaseViewModel, S : Any> viewModel(
  noinline initialState: () -> S
): VM {
  TODO("not implemented")
}

@Composable
actual inline fun <reified VM : BaseViewModel, S : Any> viewModel(
  noinline initialState: () -> S,
  saver: Saver<S, Any>
): VM {
  TODO("not implemented")
}
