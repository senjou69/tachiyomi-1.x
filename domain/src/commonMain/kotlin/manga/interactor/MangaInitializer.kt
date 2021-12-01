/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.domain.manga.interactor

import kotlinx.datetime.Clock
import tachiyomi.core.di.Inject
import tachiyomi.domain.catalog.service.CatalogStore
import tachiyomi.domain.library.service.LibraryCovers
import tachiyomi.domain.manga.model.Manga
import tachiyomi.domain.manga.model.MangaUpdate
import tachiyomi.domain.manga.service.MangaRepository
import tachiyomi.source.Source
import tachiyomi.source.model.AnimeInfo
import java.util.concurrent.TimeUnit

class MangaInitializer @Inject internal constructor(
  private val mangaRepository: MangaRepository,
  private val catalogStore: CatalogStore,
  private val libraryCovers: LibraryCovers
) {

  // TODO error handling
  suspend fun await(source: Source, manga: Manga, force: Boolean = false): Manga? {
    if (!force && lastInitBelowMinInterval(manga)) return null

    val now = Clock.System.now().toEpochMilliseconds()

    val infoQuery = AnimeInfo(
      key = manga.key,
      title = manga.title,
      artist = manga.artist,
      author = manga.author,
      description = manga.description,
      genres = manga.genres,
      status = manga.status,
      cover = manga.cover
    )
    val newInfo = source.getMangaDetails(infoQuery)

    val update = MangaUpdate(
      id = manga.id,
      key = if (newInfo.key.isEmpty() || newInfo.key == manga.key) {
        null
      } else {
        newInfo.key
      },
      title = if (newInfo.title.isEmpty() || newInfo.title == manga.title) {
        null
      } else {
        newInfo.title
      },
      artist = newInfo.artist,
      author = newInfo.author,
      description = newInfo.description,
      genres = newInfo.genres,
      status = newInfo.status,
      cover = newInfo.cover,
      lastInit = now
    )

    val updatedManga = manga.copy(
      key = if (newInfo.key.isEmpty()) manga.key else newInfo.key,
      title = if (newInfo.title.isEmpty()) manga.title else newInfo.title,
      artist = newInfo.artist,
      author = newInfo.author,
      description = newInfo.description,
      genres = newInfo.genres,
      status = newInfo.status,
      cover = newInfo.cover,
      lastInit = now
    )

    mangaRepository.updatePartial(update)
    libraryCovers.invalidate(manga.id)

    return updatedManga
  }

  suspend fun await(manga: Manga, force: Boolean = false): Manga? {
    val source = catalogStore.get(manga.sourceId)?.source ?: return null
    return await(source, manga, force)
  }

  private fun lastInitBelowMinInterval(manga: Manga): Boolean {
    return Clock.System.now().toEpochMilliseconds() - manga.lastInit < INIT_MIN_INTERVAL
  }

  private companion object {
    val INIT_MIN_INTERVAL = TimeUnit.DAYS.toMillis(30)
  }

}
