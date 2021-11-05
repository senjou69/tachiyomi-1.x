/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

@file:JvmName("AlertDialogPlatform")

package tachiyomi.ui.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.Dialog

@Composable
internal actual fun PlatformAlertDialog(
  onDismissRequest: () -> Unit,
  buttons: @Composable () -> Unit,
  modifier: Modifier,
  title: @Composable (() -> Unit)?,
  text: @Composable (() -> Unit)?,
  shape: Shape,
  backgroundColor: Color,
  contentColor: Color,
  properties: DialogProperties
) {
  Dialog(
    onCloseRequest = onDismissRequest
  ) {
    Column(modifier = modifier) {
      title?.invoke()
      text?.invoke()
      buttons.invoke()
    }
  }
}
