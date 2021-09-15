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
