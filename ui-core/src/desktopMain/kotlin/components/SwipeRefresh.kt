package tachiyomi.ui.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

actual class SwipeRefreshState(actual var isRefreshing: Boolean)

@Composable
actual fun rememberSwipeRefreshState(isRefreshing: Boolean): SwipeRefreshState {
  return remember { SwipeRefreshState(isRefreshing) }
}

@Composable
actual fun SwipeRefresh(
  state: SwipeRefreshState,
  onRefresh: () -> Unit,
  content: @Composable () -> Unit
) {
  content()
}
