/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.more

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.outlined.GetApp
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import tachiyomi.domain.ui.UiPreferences
import tachiyomi.ui.R
import tachiyomi.ui.core.prefs.PreferenceRow
import tachiyomi.ui.core.prefs.SwitchPreference
import tachiyomi.ui.core.viewmodel.BaseViewModel
import tachiyomi.ui.core.viewmodel.viewModel
import tachiyomi.ui.more.components.LogoHeaderScaffold
import javax.inject.Inject

class MoreViewModel @Inject constructor(
  uiPreferences: UiPreferences
) : BaseViewModel() {

  val downloadedOnly = uiPreferences.downloadedOnly().asState()
  val incognitoMode = uiPreferences.incognitoMode().asState()
}

@Composable
fun MoreScreen(
  openDownloads: () -> Unit,
  openCategories: () -> Unit,
  openSettings: () -> Unit,
  openAbout: () -> Unit
) {
  val vm = viewModel<MoreViewModel>()
  val uriHandler = LocalUriHandler.current

  LogoHeaderScaffold(R.string.more_label) {
    LazyColumn {
      item {
        SwitchPreference(
          preference = vm.downloadedOnly,
          title = R.string.downloaded_only,
          subtitle = R.string.downloaded_only_subtitle,
          icon = Icons.Default.CloudOff,
        )
      }
      item {
        SwitchPreference(
          preference = vm.incognitoMode,
          title = R.string.incognito_mode,
          subtitle = R.string.incognito_mode_subtitle,
          icon = ImageVector.vectorResource(R.drawable.ic_glasses),
        )
      }
      item {
        Divider()
      }
      item {
        PreferenceRow(
          title = R.string.download_queue_label,
          icon = Icons.Outlined.GetApp,
          onClick = openDownloads,
        )
      }
      item {
        PreferenceRow(
          title = R.string.categories_label,
          icon = Icons.Outlined.Label,
          onClick = openCategories,
        )
      }
      item {
        Divider()
      }
      item {
        PreferenceRow(
          title = R.string.settings_label,
          icon = Icons.Outlined.Settings,
          onClick = openSettings,
        )
      }
      item {
        PreferenceRow(
          title = R.string.about_label,
          icon = Icons.Outlined.Info,
          onClick = openAbout,
        )
      }
      item {
        PreferenceRow(
          title = R.string.help_label,
          icon = Icons.Outlined.HelpOutline,
          onClick = { uriHandler.openUri("https://tachiyomi.org/help/") },
        )
      }
    }
  }
}
