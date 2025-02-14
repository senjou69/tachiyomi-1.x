/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.core.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.selectedBackground(isSelected: Boolean): Modifier = composed {
  if (isSelected) {
    val alpha = if (isSystemInDarkTheme()) 0.08f else 0.22f
    background(MaterialTheme.colors.onBackground.copy(alpha = alpha))
  } else {
    this
  }
}
