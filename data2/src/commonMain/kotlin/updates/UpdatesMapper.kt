/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.data.updates

import tachiyomi.domain.updates.model.UpdatesManga

internal val updatesMapper =
  { id: Long, sourceId: Long, key: String, title: String, cover: String, favorite: Boolean,
    dateUpload: Long, chapterId: Long, name: String, read: Boolean, number: Float, date: String ->

    UpdatesManga(
      id = id,
      sourceId = sourceId,
      key = key,
      title = title,
      cover = cover,
      favorite = favorite,
      dateUpload = dateUpload,
      chapterId = chapterId,
      name = name,
      read = read,
      number = number,
      date = date
    )
  }
