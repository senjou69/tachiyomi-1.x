/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.browse.components

import androidx.compose.runtime.Composable
import tachiyomi.domain.catalog.model.Catalog

@Composable
internal actual fun CatalogMenuButton(
  catalog: Catalog,
  onPinToggle: (() -> Unit)?,
  onUninstall: (() -> Unit)?
) {
  // TODO not implemented in desktop (waiting for multiplatform DropdownMenu so we don't need this)
}
