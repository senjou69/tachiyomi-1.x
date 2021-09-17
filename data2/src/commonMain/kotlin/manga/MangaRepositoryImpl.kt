/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.manga

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import tachiyomi.core.di.Inject
import tachiyomi.data.Database
import tachiyomi.data.DatabaseDispatcher
import tachiyomi.domain.manga.model.Manga
import tachiyomi.domain.manga.model.MangaUpdate
import tachiyomi.domain.manga.service.MangaRepository

class MangaRepositoryImpl @Inject constructor(
  private val db: Database
) : MangaRepository {

  override suspend fun find(mangaId: Long): Manga? {
    return withContext(DatabaseDispatcher) {
      db.mangaQueries.findById(mangaId, mangaMapper).executeAsOneOrNull()
    }
  }

  override suspend fun find(key: String, sourceId: Long): Manga? {
    return withContext(DatabaseDispatcher) {
      db.mangaQueries.findByKey(key, sourceId, mangaMapper).executeAsOneOrNull()
    }
  }

  override suspend fun findFavorites(): List<Manga> {
    return withContext(DatabaseDispatcher) {
      db.mangaQueries.findFavorites(mangaMapper).executeAsList()
    }
  }

  override suspend fun insert(manga: Manga): Long {
    return withContext(DatabaseDispatcher) {
      db.transactionWithResult {
        db.mangaQueries.insert(
          id = null,
          sourceId = manga.sourceId,
          key = manga.key,
          title = manga.title,
          artist = manga.artist,
          author = manga.author,
          description = manga.description,
          genres = manga.genres,
          status = manga.status,
          cover = manga.cover,
          customCover = manga.customCover,
          favorite = manga.favorite,
          lastUpdate = manga.lastUpdate,
          lastInit = manga.lastInit,
          dateAdded = manga.dateAdded,
          viewer = manga.viewer,
          flags = manga.flags
        )
        db.mangaQueries.lastInsertedId().executeAsOne()
      }
    }
  }

  override fun subscribe(mangaId: Long): Flow<Manga?> {
    return db.mangaQueries.findById(mangaId, mangaMapper).asFlow()
      .mapToOneOrNull(DatabaseDispatcher)
  }

  override fun subscribe(key: String, sourceId: Long): Flow<Manga?> {
    return db.mangaQueries.findByKey(key, sourceId, mangaMapper).asFlow()
      .mapToOneOrNull(DatabaseDispatcher)
  }

  override suspend fun updatePartial(update: MangaUpdate) {
    TODO("not implemented")
  }

  override suspend fun deleteNonFavorite() {
    withContext(DatabaseDispatcher) {
      db.mangaQueries.deleteNonFavorite()
    }
  }

}
