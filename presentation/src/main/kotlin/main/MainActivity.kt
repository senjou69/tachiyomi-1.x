/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import tachiyomi.core.di.AppScope
import tachiyomi.domain.ui.UiPreferences
import tachiyomi.domain.ui.model.StartScreen
import tachiyomi.ui.core.activity.BaseActivity
import tachiyomi.ui.core.theme.AppTheme

class MainActivity : BaseActivity() {

  private val uiPrefs = AppScope.getInstance<UiPreferences>()
  private lateinit var navController: NavHostController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Do not let the launcher create a new activity http://stackoverflow.com/questions/16283079
    if (!isTaskRoot) {
      finish()
      return
    }

    val startRoute = uiPrefs.startScreen().get().toRoute()

    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      AppTheme {
        navController = MainNavHost(startRoute)
        ConfirmExitBackHandler(uiPrefs)
      }
    }

    // Needs to be delayed so the app contents can load first
    Handler(Looper.getMainLooper()).post {
      handleIntentAction(intent)
    }
  }

  override fun onNewIntent(intent: Intent) {
    if (!handleIntentAction(intent)) {
      super.onNewIntent(intent)
    }
  }

  private fun handleIntentAction(intent: Intent): Boolean {
    when (intent.action) {
      SHORTCUT_DEEPLINK_CHAPTER -> {
        navController.navigate("${Route.Reader.id}/${intent.getLongExtra(INTENT_ID_EXTRA, -1L)}")
      }
      SHORTCUT_DEEPLINK_MANGA -> {
        navController.navigate(
          "${Route.LibraryManga.id}/${
            intent.getLongExtra(
              INTENT_ID_EXTRA,
              -1L
            )
          }"
        )
      }
      else -> return false
    }
    return true
  }

  companion object {
    const val SHORTCUT_DEEPLINK_MANGA = "tachiyomi.app.action.DEEPLINK_MANGA"
    const val SHORTCUT_DEEPLINK_CHAPTER = "tachiyomi.app.action.DEEPLINK_CHAPTER"
    const val INTENT_ID_EXTRA = "tachiyomi.app.extra.ID"
  }
}

private fun StartScreen.toRoute(): Route {
  return when (this) {
    StartScreen.Library -> Route.Library
    StartScreen.Updates -> Route.Updates
    StartScreen.History -> Route.History
    StartScreen.Browse -> Route.Browse
    StartScreen.More -> Route.More
  }
}
