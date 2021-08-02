/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.browse

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import tachiyomi.domain.catalog.model.Catalog
import tachiyomi.domain.catalog.model.CatalogBundled
import tachiyomi.domain.catalog.model.CatalogInstalled
import tachiyomi.domain.catalog.model.CatalogLocal
import tachiyomi.domain.catalog.model.CatalogRemote
import tachiyomi.domain.catalog.model.InstallStep
import tachiyomi.ui.core.components.EmojiText
import tachiyomi.ui.core.theme.RandomColors
import kotlin.math.max

@Composable
fun CatalogItem(
  catalog: Catalog,
  showInstallButton: Boolean = false,
  installStep: InstallStep? = null,
  onClick: () -> Unit,
  onInstall: () -> Unit,
  onUninstall: () -> Unit,
  onPinToggle: () -> Unit
) {
  val title = buildAnnotatedString {
    append("${catalog.name} ")
    val versionSpan = SpanStyle(
      fontSize = 12.sp,
      color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
    )
    if (catalog is CatalogInstalled) {
      withStyle(versionSpan) { append("v${catalog.versionCode}") }
    } else if (catalog is CatalogRemote) {
      withStyle(versionSpan) { append("v${catalog.versionCode}") }
    }
  }
  val lang = when (catalog) {
    is CatalogBundled -> null
    is CatalogInstalled -> catalog.source.lang
    is CatalogRemote -> catalog.lang
  }?.let { Language(it).toEmoji() }

  val rowModifier = if (catalog !is CatalogRemote) {
    Modifier.clickable(onClick = onClick)
  } else {
    Modifier
  }

  Layout(
    modifier = rowModifier,
    content = {
      CatalogPic(
        catalog = catalog,
        modifier = Modifier
          .layoutId("pic")
          .padding(12.dp)
          .size(48.dp)
      )
      if (lang != null) {
        EmojiText(
          text = lang,
          modifier = Modifier
            .layoutId("lang")
            .padding(8.dp)
        )
      }
      Text(
        text = title,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .layoutId("title")
          .padding(top = 12.dp)
      )
      Text(
        text = catalog.description,
        style = MaterialTheme.typography.body2,
        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .layoutId("desc")
          .padding(bottom = 12.dp, end = 12.dp)
      )
      CatalogButtons(
        catalog = catalog,
        showInstallButton = showInstallButton,
        installStep = installStep,
        onInstall = onInstall,
        onUninstall = onUninstall,
        onPinToggle = onPinToggle,
        modifier = Modifier
          .layoutId("icons")
          .padding(end = 4.dp)
      )
    },
    measurePolicy = { measurables, fullConstraints ->
      val picPlaceable = measurables.first { it.layoutId == "pic" }.measure(fullConstraints)
      val langPlaceable = measurables.find { it.layoutId == "lang" }?.measure(fullConstraints)

      val constraints = fullConstraints.copy(
        maxWidth = fullConstraints.maxWidth - picPlaceable.width
      )

      val iconsPlaceable = measurables.first { it.layoutId == "icons" }.measure(constraints)
      val titlePlaceable = measurables.first { it.layoutId == "title" }
        .measure(constraints.copy(maxWidth = constraints.maxWidth - iconsPlaceable.width))
      val descPlaceable = measurables.first { it.layoutId == "desc" }.measure(constraints)

      val height = max(picPlaceable.height, titlePlaceable.height + descPlaceable.height)

      layout(fullConstraints.maxWidth, height) {
        picPlaceable.placeRelative(0, 0)
        langPlaceable?.placeRelative(
          x = picPlaceable.width - langPlaceable.width,
          y = picPlaceable.height - langPlaceable.height
        )
        titlePlaceable.placeRelative(picPlaceable.width, 0)
        descPlaceable.placeRelative(picPlaceable.width, titlePlaceable.height)
        iconsPlaceable.placeRelative(
          x = constraints.maxWidth - iconsPlaceable.width + picPlaceable.width,
          y = 0
        )
      }
    }
  )
}

@Composable
private fun CatalogPic(catalog: Catalog, modifier: Modifier = Modifier) {
  when (catalog) {
    is CatalogBundled -> {
      val letter = catalog.name.take(1)
      Surface(
        modifier = modifier,
        shape = CircleShape, color = RandomColors.get(letter)
      ) {
        Text(
          text = letter,
          color = Color.White,
          modifier = Modifier.wrapContentSize(Alignment.Center),
          style = MaterialTheme.typography.h6
        )
      }
    }
    else -> {
      Image(
        painter = rememberImagePainter(catalog),
        contentDescription = null,
        modifier = modifier
      )
    }
  }
}

@Composable
private fun CatalogButtons(
  catalog: Catalog,
  showInstallButton: Boolean,
  installStep: InstallStep?,
  modifier: Modifier = Modifier,
  onInstall: () -> Unit,
  onUninstall: () -> Unit,
  onPinToggle: () -> Unit
) {
  Row(modifier = modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
      // Show either progress indicator or install button
      if (installStep != null && !installStep.isFinished()) {
        CircularProgressIndicator(
          modifier = Modifier
            .size(48.dp)
            .padding(12.dp)
        )
      } else if (showInstallButton) {
        IconButton(onClick = onInstall) {
          Icon(
            imageVector = Filled.GetApp,
            contentDescription = null
          )
        }
      }
      if (catalog !is CatalogRemote) {
        CatalogMenuButton(
          catalog = catalog,
          onUninstall = onUninstall,
          onPinToggle = onPinToggle
        )
      }
    }
  }
}

@Composable
private fun CatalogMenuButton(
  catalog: Catalog,
  modifier: Modifier = Modifier,
  onPinToggle: () -> Unit,
  onUninstall: () -> Unit
) {
  var expanded by remember { mutableStateOf(false) }
  Box(modifier) {
    IconButton(onClick = { expanded = true }) {
      Icon(imageVector = Filled.MoreVert, contentDescription = null)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      if (catalog is CatalogLocal) {
        DropdownMenuItem(onClick = { /*TODO*/ }) {
          Text("Details")
        }
        DropdownMenuItem(onClick = onPinToggle) {
          Text(if (!catalog.isPinned) "Pin" else "Unpin")
        }
      }
      if (catalog is CatalogInstalled) {
        DropdownMenuItem(onClick = onUninstall) {
          Text("Uninstall")
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun CatalogItemPreview() {
  CatalogItem(
    catalog = CatalogRemote(
      name = "My Catalog",
      description = "Some description",
      sourceId = 0L,
      pkgName = "my.catalog",
      versionName = "1.0.0",
      versionCode = 1,
      lang = "en",
      pkgUrl = "",
      iconUrl = "",
      nsfw = false,
    ),
    onClick = {},
    onInstall = {},
    onUninstall = {},
    onPinToggle = {}
  )
}
