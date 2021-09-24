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
import tachiyomi.domain.manga.model.Manga
import tachiyomi.domain.manga.model.MangaUpdate
import tachiyomi.domain.manga.service.MangaRepository

internal class MangaRepositoryImpl @Inject constructor(
  private val handler: DatabaseHandler
) : MangaRepository {

  override suspend fun find(mangaId: Long): Manga? {
    return handler.awaitOneOrNull { mangaQueries.findById(mangaId, mangaMapper) }
  }

  override suspend fun find(key: String, sourceId: Long): Manga? {
    return handler.awaitOneOrNull { mangaQueries.findByKey(key, sourceId, mangaMapper) }
  }

  override suspend fun findFavorites(): List<Manga> {
    return handler.awaitList { mangaQueries.findFavorites(mangaMapper) }
  }

  override fun subscribe(mangaId: Long): Flow<Manga?> {
    return handler.subscribeToOneOrNull { mangaQueries.findById(mangaId, mangaMapper) }
  }

  override fun subscribe(key: String, sourceId: Long): Flow<Manga?> {
    return handler.subscribeToOneOrNull { mangaQueries.findByKey(key, sourceId, mangaMapper) }
  }

  override suspend fun insert(manga: Manga): Long {
    return handler.awaitOne(inTransaction = true) {
      insertBlocking(manga)
      mangaQueries.lastInsertedId()
    }
  }

  override suspend fun updatePartial(update: MangaUpdate) {
    handler.await { updatePartialBlocking(update) }
  }

  override suspend fun deleteNonFavorite() {
    handler.await { mangaQueries.deleteNonFavorite() }
  }

  private fun Database.insertBlocking(manga: Manga) {
    mangaQueries.insert(
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
  }

  private fun Database.updatePartialBlocking(update: MangaUpdate) {
    mangaQueries.update(
      update.sourceId,
      update.key,
      update.title,
      update.artist,
      update.author,
      update.description,
      update.genres?.let(mangaGenresConverter::encode),
      update.status?.toLong(),
      update.cover,
      update.customCover,
      update.favorite?.let { if (it) 1 else 0 },
      update.lastUpdate,
      update.lastInit,
      update.dateAdded,
      update.viewer?.toLong(),
      update.flags?.toLong(),
      id = update.id,
    )
  }

}
