/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.manga

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseDispatcher
import tachiyomi.domain.manga.model.Chapter
import tachiyomi.domain.manga.model.ChapterUpdate
import tachiyomi.domain.manga.service.ChapterRepository

class ChapterRepositoryImpl @Inject constructor(
  private val db: Database
) : ChapterRepository {

  override fun subscribeForManga(mangaId: Long): Flow<List<Chapter>> {
    return db.chapterQueries.findForManga(mangaId, chapterMapper).asFlow()
      .mapToList(DatabaseDispatcher)
  }

  override fun subscribe(chapterId: Long): Flow<Chapter?> {
    return db.chapterQueries.findById(chapterId, chapterMapper).asFlow()
      .mapToOneOrNull(DatabaseDispatcher)
  }

  override suspend fun findForManga(mangaId: Long): List<Chapter> {
    return withContext(DatabaseDispatcher) {
      db.chapterQueries.findForManga(mangaId, chapterMapper).executeAsList()
    }
  }

  override suspend fun find(chapterId: Long): Chapter? {
    return withContext(DatabaseDispatcher) {
      db.chapterQueries.findById(chapterId, chapterMapper).executeAsOneOrNull()
    }
  }

  override suspend fun find(chapterKey: String, mangaId: Long): Chapter? {
    return withContext(DatabaseDispatcher) {
      db.chapterQueries.findByKey(mangaId, chapterKey, chapterMapper).executeAsOneOrNull()
    }
  }

  override suspend fun insert(chapters: List<Chapter>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (chapter in chapters) {
          insertBlocking(chapter)
        }
      }
    }
  }

  override suspend fun update(chapters: List<Chapter>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (chapter in chapters) {
          updateBlocking(chapter)
        }
      }
    }
  }

  override suspend fun updatePartial(updates: List<ChapterUpdate>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (update in updates) {
          updateBlocking(update)
        }
      }
    }
  }

  override suspend fun updateOrder(chapters: List<Chapter>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (chapter in chapters) {
          db.chapterQueries.updateOrder(chapter.sourceOrder, chapter.mangaId, chapter.key)
        }
      }
    }
  }

  override suspend fun delete(chapters: List<Chapter>) {
    withContext(DatabaseDispatcher) {
      db.transaction {
        for (chapter in chapters) {
          db.chapterQueries.delete(chapter.id)
        }
      }
    }
  }

  private fun insertBlocking(chapter: Chapter) {
    db.chapterQueries.insert(
      id = chapter.id,
      mangaId = chapter.mangaId,
      key = chapter.key,
      name = chapter.name,
      read = chapter.read,
      bookmark = chapter.bookmark,
      progress = chapter.progress,
      dateUpload = chapter.dateUpload,
      dateFetch = chapter.dateFetch,
      sourceOrder = chapter.sourceOrder,
      number = chapter.number,
      scanlator = chapter.scanlator
    )
  }

  private fun updateBlocking(chapter: Chapter) {
    db.chapterQueries.update(
      chapter.mangaId,
      chapter.key,
      chapter.name,
      chapter.read.let { if (it) 1 else 0 },
      chapter.bookmark.let { if (it) 1 else 0 },
      chapter.progress.toLong(),
      chapter.dateUpload,
      chapter.dateFetch,
      chapter.sourceOrder.toLong(),
      chapter.number.toDouble(),
      chapter.scanlator,
      id = chapter.id
    )
  }

  private fun updateBlocking(chapter: ChapterUpdate) {
    db.chapterQueries.update(
      chapter.mangaId,
      chapter.key,
      chapter.name,
      chapter.read?.let { if (it) 1 else 0 },
      chapter.bookmark?.let { if (it) 1 else 0 },
      chapter.progress?.toLong(),
      chapter.dateUpload,
      chapter.dateFetch,
      chapter.sourceOrder?.toLong(),
      chapter.number?.toDouble(),
      chapter.scanlator,
      id = chapter.id
    )
  }

}
