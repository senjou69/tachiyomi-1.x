package tachiyomi.ui.core.components

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
}

@Composable
actual fun BackHandler(onBack: () -> Unit) {
}
