/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.download

import tachiyomi.domain.download.model.SavedDownload

val downloadMapper =
  { chapterId: Long, mangaId: Long, priority: Int, sourceId: Long, mangaName: String,
    chapterKey: String, chapterName: String, scanlator: String ->

    SavedDownload(
      chapterId = chapterId,
      mangaId = mangaId,
      priority = priority,
      sourceId = sourceId,
      mangaName = mangaName,
      chapterKey = chapterKey,
      chapterName = chapterName,
      scanlator = scanlator
    )
  }
