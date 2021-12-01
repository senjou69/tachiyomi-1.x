/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.domain.manga.interactor

import tachiyomi.core.di.Inject
import tachiyomi.domain.manga.model.Manga
import tachiyomi.domain.manga.service.MangaRepository
import tachiyomi.source.model.AnimeInfo

class GetOrAddMangaFromSource @Inject internal constructor(
  private val mangaRepository: MangaRepository
) {

  suspend fun await(manga: AnimeInfo, sourceId: Long): Manga {
    val dbManga = mangaRepository.find(manga.key, sourceId)
    return if (dbManga != null) {
      dbManga
    } else {
      val newManga = Manga(
        id = 0,
        sourceId = sourceId,
        key = manga.key,
        title = manga.title,
        artist = manga.artist,
        author = manga.author,
        description = manga.description,
        genres = manga.genres,
        status = manga.status,
        cover = manga.cover
      )
      val id = mangaRepository.insert(newManga)
      newManga.copy(id = id)
    }
  }

}
