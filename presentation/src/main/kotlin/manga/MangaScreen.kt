/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.manga

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import tachiyomi.ui.core.components.BackIconButton
import tachiyomi.ui.core.components.LoadingScreen
import tachiyomi.ui.core.components.Toolbar
import tachiyomi.ui.core.theme.TransparentStatusBar
import tachiyomi.ui.core.viewmodel.viewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MangaScreen(
  mangaId: Long,
  navigateUp: () -> Unit,
  openChapter: (Long) -> Unit,
  openWebView: (Long, String) -> Unit
) {
  val vm = viewModel<MangaViewModel, MangaViewModel.Params>(
    initialState = { MangaViewModel.Params(mangaId) }
  )

  val manga = vm.manga
  if (manga == null) {
    // TODO: loading UX
    TransparentStatusBar {
      Column {
        Toolbar(
          title = {},
          navigationIcon = { BackIconButton(navigateUp) },
          contentColor = MaterialTheme.colors.onBackground,
          backgroundColor = Color.Transparent,
          elevation = 0.dp
        )
        LoadingScreen()
      }
    }
    return
  }

  // TODO
  val onTracking = {}
  val onWebView = {
    val url = URLEncoder.encode(manga.key, StandardCharsets.UTF_8.toString())
    openWebView(manga.sourceId, url)
  }
  val onFavorite = { vm.toggleFavorite() }
  val onToggle = { vm.toggleExpandedSummary() }

  TransparentStatusBar {
    SwipeRefresh(
      state = rememberSwipeRefreshState(vm.isRefreshing),
      onRefresh = { vm.updateManga(metadata = true, chapters = true, tracking = true) },
      // TODO(inorichi) the following parameters are the default values, however they all have to
      //  be provided explicitly for now, because the app crashes without them (at least for me)
      swipeEnabled = true,
      modifier = Modifier,
      indicatorAlignment = Alignment.TopCenter,
      indicatorPadding = PaddingValues(0.dp),
      indicator = { s, trigger -> SwipeRefreshIndicator(s, trigger) }
    ) {
      LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
          MangaInfoHeader(
            manga,
            vm.source,
            vm.expandedSummary,
            navigateUp,
            onFavorite,
            onTracking,
            onWebView,
            onToggle
          )
        }

        item {
          ChapterHeader(vm.chapters, {})
        }

        items(vm.chapters) { chapter ->
          ChapterRow(
            chapter = chapter,
            isDownloaded = false,
            onClick = { openChapter(chapter.id) },
            onDownloadClick = {},
            onDeleteClick = {},
          )
        }
      }
    }
  }
}
