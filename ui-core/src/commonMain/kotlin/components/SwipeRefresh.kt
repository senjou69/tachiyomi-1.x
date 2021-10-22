/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.core.components

import androidx.compose.runtime.Composable

// TODO(inorichi): this should be removed when accompanist moves to multiplatform
expect class SwipeRefreshState {
  var isRefreshing: Boolean
}

@Composable
expect fun rememberSwipeRefreshState(isRefreshing: Boolean): SwipeRefreshState

@Composable
expect fun SwipeRefresh(
  state: SwipeRefreshState,
  onRefresh: () -> Unit,
  content: @Composable () -> Unit
)
