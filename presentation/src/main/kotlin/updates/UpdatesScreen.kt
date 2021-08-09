/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.updates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import tachiyomi.ui.R
import tachiyomi.ui.core.coil.rememberMangaCover
import tachiyomi.ui.core.components.MangaListItem
import tachiyomi.ui.core.components.MangaListItemColumn
import tachiyomi.ui.core.components.MangaListItemImage
import tachiyomi.ui.core.components.MangaListItemSubtitle
import tachiyomi.ui.core.components.MangaListItemTitle
import tachiyomi.ui.core.components.selectedBackground
import tachiyomi.ui.core.util.getRelativeTimeString
import tachiyomi.ui.core.viewmodel.viewModel
import tachiyomi.ui.main.Route
import tachiyomi.domain.updates.model.UpdatesManga as Manga

@Composable
fun UpdatesScreen(navController: NavController) {
  val vm = viewModel<UpdatesViewModel>()

  Scaffold(
    topBar = {
      UpdatesToolbar(
        selectedManga = vm.selectedManga,
        selectionMode = vm.selectionMode,
        onClickCancelSelection = { vm.unselectAll() },
        onClickSelectAll = { vm.selectAll() },
        onClickFlipSelection = { vm.flipSelection() },
        onClickRefresh = { vm.updateLibrary() }
      )
    }
  ) {
    LazyColumn(
      contentPadding = rememberInsetsPaddingValues(
        insets = LocalWindowInsets.current.navigationBars,
        additionalBottom = 16.dp,
        additionalTop = 8.dp
      )
    ) {
      vm.updatesMap.forEach { (date, updates) ->
        stickyHeader {
          Text(
            text = date.getRelativeTimeString(),
            modifier = Modifier
              .background(MaterialTheme.colors.background)
              .padding(horizontal = 16.dp, vertical = 4.dp)
              .fillMaxWidth(),
            color = MaterialTheme.colors.onBackground
          )
        }

        items(updates) { manga ->
          UpdatesItem(
            manga = manga,
            isSelected = manga.chapterId in vm.selectedManga,
            onLongClickItem = { vm.toggleManga(manga.chapterId) },
            onClickCover = { navController.navigate("${Route.LibraryManga.id}/${manga.id}") }
          )
        }
      }
    }
  }
}

@Composable
fun UpdatesItem(
  manga: Manga,
  isSelected: Boolean,
  onClickItem: (Manga) -> Unit = { /* TODO Open chapter in reader */ },
  onLongClickItem: (Manga) -> Unit,
  onClickCover: (Manga) -> Unit,
  onClickDownload: (Manga) -> Unit = { /* TODO */ }
) {
  MangaListItem(
    modifier = Modifier
      .combinedClickable(onClick = { onClickItem(manga) }, onLongClick = { onLongClickItem(manga) })
      .selectedBackground(isSelected)
      .height(56.dp)
      .fillMaxWidth()
      .padding(end = 4.dp)
  ) {
    MangaListItemImage(
      modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        .clip(MaterialTheme.shapes.medium)
        .clickable { onClickCover(manga) },
      mangaCover = rememberMangaCover(manga)
    )
    MangaListItemColumn(
      modifier = Modifier
        .weight(1f)
        .padding(start = 16.dp)
    ) {
      MangaListItemTitle(
        text = manga.title,
        fontWeight = FontWeight.SemiBold
      )
      MangaListItemSubtitle(
        text = stringResource(R.string.updates_subtitle, manga.number, manga.name)
      )
    }
    // TODO Replace with Download Composable when that is implemented
    IconButton(onClick = { onClickDownload(manga) }) {
      Icon(imageVector = Icons.Outlined.Download, contentDescription = "")
    }
  }
}
