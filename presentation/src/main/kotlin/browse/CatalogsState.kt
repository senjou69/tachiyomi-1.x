/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.browse

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import tachiyomi.domain.catalog.model.CatalogInstalled
import tachiyomi.domain.catalog.model.CatalogLocal
import tachiyomi.domain.catalog.model.CatalogRemote
import tachiyomi.domain.catalog.model.InstallStep

@Stable
interface CatalogsState {
  val localCatalogs: List<CatalogLocal>
  val updatableCatalogs: List<CatalogInstalled>
  val remoteCatalogs: List<CatalogRemote>
  val languageChoices: List<LanguageChoice>
  val selectedLanguage: LanguageChoice
  var expandPinned: Boolean
  var expandInstalled: Boolean
  var expandAvailable: Boolean
  val installSteps: Map<String, InstallStep>
  val isRefreshing: Boolean
  val searchQuery: String?
}

fun CatalogsState(): CatalogsState {
  return CatalogsStateImpl()
}

class CatalogsStateImpl : CatalogsState {
  override var localCatalogs by mutableStateOf(emptyList<CatalogLocal>())
  override var updatableCatalogs by mutableStateOf(emptyList<CatalogInstalled>())
  override var remoteCatalogs by mutableStateOf(emptyList<CatalogRemote>())
  override var languageChoices by mutableStateOf(emptyList<LanguageChoice>())
  override var selectedLanguage by mutableStateOf<LanguageChoice>(LanguageChoice.All)
  override var expandPinned by mutableStateOf(true)
  override var expandInstalled by mutableStateOf(true)
  override var expandAvailable by mutableStateOf(true)
  override var installSteps by mutableStateOf(emptyMap<String, InstallStep>())
  override var isRefreshing by mutableStateOf(false)
  override var searchQuery by mutableStateOf<String?>(null)

  var allUpdatedCatalogs by mutableStateOf(
    emptyList<CatalogLocal>(),
    referentialEqualityPolicy()
  )
  var allUpdatableCatalogs by mutableStateOf(
    emptyList<CatalogInstalled>(),
    referentialEqualityPolicy()
  )
  var allRemoteCatalogs by mutableStateOf(
    emptyList<CatalogRemote>(),
    referentialEqualityPolicy()
  )
}
