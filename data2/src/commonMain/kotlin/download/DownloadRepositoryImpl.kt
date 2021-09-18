/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.download

import kotlinx.coroutines.withContext
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseDispatcher
import tachiyomi.domain.download.model.SavedDownload
import tachiyomi.domain.download.service.DownloadRepository

class DownloadRepositoryImpl @Inject constructor(
  private val db: Database
) : DownloadRepository {

  override suspend fun findAll(): List<SavedDownload> {
    return withContext(DatabaseDispatcher) {
      db.downloadQueries.findAll(downloadMapper).executeAsList()
    }
  }

  override suspend fun insert(downloads: List<SavedDownload>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (download in downloads) {
          insertBlocking(download)
        }
      }
    }
  }

  override suspend fun delete(downloads: List<SavedDownload>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (download in downloads) {
          db.downloadQueries.delete(download.chapterId)
        }
      }
    }
  }

  override suspend fun delete(chapterId: Long) {
    withContext(DatabaseDispatcher) {
      db.downloadQueries.delete(chapterId)
    }
  }

  private fun insertBlocking(download: SavedDownload) {
    db.downloadQueries.insert(
      chapterId = download.chapterId,
      mangaId = download.mangaId,
      priority = download.priority
    )
  }

}
