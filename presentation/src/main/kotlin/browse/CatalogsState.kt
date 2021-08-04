/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.ui.browse

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import tachiyomi.domain.catalog.model.CatalogInstalled
import tachiyomi.domain.catalog.model.CatalogLocal
import tachiyomi.domain.catalog.model.CatalogRemote
import tachiyomi.domain.catalog.model.InstallStep

class CatalogsState {

  var localCatalogs by mutableStateOf(emptyList<CatalogLocal>())
  var updatableCatalogs by mutableStateOf(emptyList<CatalogInstalled>())
  var remoteCatalogs by mutableStateOf(emptyList<CatalogRemote>())
  var languageChoices by mutableStateOf(emptyList<LanguageChoice>())
  var selectedLanguage by mutableStateOf<LanguageChoice>(LanguageChoice.All)
  var installSteps by mutableStateOf(emptyMap<String, InstallStep>())
  var refreshingCatalogs by mutableStateOf(false)
  var searchQuery by mutableStateOf<String?>(null)

  var unfilteredUpdatedCatalogs by mutableStateOf(
    emptyList<CatalogLocal>(),
    referentialEqualityPolicy()
  )

  var unfilteredUpdatableCatalogs by mutableStateOf(
    emptyList<CatalogInstalled>(),
    referentialEqualityPolicy()
  )

  var unfilteredRemoteCatalogs by mutableStateOf(
    emptyList<CatalogRemote>(),
    referentialEqualityPolicy()
  )

}
