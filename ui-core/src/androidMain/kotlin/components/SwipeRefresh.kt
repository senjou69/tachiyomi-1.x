/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.core.components

import androidx.compose.runtime.Composable

actual typealias SwipeRefreshState = com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
actual fun rememberSwipeRefreshState(isRefreshing: Boolean): SwipeRefreshState {
  return com.google.accompanist.swiperefresh.rememberSwipeRefreshState(isRefreshing)
}

@Composable
actual fun SwipeRefresh(
  state: SwipeRefreshState,
  onRefresh: () -> Unit,
  content: @Composable () -> Unit
) {
  com.google.accompanist.swiperefresh.SwipeRefresh(
    state = state,
    onRefresh = onRefresh,
    content = content
  )
}
