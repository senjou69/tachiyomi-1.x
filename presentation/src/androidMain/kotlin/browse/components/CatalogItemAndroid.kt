/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.browse.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import tachiyomi.domain.catalog.model.Catalog
import tachiyomi.domain.catalog.model.CatalogLocal
import tachiyomi.domain.catalog.model.CatalogRemote
import tachiyomi.i18n.MR
import tachiyomi.i18n.localize

@Composable
internal actual fun CatalogMenuButton(
  catalog: Catalog,
  onPinToggle: (() -> Unit)?,
  onUninstall: (() -> Unit)?
) {
  var expanded by remember { mutableStateOf(false) }
  Box {
    IconButton(onClick = { expanded = true }) {
      Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      DropdownMenuItem(onClick = { /*TODO*/ }) {
        Text(localize(MR.strings.catalog_details))
      }
      if (onPinToggle != null && catalog is CatalogLocal) {
        DropdownMenuItem(onClick = onPinToggle) {
          Text(
            localize(
              if (!catalog.isPinned) MR.strings.catalog_pin else MR.strings
                .catalog_unpin
            )
          )
        }
      }
      if (onUninstall != null) {
        DropdownMenuItem(onClick = onUninstall) {
          Text(localize(MR.strings.catalog_uninstall))
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
