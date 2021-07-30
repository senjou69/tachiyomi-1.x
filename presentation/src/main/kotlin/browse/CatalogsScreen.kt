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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import tachiyomi.domain.catalog.model.Catalog
import tachiyomi.domain.catalog.model.CatalogBundled
import tachiyomi.domain.catalog.model.CatalogInstalled
import tachiyomi.domain.catalog.model.CatalogLocal
import tachiyomi.domain.catalog.model.CatalogRemote
import tachiyomi.domain.catalog.model.InstallStep
import tachiyomi.ui.R
import tachiyomi.ui.core.components.EmojiText
import tachiyomi.ui.core.components.Toolbar
import tachiyomi.ui.core.theme.RandomColors
import tachiyomi.ui.core.viewmodel.viewModel
import tachiyomi.ui.main.Route
import kotlin.math.max

@Composable
fun CatalogsScreen(navController: NavController) {
  val vm = viewModel<CatalogsViewModel>()

  val onClick: (Catalog) -> Unit = {
    navController.navigate("${Route.BrowseCatalog.id}/${it.sourceId}")
  }

  Scaffold(
    topBar = {
      Toolbar(
        title = { Text(stringResource(R.string.browse_label)) },
        actions = {
          IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Search, contentDescription = null)
          }
          IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.MoreVert, contentDescription = null)
          }
        }
      )
    }
  ) {
    LazyColumn {
      if (vm.updatableCatalogs.isNotEmpty() || vm.localCatalogs.isNotEmpty()) {
        item {
          Text(
            "Installed".uppercase(),
            style = MaterialTheme.typography.subtitle2,
            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 4.dp)
          )
        }
      }
      if (vm.updatableCatalogs.isNotEmpty()) {
        items(vm.updatableCatalogs) { catalog ->
          CatalogItem(
            catalog = catalog,
            showInstallButton = true,
            installStep = vm.installSteps[catalog.pkgName],
            onClick = { onClick(catalog) },
            onInstall = { vm.installCatalog(catalog) },
            onUninstall = { vm.uninstallCatalog(catalog) }
          )
        }
      }
      if (vm.localCatalogs.isNotEmpty()) {
        items(vm.localCatalogs) { catalog ->
          CatalogItem(
            catalog = catalog,
            showInstallButton = false,
            installStep = null,
            onClick = { onClick(catalog) },
            onInstall = { vm.installCatalog(catalog) },
            onUninstall = { vm.uninstallCatalog(catalog) }
          )
        }
      }
      if (vm.remoteCatalogs.isNotEmpty()) {
        item {
          Text(
            "Available".uppercase(),
            style = MaterialTheme.typography.subtitle2,
            color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 4.dp)
          )
        }

        item {
          LazyRow(modifier = Modifier.padding(8.dp)) {
            items(vm.languageChoices) { choice ->
              LanguageChip(
                choice = choice,
                isSelected = choice == vm.selectedLanguage,
                onClick = { vm.setLanguageChoice(choice) }
              )
            }
          }
        }

        items(vm.remoteCatalogs) { catalog ->
          CatalogItem(
            catalog = catalog,
            showInstallButton = true,
            installStep = vm.installSteps[catalog.pkgName],
            onClick = { onClick(catalog) },
            onInstall = { vm.installCatalog(catalog) },
            onUninstall = { }
          )
        }
      }
    }
  }
}

@Composable
fun LanguageChip(choice: LanguageChoice, isSelected: Boolean, onClick: () -> Unit) {
  Surface(
    color = if (isSelected) {
      MaterialTheme.colors.primary
    } else {
      MaterialTheme.colors.onSurface.copy(alpha = 0.25f)
    },
    modifier = Modifier
      .widthIn(min = 56.dp)
      .requiredHeight(40.dp)
      .padding(4.dp)
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
  ) {
    val text = when (choice) {
      LanguageChoice.All -> stringResource(R.string.lang_all)
      is LanguageChoice.One -> choice.language.toEmoji() ?: ""
      is LanguageChoice.Others -> stringResource(R.string.lang_others)
    }
    if (choice is LanguageChoice.One) {
      EmojiText(text, modifier = Modifier.wrapContentSize(Alignment.Center))
    } else {
      Text(
        text = text,
        modifier = Modifier.wrapContentSize(Alignment.Center),
        color = if (isSelected) {
          MaterialTheme.colors.onPrimary
        } else {
          Color.Black
        }
      )
    }
  }
}

@Composable
fun CatalogItem(
  catalog: Catalog,
  showInstallButton: Boolean = false,
  installStep: InstallStep? = null,
  onClick: () -> Unit,
  onInstall: () -> Unit,
  onUninstall: () -> Unit
) {
  val mediumColor = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
  val title = buildAnnotatedString {
    append("${catalog.name} ")
    val versionSpan = SpanStyle(fontSize = 12.sp, color = mediumColor)
    if (catalog is CatalogInstalled) {
      withStyle(versionSpan) { append("v${catalog.versionCode}") }
    } else if (catalog is CatalogRemote) {
      withStyle(versionSpan) { append("v${catalog.versionCode}") }
    }
  }

  Layout(
    modifier = Modifier.clickable(onClick = onClick),
    content = {
      CatalogPic(
        catalog = catalog,
        modifier = Modifier
          .layoutId("pic")
          .padding(12.dp)
          .size(48.dp)
      )
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
        color = mediumColor,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .layoutId("desc")
          .padding(bottom = 12.dp, end = 12.dp)
      )
      Row(modifier = Modifier.layoutId("icons").padding(end = 4.dp)) {
        // Show either progress indicator or install button
        if (installStep != null && !installStep.isFinished()) {
          CircularProgressIndicator(
            modifier = Modifier
              .size(48.dp)
              .padding(4.dp)
          )
        } else if (showInstallButton) {
          IconButton(onClick = onInstall) {
            Icon(
              imageVector = Icons.Filled.GetApp,
              contentDescription = null,
              tint = LocalContentColor.current.copy(ContentAlpha.medium)
            )
          }
        }

        var expanded by remember { mutableStateOf(false) }
        Box {
          IconButton(onClick = { expanded = true }) {
            Icon(
              imageVector = Icons.Filled.MoreVert,
              contentDescription = null,
              tint = LocalContentColor.current.copy(ContentAlpha.medium)
            )
          }
          DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (catalog is CatalogLocal) {
              DropdownMenuItem(onClick = { /*TODO*/ }) {
                Text("Open catalog profile")
              }
              DropdownMenuItem(onClick = { /*TODO*/ }) {
                Text("Pin")
              }
            }
            if (catalog is CatalogInstalled) {
              DropdownMenuItem(onClick = { /*TODO*/ }) {
                Text("Uninstall")
              }
            }
          }
        }
      }
    },
    measurePolicy = { measurables, fullConstraints ->
      val picPlaceable = measurables.first { it.layoutId == "pic" }.measure(fullConstraints)
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
        titlePlaceable.placeRelative(picPlaceable.width, 0)
        descPlaceable.placeRelative(picPlaceable.width, titlePlaceable.height)
        iconsPlaceable.placeRelative(
          x = constraints.maxWidth - iconsPlaceable.width + picPlaceable.width,
          y = -2.dp.roundToPx()
        )
      }
    }
  )
}

@Composable
fun CatalogPic(catalog: Catalog, modifier: Modifier = Modifier) {
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
  )
}
