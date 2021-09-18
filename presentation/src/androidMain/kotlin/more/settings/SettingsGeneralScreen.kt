/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.more.settings

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import tachiyomi.domain.ui.UiPreferences
import tachiyomi.domain.ui.model.StartScreen
import tachiyomi.i18n.MR
import tachiyomi.i18n.localize
import tachiyomi.ui.core.components.BackIconButton
import tachiyomi.ui.core.components.Toolbar
import tachiyomi.ui.core.prefs.ChoicePreference
import tachiyomi.ui.core.prefs.PreferenceRow
import tachiyomi.ui.core.prefs.SwitchPreference
import tachiyomi.ui.core.viewmodel.BaseViewModel
import tachiyomi.ui.core.viewmodel.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class SettingsGeneralViewModel @Inject constructor(
  uiPreferences: UiPreferences
) : BaseViewModel() {

  val startScreen = uiPreferences.startScreen().asState()
  val confirmExit = uiPreferences.confirmExit().asState()
  val hideBottomBarOnScroll = uiPreferences.hideBottomBarOnScroll().asState()
  val language = uiPreferences.language().asState()
  val dateFormat = uiPreferences.dateFormat().asState()

  private val now = Date()

  @Composable
  fun getLanguageChoices(): Map<String, String> {
    val currentLocaleDisplayName = Locale.getDefault().let { it.getDisplayName(it).capitalize() }
    return mapOf(
      "" to "${localize(MR.strings.system_default)} ($currentLocaleDisplayName)"
    )
  }

  @Composable
  fun getDateChoices(): Map<String, String> {
    return mapOf(
      "" to localize(MR.strings.system_default),
      "MM/dd/yy" to "MM/dd/yy",
      "dd/MM/yy" to "dd/MM/yy",
      "yyyy-MM-dd" to "yyyy-MM-dd"
    ).mapValues { "${it.value} (${getFormattedDate(it.key)})" }
  }

  private fun getFormattedDate(prefValue: String): String {
    return when (prefValue) {
      "" -> DateFormat.getDateInstance(DateFormat.SHORT)
      else -> SimpleDateFormat(prefValue, Locale.getDefault())
    }.format(now.time)
  }
}

@Composable
fun SettingsGeneralScreen(
  navigateUp: () -> Unit
) {
  val vm = viewModel<SettingsGeneralViewModel>()
  val context = LocalContext.current
  Column {
    Toolbar(
      title = { Text(localize(MR.strings.general_label)) },
      navigationIcon = { BackIconButton(navigateUp) }
    )
    LazyColumn {
      item {
        ChoicePreference(
          preference = vm.startScreen,
          title = MR.strings.start_screen,
          choices = mapOf(
            StartScreen.Library to MR.strings.library_label,
            StartScreen.Updates to MR.strings.updates_label,
            StartScreen.History to MR.strings.history_label,
            StartScreen.Browse to MR.strings.browse_label,
            StartScreen.More to MR.strings.more_label,
          )
        )
      }
      item {
        SwitchPreference(preference = vm.confirmExit, title = MR.strings.confirm_exit)
      }
      item {
        SwitchPreference(
          preference = vm.hideBottomBarOnScroll,
          title = MR.strings.hide_bottom_bar_on_scroll
        )
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        item {
          PreferenceRow(title = MR.strings.manage_notifications, onClick = {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
              putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
          })
        }
      }
      item {
        Divider()
      }
      item {
        ChoicePreference(
          preference = vm.language,
          title = localize(MR.strings.language),
          choices = vm.getLanguageChoices(),
        )
      }
      item {
        ChoicePreference(
          preference = vm.dateFormat,
          title = localize(MR.strings.date_format),
          choices = vm.getDateChoices()
        )
      }
    }
  }
}
