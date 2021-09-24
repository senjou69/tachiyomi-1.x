/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.catalog

import kotlinx.coroutines.flow.Flow
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.catalog.model.CatalogRemote
import tachiyomi.domain.catalog.service.CatalogRemoteRepository

internal class CatalogRemoteRepositoryImpl @Inject constructor(
  private val handler: DatabaseHandler
) : CatalogRemoteRepository {

  override suspend fun getRemoteCatalogs(): List<CatalogRemote> {
    return handler.awaitList { catalogRemoteQueries.findAll(catalogRemoteMapper) }
  }

  override fun getRemoteCatalogsFlow(): Flow<List<CatalogRemote>> {
    return handler.subscribeToList { catalogRemoteQueries.findAll(catalogRemoteMapper) }
  }

  override suspend fun setRemoteCatalogs(catalogs: List<CatalogRemote>) {
    handler.await(inTransaction = true) {
      catalogRemoteQueries.deleteAll()
      for (catalog in catalogs) {
        insertBlocking(catalog)
      }
    }
  }

  private fun Database.insertBlocking(catalog: CatalogRemote) {
    catalogRemoteQueries.insert(
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
