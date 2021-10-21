/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import tachiyomi.ui.MainApp
import kotlin.system.exitProcess

fun main() = application {
  Window(
    onCloseRequest = { exitProcess(0) },
    title = "Tachiyomi for Desktop",
    state = rememberWindowState()
  ) {
    MainApp()
  }
}
