/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.browse

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import tachiyomi.domain.catalog.model.Catalog
import tachiyomi.ui.R
import tachiyomi.ui.core.components.Toolbar
import tachiyomi.ui.core.viewmodel.viewModel
import tachiyomi.ui.main.Route

@Composable
fun CatalogsScreen(navController: NavController) {
  val vm = viewModel<CatalogsViewModel>()

  val onClickCatalog: (Catalog) -> Unit = {
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
            onClick = { onClickCatalog(catalog) },
            onInstall = { vm.installCatalog(catalog) },
            onUninstall = { vm.uninstallCatalog(catalog) },
            onPinToggle = { vm.togglePinnedCatalog(catalog) }
          )
        }
      }
      if (vm.localCatalogs.isNotEmpty()) {
        items(vm.localCatalogs) { catalog ->
          CatalogItem(
            catalog = catalog,
            showInstallButton = false,
            installStep = null,
            onClick = { onClickCatalog(catalog) },
            onInstall = { vm.installCatalog(catalog) },
            onUninstall = { vm.uninstallCatalog(catalog) },
            onPinToggle = { vm.togglePinnedCatalog(catalog) }
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
            onClick = { onClickCatalog(catalog) },
            onInstall = { vm.installCatalog(catalog) },
            onUninstall = { },
            onPinToggle = { }
          )
        }
      }
    }
  }
}
