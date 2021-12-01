/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.source

import tachiyomi.source.model.EpisodeInfo
import tachiyomi.source.model.AnimeInfo
import tachiyomi.source.model.Video

/**
 * A basic interface for creating a source. It could be an online source, a local source, etc...
 */
interface Source {

  /**
   * Id for the source. Must be unique.
   */
  val id: Long

  /**
   * Name of the source.
   */
  val name: String

  // TODO remove CatalogSource?
  val lang: String

  /**
   * Returns an observable with the updated details for a manga.
   *
   * @param manga the manga to update.
   */
  suspend fun getMangaDetails(manga: AnimeInfo): AnimeInfo

  /**
   * Returns an observable with all the available chapters for a manga.
   *
   * @param manga the manga to update.
   */
  suspend fun getChapterList(manga: AnimeInfo): List<EpisodeInfo>

  /**
   * Returns an observable with the list of pages a chapter has.
   *
   * @param chapter the chapter.
   */
  suspend fun getPageList(chapter: EpisodeInfo): List<Video>

  /**
   * Returns a regex used to determine chapter information.
   *
   * @return empty regex will run default parser.
   */
  fun getRegex(): Regex {
    return Regex("")
  }

}
