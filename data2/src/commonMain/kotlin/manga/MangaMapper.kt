/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.manga

import tachiyomi.domain.manga.model.Manga

internal val mangaMapper =
  { id: Long, sourceId: Long, key: String, title: String, artist: String, author: String,
    description: String, genres: List<String>, status: Int, cover: String, customCover: String,
    favorite: Boolean, lastUpdate: Long, lastInit: Long, dateAdded: Long, viewer: Int,
    flags: Int ->

    Manga(
      id = id,
      sourceId = sourceId,
      key = key,
      title = title,
      artist = artist,
      author = author,
      description = description,
      genres = genres,
      status = status,
      cover = cover,
      customCover = customCover,
      favorite = favorite,
      lastUpdate = lastUpdate,
      lastInit = lastInit,
      dateAdded = dateAdded,
      viewer = viewer,
      flags = flags
    )
  }
