/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.core.http

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Cache
import java.io.File
import javax.inject.Inject
import javax.inject.Provider

/**
 * Provider to instantiate an [Http] class. The required dependencies to create the instance are
 * also provided through constructor injection.
 */
class HttpProvider @Inject constructor(
  private val context: Application,
  private val jsFactory: JSFactory
) : Provider<Http> {

  /**
   * Returns a new instance of [Http] providing it a [Cache], a [CookieManager] and a [JSFactory].
   */
  override fun get(): Http {
    val cacheDir = File(context.cacheDir, "network_cache")
    val cacheSize = 15L * 1024 * 1024
    val cache = Cache(cacheDir, cacheSize)
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val prefStore = PreferenceDataStoreFactory.create(
      scope = scope,
      produceFile = { context.preferencesDataStoreFile("cookie_store") }
    )
    val cookieStore = CookiesDataStore(prefStore, scope)
    val cookieManager = CookieManager(cookieStore)
    return Http(cache, cookieManager, jsFactory)
  }
}
