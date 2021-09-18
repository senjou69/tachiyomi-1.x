/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.history.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import tachiyomi.data.AppDatabase
import tachiyomi.data.history.model.TmpHistoryWithRelations
import tachiyomi.domain.history.model.History
import tachiyomi.domain.history.model.HistoryWithRelations
import tachiyomi.domain.history.service.HistoryRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
  appDatabase: AppDatabase
) : HistoryRepository {

  val db = appDatabase.history

  override suspend fun insert(history: History) {
    db.insert(history)
  }

  override suspend fun insert(history: List<History>) {
    db.insert(history)
  }

  override suspend fun delete(history: History) {
    db.delete(history)
  }

  override suspend fun delete(history: List<History>) {
    db.delete(history)
  }

  override suspend fun deleteAll() {
    db.deleteAll()
  }

  override suspend fun update(history: History) {
    db.update(history)
  }

  override suspend fun find(mangaId: Long, chapterId: Long): History? {
    return db.find(mangaId, chapterId)
  }

  override suspend fun findAll(): List<History> {
    return db.getHistory()
  }

  override fun subscribeAllWithRelationByDate(): Flow<Map<Date, List<HistoryWithRelations>>> {
    val formatter = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
    return db.getHistoryWithRelations()
      .mapLatest { history ->
        history.asIterable()
          .map { it.toHistory() }
          .groupBy { formatter.parse(it.date) }
          .toSortedMap(reverseOrder())
      }
  }

  private fun TmpHistoryWithRelations.toHistory(): HistoryWithRelations {
    return HistoryWithRelations(
      mangaId = manga.id,
      chapterId = chapter.id,
      readAt = history.readAt,
      mangaTitle = manga.title,
      sourceId = manga.sourceId,
      cover = manga.cover,
      favorite = manga.favorite,
      chapterNumber = chapter.number,
      date = date
    )
  }
}
