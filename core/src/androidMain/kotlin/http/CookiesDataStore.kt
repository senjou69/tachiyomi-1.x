/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.core.http

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * An implementation of a [CookieStore] backed by a file managed through [DataStore].
 */
class CookiesDataStore(
  private val store: DataStore<Preferences>,
  private val scope: CoroutineScope
) : CookieStore {

  /**
   * Returns a map of all the cookies stored by domain.
   */
  override fun load(): Map<String, Set<String>> {
    @Suppress("UNCHECKED_CAST")
    return runBlocking {
      buildMap {
        store.data.first().asMap().forEach { (key, value) ->
          put(key.name, value as Set<String>)
        }
      }
    }
  }

  /**
   * Updates the cookies stored for this [domain] with the provided by [cookies].
   */
  override fun update(domain: String, cookies: Set<String>) {
    scope.launch {
      val key = stringSetPreferencesKey(domain)
      store.edit { it[key] = cookies }
    }
  }

  /**
   * Clears all the cookies saved in this store.
   */
  override fun clear() {
    scope.launch {
      store.edit { it.clear() }
    }
  }

}
