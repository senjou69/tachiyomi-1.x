package tachiyomi.ui.core.components

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.selectedBackground(isSelected: Boolean): Modifier = composed {
  if (isSelected) {
    background(MaterialTheme.colors.onBackground.copy(alpha = 0.2f))
  } else {
    this
  }
}
