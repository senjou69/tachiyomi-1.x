/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.more.about

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import tachiyomi.ui.R
import tachiyomi.ui.core.components.BackIconButton
import tachiyomi.ui.core.components.Toolbar
import tachiyomi.ui.core.prefs.PreferenceRow

@Composable
fun AboutScreen(
  openLicenses: () -> Unit,
  navigateUp: () -> Unit
) {
  Scaffold(
    topBar = {
      Toolbar(
        title = { Text(stringResource(R.string.about_label)) },
        navigationIcon = { BackIconButton(navigateUp) },
      )
    }
  ) {
    LazyColumn {
      item {
        PreferenceRow(
          title = R.string.licenses_label,
          onClick = openLicenses
        )
      }
    }
  }
}
