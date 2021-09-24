/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.download

import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.download.model.SavedDownload
import tachiyomi.domain.download.service.DownloadRepository

internal class DownloadRepositoryImpl @Inject constructor(
  private val handler: DatabaseHandler
) : DownloadRepository {

  override suspend fun findAll(): List<SavedDownload> {
    return handler.awaitList { downloadQueries.findAll(downloadMapper) }
  }

  override suspend fun insert(downloads: List<SavedDownload>) {
    handler.await(inTransaction = true) {
      for (download in downloads) {
        insertBlocking(download)
      }
    }
  }

  override suspend fun delete(downloads: List<SavedDownload>) {
    handler.await(inTransaction = true) {
      for (download in downloads) {
        downloadQueries.delete(download.chapterId)
      }
    }
  }

  override suspend fun delete(chapterId: Long) {
    handler.await { downloadQueries.delete(chapterId) }
  }

  private fun Database.insertBlocking(download: SavedDownload) {
    downloadQueries.insert(
      chapterId = download.chapterId,
      mangaId = download.mangaId,
      priority = download.priority
    )
  }

}
