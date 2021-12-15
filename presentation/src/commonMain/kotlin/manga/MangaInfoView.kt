/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.manga

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import tachiyomi.domain.manga.model.Manga
import tachiyomi.i18n.MR
import tachiyomi.i18n.localize
import tachiyomi.source.Source
import tachiyomi.ui.core.components.BackIconButton
import tachiyomi.ui.core.components.DropdownMenu
import tachiyomi.ui.core.components.DropdownMenuItem
import tachiyomi.ui.core.components.FlowRow
import tachiyomi.ui.core.components.Toolbar
import tachiyomi.ui.core.image.rememberImagePainter
import tachiyomi.ui.core.manga.rememberMangaCover

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MangaInfoHeader(
  manga: Manga,
  source: Source?,
  expandedSummary: Boolean,
  navigateUp: () -> Unit,
  onFavorite: () -> Unit,
  onTracking: () -> Unit,
  onWebView: () -> Unit,
  onToggle: () -> Unit
) {
  Box(modifier = Modifier.height(IntrinsicSize.Min)) {
    val cover = rememberMangaCover(manga)

    var imageLoaded by remember { mutableStateOf(false) }
    val fadeInImage by animateFloatAsState(
      if (imageLoaded) 0.2f else 0f, tween(easing = LinearOutSlowInEasing)
    )

    Image(
      painter = rememberImagePainter(
        data = cover,
        builder = {
          listener(onSuccess = { _, _ ->
            imageLoaded = true
          })
        }
      ),
      contentDescription = null,
      modifier = Modifier
        .fillMaxSize()
        .alpha(fadeInImage),
      contentScale = ContentScale.Crop,
    )

    Box(
      Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(
          Brush.verticalGradient(
            listOf(
              Color.Transparent,
              MaterialTheme.colors.background,
            )
          )
        )
        .align(Alignment.BottomCenter)
    )

    var showDownloadMenu by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }

    Column {
      Toolbar(
        title = {},
        navigationIcon = { BackIconButton(navigateUp) },
        contentColor = MaterialTheme.colors.onBackground,
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        actions = {
          IconButton(
            onClick = { /* TODO(ghostbear): Add share intent */ },
          ) {
            Icon(Icons.Outlined.Share, null)
          }
          IconButton(
            onClick = { showDownloadMenu = true },
          ) {
            Icon(Icons.Outlined.Download, null)
          }
          DropdownMenu(
            expanded = showDownloadMenu,
            onDismissRequest = { showDownloadMenu = false },
            offset = DpOffset(48.dp, 0.dp)
          ) {
            DropdownMenuItem(onClick = { /*TODO*/ }) {
              Text(localize(MR.strings.download_one))
            }
            DropdownMenuItem(onClick = { /*TODO*/ }) {
              Text(localize(MR.strings.download_five))
            }
            DropdownMenuItem(onClick = { /*TODO*/ }) {
              Text(localize(MR.strings.download_ten))
            }
            DropdownMenuItem(onClick = { /*TODO*/ }) {
              Text(localize(MR.strings.download_custom))
            }
            DropdownMenuItem(onClick = { /*TODO*/ }) {
              Text(localize(MR.strings.download_unread))
            }
            DropdownMenuItem(onClick = { /*TODO*/ }) {
              Text(localize(MR.strings.download_all))
            }
          }
          IconButton(
            onClick = { showMoreMenu = true },
          ) {
            Icon(Icons.Outlined.MoreVert, null)
          }
          DropdownMenu(
            expanded = showMoreMenu,
            onDismissRequest = { showMoreMenu = false }
          ) {
            DropdownMenuItem(onClick = { /*TODO*/ }) {
              Text(localize(MR.strings.action_edit_categories))
            }
            DropdownMenuItem(onClick = { /*TODO*/ }) {
              Text(localize(MR.strings.action_migrate))
            }
          }
        }
      )

      // Cover + main info
      Row(modifier = Modifier.padding(top = 16.dp)) {
        Image(
          painter = rememberImagePainter(cover),
          contentDescription = null,
          modifier = Modifier
            .padding(16.dp)
            .weight(0.33f)
            .aspectRatio(3f / 4f)
            .clip(MaterialTheme.shapes.medium)
        )

        Column(
          modifier = Modifier
            .padding(bottom = 16.dp)
            .weight(0.67f)
            .align(Alignment.Bottom)
        ) {
          Text(manga.title, style = MaterialTheme.typography.h6, maxLines = 3)

          ProvideTextStyle(
            MaterialTheme.typography.body2.copy(
              color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
          ) {
            Text(manga.author, modifier = Modifier.padding(top = 4.dp))
            if (manga.artist.isNotBlank() && manga.artist != manga.author) {
              Text(manga.artist)
            }
            Row(
              modifier = Modifier.padding(top = 4.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(manga.status.toString())
              Text("•")
              Text(source?.name.orEmpty())
            }
          }
        }
      }
    }
  }

  // Action bar
  Row(modifier = Modifier.padding(horizontal = 16.dp)) {
    val activeButtonColors = ButtonDefaults.textButtonColors()
    val inactiveButtonColors = ButtonDefaults.textButtonColors(
      contentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    )

    TextButton(
      onClick = onFavorite,
      modifier = Modifier.weight(1f),
      colors = if (manga.favorite) activeButtonColors else inactiveButtonColors
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = if (manga.favorite) {
            Icons.Default.Favorite
          } else {
            Icons.Default.FavoriteBorder
          }, contentDescription = null
        )
        Text(if (manga.favorite) "In library" else "Add to library")
      }
    }
    TextButton(
      onClick = onTracking,
      modifier = Modifier.weight(1f),
      colors = inactiveButtonColors
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = Icons.Default.Sync, contentDescription = null)
        Text(localize(MR.strings.tracking_label))
      }
    }
    TextButton(
      onClick = onWebView,
      modifier = Modifier.weight(1f),
      colors = inactiveButtonColors
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = Icons.Default.Public, contentDescription = null)
        Text(localize(MR.strings.webview_label))
      }
    }
  }

  // Description
  Box(modifier = Modifier.animateContentSize()) {
    MangaSummary(
      onToggle,
      manga.description,
      manga.genres,
      expandedSummary
    )
  }
}

@Composable
private fun MangaSummary(
  onClickToggle: () -> Unit,
  description: String,
  genres: List<String>,
  expandedSummary: Boolean
) {
  var (isExpandable, setIsExpandable) = remember { mutableStateOf(false) }

  Column {
    MangaSummaryDescription(
      description,
      isExpandable,
      setIsExpandable,
      expandedSummary,
      onClickToggle
    )
    if (expandedSummary || !isExpandable) {
      FlowRow(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        mainAxisSpacing = 4.dp,
        crossAxisSpacing = 6.dp
      ) {
        genres.forEach { genre ->
          GenreChip(genre)
        }
      }
    } else {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        items(genres) { genre ->
          GenreChip(genre)
        }
      }
    }

  }
}

private val TextLayoutResult.textHeight: Int
  get() {
    return multiParagraph.height.toInt()
  }

const val COLLAPSED_MAX_LINES = 3

@Composable
fun MangaSummaryDescription(
  description: String,
  isExpandable: Boolean,
  setIsExpandable: (Boolean) -> Unit,
  isExpanded: Boolean,
  onClickToggle: () -> Unit
) {
  var collapseMaxHeight by remember { mutableStateOf(0) }

  SubcomposeLayout(
    modifier = Modifier
      .clipToBounds()
      .clickable(enabled = isExpandable, onClick = onClickToggle),
    measurePolicy = { constraints ->
      val isCollapsed = !isExpanded
      val icon = if (isCollapsed) Icons.Outlined.ExpandMore else Icons.Outlined.ExpandLess
      var slotId = 0

      val textPlaceable = subcompose(slotId++) {
        Text(
          text = description,
          modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp),
          onTextLayout = { layoutResult ->
            setIsExpandable(layoutResult.lineCount > COLLAPSED_MAX_LINES)
            collapseMaxHeight =
              (layoutResult.textHeight / layoutResult.lineCount) * COLLAPSED_MAX_LINES
          }
        )
      }
        .first()
        .measure(constraints)

      if (!isExpandable) {
        return@SubcomposeLayout layout(constraints.maxWidth, textPlaceable.height) {
          textPlaceable.placeRelative(0, 0)
        }
      }

      val buttonPlaceable = subcompose(slotId++) {
        IconButton(
          onClick = onClickToggle
        ) {
          Icon(icon, null)
        }
      }
        .first()
        .measure(constraints)

      var height = if (isCollapsed) collapseMaxHeight else textPlaceable.height
      height += if (isCollapsed) buttonPlaceable.height / 2 else buttonPlaceable.height

      val boxConstraints = constraints.copy(
        maxHeight = height / 2
      )
      val boxPlaceable = subcompose(slotId++) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                0f to Color.Transparent,
                0.4f to MaterialTheme.colors.background.copy(alpha = 0.9f),
                0.5f to MaterialTheme.colors.background
              )
            )
        )
      }
        .first()
        .measure(boxConstraints)

      layout(constraints.maxWidth, height) {
        textPlaceable.placeRelative(0, 0)
        if (isCollapsed) {
          boxPlaceable.placeRelative(0, height - boxPlaceable.height)
        }
        buttonPlaceable.placeRelative(
          x = constraints.maxWidth / 2 - buttonPlaceable.width / 2,
          y = height - buttonPlaceable.height
        )
      }
    }
  )
}

@Composable
private fun GenreChip(genre: String) {
  Surface(
    shape = CircleShape,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary)
  ) {
    Text(
      genre,
      color = MaterialTheme.colors.primary,
      style = MaterialTheme.typography.caption,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    )
  }
}
