/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.history

import androidx.compose.animation.Crossfade
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import tachiyomi.ui.core.components.LoadingScreen
import tachiyomi.ui.core.viewmodel.viewModel
import tachiyomi.ui.history.components.HistoryContent
import tachiyomi.ui.history.components.HistoryToolbar
import tachiyomi.ui.main.Route

@Composable
fun HistoryScreen(navController: NavController) {
  val vm = viewModel<HistoryViewModel, HistoryState>(
    initialState = { HistoryState() }
  )

  Scaffold(
    topBar = {
      HistoryToolbar(
        state = vm,
        onClickDeleteAll = { vm.deleteAll() }
      )
    }
  ) {
    Crossfade(targetState = vm.isLoading) { isLoading ->
      when (isLoading) {
        true -> LoadingScreen()
        false -> HistoryContent(
          state = vm,
          onClickItem = { navController.navigate("${Route.LibraryManga.id}/${it.id}") },
          onClickDelete = { vm.delete(it) },
          onClickPlay = { navController.navigate("${Route.Reader.id}/${it.id}") }
        )
      }
    }
  }
}

