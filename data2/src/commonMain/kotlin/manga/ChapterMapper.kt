/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.manga

import tachiyomi.domain.manga.model.Chapter

internal val chapterMapper =
  { id: Long, mangaId: Long, key: String, name: String, read: Boolean, bookmark: Boolean,
    progress: Int, dateUpload: Long, dateFetch: Long, sourceOrder: Int, number: Float,
    scanlator: String ->

    Chapter(
      id = id,
      mangaId = mangaId,
      key = key,
      name = name,
      read = read,
      bookmark = bookmark,
      progress = progress,
      dateUpload = dateUpload,
      dateFetch = dateFetch,
      sourceOrder = sourceOrder,
      number = number,
      scanlator = scanlator
    )
  }
