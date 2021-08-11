/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.updates

import androidx.compose.animation.Crossfade
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import tachiyomi.ui.core.components.LoadingScreen
import tachiyomi.ui.core.viewmodel.viewModel
import tachiyomi.ui.main.Route
import tachiyomi.ui.updates.components.UpdatesContent
import tachiyomi.ui.updates.components.UpdatesToolbar

@Composable
fun UpdatesScreen(navController: NavController) {
  val vm = viewModel<UpdatesViewModel, UpdatesState>(
    initialState = { UpdatesState() }
  )

  Scaffold(
    topBar = {
      UpdatesToolbar(
        state = vm,
        onClickCancelSelection = { vm.unselectAll() },
        onClickSelectAll = { vm.selectAll() },
        onClickFlipSelection = { vm.flipSelection() },
        onClickRefresh = { vm.updateLibrary() }
      )
    }
  ) {
    Crossfade(targetState = vm.isLoading) { isLoading ->
      when (isLoading) {
        true -> LoadingScreen()
        false -> UpdatesContent(
          state = vm,
          onClickItem = {
            when {
              vm.hasSelection -> vm.toggleManga(it)
              else -> navController.navigate("${Route.Reader.id}/${it.chapterId}")
            }
          },
          onLongClickItem = { vm.toggleManga(it) },
          onClickCover = { navController.navigate("${Route.LibraryManga.id}/${it.id}") },
          onClickDownload = { /* TOOD */ }
        )
      }
    }
  }
}
