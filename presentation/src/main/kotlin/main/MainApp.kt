/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import tachiyomi.domain.ui.UiPreferences
import tachiyomi.ui.core.theme.AppTheme
import tachiyomi.ui.core.viewmodel.BaseViewModel
import tachiyomi.ui.core.viewmodel.viewModel
import javax.inject.Inject

@Composable
fun MainApp() {
  val vm = viewModel<MainAppViewModel>()
  AppTheme {
    MainNavHost(vm.startRoute)
    ConfirmExitBackHandler(vm.confirmExit)
  }
}

class MainAppViewModel @Inject internal constructor(
  uiPrefs: UiPreferences
) : BaseViewModel() {

  val startRoute = uiPrefs.startScreen().get()
  val confirmExit by uiPrefs.confirmExit().asState()
}
