/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tachiyomi.i18n.MR
import tachiyomi.i18n.localize
import tachiyomi.ui.core.util.toast

@Composable
fun ConfirmExitBackHandler(confirmExit: Boolean) {
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  val message = localize(MR.strings.confirm_exit_message)

  var isConfirmingExit by remember { mutableStateOf(false) }

  // Always install the back handler even if the preference is not active because the order of
  // installation matters and when the setting is enabled it'd be placed at the top, overriding
  // the navigation back handler.
  BackHandler(enabled = confirmExit && !isConfirmingExit) {
    isConfirmingExit = true
    context.toast(message, Toast.LENGTH_LONG)
    scope.launch {
      delay(2000)
      isConfirmingExit = false
    }
  }
}
