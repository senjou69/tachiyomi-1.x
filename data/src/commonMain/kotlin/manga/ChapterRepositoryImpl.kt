/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.manga

import kotlinx.coroutines.flow.Flow
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.manga.model.Chapter
import tachiyomi.domain.manga.model.ChapterUpdate
import tachiyomi.domain.manga.service.ChapterRepository

class ChapterRepositoryImpl @Inject constructor(
  private val handler: DatabaseHandler
) : ChapterRepository {

  override fun subscribeForManga(mangaId: Long): Flow<List<Chapter>> {
    return handler.subscribeToList { chapterQueries.findForManga(mangaId, chapterMapper) }
  }

  override fun subscribe(chapterId: Long): Flow<Chapter?> {
    return handler.subscribeToOneOrNull { chapterQueries.findById(chapterId, chapterMapper) }
  }

  override suspend fun findForManga(mangaId: Long): List<Chapter> {
    return handler.awaitList { chapterQueries.findForManga(mangaId, chapterMapper) }
  }

  override suspend fun find(chapterId: Long): Chapter? {
    return handler.awaitOneOrNull { chapterQueries.findById(chapterId, chapterMapper) }
  }

  override suspend fun find(chapterKey: String, mangaId: Long): Chapter? {
    return handler.awaitOneOrNull { chapterQueries.findByKey(mangaId, chapterKey, chapterMapper) }
  }

  override suspend fun insert(chapters: List<Chapter>) {
    handler.await(inTransaction = true) {
      for (chapter in chapters) {
        insertBlocking(chapter)
      }
    }
  }

  override suspend fun update(chapters: List<Chapter>) {
    handler.await(inTransaction = true) {
      for (chapter in chapters) {
        updateBlocking(chapter)
      }
    }
  }

  override suspend fun updatePartial(updates: List<ChapterUpdate>) {
    handler.await(inTransaction = true) {
      for (update in updates) {
        updateBlocking(update)
      }
    }
  }

  override suspend fun updateOrder(chapters: List<Chapter>) {
    handler.await(inTransaction = true) {
      for (chapter in chapters) {
        chapterQueries.updateOrder(chapter.sourceOrder, chapter.mangaId, chapter.key)
      }
    }
  }

  override suspend fun delete(chapters: List<Chapter>) {
    handler.await(inTransaction = true) {
      for (chapter in chapters) {
        chapterQueries.delete(chapter.id)
      }
    }
  }

  private fun Database.insertBlocking(chapter: Chapter) {
    chapterQueries.insert(
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

  private fun Database.updateBlocking(chapter: Chapter) {
    chapterQueries.update(
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

  private fun Database.updateBlocking(chapter: ChapterUpdate) {
    chapterQueries.update(
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
