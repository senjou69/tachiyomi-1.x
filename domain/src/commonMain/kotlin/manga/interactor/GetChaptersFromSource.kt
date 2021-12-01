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
import tachiyomi.source.Source
import tachiyomi.source.model.EpisodeInfo
import tachiyomi.source.model.AnimeInfo

class GetChaptersFromSource @Inject internal constructor() {

  suspend fun await(source: Source, manga: Manga): List<EpisodeInfo> {
    val mangaInfo = AnimeInfo(
      manga.key,
      manga.title,
      manga.artist,
      manga.author,
      manga.description,
      manga.genres,
      manga.status,
      manga.cover
    )
    return source.getChapterList(mangaInfo)
  }

}
