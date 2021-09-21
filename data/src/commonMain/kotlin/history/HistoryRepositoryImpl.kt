/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.history

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseDispatcher
import tachiyomi.domain.history.model.History
import tachiyomi.domain.history.model.HistoryWithRelations
import tachiyomi.domain.history.service.HistoryRepository
import java.util.Date

class HistoryRepositoryImpl @Inject constructor(
  private val db: Database
) : HistoryRepository {

  override suspend fun find(mangaId: Long, chapterId: Long): History? {
    return withContext(DatabaseDispatcher) {
      db.historyQueries.find(mangaId, chapterId, historyMapper).executeAsOneOrNull()
    }
  }

  override suspend fun findAll(): List<History> {
    return withContext(DatabaseDispatcher) {
      db.historyQueries.findAll(historyMapper).executeAsList()
    }
  }

  override fun subscribeAllWithRelationByDate(): Flow<Map<Date, List<HistoryWithRelations>>> {
    // TODO
    //val formatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
    return db.historyQueries.findAllWithRelations(historyWithRelationsMapper).asFlow()
      .mapToList(DatabaseDispatcher)
      .mapLatest { history ->
        history
          .groupBy { Date(it.readAt) }
//          .groupBy { formatter.parse(it.date) }
//          .toSortedMap(reverseOrder())
      }
  }

  override suspend fun insert(history: History) {
    withContext(DatabaseDispatcher) {
      insertBlocking(history)
    }
  }

  override suspend fun insert(history: List<History>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (h in history) {
          insertBlocking(h)
        }
      }
    }
  }

  override suspend fun update(history: History) {
    withContext(DatabaseDispatcher) {
      updateBlocking(history)
    }
  }

  override suspend fun delete(history: History) {
    withContext(DatabaseDispatcher) {
      db.historyQueries.deleteById(history.mangaId, history.chapterId)
    }
  }

  override suspend fun delete(history: List<History>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (h in history) {
          db.historyQueries.deleteById(h.mangaId, h.chapterId)
        }
      }
    }
  }

  override suspend fun deleteAll() {
    withContext(DatabaseDispatcher) {
      db.historyQueries.deleteAll()
    }
  }

  private fun insertBlocking(history: History) {
    db.historyQueries.insert(
      mangaId = history.mangaId,
      chapterId = history.chapterId,
      readAt = history.readAt
    )
  }

  private fun updateBlocking(history: History) {
    db.historyQueries.update(
      readAt = history.readAt,
      mangaId = history.mangaId,
      chapterId = history.chapterId
    )
  }

}
