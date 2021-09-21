/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.catalog

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseDispatcher
import tachiyomi.domain.catalog.model.CatalogRemote
import tachiyomi.domain.catalog.service.CatalogRemoteRepository

class CatalogRemoteRepositoryImpl @Inject constructor(
  private val db: Database
) : CatalogRemoteRepository {

  override suspend fun getRemoteCatalogs(): List<CatalogRemote> {
    return withContext(DatabaseDispatcher) {
      db.catalogRemoteQueries.findAll(catalogRemoteMapper).executeAsList()
    }
  }

  override fun getRemoteCatalogsFlow(): Flow<List<CatalogRemote>> {
    return db.catalogRemoteQueries.findAll(catalogRemoteMapper).asFlow()
      .mapToList(DatabaseDispatcher)
  }

  override suspend fun setRemoteCatalogs(catalogs: List<CatalogRemote>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        db.catalogRemoteQueries.deleteAll()
        for (catalog in catalogs) {
          insertBlocking(catalog)
        }
      }
    }
  }

  private fun insertBlocking(catalog: CatalogRemote) {
    db.catalogRemoteQueries.insert(
      id = catalog.sourceId,
      name = catalog.name,
      description = catalog.description,
      pkgName = catalog.pkgName,
      versionName = catalog.versionName,
      versionCode = catalog.versionCode,
      lang = catalog.lang,
      apkUrl = catalog.pkgUrl,
      iconUrl = catalog.iconUrl,
      nsfw = catalog.nsfw
    )
  }

}
