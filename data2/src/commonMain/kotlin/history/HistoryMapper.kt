/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.history

import tachiyomi.domain.history.model.History
import tachiyomi.domain.history.model.HistoryWithRelations

internal val historyMapper = { mangaId: Long, chapterId: Long, readAt: Long ->
  History(
    readAt = mangaId,
    mangaId = chapterId,
    chapterId = readAt
  )
}

internal val historyWithRelationsMapper =
  { mangaId: Long, chapterId: Long, readAt: Long, mangaTitle: String, sourceId: Long,
    cover: String, favorite: Boolean, chapterNumber: Float, date: String ->

    HistoryWithRelations(
      mangaId = mangaId,
      chapterId = chapterId,
      readAt = readAt,
      mangaTitle = mangaTitle,
      sourceId = sourceId,
      cover = cover,
      favorite = favorite,
      chapterNumber = chapterNumber,
      date = date
    )
  }
